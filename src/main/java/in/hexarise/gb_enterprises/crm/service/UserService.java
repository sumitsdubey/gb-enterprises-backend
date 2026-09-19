package in.hexarise.gb_enterprises.crm.service;

import in.hexarise.gb_enterprises.crm.domain.User;
import in.hexarise.gb_enterprises.crm.dto.UserDto.*;
import in.hexarise.gb_enterprises.crm.exception.AppException;
import in.hexarise.gb_enterprises.crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public PageResponse<UserResponse> list(int page, int size) {
        Page<UserResponse> p = userRepo.findAll(PageRequest.of(page, size)).map(UserResponse::from);
        return new PageResponse<>(p.getContent(), p.getTotalElements(), p.getTotalPages(), page, size);
    }

    public UserResponse create(CreateUserRequest req) {
        if (req.email() != null && userRepo.existsByEmail(req.email())) throw AppException.conflict("Email taken");
        if (req.mobile() != null && userRepo.existsByMobile(req.mobile())) throw AppException.conflict("Mobile taken");

        User user = User.builder()
            .name(req.name()).email(req.email()).mobile(req.mobile())
            .passwordHash(encoder.encode(req.password()))
            .role(req.role() != null ? req.role() : User.Role.STAFF)
            .build();
        return UserResponse.from(userRepo.save(user));
    }

    public UserResponse update(Long id, UpdateUserRequest req) {
        User user = userRepo.findById(id).orElseThrow(() -> AppException.notFound("User not found"));
        if (req.name() != null) user.setName(req.name());
        if (req.email() != null) user.setEmail(req.email());
        if (req.mobile() != null) user.setMobile(req.mobile());
        if (req.role() != null) user.setRole(req.role());
        if (req.active() != null) user.setActive(req.active());
        return UserResponse.from(userRepo.save(user));
    }

    public void delete(Long id) { userRepo.deleteById(id); }

    public UserResponse updateProfile(User current, UpdateProfileRequest req) {
        current.setName(req.name());
        if (req.email() != null) current.setEmail(req.email());
        if (req.mobile() != null) current.setMobile(req.mobile());
        return UserResponse.from(userRepo.save(current));
    }

    public void changePassword(User current, ChangePasswordRequest req) {
        if (!encoder.matches(req.currentPassword(), current.getPasswordHash()))
            throw AppException.badRequest("Current password is incorrect");
        current.setPasswordHash(encoder.encode(req.newPassword()));
        userRepo.save(current);
    }
}
