import {
  Body,
  Controller,
  Delete,
  Get,
  Param,
  Post,
  Put,
  Query,
  UseGuards,
} from '@nestjs/common';
import {
  ApiOperation,
  ApiParam,
  ApiQuery,
  ApiResponse,
  ApiTags,
} from '@nestjs/swagger';
import { PaginationDto } from 'src/dto/pagination.dto';
import { AdminOnly } from '../auth/admin-only.decorator';
import { CurrentUser } from '../auth/decorators/current-user.decorator';
import { UserDataDTO } from '../auth/dto';
import { JwtAuthGuard } from '../auth/jwt.auth.guard';
import { ItemCreateDto } from './dto/create.dto';
import { ItemUpdateDto } from './dto/update.dto';
import { ItemService } from './item.service';

@ApiTags('Item Management')
@Controller('item')
export class ItemController {
  constructor(private readonly service: ItemService) {}

  @Post()
  @AdminOnly()
  @ApiOperation({ summary: 'Tạo sản phẩm mới' })
  @ApiResponse({ status: 201, description: 'Tạo thành công' })
  @ApiResponse({ status: 400, description: 'Dữ liệu không hợp lệ' })
  @ApiResponse({ status: 403, description: 'Không có quyền admin' })
  async create(
    @Body() createDto: ItemCreateDto,
    @CurrentUser() user: UserDataDTO,
  ) {
    return this.service.create(createDto, user);
  }

  @Get()
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Lấy danh sách sản phẩm' })
  @ApiQuery({ name: 'page', required: false, type: Number })
  @ApiQuery({ name: 'limit', required: false, type: Number })
  @ApiQuery({ name: 'search', required: false, type: String })
  @ApiResponse({ status: 200, description: 'Lấy danh sách thành công' })
  async findAll(@Query() pagination: PaginationDto<any>) {
    return this.service.findAll(pagination);
  }

  @Get('featured')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Lấy danh sách sản phẩm nổi bật' })
  @ApiQuery({ name: 'limit', required: false, type: Number })
  @ApiResponse({ status: 200, description: 'Lấy danh sách thành công' })
  async getFeatured(@Query('limit') limit?: number) {
    return this.service.getFeaturedItems(limit);
  }

  @Get('new')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Lấy danh sách sản phẩm mới' })
  @ApiQuery({ name: 'limit', required: false, type: Number })
  @ApiResponse({ status: 200, description: 'Lấy danh sách thành công' })
  async getNew(@Query('limit') limit?: number) {
    return this.service.getNewItems(limit);
  }

  @Get('category/:category')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Lấy sản phẩm theo danh mục' })
  @ApiParam({ name: 'category', description: 'Danh mục sản phẩm' })
  @ApiQuery({ name: 'page', required: false, type: Number })
  @ApiQuery({ name: 'limit', required: false, type: Number })
  @ApiResponse({ status: 200, description: 'Lấy danh sách thành công' })
  async findByCategory(
    @Param('category') category: string,
    @Query() pagination: PaginationDto<any>,
  ) {
    return this.service.findByCategory(category, pagination);
  }

  @Get(':id')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Lấy chi tiết sản phẩm' })
  @ApiParam({ name: 'id', description: 'ID sản phẩm' })
  @ApiResponse({ status: 200, description: 'Lấy chi tiết thành công' })
  @ApiResponse({ status: 404, description: 'Không tìm thấy sản phẩm' })
  async findById(@Param('id') id: string) {
    return this.service.findById(id);
  }

  @Put(':id')
  @AdminOnly()
  @ApiOperation({ summary: 'Cập nhật sản phẩm' })
  @ApiParam({ name: 'id', description: 'ID sản phẩm' })
  @ApiResponse({ status: 200, description: 'Cập nhật thành công' })
  @ApiResponse({ status: 400, description: 'Dữ liệu không hợp lệ' })
  @ApiResponse({ status: 403, description: 'Không có quyền admin' })
  @ApiResponse({ status: 404, description: 'Không tìm thấy sản phẩm' })
  async update(
    @Param('id') id: string,
    @Body() updateDto: ItemUpdateDto,
    @CurrentUser() user: UserDataDTO,
  ) {
    return this.service.update(id, updateDto, user);
  }

  @Put(':id/stock')
  @AdminOnly()
  @ApiOperation({ summary: 'Cập nhật số lượng tồn kho' })
  @ApiParam({ name: 'id', description: 'ID sản phẩm' })
  @ApiResponse({ status: 200, description: 'Cập nhật thành công' })
  async updateStock(
    @Param('id') id: string,
    @Body('quantity') quantity: number,
    @CurrentUser() user: UserDataDTO,
  ) {
    return this.service.updateStock(id, quantity, user);
  }

  @Delete(':id')
  @AdminOnly()
  @ApiOperation({ summary: 'Xóa sản phẩm' })
  @ApiParam({ name: 'id', description: 'ID sản phẩm' })
  @ApiResponse({ status: 200, description: 'Xóa thành công' })
  @ApiResponse({ status: 403, description: 'Không có quyền admin' })
  @ApiResponse({ status: 404, description: 'Không tìm thấy sản phẩm' })
  async delete(@Param('id') id: string, @CurrentUser() user: UserDataDTO) {
    await this.service.delete(id, user);
    return { message: 'Xóa sản phẩm thành công' };
  }

  @Put(':id/soft-delete')
  @AdminOnly()
  @ApiOperation({ summary: 'Ngừng kinh doanh sản phẩm (soft delete)' })
  @ApiParam({ name: 'id', description: 'ID sản phẩm' })
  @ApiResponse({ status: 200, description: 'Cập nhật thành công' })
  async softDelete(@Param('id') id: string, @CurrentUser() user: UserDataDTO) {
    return this.service.softDelete(id, user);
  }

  @Put('bulk/status')
  @AdminOnly()
  @ApiOperation({ summary: 'Cập nhật trạng thái hàng loạt' })
  @ApiResponse({ status: 200, description: 'Cập nhật thành công' })
  async bulkUpdateStatus(
    @Body('ids') ids: string[],
    @Body('status') status: string,
    @CurrentUser() user: UserDataDTO,
  ) {
    await this.service.bulkUpdateStatus(ids, status, user);
    return { message: 'Cập nhật trạng thái thành công' };
  }
}
