import { Module } from '@nestjs/common'
import { ActionLogRepository } from '../../repositories'
import { TypeOrmExModule } from '../../typeorm'
import { ActionLogController } from './actionLog.controller'
import { ActionLogService } from './actionLog.service'

@Module({
  imports: [TypeOrmExModule.forCustomRepository([ActionLogRepository])],
  controllers: [ActionLogController],
  providers: [ActionLogService],
  exports: [ActionLogService],
})
export class ActionLogModule {}
