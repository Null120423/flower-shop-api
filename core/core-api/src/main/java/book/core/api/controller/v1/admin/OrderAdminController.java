package book.core.api.controller.v1.admin;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import book.core.api.controller.v1.admin.dto.order.OrderItemDto;
import book.core.api.controller.v1.admin.dto.order.OrderResponseDto;
import book.core.api.controller.v1.admin.dto.order.UpdateOrderStatusDto;
import book.core.enums.OrderStatus;
import book.core.support.response.ApiResponse;
import book.storage.db.core.entity.OrderEntity;
import book.storage.db.core.entity.OrderItemEntity;
import book.storage.db.core.repository.OrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Order Management", description = "API for managing customer orders and order processing")
public class OrderAdminController extends AdminV1Controller {

    private final OrderRepository orderRepository;

    public OrderAdminController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Get all orders with pagination and filtering
    @GetMapping("/orders")
    @Operation(summary = "Get all orders", description = "Retrieve all orders with optional status filtering")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> getAllOrders(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Filter by order status") @RequestParam(required = false) OrderStatus status
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<OrderEntity> orders;
        if (status != null) {
            List<OrderEntity> filtered = orderRepository.findByStatus(status);
            orders = new PageImpl<>(filtered, pageable, filtered.size());
        } else {
            orders = orderRepository.findAll(pageable);
        }
        
        Page<OrderResponseDto> response = orders.map(this::toResponseDto);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // Get order by ID
    @GetMapping("/orders/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order with all details")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(
            @Parameter(description = "Order ID") @PathVariable UUID id
    ) {
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok(ApiResponse.ok(toResponseDto(order))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Order not found")));
    }

    // Get orders by user
    @GetMapping("/orders/user/{userId}")
    @Operation(summary = "Get user orders", description = "Retrieve all orders for a specific user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User orders retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> getOrdersByUser(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<OrderEntity> ordersList = orderRepository.findByUserId(userId);
        Page<OrderEntity> orders = new PageImpl<>(ordersList, pageable, ordersList.size());
        
        Page<OrderResponseDto> response = orders.map(this::toResponseDto);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // Update order status
    @PatchMapping("/orders/{id}/status")
    @Operation(summary = "Update order status", description = "Change the status of an order")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order status updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrderStatus(
            @Parameter(description = "Order ID") @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New order status") 
            @Valid @RequestBody UpdateOrderStatusDto request
    ) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(request.getStatus());
                    OrderEntity updated = orderRepository.save(order);
                    return ResponseEntity.ok(ApiResponse.ok(toResponseDto(updated)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Order not found")));
    }

    // Cancel order
    @PatchMapping("/orders/{id}/cancel")
    @Operation(summary = "Cancel order", description = "Cancel an order (only if not delivered or already cancelled)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Cannot cancel order in current status"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(
            @Parameter(description = "Order ID") @PathVariable UUID id
    ) {
        return orderRepository.findById(id)
                .map(order -> {
                    if (order.getStatus() == OrderStatus.DELIVERED || 
                        order.getStatus() == OrderStatus.CANCELLED) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.<OrderResponseDto>fail(null, 
                                    "Cannot cancel order with status: " + order.getStatus()));
                    }
                    
                    order.setStatus(OrderStatus.CANCELLED);
                    OrderEntity updated = orderRepository.save(order);
                    return ResponseEntity.ok(ApiResponse.ok(toResponseDto(updated)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Order not found")));
    }

    // Get pending orders (for admin attention)
    @GetMapping("/orders/pending")
    @Operation(summary = "Get pending orders", description = "Retrieve orders pending admin attention")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pending orders retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getPendingOrders() {
        List<OrderEntity> pending = orderRepository.findByStatus(OrderStatus.PENDING);
        
        List<OrderResponseDto> response = pending.stream()
                .map(this::toResponseDto)
                .limit(50)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    private OrderResponseDto toResponseDto(OrderEntity order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setUserEmail(order.getUser().getEmail());
        dto.setStatus(order.getStatus());
        dto.setSubtotal(order.getSubtotal());
        dto.setShippingCost(order.getShippingCost());
        dto.setDiscount(order.getDiscount());
        dto.setTotalAmount(order.getTotalAmount());
        
        if (order.getAppliedPromotion() != null) {
            dto.setPromotionCode(order.getAppliedPromotion().getCode());
        }
        
        if (order.getPayment() != null) {
            dto.setPaymentStatus(order.getPayment().getStatus());
        }
        
        if (order.getShipment() != null) {
            dto.setShipmentStatus(order.getShipment().getStatus());
            String fullAddress = order.getShipment().getAddressLine1() +
                    (order.getShipment().getAddressLine2() != null ? ", " + order.getShipment().getAddressLine2() : "") +
                    ", " + order.getShipment().getCity() +
                    (order.getShipment().getState() != null ? ", " + order.getShipment().getState() : "") +
                    " " + order.getShipment().getPostalCode() +
                    ", " + order.getShipment().getCountry();
            dto.setShippingAddress(fullAddress);
        }
        
        List<OrderItemDto> items = order.getItems().stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList());
        dto.setItems(items);
        
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        return dto;
    }

    private OrderItemDto toOrderItemDto(OrderItemEntity item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductSku(item.getProduct().getSku());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setSubtotal(item.getTotalPrice());
        return dto;
    }
}
