import { ApiProperty } from '@nestjs/swagger';
import {
  IsBoolean,
  IsNumber,
  IsOptional,
  IsString,
  MaxLength,
  Min,
} from 'class-validator';

export class ItemUpdateDto {
  @ApiProperty({
    description: 'Tên loại hoa',
    example: 'Hoa hồng đỏ Valentine',
    required: false,
  })
  @IsOptional()
  @IsString()
  @MaxLength(255)
  name?: string;

  @ApiProperty({
    description: 'Ảnh chính của hoa',
    example: 'https://example.com/rose.jpg',
    required: false,
  })
  @IsOptional()
  @IsString()
  thumbnail?: string;

  @ApiProperty({
    description: 'Danh mục hoa',
    example: 'Hoa Valentine',
    required: false,
  })
  @IsOptional()
  @IsString()
  @MaxLength(100)
  category?: string;

  @ApiProperty({
    description: 'Mô tả chi tiết về loại hoa',
    example: 'Hoa hồng đỏ tươi, phù hợp cho ngày Valentine',
    required: false,
  })
  @IsOptional()
  @IsString()
  description?: string;

  @ApiProperty({
    description: 'Giá bán (VND)',
    example: 250000,
    required: false,
  })
  @IsOptional()
  @IsNumber()
  @Min(0)
  price?: number;

  @ApiProperty({
    description: 'Giá gốc (VND)',
    example: 300000,
    required: false,
  })
  @IsOptional()
  @IsNumber()
  @Min(0)
  originalPrice?: number;

  @ApiProperty({
    description: 'Số lượng tồn kho',
    example: 50,
    required: false,
  })
  @IsOptional()
  @IsNumber()
  @Min(0)
  stockQuantity?: number;

  @ApiProperty({
    description: 'Màu sắc chủ đạo',
    example: 'Đỏ',
    required: false,
  })
  @IsOptional()
  @IsString()
  @MaxLength(50)
  primaryColor?: string;

  @ApiProperty({
    description: 'Mã SKU',
    example: 'ROSE-RED-001',
    required: false,
  })
  @IsOptional()
  @IsString()
  @MaxLength(50)
  sku?: string;

  @ApiProperty({
    description: 'Loại hoa',
    example: 'Hoa hồng',
    required: false,
  })
  @IsOptional()
  @IsString()
  @MaxLength(100)
  flowerType?: string;

  @ApiProperty({
    description: 'Thứ tự hiển thị',
    example: 1,
    required: false,
  })
  @IsOptional()
  @IsNumber()
  @Min(0)
  order?: number;

  @ApiProperty({
    description: 'Dịp sử dụng phù hợp',
    example: 'Valentine, Sinh nhật, Kỷ niệm',
    required: false,
  })
  @IsOptional()
  @IsString()
  suitableOccasions?: string;

  @ApiProperty({
    description: 'Thời gian bảo quản (ngày)',
    example: 7,
    required: false,
  })
  @IsOptional()
  @IsNumber()
  @Min(1)
  shelfLifeDays?: number;

  @ApiProperty({
    description: 'Hướng dẫn chăm sóc',
    example: 'Cắt thân hoa mỗi 2 ngày, thay nước sạch',
    required: false,
  })
  @IsOptional()
  @IsString()
  careInstructions?: string;

  @ApiProperty({
    description: 'Trạng thái sản phẩm',
    example: 'available',
    enum: ['available', 'out_of_stock', 'discontinued'],
    required: false,
  })
  @IsOptional()
  @IsString()
  status?: string;

  @ApiProperty({
    description: 'Có phải hoa nhập khẩu',
    example: false,
    required: false,
  })
  @IsOptional()
  @IsBoolean()
  isImported?: boolean;

  @ApiProperty({
    description: 'Xuất xứ',
    example: 'Việt Nam',
    required: false,
  })
  @IsOptional()
  @IsString()
  @MaxLength(100)
  origin?: string;

  @ApiProperty({
    description: 'Tags tìm kiếm',
    example: 'hoa, valentine, đỏ, tình yêu',
    required: false,
  })
  @IsOptional()
  @IsString()
  searchTags?: string;

  @ApiProperty({
    description: 'Sản phẩm nổi bật',
    example: false,
    required: false,
  })
  @IsOptional()
  @IsBoolean()
  isFeatured?: boolean;

  @ApiProperty({
    description: 'Sản phẩm mới',
    example: true,
    required: false,
  })
  @IsOptional()
  @IsBoolean()
  isNew?: boolean;

  @ApiProperty({
    description: 'Sản phẩm liên quan (JSON array)',
    example: '["item-id-1", "item-id-2"]',
    required: false,
  })
  @IsOptional()
  @IsString()
  relatedProducts?: string;
}
