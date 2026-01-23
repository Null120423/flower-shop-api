# Flower Shop Admin API Documentation

## Overview
This is a comprehensive admin API for managing a flower shop. The API provides smart management capabilities for products, inventory, orders, promotions, reviews, and analytics.

## Base URL
```
/v1/admin
```

## API Endpoints

### 1. Product Management

#### Get All Products (with pagination)
```http
GET /v1/admin/products?page=0&size=20&sortBy=createdAt&sortDirection=DESC
```

#### Get Product by ID
```http
GET /v1/admin/products/{id}
```

#### Create Product
```http
POST /v1/admin/products
Content-Type: application/json

{
  "name": "Red Rose Bouquet",
  "description": "Beautiful red roses arrangement",
  "price": 49.99,
  "sku": "ROSE-RED-001",
  "type": "BOUQUET",
  "imageUrl": "https://example.com/rose.jpg",
  "categoryIds": ["uuid1", "uuid2"],
  "stockQuantity": 50,
  "minStockLevel": 10
}
```

#### Update Product
```http
PUT /v1/admin/products/{id}
Content-Type: application/json

{
  "name": "Red Rose Bouquet - Premium",
  "price": 59.99,
  ...
}
```

#### Delete Product (Soft Delete)
```http
DELETE /v1/admin/products/{id}
```

#### Search Products
```http
GET /v1/admin/products/search?query=rose&page=0&size=20
```

---

### 2. Category Management

#### Get All Categories
```http
GET /v1/admin/categories
```

#### Get Category by ID
```http
GET /v1/admin/categories/{id}
```

#### Create Category
```http
POST /v1/admin/categories
Content-Type: application/json

{
  "name": "Wedding Flowers",
  "slug": "wedding-flowers",
  "description": "Perfect flowers for your special day"
}
```

#### Update Category
```http
PUT /v1/admin/categories/{id}
Content-Type: application/json

{
  "name": "Wedding Flowers Premium",
  "slug": "wedding-flowers-premium",
  "description": "Luxury flowers for weddings"
}
```

#### Delete Category
```http
DELETE /v1/admin/categories/{id}
```

---

### 3. Inventory Management

#### Get All Inventory
```http
GET /v1/admin/inventory?page=0&size=20
```

#### Get Low Stock Products
```http
GET /v1/admin/inventory/low-stock
```
Returns products where available quantity ≤ minimum stock level

#### Get Inventory by Product ID
```http
GET /v1/admin/inventory/product/{productId}
```

#### Update Inventory
```http
PUT /v1/admin/inventory/{id}
Content-Type: application/json

{
  "stockQuantity": 100,
  "minStockLevel": 20
}
```

#### Adjust Stock (Add/Subtract)
```http
PATCH /v1/admin/inventory/{id}/adjust?quantity=10&operation=add
```
Operations: `add` or `subtract`

---

### 4. Order Management

#### Get All Orders
```http
GET /v1/admin/orders?page=0&size=20&status=PENDING
```
Status filter (optional): PENDING, PAID, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED

#### Get Order by ID
```http
GET /v1/admin/orders/{id}
```

#### Get Orders by User
```http
GET /v1/admin/orders/user/{userId}?page=0&size=20
```

#### Update Order Status
```http
PATCH /v1/admin/orders/{id}/status
Content-Type: application/json

{
  "status": "PROCESSING",
  "note": "Order is being prepared"
}
```

#### Cancel Order
```http
PATCH /v1/admin/orders/{id}/cancel
```

#### Get Pending Orders
```http
GET /v1/admin/orders/pending
```
Returns orders requiring admin attention

---

### 5. Promotion Management

#### Get All Promotions
```http
GET /v1/admin/promotions?page=0&size=20&active=true
```

#### Get Promotion by ID
```http
GET /v1/admin/promotions/{id}
```

#### Create Promotion
```http
POST /v1/admin/promotions
Content-Type: application/json

{
  "code": "SUMMER2026",
  "name": "Summer Sale 2026",
  "description": "Get 20% off on all flower bouquets",
  "type": "PERCENTAGE",
  "value": 20.00,
  "startAt": "2026-06-01T00:00:00",
  "endAt": "2026-08-31T23:59:59",
  "active": true,
  "usageLimit": 1000,
  "productIds": ["uuid1", "uuid2"]
}
```

Promotion Types:
- `PERCENTAGE`: Discount percentage (e.g., 20% off)
- `FIXED_AMOUNT`: Fixed discount amount (e.g., $10 off)

#### Update Promotion
```http
PUT /v1/admin/promotions/{id}
Content-Type: application/json
```

#### Toggle Promotion Active Status
```http
PATCH /v1/admin/promotions/{id}/toggle
```

#### Delete Promotion
```http
DELETE /v1/admin/promotions/{id}
```

#### Get Active Promotions
```http
GET /v1/admin/promotions/active
```

---

### 6. Review Management

#### Get All Reviews
```http
GET /v1/admin/reviews?page=0&size=20&status=PENDING
```
Status filter (optional): PENDING, APPROVED, REJECTED

#### Get Review by ID
```http
GET /v1/admin/reviews/{id}
```

#### Get Reviews by Product
```http
GET /v1/admin/reviews/product/{productId}
```

#### Get Pending Reviews (for moderation)
```http
GET /v1/admin/reviews/pending
```

#### Update Review Status
```http
PATCH /v1/admin/reviews/{id}/status
Content-Type: application/json

{
  "status": "APPROVED",
  "adminNote": "Review approved after verification"
}
```

#### Approve Review
```http
PATCH /v1/admin/reviews/{id}/approve
```

#### Reject Review
```http
PATCH /v1/admin/reviews/{id}/reject
```

#### Delete Review
```http
DELETE /v1/admin/reviews/{id}
```

