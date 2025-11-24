import { NotificationEntity, PostEntity, UserEntity } from 'src/entities';
import { ActionLogEntity } from 'src/entities/actionLog.entity';
import { FileArchivalEntity } from 'src/entities/fire-archival.entity';
import { ItemEntity } from 'src/entities/item.entity';
import { UserDetailEntity } from 'src/entities/userDetail.entity';
import { CustomRepository } from 'src/typeorm';
import { Repository } from 'typeorm';
@CustomRepository(UserEntity)
export class UserRepository extends Repository<UserEntity> {}
@CustomRepository(UserDetailEntity)
export class UserDetailRepository extends Repository<UserDetailEntity> {}
@CustomRepository(FileArchivalEntity)
export class FileArchiveRepository extends Repository<FileArchivalEntity> {}
@CustomRepository(ItemEntity)
export class ItemRepository extends Repository<ItemEntity> {}
@CustomRepository(ActionLogEntity)
export class ActionLogRepository extends Repository<ActionLogEntity> {}

@CustomRepository(PostEntity)
export class PostRepository extends Repository<PostEntity> {}

@CustomRepository(NotificationEntity)
export class NotificationRepository extends Repository<NotificationEntity> {}
