import {
  CanActivate,
  ExecutionContext,
  ForbiddenException,
  Injectable,
  UnauthorizedException,
} from '@nestjs/common';
import { UserEntity } from '../../entities/user.entity';
import { JwtAuthGuard } from './jwt.auth.guard';

@Injectable()
export class AdminGuard extends JwtAuthGuard implements CanActivate {
  async canActivate(context: ExecutionContext): Promise<boolean> {
    // First check if user is authenticated
    const isAuthenticated = await super.canActivate(context);

    if (!isAuthenticated) {
      throw new UnauthorizedException('User is not authenticated');
    }

    const request = context.switchToHttp().getRequest();
    const user: UserEntity = request.user;

    if (!user) {
      throw new UnauthorizedException('User not found in request');
    }

    // Check if user is admin
    if (!user.isAdmin) {
      throw new ForbiddenException('Access denied. Admin privileges required.');
    }

    return true;
  }
}
