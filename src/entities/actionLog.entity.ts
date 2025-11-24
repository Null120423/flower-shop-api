import { ApiProperty } from '@nestjs/swagger';
import { Column, CreateDateColumn, Entity, Index } from 'typeorm';
import { BaseEntityCustom } from './base.entity';

@Entity('action_log')
export class ActionLogEntity extends BaseEntityCustom {
  /** Tên người tạo (username) */
  @Column({ type: 'varchar', length: 250, nullable: false })
  createdByName: string;

  /** Thông tin người tạo */
  @Column({ type: 'varchar', length: 500, nullable: true })
  createdNote: string;

  /** Mô tả thao tác lịch sử */
  @Column({ type: 'text', nullable: false })
  description: string;

  /** dữ liệu cũ */
  @Column({ type: 'text', nullable: true })
  dataOld: string;

  /**dữ liệu mới*/
  @Column({ type: 'text', nullable: true })
  dataNew: string;

  /** Loại thao tác */
  @Column({ type: 'varchar', length: 36, nullable: false })
  type: string;

  /** Tên entity lưu lịch sử */
  @Column({
    type: 'varchar',
    length: 250,
    nullable: false,
  })
  functionType: string;

  /** Id entity lưu lịch sử */
  @ApiProperty({ description: 'Id entity lưu lịch sử', required: false })
  @Index()
  @Column({ type: 'varchar', length: 36, nullable: true })
  functionId?: string;

  @ApiProperty({ description: 'Ngày tạo' })
  @Index({})
  @CreateDateColumn({ nullable: false })
  createdAt: Date;
}
