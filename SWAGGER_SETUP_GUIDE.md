# 🌸 Swagger/OpenAPI Setup Guide

## Overview
Swagger has been successfully added to your Flower Shop Admin API project. This enables automatic API documentation with an interactive UI.

## What Was Added

### 1. **Dependency**
Added Springdoc OpenAPI to `core/core-api/build.gradle`:
```gradle
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0'
```

### 2. **Configuration Class**
Created `SwaggerConfig.java` that provides:
- API title and version
- Contact information
- License details
- JWT authentication scheme

### 3. **Controller Annotations**
Added Swagger annotations to all admin controllers:
- `@Tag` - Groups endpoints by feature
- `@Operation` - Describes each endpoint
- `@ApiResponse` - Documents response codes
- `@Parameter` - Describes request parameters
- `@RequestBody` - Documents request bodies

## Accessing Swagger UI

### 1. **Swagger UI Dashboard**
```
http://localhost:8080/swagger-ui.html
```

### 2. **API Documentation (JSON)**
```
http://localhost:8080/v3/api-docs
```

### 3. **API Documentation (YAML)**
```
http://localhost:8080/v3/api-docs.yaml
```

## Features Available in Swagger UI

✅ **Interactive API Testing**
- Try out endpoints directly from the UI
- See real-time responses
- Test with different parameters

✅ **Complete Documentation**
- Request/response schemas
- Required/optional fields
- Data types and validation rules
- Status codes and error messages

✅ **Authentication Support**
- JWT Bearer token support
- Click "Authorize" to add your token
- Secured endpoints clearly marked

✅ **Search & Filter**
- Search endpoints by name
- Filter by tags (Product, Category, etc.)
- Quick navigation

✅ **Schema Documentation**
- Full DTO class documentation
- Required fields highlighted
- Example values

## API Endpoints Documented

### Product Management (6 endpoints)
- Get all products (paginated)
- Get product by ID
- Create product
- Update product
- Delete product
- Search products

### Category Management (5 endpoints)
- Get all categories
- Get category by ID
- Create category
- Update category
- Delete category

### Inventory Management (5 endpoints)
- Get all inventory
- Get low-stock products ⭐ (smart alert)
- Get inventory by product
- Update inventory
- Adjust stock (add/subtract)

### Order Management (6 endpoints)
- Get all orders (with filters)
- Get order by ID
- Get orders by user
- Update order status
- Cancel order
- Get pending orders ⭐ (queue)

### Promotion Management (7 endpoints)
- Get all promotions
- Get promotion by ID
- Create promotion
- Update promotion
- Toggle promotion status ⭐ (quick action)
- Delete promotion
- Get active promotions

### Review Management (9 endpoints)
- Get all reviews
- Get review by ID
- Get reviews by product
- Get pending reviews ⭐ (moderation queue)
- Update review status
- Approve review ⭐ (quick action)
- Reject review ⭐ (quick action)
- Delete review

### Dashboard & Analytics (8 endpoints)
- Get dashboard statistics ⭐ (all-in-one)
- Get sales summary
- Get orders summary
- Get products summary
- Get customers summary
- Get top products
- Get low stock alerts
- Get recent orders

## Key Swagger Features for Your API

### 🎯 All-in-One Endpoint
```
GET /v1/admin/dashboard/stats
```
Single endpoint to get all dashboard data:
- Sales metrics (today/week/month/total)
- Order status breakdown
- Product inventory health
- Customer metrics
- Top selling products
- Low stock alerts
- Recent orders

### ⚡ Quick Action Endpoints
```
PATCH /v1/admin/promotions/{id}/toggle
PATCH /v1/admin/reviews/{id}/approve
PATCH /v1/admin/reviews/{id}/reject
PATCH /v1/admin/inventory/{id}/adjust
```

### 🔔 Alert/Queue Endpoints
```
GET /v1/admin/inventory/low-stock
GET /v1/admin/orders/pending
GET /v1/admin/reviews/pending
```

## Example API Calls

### 1. Test Dashboard
```bash
curl -X GET "http://localhost:8080/v1/admin/dashboard/stats" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json"
```

### 2. Search Products
```bash
curl -X GET "http://localhost:8080/v1/admin/products/search?query=rose&page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 3. Adjust Inventory
```bash
curl -X PATCH "http://localhost:8080/v1/admin/inventory/{id}/adjust?quantity=50&operation=add" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4. Approve Review
```bash
curl -X PATCH "http://localhost:8080/v1/admin/reviews/{id}/approve" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Authentication in Swagger

1. Click the **Authorize** button in Swagger UI
2. Enter your JWT token in the following format:
   ```
   Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
3. Click **Authorize**
4. All subsequent requests will include the token

## Configuration Options

Located in application.yml or application-{env}.yml:

```yaml
springdoc:
  swagger-ui:
    enabled: true
    path: /swagger-ui.html
    operations-sorter: method  # method, alpha, or none
    tags-sorter: alpha         # alpha or none
    display-request-duration: true
    show-extensions: true
  api-docs:
    path: /v3/api-docs
  show-actuator: false
```

## Building OpenAPI Spec Programmatically

If you want to build additional OpenAPI features, the `SwaggerConfig.java` can be extended:

```java
// Already configured with:
- API info (title, version, description)
- Contact information
- License details
- JWT Bearer authentication scheme
```

## Production Considerations

### Security
- Don't expose Swagger UI in production
- Add authentication to Swagger endpoints
- Disable API docs in production if needed

### Configuration
```yaml
# application-prod.yml
springdoc:
  swagger-ui:
    enabled: false
  api-docs:
    enabled: false
```

### Alternative
```java
@Configuration
public class SwaggerSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(authz -> authz
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        );
        return http.build();
    }
}
```

## Troubleshooting

### Swagger UI not loading
✓ Ensure dependency is added to build.gradle
✓ Check Spring Boot is running on correct port
✓ Try accessing http://localhost:8080/swagger-ui/index.html

### Missing endpoints
✓ Ensure `@RestController` is on class
✓ Check `@Operation` annotations are present
✓ Verify endpoints are not excluded from component scan

### Authentication issues
✓ Ensure JWT is properly formatted
✓ Check token has not expired
✓ Verify token is for ADMIN role

## Next Steps

1. ✅ Run the application
2. ✅ Access http://localhost:8080/swagger-ui.html
3. ✅ Authorize with JWT token
4. ✅ Test endpoints directly from Swagger UI
5. ✅ Share API documentation with frontend team

## Documentation Files

- `SwaggerConfig.java` - Swagger configuration bean
- `swagger-config.yml` - Springdoc configuration reference
- All controllers have `@Tag` and `@Operation` annotations

## Additional Resources

- [Springdoc-OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI 3.0 Specification](https://spec.openapis.org/oas/v3.0.3)
- [Swagger UI Guide](https://swagger.io/tools/swagger-ui/)

---

## Quick Commands

### Build Project
```bash
./gradlew clean build
```

### Run Application
```bash
./gradlew bootRun
```

### Access Swagger
```
http://localhost:8080/swagger-ui.html
```

### Get API Docs (JSON)
```bash
curl http://localhost:8080/v3/api-docs
```

### Get API Docs (YAML)
```bash
curl http://localhost:8080/v3/api-docs.yaml
```

---

**🌸 Your Flower Shop API is now fully documented with Swagger!** 🌸
