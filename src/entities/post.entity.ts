import { Column, Entity } from 'typeorm';
import { BaseEntityCustom } from './base.entity';
export enum PostStatus {
  PUBLISH = 'publish',
  PENDING = 'pending',
  REJECTED = 'rejected',
}

export const PostStatusData = {
  [PostStatus.PUBLISH]: {
    label: 'Published',
    color: 'green',
  },
  [PostStatus.PENDING]: {
    label: 'Pending',
    color: 'yellow',
  },
  [PostStatus.REJECTED]: {
    label: 'Rejected',
    color: 'red',
  },
};

/** khi người dùng trên app tạo bài viết thì lưu ở đây */
@Entity('post')
export class PostEntity extends BaseEntityCustom {
  @Column({ type: 'varchar', length: 255, nullable: false })
  title: string;
  @Column('text')
  thumbnail: string;

  @Column('text')
  imgs: string;

  @Column({ type: 'varchar', length: 255, nullable: true })
  tag: string;

  @Column({ type: 'text', nullable: false })
  content: string;

  @Column({
    default: 0,
    type: 'int',
  })
  timePosted: number;

  @Column({
    default: 0,
    type: 'int',
  })
  commentCount: number;
  @Column({
    type: 'enum',
    enum: PostStatus,
    default: PostStatus.PUBLISH,
  })
  status: PostStatus;
}
