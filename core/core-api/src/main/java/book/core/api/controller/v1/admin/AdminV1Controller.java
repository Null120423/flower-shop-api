package book.core.api.controller.v1.admin;

import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Base controller for all admin API endpoints
 */
@RequestMapping("/v1/admin")
@Tag(name = "Admin API", description = "Base controller for flower shop admin operations")
public class AdminV1Controller {
}
