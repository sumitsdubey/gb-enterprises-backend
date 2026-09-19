package in.hexarise.gb_enterprises.crm.controller;

import in.hexarise.gb_enterprises.crm.domain.User;
import in.hexarise.gb_enterprises.crm.dto.LeadDto.*;
import in.hexarise.gb_enterprises.crm.dto.UserDto.PageResponse;
import in.hexarise.gb_enterprises.crm.service.LeadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @GetMapping
    public PageResponse<LeadResponse> list(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String mobile,
        @RequestParam(required = false) Boolean valid,
        @RequestParam(required = false) Boolean alreadyInstalled,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size) {
        return leadService.search(name, mobile, valid, alreadyInstalled, page, size);
    }

    @GetMapping("/{id}")
    public LeadResponse get(@PathVariable Long id) { return leadService.get(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeadResponse create(@Valid @RequestBody CreateLeadRequest req,
                               @AuthenticationPrincipal User actor) {
        return leadService.create(req, actor);
    }

    @PatchMapping("/{id}")
    public LeadResponse update(@PathVariable Long id, @RequestBody UpdateLeadRequest req) {
        return leadService.update(id, req);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public List<LeadResponse> upload(@RequestParam("file") MultipartFile file,
                                     @AuthenticationPrincipal User actor) {
        return leadService.upload(file, actor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public void delete(@PathVariable Long id) {
        leadService.delete(id);
    }
}
