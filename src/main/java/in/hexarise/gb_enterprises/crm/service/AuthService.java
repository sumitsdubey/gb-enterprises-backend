package in.hexarise.gb_enterprises.crm.service;

import in.hexarise.gb_enterprises.crm.config.JwtUtil;
import in.hexarise.gb_enterprises.crm.domain.User;
import in.hexarise.gb_enterprises.crm.dto.UserDto.*;
import in.hexarise.gb_enterprises.crm.exception.AppException;
import in.hexarise.gb_enterprises.crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final JavaMailSender mailSender;

    public AuthResponse login(LoginRequest req) {
        User user = userRepo.findByEmail(req.credential())
            .or(() -> userRepo.findByMobile(req.credential()))
            .orElseThrow(() -> AppException.unauthorized("Invalid credentials"));

        if (!user.isActive()) throw AppException.forbidden("Account disabled");
        if (!encoder.matches(req.password(), user.getPasswordHash()))
            throw AppException.unauthorized("Invalid credentials");

        return buildTokens(user);
    }

    public AuthResponse refresh(RefreshRequest req) {
        var claims = jwtUtil.parse(req.refreshToken());
        if (!"refresh".equals(claims.get("type"))) throw AppException.unauthorized("Invalid token type");

        User user = userRepo.findByEmail(claims.getSubject())
            .or(() -> userRepo.findByMobile(claims.getSubject()))
            .orElseThrow(() -> AppException.unauthorized("User not found"));

        return buildTokens(user);
    }

    public void forgotPassword(ForgotPasswordRequest req) {
        User user = userRepo.findByEmail(req.email())
            .orElseThrow(() -> AppException.notFound("No account found with this email"));

        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setResetOtp(encoder.encode(otp));
        user.setOtpExpiry(Instant.now().plusSeconds(600)); // 10 min
        userRepo.save(user);

        var msg = new SimpleMailMessage();
        msg.setTo(req.email());
        msg.setSubject("GB CRM — Password Reset OTP");
        msg.setText("Your OTP: " + otp + "\nValid for 10 minutes.");
        mailSender.send(msg);
    }

    public void resetPassword(ResetPasswordRequest req) {
        User user = userRepo.findByEmail(req.email())
            .orElseThrow(() -> AppException.notFound("User not found"));

        if (user.getOtpExpiry() == null || Instant.now().isAfter(user.getOtpExpiry()))
            throw AppException.badRequest("OTP expired");
        if (!encoder.matches(req.otp(), user.getResetOtp()))
            throw AppException.badRequest("Invalid OTP");

        user.setPasswordHash(encoder.encode(req.newPassword()));
        user.setResetOtp(null);
        user.setOtpExpiry(null);
        userRepo.save(user);
    }

    private AuthResponse buildTokens(User user) {
        String subject = user.getEmail() != null ? user.getEmail() : user.getMobile();
        return new AuthResponse(
            jwtUtil.generate(subject, user.getRole().name(), false),
            jwtUtil.generate(subject, user.getRole().name(), true),
            UserResponse.from(user));
    }
}
