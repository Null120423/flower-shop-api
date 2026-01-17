package book.core.api.controller.v1.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import  org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


@RestController
public class DemoController {

    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> health(Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", Instant.now());
        response.put("service", "core-api");
        response.put("user", auth.getName());
        response.put("roles", auth.getAuthorities());
        response.put("UID", auth.getPrincipal());

        return ResponseEntity.ok(response);
    }

}
