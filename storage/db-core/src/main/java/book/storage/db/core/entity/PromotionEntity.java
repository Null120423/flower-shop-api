package book.storage.db.core.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import book.core.enums.PromotionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "promotions",
        uniqueConstraints = @UniqueConstraint(columnNames = "code"),
        indexes = {
                @Index(name = "idx_promotions_code", columnList = "code"),
                @Index(name = "idx_promotions_active", columnList = "active"),
                @Index(name = "idx_promotions_dates", columnList = "startAt,endAt")
        }
)
@SQLDelete(sql = "UPDATE promotions SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class PromotionEntity extends BaseEntity {

    @Column(nullable = false, length = 64, unique = true)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private PromotionType type;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal value;

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    @Column(nullable = false)
    private Boolean active = true;

    @Column
    private Integer usageLimit;

    @Column(nullable = false)
    private Integer usageCount = 0;

    @ManyToMany(mappedBy = "promotions", fetch = FetchType.LAZY)
    private Set<ProductEntity> products = new HashSet<>();

    public PromotionEntity() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public PromotionType getType() {
        return type;
    }

    public void setType(PromotionType type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Set<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductEntity> products) {
        this.products = products;
    }

    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return active && 
               (startAt == null || now.isAfter(startAt)) && 
               (endAt == null || now.isBefore(endAt)) &&
               (usageLimit == null || usageCount < usageLimit);
    }
}
