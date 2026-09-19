package in.hexarise.gb_enterprises.crm.controller;

import in.hexarise.gb_enterprises.crm.domain.User;
import in.hexarise.gb_enterprises.crm.dto.UserDto.*;
import in.hexarise.gb_enterprises.crm.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ── Admin: user management ──────────────────────────────────────────────
    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public PageResponse<UserResponse> list(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return userService.list(page, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public UserResponse create(@Valid @RequestBody CreateUserRequest req) { return userService.create(req); }

    @PatchMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public UserResponse update(@PathVariable Long id, @RequestBody UpdateUserRequest req) {
        return userService.update(id, req);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void delete(@PathVariable Long id) { userService.delete(id); }

    // ── Self: profile ───────────────────────────────────────────────────────
    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal User user) { return UserResponse.from(user); }

    @PatchMapping("/me")
    public UserResponse updateProfile(@AuthenticationPrincipal User user,
                                      @Valid @RequestBody UpdateProfileRequest req) {
        return userService.updateProfile(user, req);
    }

    @PostMapping("/me/change-password")
    public void changePassword(@AuthenticationPrincipal User user,
                               @Valid @RequestBody ChangePasswordRequest req) {
        userService.changePassword(user, req);
    }
}
