import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { PaginationDto } from 'src/dto/pagination.dto';
import {
  NotificationEntity,
  NotificationTypeData,
  UserEntity,
} from 'src/entities';
import { NotificationRepository } from 'src/repositories';
import { v4 as uuidv4 } from 'uuid';
import { EmailService } from './../email/email.service';
import { CreateNotificationDto } from './dto';
import { FirebaseService } from './firebase.service';

@Injectable()
export class NotificationService {
  constructor(
    public readonly configService: ConfigService,
    private readonly repo: NotificationRepository,
    private readonly emailService: EmailService,
    private readonly firebaseService: FirebaseService,
  ) {}

  async read(user: UserEntity, id: string) {
    const notification = await this.repo.findOne({
      where: { id, userId: user.id },
    });
    if (!notification) {
      throw new Error('Notification not found');
    }
    notification.isRead = true;
    notification.updatedBy = user.id;
    notification.updatedAt = new Date();
    await this.repo.save(notification);
    return {
      message: 'Notification marked as read',
      data: notification,
    };
  }
  //#endregion
  //#endregion notification
  async getNotificationById(id: string) {
    const notification: any = await this.repo.findOne({
      where: { id },
      relations: {
        user: true,
      },
    });
    if (!notification) {
      throw new Error('Notification not found');
    }
    const type =
      NotificationTypeData[
        notification.type as keyof typeof NotificationTypeData
      ];
    if (type) {
      notification.typeData = type;
    }

    await this.read(notification.user, id);
    return notification;
  }
  /** get notification by user  */
  async notificationPagination(userId: string, body: PaginationDto<any>) {
    const { skip, take, where } = body;
    const query = this.repo
      .createQueryBuilder('notification')
      .where('notification.userId = :userId', { userId })
      .orderBy('notification.createdAt', 'DESC')
      .skip(skip)
      .take(take);
    if (where?.isRead) {
      query.andWhere('notification.isRead = :isRead', { isRead: where.isRead });
    }
    const res: any = await query.getManyAndCount();

    const userIds = res[0].map((item: any) => item.userId);
    const dictUserById = userIds.reduce((acc: any, item: any) => {
      acc[item.userId] = item.user;
      return acc;
    }, {});
    const unreadCount = await this.repo.count({
      where: {
        userId,
        isRead: false,
      },
    });

    for (const item of res[0]) {
      const type =
        NotificationTypeData[item.type as keyof typeof NotificationTypeData];
      if (type) {
        item.typeData = type;
      }
      item.user = dictUserById[item.userId] || null;
      item.username = item.user ? item.user.username : null;
      item.avatar = item.user ? item.user.avatar : null;
    }
    return {
      data: res[0],
      total: res[1],
      unreadCount,
      skip,
      take,
      hasNext: res[0].length === take,
      nextSkip: skip + take,
    };
  }
  /** create notification */
  async createNotification(
    data: CreateNotificationDto,
    userId: string,
    repo: any = this.repo,
    createdBy: string = 'SYSTEM',
  ) {
    const notification = new NotificationEntity();
    notification.id = uuidv4();
    notification.userId = userId;
    notification.title = data.title;
    notification.message = data.message;
    notification.isRead = data.isRead || false;
    notification.type = data.type;
    notification.metadata = data.metaData || {};
    notification.createdBy = createdBy;
    notification.createdAt = new Date();
    if (data.scheduledNotificationId) {
      notification.scheduledNotificationId = data.scheduledNotificationId;
    }
    await repo.insert(notification);

    // // get devices of users
    // const devices = await this.userDeviceRepo.find({
    //   where: { userId: In([userId]) },
    //   select: ['deviceId', 'fcmToken', 'platform'],
    // });

    // await this.firebaseService.sendFCMNotifications(
    //   devices.map((device) => device.fcmToken),
    //   data.title,
    //   data.message,
    // );
  }
  //#endregion

  //#endregion admin notification pagination
  async pagination(body: PaginationDto<any>) {
    const { skip, take, where } = body;
    const query = this.repo
      .createQueryBuilder('notification')
      .leftJoinAndSelect('notification.user', 'user')
      .orderBy('notification.createdAt', 'DESC')
      .skip(skip)
      .take(take);

    if (where?.isRead !== undefined) {
      query.andWhere('notification.isRead = :isRead', { isRead: where.isRead });
    }
    if (where?.type) {
      query.andWhere('notification.type = :type', { type: where.type });
    }
    if (where?.userId) {
      query.andWhere('notification.userId = :userId', { userId: where.userId });
    }
    if (where?.sentAt?.gte) {
      query.andWhere('notification.createdAt >= :gte', {
        gte: where.sentAt.gte,
      });
    }
    if (where?.sentAt?.lte) {
      query.andWhere('notification.createdAt <= :lte', {
        lte: where.sentAt.lte,
      });
    }
    if (where?.title?.contains) {
      query.andWhere('notification.title ILIKE :title', {
        title: `%${where.title.contains}%`,
      });
    }
    if (where?.message?.contains) {
      query.andWhere('notification.message ILIKE :message', {
        message: `%${where.message.contains}%`,
      });
    }

    const res: any = await query.getManyAndCount();

    for (const item of res[0]) {
      const type =
        NotificationTypeData[item.type as keyof typeof NotificationTypeData];
      if (type) {
        item.typeData = type;
      }
    }
    const unreadCount = await this.repo.count({
      where: {
        isRead: false,
      },
    });
    return {
      data: res[0],
      total: res[1],
      skip,
      take,
      unreadCount,
    };
  }

  getTotalUnreadNotificationCount(userId: string): Promise<number> {
    return this.repo.count({
      where: {
        userId,
        isRead: false,
      },
    });
  }
}
