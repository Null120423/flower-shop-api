import { Module } from '@nestjs/common';
import { FileArchiveRepository } from 'src/repositories/master-data.repository';
import { TypeOrmExModule } from '../../typeorm';
import { ActionLogService } from '../actionLog/actionLog.service';

@Module({
  imports: [TypeOrmExModule.forCustomRepository([FileArchiveRepository])],
  controllers: [],
  providers: [ActionLogService],
  exports: [ActionLogService],
})
export class FileArchiveModule {}
