package in.hexarise.gb_enterprises.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class KeepAliveService {

    private static final Logger log = LoggerFactory.getLogger(KeepAliveService.class);

    // Render automatically provides RENDER_EXTERNAL_URL (e.g.
    // https://crm-backend.onrender.com)
    // You can also set BACKEND_URL or app.backend-url manually
    @Value("${app.backend-url:${RENDER_EXTERNAL_URL:${BACKEND_URL:}}}")
    private String backendUrl;

    @Value("${server.servlet.context-path:/api/v1}")
    private String contextPath;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * Pings the public endpoint every 50 seconds (50,000 ms)
     * Starts 1 minute after server boot to allow full startup.
     */
    @Scheduled(fixedRate = 50000, initialDelay = 50000)
    public void pingSelf() {
        if (backendUrl == null || backendUrl.isBlank()) {
            log.debug("Keep-alive self-ping skipped: Backend URL not configured (RENDER_EXTERNAL_URL is empty)");
            return;
        }

        try {
            String normalizedBase = backendUrl.trim().replaceAll("/+$", "");
            String normalizedContext = (contextPath != null) ? contextPath.trim().replaceAll("^/+|/+$", "") : "";

            String pingUrl;
            if (normalizedContext.isEmpty()) {
                pingUrl = normalizedBase + "/ping";
            } else if (normalizedBase.endsWith("/" + normalizedContext)) {
                pingUrl = normalizedBase + "/ping";
            } else {
                pingUrl = normalizedBase + "/" + normalizedContext + "/ping";
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(pingUrl))
                    .header("User-Agent", "CRM-KeepAlive-Scheduler/1.0")
                    .GET()
                    .timeout(Duration.ofSeconds(15))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Keep-alive ping sent to [{}]. Status code: {}", pingUrl, response.statusCode());
        } catch (Exception e) {
            log.warn("Keep-alive ping failed: {}", e.getMessage());
        }
    }
}
