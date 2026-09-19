package in.hexarise.gb_enterprises.crm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class PingController {

    @GetMapping({"/ping", "/health"})
    public Map<String, Object> ping() {
        return Map.of(
            "status", "UP",
            "message", "pong",
            "timestamp", Instant.now().toString()
        );
    }
}
