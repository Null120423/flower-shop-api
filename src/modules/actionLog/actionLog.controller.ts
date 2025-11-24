import { Body, Controller, Post, UseGuards } from '@nestjs/common';
import { ApiBearerAuth, ApiOperation, ApiTags } from '@nestjs/swagger';
import { PaginationDto } from 'src/dto/pagination.dto';
import { CurrentUser } from 'src/helpers/decorators';
import { UserDataDTO } from '../auth/dto';
import { JwtAuthGuard } from '../auth/jwt.auth.guard';
import { ActionLogService } from './actionLog.service';

@ApiBearerAuth()
@UseGuards(JwtAuthGuard)
@ApiTags('ActionLog')
@Controller('action-log')
export class ActionLogController {
  constructor(private readonly service: ActionLogService) {}

  @ApiOperation({ summary: 'Hàm phân trang' })
  @Post('pagination')
  public async pagination(
    @CurrentUser() user: UserDataDTO,
    @Body() data: PaginationDto<any>,
  ) {
    return this.service.pagination(user, data);
  }
}
