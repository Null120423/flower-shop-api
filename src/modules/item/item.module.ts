import { Module } from '@nestjs/common';
import { ItemRepository } from 'src/repositories/master-data.repository';
import { TypeOrmExModule } from '../../typeorm';
import { ActionLogModule } from '../actionLog/actionLog.module';
import { ItemController } from './item.controller';
import { ItemService } from './item.service';

@Module({
  imports: [
    TypeOrmExModule.forCustomRepository([ItemRepository]),
    ActionLogModule,
  ],
  controllers: [ItemController],
  providers: [ItemService],
  exports: [ItemService],
})
export class ItemModule {}
