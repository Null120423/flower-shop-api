package book.storage.db.core.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
                @UniqueConstraint(columnNames = "slug")
        },
        indexes = {
                @Index(name = "idx_categories_name", columnList = "name"),
                @Index(name = "idx_categories_slug", columnList = "slug")
        }
)
@SQLDelete(sql = "UPDATE categories SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class CategoryEntity extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 120, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<ProductEntity> products = new HashSet<>();

    public CategoryEntity() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductEntity> products) {
        this.products = products;
    }
}
