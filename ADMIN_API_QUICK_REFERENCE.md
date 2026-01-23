# Flower Shop Admin API - Quick Reference

## 🌸 Overview
Smart admin panel API for flower shop management with comprehensive features for products, inventory, orders, promotions, and analytics.

---

## 📊 Quick Stats Dashboard
```http
GET /v1/admin/dashboard/stats
```
One endpoint for all key metrics!

---

## 🌹 Product Management

| Action | Endpoint | Method |
|--------|----------|--------|
| List all products | `/v1/admin/products` | GET |
| Get product | `/v1/admin/products/{id}` | GET |
| Create product | `/v1/admin/products` | POST |
| Update product | `/v1/admin/products/{id}` | PUT |
| Delete product | `/v1/admin/products/{id}` | DELETE |
| Search products | `/v1/admin/products/search?query=rose` | GET |

---

## 📦 Inventory Management

| Action | Endpoint | Method |
|--------|----------|--------|
| View inventory | `/v1/admin/inventory` | GET |
| **Low stock alert** | `/v1/admin/inventory/low-stock` | GET |
| Update stock | `/v1/admin/inventory/{id}` | PUT |
| **Quick adjust** | `/v1/admin/inventory/{id}/adjust?quantity=10&operation=add` | PATCH |

💡 **Smart Feature**: Automatic low stock alerts when available ≤ minimum level

---

## 📋 Order Management

| Action | Endpoint | Method |
|--------|----------|--------|
| List orders | `/v1/admin/orders?status=PENDING` | GET |
| Get order details | `/v1/admin/orders/{id}` | GET |
| **Pending queue** | `/v1/admin/orders/pending` | GET |
| Update status | `/v1/admin/orders/{id}/status` | PATCH |
| Cancel order | `/v1/admin/orders/{id}/cancel` | PATCH |

Order Statuses: `PENDING` → `PAID` → `PROCESSING` → `SHIPPED` → `DELIVERED`

---

## 🎁 Promotion Management

| Action | Endpoint | Method |
|--------|----------|--------|
| List promotions | `/v1/admin/promotions?active=true` | GET |
| Create promotion | `/v1/admin/promotions` | POST |
| **Quick toggle** | `/v1/admin/promotions/{id}/toggle` | PATCH |
| Active promos | `/v1/admin/promotions/active` | GET |

Promotion Types: `PERCENTAGE` | `FIXED_AMOUNT`

---

## ⭐ Review Moderation

| Action | Endpoint | Method |
|--------|----------|--------|
| **Pending reviews** | `/v1/admin/reviews/pending` | GET |
| Quick approve | `/v1/admin/reviews/{id}/approve` | PATCH |
| Quick reject | `/v1/admin/reviews/{id}/reject` | PATCH |
| List all reviews | `/v1/admin/reviews?status=APPROVED` | GET |

Review Statuses: `PENDING` | `APPROVED` | `REJECTED`

---

## 🏷️ Category Management

| Action | Endpoint | Method |
|--------|----------|--------|
| List categories | `/v1/admin/categories` | GET |
| Create category | `/v1/admin/categories` | POST |
| Update category | `/v1/admin/categories/{id}` | PUT |
| Delete category | `/v1/admin/categories/{id}` | DELETE |

---

## 📈 Analytics Endpoints

| Metric | Endpoint |
|--------|----------|
| **Full dashboard** | `/v1/admin/dashboard/stats` |
| Sales summary | `/v1/admin/dashboard/sales` |
| Orders summary | `/v1/admin/dashboard/orders` |
| Products summary | `/v1/admin/dashboard/products` |
| Customers summary | `/v1/admin/dashboard/customers` |
| **Top sellers** | `/v1/admin/dashboard/top-products?limit=10` |
| **Low stock** | `/v1/admin/dashboard/low-stock` |
| Recent orders | `/v1/admin/dashboard/recent-orders?limit=10` |

---

## 💡 Smart Features

### 🔔 Inventory Alerts
Automatically tracks and alerts when:
- Stock ≤ minimum level
- Products out of stock
- Real-time dashboard widget

### ⚡ Quick Actions
- One-click approve/reject reviews
- Toggle promotion status
- Quick stock adjustments (±)
- Fast order status updates

