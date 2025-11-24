import { ApiProperty } from '@nestjs/swagger';
import { Column, Entity, Index } from 'typeorm';
import { BaseEntityCustom } from './base.entity';

/** Bảng lưu thông tin file, hình ảnh */
@Entity('file_archival')
export class FileArchivalEntity extends BaseEntityCustom {
  @Column({ type: 'text', nullable: false })
  url: string;

  @Column({ type: 'varchar', length: 250, nullable: true })
  name: string;

  @Column({ type: 'varchar', length: 50, nullable: true })
  dataType: string;

  /** Id entity lưu lịch sử */
  @ApiProperty({ description: 'Id entity lưu lịch sử', required: false })
  @Index()
  @Column({ type: 'varchar', length: 36, nullable: true })
  functionId?: string;
}
