import { applyDecorators, UseGuards } from '@nestjs/common';
import {
  ApiBearerAuth,
  ApiForbiddenResponse,
  ApiUnauthorizedResponse,
} from '@nestjs/swagger';
import { AdminGuard } from './admin.guard';

/**
 * Decorator để bảo vệ API chỉ dành cho admin
 * Kết hợp JWT authentication và admin authorization
 */
export function AdminOnly() {
  return applyDecorators(
    UseGuards(AdminGuard),
    ApiBearerAuth(),
    ApiUnauthorizedResponse({
      description: 'Unauthorized - User is not authenticated',
    }),
    ApiForbiddenResponse({
      description: 'Forbidden - Admin privileges required',
    }),
  );
}
