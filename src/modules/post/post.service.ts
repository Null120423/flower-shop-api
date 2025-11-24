import { Injectable } from '@nestjs/common';
import { PostRepository, UserRepository } from 'src/repositories';
import { NotificationService } from '../notification/notification.service';

@Injectable()
export class PostService {
  constructor(
    private readonly repo: PostRepository,
    private readonly userRepo: UserRepository,
    private readonly notificationService: NotificationService,
  ) {}
}
