import { Controller, UseGuards } from '@nestjs/common';
import { ApiTags } from '@nestjs/swagger';
import { JwtAuthGuard } from '../auth/jwt.auth.guard';
import { TransactionService } from './transaction.service';
@UseGuards(JwtAuthGuard)
@ApiTags('transaction API')
@Controller('transaction')
export class TransactionController {
  constructor(private readonly service: TransactionService) {}
}
