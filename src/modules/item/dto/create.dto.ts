import { Type } from 'class-transformer';
import {
  IsArray,
  IsInt,
  IsNumber,
  IsOptional,
  IsString,
  IsUrl,
  Min,
  MinLength,
} from 'class-validator';

export class ItemCreateDto {
  @IsString()
  @MinLength(1)
  name: string;

  @IsOptional()
  @IsUrl()
  thumbnail?: string;

  @IsOptional()
  @IsString()
  category?: string;

  @IsString()
  @MinLength(1)
  description: string;

  @Type(() => Number)
  @IsNumber()
  @Min(0)
  price: number;

  @IsOptional()
  @Type(() => Number)
  @IsNumber()
  @Min(0)
  originalPrice: number;

  @IsOptional()
  @Type(() => Number)
  @IsInt()
  @Min(0)
  stockQuantity: number;

  @IsOptional()
  @IsString()
  primaryColor: string;

  @IsOptional()
  @IsString()
  sku?: string;

  @IsOptional()
  @IsString()
  size?: string;

  @IsOptional()
  @IsString()
  sizeDetails?: string;

  @IsOptional()
  @IsString()
  flowerType?: string;

  @IsOptional()
  @Type(() => Number)
  @IsInt()
  sizeOrder?: number;

  @IsOptional()
  @IsString()
  origin?: string;

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  relatedProducts?: string[];
}
