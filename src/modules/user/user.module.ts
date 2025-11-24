import { Module } from '@nestjs/common';
import { UserDetailRepository, UserRepository } from 'src/repositories';
import { TypeOrmExModule } from 'src/typeorm';
import { AuthModule } from '../auth/auth.module';
import { UploadModule } from '../upload/upload.module';
import { UserController } from './user.controller';
import { UserService } from './user.service';

@Module({
  imports: [
    AuthModule,
    TypeOrmExModule.forCustomRepository([UserRepository, UserDetailRepository]),
    UploadModule,
  ],
  providers: [UserService],
  controllers: [UserController],
  exports: [UserService],
})
export class UserModule {}
