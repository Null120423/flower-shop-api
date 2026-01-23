# 🌸 Swagger Quick Start

## Access the API Documentation

### Step 1: Start the Application
```bash
cd /Volumes/SAMSUNG_SSD/FLOWER-SHOP/flower-shop-api
./gradlew bootRun
```

### Step 2: Open Swagger UI
Navigate to your browser:
```
http://localhost:8080/swagger-ui.html
```

### Step 3: Authenticate (if JWT is enabled)
1. Click the **Authorize** button
2. Enter your JWT token:
   ```
   Bearer YOUR_JWT_TOKEN_HERE
   ```
3. Click **Authorize**

## Explore the API

### Navigate by Tags
Left sidebar shows all endpoint categories:
- ✅ Product Management
- ✅ Category Management
- ✅ Inventory Management
- ✅ Order Management
- ✅ Promotion Management
- ✅ Review Management
- ✅ Dashboard & Analytics

### Try Out Endpoints
1. Click any endpoint to expand it
2. Click **Try it out** button
3. Fill in parameters if needed
4. Click **Execute**
5. See live response

## Example Workflows

### Workflow 1: Check Dashboard Stats
```
GET /v1/admin/dashboard/stats
```
Get all metrics in one call:
- Sales summary
- Order breakdown
- Inventory health
- Top products
- Low stock alerts

### Workflow 2: Manage Low Stock
```
GET /v1/admin/inventory/low-stock
```
Then adjust stock:
```
PATCH /v1/admin/inventory/{id}/adjust?quantity=100&operation=add
```

### Workflow 3: Moderate Reviews
```
GET /v1/admin/reviews/pending
```
Then approve or reject:
```
PATCH /v1/admin/reviews/{id}/approve
PATCH /v1/admin/reviews/{id}/reject
```

### Workflow 4: Search Products
```
GET /v1/admin/products/search?query=rose&page=0&size=10
```

## Available Endpoints

| Category | Count | Key Endpoints |
|----------|-------|---------------|
| Products | 6 | search, pagination |
| Categories | 5 | basic CRUD |
| Inventory | 5 | low-stock alert, adjust |
| Orders | 6 | status update, pending queue |
| Promotions | 7 | toggle, active list |
| Reviews | 9 | moderation queue, approve/reject |
| Dashboard | 8 | all-in-one stats |
| **TOTAL** | **44** | ⭐ Production Ready |

## Tips & Tricks

### 🔍 Search
Use the search box at top to find endpoints by name

### 📋 Filter by Tag
Click any tag in left sidebar to filter

### 💾 Copy cURL
Click the cURL button to copy command

### 📊 View Schema
Scroll down to see request/response schemas

### ✔️ Required Fields
Required fields are marked with *

## Common Issues

**❌ "401 Unauthorized"**
→ Add JWT token in Authorize button

**❌ "404 Not Found"**
→ Check endpoint path and parameters

**❌ Swagger UI blank**
→ Ensure Spring Boot is running on :8080

## Alternative Access Methods

### JSON Format
```
http://localhost:8080/v3/api-docs
```

### YAML Format
```
http://localhost:8080/v3/api-docs.yaml
```

### Download Spec
```bash
curl http://localhost:8080/v3/api-docs > api-spec.json
```

---

**Happy exploring your API! 🎉**
