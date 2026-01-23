package book.core.api.controller.v1.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import book.core.api.controller.v1.admin.dto.dashboard.CustomersSummary;
import book.core.api.controller.v1.admin.dto.dashboard.DashboardStatsDto;
import book.core.api.controller.v1.admin.dto.dashboard.LowStockProductDto;
import book.core.api.controller.v1.admin.dto.dashboard.OrdersSummary;
import book.core.api.controller.v1.admin.dto.dashboard.ProductsSummary;
import book.core.api.controller.v1.admin.dto.dashboard.RecentOrderDto;
import book.core.api.controller.v1.admin.dto.dashboard.SalesSummary;
import book.core.api.controller.v1.admin.dto.dashboard.TopProductDto;
import book.core.enums.OrderStatus;
import book.core.support.response.ApiResponse;
import book.storage.db.core.repository.InventoryRepository;
import book.storage.db.core.repository.OrderRepository;
import book.storage.db.core.repository.ProductRepository;
import book.storage.db.core.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Dashboard & Analytics", description = "API for dashboard statistics and business analytics")
public class DashboardAdminController extends AdminV1Controller {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;

    public DashboardAdminController(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            InventoryRepository inventoryRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
    }

    // Get comprehensive dashboard statistics
    @GetMapping("/dashboard/stats")
    @Operation(summary = "Get dashboard statistics", description = "Retrieve comprehensive dashboard data in a single call")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dashboard statistics retrieved successfully")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getDashboardStats() {
        DashboardStatsDto stats = DashboardStatsDto.builder()
                .salesSummary(buildSalesSummary())
                .ordersSummary(getOrdersSummaryData())
                .productsSummary(getProductsSummaryData())
                .customersSummary(getCustomersSummaryData())
                .topSellingProducts(getTopSellingProductsList())
                .lowStockProducts(getLowStockProductsList())
                .recentOrders(getRecentOrdersList(10))
                .build();
        
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }

    // Get sales summary
    @GetMapping("/dashboard/sales")
    @Operation(summary = "Get sales summary", description = "Retrieve sales metrics for today, week, month, and total")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sales summary retrieved successfully")
    public ResponseEntity<ApiResponse<SalesSummary>> getSalesSummary() {
        SalesSummary summary = buildSalesSummary();
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }

    // Get orders summary
    @GetMapping("/dashboard/orders")
    @Operation(summary = "Get orders summary", description = "Retrieve order status distribution and metrics")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orders summary retrieved successfully")
    public ResponseEntity<ApiResponse<OrdersSummary>> getOrdersSummary() {
        OrdersSummary summary = getOrdersSummaryData();
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }

    // Get products summary
    @GetMapping("/dashboard/products")
    @Operation(summary = "Get products summary", description = "Retrieve product inventory and status metrics")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products summary retrieved successfully")
    public ResponseEntity<ApiResponse<ProductsSummary>> getProductsSummary() {
        ProductsSummary summary = getProductsSummaryData();
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }

    // Get customers summary
    @GetMapping("/dashboard/customers")
    @Operation(summary = "Get customers summary", description = "Retrieve customer growth and engagement metrics")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customers summary retrieved successfully")
    public ResponseEntity<ApiResponse<CustomersSummary>> getCustomersSummary() {
        CustomersSummary summary = getCustomersSummaryData();
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }

    // Get top selling products
    @GetMapping("/dashboard/top-products")
    @Operation(summary = "Get top selling products", description = "Retrieve best-performing products")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Top products retrieved successfully")
    public ResponseEntity<ApiResponse<List<TopProductDto>>> getTopSellingProducts(
            @Parameter(description = "Number of top products to return") @RequestParam(defaultValue = "10") int limit
    ) {
        List<TopProductDto> topProducts = getTopSellingProductsList()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.ok(topProducts));
    }

    // Get low stock products
    @GetMapping("/dashboard/low-stock")
    @Operation(summary = "Get low stock products", description = "Retrieve products with inventory below minimum threshold")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Low stock products retrieved successfully")
    public ResponseEntity<ApiResponse<List<LowStockProductDto>>> getLowStockProducts() {
        List<LowStockProductDto> lowStock = getLowStockProductsList();
        return ResponseEntity.ok(ApiResponse.ok(lowStock));
    }

    // Get recent orders
    @GetMapping("/dashboard/recent-orders")
    @Operation(summary = "Get recent orders", description = "Retrieve most recent customer orders")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Recent orders retrieved successfully")
    public ResponseEntity<ApiResponse<List<RecentOrderDto>>> getRecentOrders(
            @Parameter(description = "Number of recent orders to return") @RequestParam(defaultValue = "10") int limit
    ) {
        List<RecentOrderDto> recent = getRecentOrdersList(limit);
        return ResponseEntity.ok(ApiResponse.ok(recent));
    }

    // Helper methods
    private SalesSummary buildSalesSummary() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfToday = now.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime startOfWeek = now.minusWeeks(1);
        LocalDateTime startOfMonth = now.minusMonths(1);
        
        BigDecimal todaySales = calculateSalesForPeriod(startOfToday, now);
        BigDecimal thisWeekSales = calculateSalesForPeriod(startOfWeek, now);
        BigDecimal thisMonthSales = calculateSalesForPeriod(startOfMonth, now);
        BigDecimal totalSales = calculateTotalSales();
        
        return SalesSummary.builder()
                .todaySales(todaySales)
                .thisWeekSales(thisWeekSales)
                .thisMonthSales(thisMonthSales)
                .totalSales(totalSales)
                .build();
    }

    private OrdersSummary getOrdersSummaryData() {
        return OrdersSummary.builder()
                .totalOrders(orderRepository.count())
                .pendingOrders(orderRepository.countByStatus(OrderStatus.PENDING))
                .processingOrders(orderRepository.countByStatus(OrderStatus.PROCESSING))
                .deliveredOrders(orderRepository.countByStatus(OrderStatus.DELIVERED))
                .cancelledOrders(orderRepository.countByStatus(OrderStatus.CANCELLED))
                .build();
    }

    private ProductsSummary getProductsSummaryData() {
        long lowStock = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getAvailableQuantity() <= inv.getMinStockLevel() && inv.getAvailableQuantity() > 0)
                .count();
        
        long outOfStock = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getAvailableQuantity() == 0)
                .count();
        
        return ProductsSummary.builder()
                .totalProducts(productRepository.count())
                .lowStockProducts(lowStock)
                .outOfStockProducts(outOfStock)
                .build();
    }

    private CustomersSummary getCustomersSummaryData() {
        LocalDateTime startOfMonth = LocalDateTime.now().minusMonths(1);
        
        long newCustomers = userRepository.findAll().stream()
                .filter(user -> user.getCreatedAt().isAfter(startOfMonth))
                .count();
        
        return CustomersSummary.builder()
                .totalCustomers(userRepository.count())
                .newCustomersThisMonth(newCustomers)
                .build();
    }

    private List<LowStockProductDto> getLowStockProductsList() {
        return inventoryRepository.findAll().stream()
                .filter(inv -> inv.getAvailableQuantity() <= inv.getMinStockLevel())
                .map(inv -> LowStockProductDto.builder()
                        .productId(inv.getProduct().getId())
                        .productName(inv.getProduct().getName())
                        .productSku(inv.getProduct().getSku())
                        .stockQuantity(inv.getStockQuantity())
                        .minStockLevel(inv.getMinStockLevel())
                        .build())
                .collect(Collectors.toList());
    }

    private List<RecentOrderDto> getRecentOrdersList(int limit) {
        return orderRepository.findAll(
                PageRequest.of(0, limit, org.springframework.data.domain.Sort.by("createdAt").descending())
        ).stream()
                .map(order -> RecentOrderDto.builder()
                        .orderId(order.getId())
                        .customerEmail(order.getUser().getEmail())
                        .status(order.getStatus())
                        .totalAmount(order.getTotalAmount())
                        .createdAt(order.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
    private BigDecimal calculateSalesForPeriod(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt().isAfter(start) && order.getCreatedAt().isBefore(end))
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .map(order -> order.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalSales() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .map(order -> order.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<TopProductDto> getTopSellingProductsList() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getProduct(),
                        Collectors.summingLong(item -> item.getQuantity().longValue())
                ))
                .entrySet().stream()
                .map(entry -> {
                    BigDecimal revenue = orderRepository.findAll().stream()
                            .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                            .flatMap(order -> order.getItems().stream())
                            .filter(item -> item.getProduct().getId().equals(entry.getKey().getId()))
                            .map(item -> item.getTotalPrice()) // Use getTotalPrice() instead of getSubtotal()
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    return TopProductDto.builder()
                            .productId(entry.getKey().getId())
                            .productName(entry.getKey().getName())
                            .productSku(entry.getKey().getSku())
                            .totalSold(entry.getValue())
                            .totalRevenue(revenue)
                            .build();
                })
                .sorted((a, b) -> Long.compare(b.getTotalSold(), a.getTotalSold()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
