package in.hexarise.gb_enterprises.crm.controller;

import in.hexarise.gb_enterprises.crm.dto.DashboardDto.DashboardResponse;
import in.hexarise.gb_enterprises.crm.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public DashboardResponse getDashboardData() {
        return dashboardService.getDashboardData();
    }
}
