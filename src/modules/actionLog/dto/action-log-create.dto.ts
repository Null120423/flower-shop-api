import { ApiProperty } from '@nestjs/swagger';
import { IsNotEmpty, IsOptional, IsString, MaxLength } from 'class-validator';

export class ActionLogCreateDto {
  @ApiProperty({
    description: 'Tên người tạo (username)',
    example: 'admin@example.com',
    maxLength: 250,
  })
  @IsNotEmpty()
  @IsString()
  @MaxLength(250)
  createdByName: string;

  @ApiProperty({
    description: 'Thông tin người tạo',
    example: 'Quản trị viên hệ thống',
    maxLength: 500,
    required: false,
  })
  @IsOptional()
  @IsString()
  @MaxLength(500)
  createdNote?: string;

  @ApiProperty({
    description: 'Mô tả thao tác lịch sử',
    example: 'Tạo mới sản phẩm hoa hồng đỏ',
  })
  @IsNotEmpty()
  @IsString()
  description: string;

  @ApiProperty({
    description: 'Dữ liệu cũ (JSON string)',
    example: '{"name": "Hoa hồng", "price": 100000}',
    required: false,
  })
  @IsOptional()
  @IsString()
  dataOld?: string;

  @ApiProperty({
    description: 'Dữ liệu mới (JSON string)',
    example: '{"name": "Hoa hồng đỏ", "price": 120000}',
    required: false,
  })
  @IsOptional()
  @IsString()
  dataNew?: string;

  @ApiProperty({
    description: 'Loại thao tác',
    example: 'CREATE',
    enum: ['CREATE', 'UPDATE', 'DELETE', 'LOGIN', 'LOGOUT', 'VIEW'],
    maxLength: 36,
  })
  @IsNotEmpty()
  @IsString()
  @MaxLength(36)
  type: string;

  @ApiProperty({
    description: 'Tên entity lưu lịch sử',
    example: 'ItemEntity',
    maxLength: 250,
  })
  @IsNotEmpty()
  @IsString()
  @MaxLength(250)
  functionType: string;

  @ApiProperty({
    description: 'Id entity lưu lịch sử',
    example: '123e4567-e89b-12d3-a456-426614174000',
    required: false,
    maxLength: 36,
  })
  @IsOptional()
  @IsString()
  @MaxLength(36)
  functionId?: string;
}
