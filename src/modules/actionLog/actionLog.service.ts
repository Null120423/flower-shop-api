import { Injectable } from '@nestjs/common';
import { PaginationDto } from 'src/dto/pagination.dto';
import { ActionLogEntity } from 'src/entities/actionLog.entity';
import { EntityManager } from 'typeorm';
import { v4 as uuidv4 } from 'uuid';
import { ActionLogRepository } from '../../repositories';
import { UserDataDTO } from '../auth/dto';
import { ActionLogCreateDto } from './dto/action-log-create.dto';

@Injectable()
export class ActionLogService {
  constructor(private repo: ActionLogRepository) {}
  async loadData(user: UserDataDTO, where: any) {
    if (user.role !== 'admin') {
      where.userId = user.id;
    }
    return this.repo.find({ where });
  }

  async create(data: ActionLogCreateDto, trans: EntityManager) {
    const repo = trans.getRepository(ActionLogEntity);
    const log = new ActionLogEntity();
    log.id = uuidv4();
    log.createdBy = data.createdByName;
    log.createdByName = data.createdByName;
    log.createdNote = data.createdNote || '';
    log.description = data.description;
    log.type = data.type;
    log.functionType = data.functionType;
    log.functionId = data.functionId;
    log.createdAt = new Date();
    await repo.insert(log);
  }

  async pagination(user: UserDataDTO, data: PaginationDto<any>) {
    const rs = await this.loadData(user, data.where);
    const res: any = await this.repo.findAndCount({
      where: data.where,
      skip: data.skip,
      take: data.take,
      order: { createdAt: 'DESC' },
    });

    return [res[0], rs.length];
  }
}
