package book.storage.db.core.entity;

import book.core.enums.ProductType;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "products",
        uniqueConstraints = @UniqueConstraint(columnNames = "sku"),
        indexes = {
                @Index(name = "idx_products_name", columnList = "name"),
                @Index(name = "idx_products_sku", columnList = "sku"),
                @Index(name = "idx_products_type", columnList = "type")
        }
)
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class ProductEntity extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 64, unique = true)
    private String sku;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ProductType type;

    @Column(length = 255)
    private String imageUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private Set<CategoryEntity> categories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "promotion_products",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_id", referencedColumnName = "id")
    )
    private Set<PromotionEntity> promotions = new HashSet<>();

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private InventoryEntity inventory;

    public ProductEntity() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryEntity> categories) {
        this.categories = categories;
    }

    public Set<PromotionEntity> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<PromotionEntity> promotions) {
        this.promotions = promotions;
    }

    public InventoryEntity getInventory() {
        return inventory;
    }

    public void setInventory(InventoryEntity inventory) {
        this.inventory = inventory;
    }
}
