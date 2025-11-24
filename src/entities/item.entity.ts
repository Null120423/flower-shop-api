import { Column, Entity } from 'typeorm';
import { BaseEntityCustom } from './base.entity';

/** Thực thể đại diện cho các loại hoa trong cửa hàng */
@Entity('items')
export class ItemEntity extends BaseEntityCustom {
  /** code loại hoa */
  @Column({ type: 'varchar', length: 255, nullable: false })
  code: string;

  /** Tên loại hoa */
  @Column({ type: 'varchar', length: 255, nullable: false })
  name: string;

  /** Ảnh chính của hoa */
  @Column({ type: 'varchar', nullable: true })
  thumbnail: string;

  /** Danh mục hoa (Hoa cưới, Hoa sinh nhật, Hoa Valentine, ...) */
  @Column({ type: 'varchar', length: 100, nullable: true })
  category: string;

  /** Mô tả chi tiết về loại hoa */
  @Column({ type: 'text', nullable: false })
  description: string;

  /** Giá bán (VND) */
  @Column({ type: 'decimal', precision: 10, scale: 2, nullable: false })
  price: number;

  /** Giá gốc (để tính discount) */
  @Column({ type: 'decimal', precision: 10, scale: 2, nullable: true })
  originalPrice: number;

  /** Số lượng tồn kho */
  @Column({ type: 'integer', default: 0 })
  stockQuantity: number;

  /** Màu sắc chủ đạo */
  @Column({ type: 'varchar', length: 50, nullable: true })
  primaryColor: string;

  /** Mã SKU riêng cho từng biến thể */
  @Column({ type: 'varchar', length: 50, nullable: true, unique: true })
  sku: string;

  /** Loại hoa (Hoa hồng, Hoa ly, Hoa cúc, ...) */
  @Column({ type: 'varchar', length: 100, nullable: true })
  flowerType: string;

  /** Thứ tự hiển thị của size */
  @Column({ type: 'integer', default: 0 })
  order: number;

  /** Dịp sử dụng phù hợp */
  @Column({ type: 'text', nullable: true })
  suitableOccasions: string;

  /** Thời gian bảo quản (số ngày) */
  @Column({ type: 'integer', nullable: true })
  shelfLifeDays: number;

  /** Hướng dẫn chăm sóc */
  @Column({ type: 'text', nullable: true })
  careInstructions: string;

  /** Trạng thái sản phẩm (available, out_of_stock, discontinued) */
  @Column({ type: 'varchar', length: 20, default: 'available' })
  status: string;

  /** Có phải hoa nhập khẩu không */
  @Column({ type: 'boolean', default: false })
  isImported: boolean;

  /** Xuất xứ */
  @Column({ type: 'varchar', length: 100, nullable: true })
  origin: string;

  /** Đánh giá trung bình */
  @Column({ type: 'float', default: 0 })
  averageRating: number;

  /** Số lượng đánh giá */
  @Column({ type: 'integer', default: 0 })
  reviewCount: number;

  /** Số lượng đã bán */
  @Column({ type: 'integer', default: 0 })
  soldCount: number;

  /** Tags tìm kiếm */
  @Column({ type: 'text', nullable: true })
  searchTags: string;

  /** Có phải sản phẩm nổi bật không */
  @Column({ type: 'boolean', default: false })
  isFeatured: boolean;

  /** Có phải sản phẩm mới không */
  @Column({ type: 'boolean', default: false })
  isNew: boolean;

  //  id sản phẩm liên quan
  @Column({ type: 'text', nullable: true })
  relatedProducts: string;
}
