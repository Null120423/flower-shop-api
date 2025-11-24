import { Column, Entity, JoinColumn, OneToOne } from 'typeorm';
import { BaseEntityCustom } from './base.entity';
import { UserEntity } from './user.entity';

/** Thông tin chi tiết của khách hàng trong cửa hàng hoa */
@Entity('user_details')
export class UserDetailEntity extends BaseEntityCustom {
  /** Họ và tên */
  @Column()
  fullName: string;

  /** Địa chỉ giao hàng mặc định */
  @Column({ nullable: true })
  defaultShippingAddress: string;

  /** Địa chỉ thanh toán */
  @Column({ nullable: true })
  billingAddress: string;

  /** Số điện thoại */
  @Column({ nullable: true })
  phoneNumber: string;

  /** Email */
  @Column({ nullable: true })
  email: string;

  /** Ảnh đại diện */
  @Column({ type: 'varchar', nullable: true })
  profilePictureUrl: string;

  /** Ngày sinh */
  @Column({ type: 'date', nullable: true })
  birthDate: Date | null;

  /** Giới tính */
  @Column({ type: 'varchar', nullable: true })
  gender: string;

  /** Sở thích về loại hoa */
  @Column({ type: 'text', nullable: true })
  flowerPreferences: string;

  /** Lịch sử mua hàng (JSON array của order IDs) */
  @Column({ type: 'text', nullable: true })
  purchaseHistory: string;

  /** Danh sách yêu thích (wishlist) */
  @Column({ type: 'text', nullable: true })
  wishlist: string;

  /** Điểm tích lũy của khách hàng */
  @Column({ type: 'integer', default: 0 })
  loyaltyPoints: number;

  /** Mức độ khách hàng (Bronze, Silver, Gold, Diamond) */
  @Column({ type: 'varchar', default: 'Bronze' })
  customerTier: string;

  /** Tổng số tiền đã chi tiêu */
  @Column({ type: 'decimal', precision: 10, scale: 2, default: 0 })
  totalSpent: number;

  /** Số lượng đơn hàng đã hoàn thành */
  @Column({ type: 'integer', default: 0 })
  completedOrders: number;

  /** Thông tin thanh toán ưu tiên */
  @Column({ type: 'varchar', nullable: true })
  preferredPaymentMethod: string;

  /** Ghi chú đặc biệt từ khách hàng */
  @Column({ type: 'text', nullable: true })
  specialNotes: string;

  /** ID người dùng liên kết */
  @Column({ type: 'varchar' })
  userId: string;

  /** Mối quan hệ 1-1 với bảng người dùng chính */
  @OneToOne(() => UserEntity, (user) => user.userDetail)
  @JoinColumn({ name: 'userId', referencedColumnName: 'id' })
  user: Promise<UserEntity>;
}