### 📊 Real-time Analytics
- Today/Week/Month/Total sales
- Order status breakdown
- Top-selling products
- Customer growth tracking

### 🔍 Smart Search
- Search products by name or SKU
- Fuzzy matching
- Paginated results

---

## 🎯 Daily Workflow

### Morning Routine ☀️
1. Check dashboard: `/v1/admin/dashboard/stats`
2. Review pending orders: `/v1/admin/orders/pending`
3. Check low stock: `/v1/admin/inventory/low-stock`

### Order Processing 📦
1. Get pending: `/v1/admin/orders/pending`
2. Update status: `PATCH /v1/admin/orders/{id}/status`
3. Track completion: Dashboard shows delivered count

### Review Moderation ⭐
1. Get pending: `/v1/admin/reviews/pending`
2. Quick approve: `PATCH /v1/admin/reviews/{id}/approve`
3. Or reject: `PATCH /v1/admin/reviews/{id}/reject`

### Inventory Management 📦
1. Check alerts: `/v1/admin/inventory/low-stock`
2. Quick adjust: `PATCH /v1/admin/inventory/{id}/adjust?quantity=50&operation=add`
3. Update minimums: `PUT /v1/admin/inventory/{id}`

---

## 📝 Common Request Bodies

### Create Product
```json
{
  "name": "Red Rose Bouquet",
  "description": "Beautiful arrangement",
  "price": 49.99,
  "sku": "ROSE-RED-001",
  "type": "BOUQUET",
  "categoryIds": ["uuid1"],
  "stockQuantity": 50,
  "minStockLevel": 10
}
```

### Create Promotion
```json
{
  "code": "SUMMER2026",
  "name": "Summer Sale",
  "type": "PERCENTAGE",
  "value": 20.00,
  "startAt": "2026-06-01T00:00:00",
  "endAt": "2026-08-31T23:59:59",
  "active": true,
  "usageLimit": 1000
}
```

### Update Order Status
```json
{
  "status": "PROCESSING",
  "note": "Order is being prepared"
}
```

---

## 🎨 Response Format

### ✅ Success
```json
{
  "result": "SUCCESS",
  "data": { /* your data */ }
}
```

### ❌ Error
```json
{
  "result": "ERROR",
  "status": 404,
  "message": "Product not found"
}
```

---

## 🔐 Security Note

All endpoints require **ADMIN** role authentication.

---

## 🚀 Pro Tips

1. **Use pagination** for large lists (default: page=0, size=20)
2. **Filter by status** to narrow results
3. **Bookmark dashboard** for quick daily overview
4. **Monitor low stock** regularly to prevent stockouts
5. **Check pending queues** daily for timely responses
6. **Use search** for quick product lookup
7. **Review analytics** weekly for trends

---

## 📱 Key Metrics to Track

- 📈 **Sales**: Today, Week, Month trends
- 📦 **Orders**: Pending count (action required!)
- 🏪 **Inventory**: Low stock alerts
- 👥 **Customers**: New signups
- ⭐ **Reviews**: Pending moderation count
- 🎁 **Promotions**: Active campaigns

---

## 🎯 Priority Actions

| Priority | Action | Endpoint |
|----------|--------|----------|
| 🔴 High | Pending orders | `/v1/admin/orders/pending` |
| 🔴 High | Low stock | `/v1/admin/inventory/low-stock` |
| 🟡 Medium | Pending reviews | `/v1/admin/reviews/pending` |
| 🟢 Low | Analytics review | `/v1/admin/dashboard/stats` |

---

## 💼 Flower Shop Specific Features

### Seasonal Management
- Create promotions for Valentine's Day, Mother's Day
- Track seasonal flower popularity
- Adjust inventory for peak seasons

### Wedding & Event Orders
- Filter large orders
- Track delivery schedules
- Monitor custom arrangements

### Fresh Flower Tracking
- Low stock alerts for perishable inventory
- Quick stock adjustments for deliveries
- Automated reorder suggestions (future)

---

**Happy Managing! 🌸**

For detailed documentation, see [ADMIN_API_DOCUMENTATION.md](./ADMIN_API_DOCUMENTATION.md)
