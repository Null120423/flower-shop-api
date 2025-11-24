import { Module } from '@nestjs/common';
import { PostRepository, UserRepository } from 'src/repositories';
import { TypeOrmExModule } from 'src/typeorm';
import { NotificationModule } from '../notification/notification.module';
import { PostController } from './post.controller';
import { PostService } from './post.service';

@Module({
  imports: [
    TypeOrmExModule.forCustomRepository([PostRepository, UserRepository]),
    NotificationModule,
  ],
  providers: [PostService],
  controllers: [PostController],
  exports: [PostService],
})
export class PostModule {}
