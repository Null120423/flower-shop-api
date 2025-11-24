import { Module } from '@nestjs/common';

import { TransactionRepository } from 'src/repositories/transactions.repository';
import { TypeOrmExModule } from 'src/typeorm';
import { NotificationModule } from '../notification/notification.module';
import { TransactionModule } from '../transactions/transaction.module';
import { SepayController } from './sepay.controller';
import { SepayService } from './sepay.service';

@Module({
  imports: [
    TypeOrmExModule.forCustomRepository([TransactionRepository]),
    TransactionModule,
    NotificationModule,
  ],
  providers: [SepayService],
  controllers: [SepayController],
  exports: [SepayService],
})
export class SepayModule {}
