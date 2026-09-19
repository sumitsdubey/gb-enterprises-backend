package in.hexarise.gb_enterprises.crm.dto;

import in.hexarise.gb_enterprises.crm.domain.User;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

public class UserDto {

    public record LoginRequest(@NotBlank String credential, @NotBlank String password) {}

    public record AuthResponse(String accessToken, String refreshToken, UserResponse user) {}

    public record RefreshRequest(@NotBlank String refreshToken) {}

    public record ForgotPasswordRequest(@NotBlank String email) {}

    public record ResetPasswordRequest(@NotBlank String email, @NotBlank String otp, @NotBlank String newPassword) {}

    public record CreateUserRequest(
        @NotBlank String name, String email, String mobile,
        @NotBlank String password, User.Role role) {}

    public record UpdateUserRequest(String name, String email, String mobile, User.Role role, Boolean active) {}

    public record UpdateProfileRequest(@NotBlank String name, String email, String mobile) {}

    public record ChangePasswordRequest(@NotBlank String currentPassword, @NotBlank String newPassword) {}

    public record UserResponse(Long id, String name, String email, String mobile, User.Role role, boolean active, Instant createdAt) {
        public static UserResponse from(User u) {
            return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getMobile(), u.getRole(), u.isActive(), u.getCreatedAt());
        }
    }

    public record PageResponse<T>(List<T> content, long totalElements, int totalPages, int page, int size) {}
}