---

### 7. Dashboard & Analytics

#### Get Dashboard Stats (Comprehensive)
```http
GET /v1/admin/dashboard/stats
```

Returns:
- Sales summary (today, this week, this month, total)
- Orders summary (total, pending, processing, delivered, cancelled)
- Products summary (total, low stock, out of stock)
- Customers summary (total, new this month)
- Top selling products
- Low stock products
- Recent orders

#### Get Sales Summary
```http
GET /v1/admin/dashboard/sales
```

Response:
```json
{
  "result": "SUCCESS",
  "data": {
    "todaySales": 1250.00,
    "thisWeekSales": 8500.00,
    "thisMonthSales": 35000.00,
    "totalSales": 450000.00
  }
}
```

#### Get Orders Summary
```http
GET /v1/admin/dashboard/orders
```

#### Get Products Summary
```http
GET /v1/admin/dashboard/products
```

#### Get Customers Summary
```http
GET /v1/admin/dashboard/customers
```

#### Get Top Selling Products
```http
GET /v1/admin/dashboard/top-products?limit=10
```

#### Get Low Stock Products
```http
GET /v1/admin/dashboard/low-stock
```

#### Get Recent Orders
```http
GET /v1/admin/dashboard/recent-orders?limit=10
```

---

## Response Format

All endpoints return responses in the following format:

### Success Response
```json
{
  "result": "SUCCESS",
  "data": { /* response data */ },
  "status": null,
  "message": null
}
```

### Error Response
```json
{
  "result": "ERROR",
  "data": null,
  "status": 404,
  "message": "Product not found"
}
```

---

## Data Models

### Product
```json
{
  "id": "uuid",
  "name": "Red Rose Bouquet",
  "description": "Beautiful arrangement",
  "price": 49.99,
  "sku": "ROSE-RED-001",
  "type": "BOUQUET",
  "imageUrl": "https://...",
  "categoryNames": ["Wedding", "Romance"],
  "stockQuantity": 50,
  "availableQuantity": 45,
  "minStockLevel": 10,
  "createdAt": "2026-01-15T10:00:00",
  "updatedAt": "2026-01-20T15:30:00"
}
```

### Order
```json
{
  "id": "uuid",
  "userEmail": "customer@example.com",
  "status": "PROCESSING",
  "subtotal": 99.98,
  "shippingCost": 10.00,
  "discount": 10.00,
  "totalAmount": 99.98,
  "promotionCode": "SUMMER2026",
  "paymentStatus": "COMPLETED",
  "shipmentStatus": "PENDING",
  "shippingAddress": "123 Main St, City",
  "items": [
    {
      "id": "uuid",
      "productName": "Red Rose Bouquet",
      "productSku": "ROSE-RED-001",
      "quantity": 2,
      "unitPrice": 49.99,
      "subtotal": 99.98
    }
  ],
  "createdAt": "2026-01-23T10:00:00",
  "updatedAt": "2026-01-23T11:00:00"
}
```

---

## Smart Features

### 1. Inventory Alerts
- Automatic low stock detection
- Alerts when available quantity ≤ minimum stock level
- Dashboard widget for quick overview

### 2. Order Management
- Filter orders by status
- Quick actions for order processing
- Pending orders queue for admin attention
- Order cancellation with validation

### 3. Promotion Management
- Time-based promotions (start/end dates)
- Usage limits and tracking
- Apply to specific products or all products
- Easy activation/deactivation

### 4. Review Moderation
- Pending reviews queue
- Quick approve/reject actions
- Product quality monitoring

### 5. Analytics Dashboard
- Real-time sales metrics
- Performance tracking (daily, weekly, monthly)
- Top-selling products analysis
- Customer growth tracking
- Inventory health monitoring

### 6. Smart Search
- Product search by name or SKU
- Fuzzy matching for better results
- Pagination support for large datasets

---

## Business Workflows

### Daily Operations
1. Check dashboard for today's metrics
2. Review pending orders → process → update status
3. Check low stock alerts → reorder flowers
4. Moderate pending reviews
5. Monitor top-selling products

### Weekly Tasks
1. Review weekly sales performance
2. Adjust inventory levels based on trends
3. Create/update promotions for upcoming events
4. Analyze customer feedback from reviews

### Monthly Planning
1. Review monthly sales and growth
2. Identify best-selling flowers
3. Plan seasonal promotions
4. Analyze customer acquisition

---

## Authentication & Authorization

**Note:** These admin endpoints should be secured with proper authentication and role-based access control (RBAC). Only users with ADMIN role should be able to access these endpoints.

Suggested implementation:
```java
@PreAuthorize("hasRole('ADMIN')")
```

---

## Best Practices

1. **Pagination**: Use for large datasets (products, orders, reviews)
2. **Filtering**: Apply status filters to narrow down results
3. **Soft Deletes**: Products, categories, and promotions use soft delete
4. **Validation**: All input is validated using Jakarta Validation
5. **Error Handling**: Comprehensive error messages for debugging
6. **Real-time Data**: Dashboard provides up-to-date metrics

---

## Future Enhancements

- [ ] Bulk operations (bulk update, bulk delete)
- [ ] Export data to CSV/Excel
- [ ] Email notifications for low stock
- [ ] Advanced analytics with charts
- [ ] Sales forecasting
- [ ] Automated reordering
- [ ] Customer segmentation
- [ ] Multi-language support
- [ ] Multi-currency support

---

## Technologies Used

- **Framework**: Spring Boot
- **Database**: JPA/Hibernate
- **Validation**: Jakarta Validation
- **Lombok**: Reduce boilerplate
- **Pagination**: Spring Data Pageable

---

## Support

For questions or issues, please contact the development team or refer to the main project documentation.
