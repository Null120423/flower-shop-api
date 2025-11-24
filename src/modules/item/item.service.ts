import { Injectable, NotFoundException } from '@nestjs/common';
import { enumData } from 'src/constants/enum-data';
import { PaginationDto } from 'src/dto/pagination.dto';
import { ItemEntity } from 'src/entities/item.entity';
import { v4 as uuidv4 } from 'uuid';
import { ItemRepository } from '../../repositories';
import { ActionLogService } from '../actionLog/actionLog.service';
import { ActionLogCreateDto } from '../actionLog/dto/action-log-create.dto';
import { UserDataDTO } from '../auth/dto';
import { ItemCreateDto } from './dto/create.dto';
import { ItemUpdateDto } from './dto/update.dto';

@Injectable()
export class ItemService {
  constructor(
    private repo: ItemRepository,
    private readonly actionLogService: ActionLogService,
  ) {}

  async genCode(body: ItemCreateDto): Promise<string> {
    const baseCode = body.name
      .toLowerCase()
      .replace(/[^a-z0-9]+/g, '-')
      .replace(/^-+|-+$/g, '')
      .substring(0, 50);

    let code = baseCode;
    let suffix = 1;

    while (await this.existsByCode(code)) {
      suffix += 1;
      code = `${baseCode}-${suffix}`;
    }

    return code;
  }
  async existsByCode(code: string): Promise<boolean> {
    const found = await this.repo.findOne({
      where: { code },
      select: ['id'],
    });
    return !!found;
  }

  /** create  */
  async create(body: ItemCreateDto, user: UserDataDTO): Promise<ItemEntity> {
    return this.repo.manager.transaction(async (trans) => {
      const repo = trans.getRepository(ItemEntity);

      const newItem = new ItemEntity();
      newItem.id = uuidv4();
      newItem.name = body.name;
      newItem.code = await this.genCode(body);
      newItem.description = body.description;
      newItem.thumbnail = body.thumbnail || '';
      newItem.category = body.category || '';
      newItem.price = body.price;
      newItem.originalPrice = body.originalPrice || 0;
      newItem.stockQuantity = body.stockQuantity || 0;
      newItem.primaryColor = body.primaryColor || '';
      newItem.sku = body.sku || '';
      newItem.flowerType = body.flowerType || '';
      newItem.order = body.sizeOrder || 0;
      newItem.origin = body.origin || '';
      newItem.relatedProducts = body.relatedProducts
        ? JSON.stringify(body.relatedProducts)
        : '';
      newItem.createdAt = new Date();
      newItem.createdBy = user.id;

      const savedItem = await repo.save(newItem);

      // Create action log
      const actionLogCreateDto: ActionLogCreateDto = {
        createdByName: user.username,
        description: `Tạo mới sản phẩm: ${newItem.name}`,
        dataNew: JSON.stringify(savedItem),
        type: enumData.ACTION_TYPES.C,
        functionType: 'ItemEntity',
        functionId: newItem.id,
      };

      await this.actionLogService.create(actionLogCreateDto, trans);
      return savedItem;
    });
  }

  /** get by id */
  async findById(id: string): Promise<ItemEntity> {
    const item = await this.repo.findOne({
      where: { id },
    });

    if (!item) {
      throw new NotFoundException(`Item with ID ${id} not found`);
    }

    return item;
  }

  /** get all with pagination */
  async findAll(pagination: PaginationDto<any>): Promise<{
    items: ItemEntity[];
    total: number;
    page: number;
    limit: number;
    totalPages: number;
  }> {
    const { skip, take, where } = pagination;

    const queryBuilder = this.repo.createQueryBuilder('item');

    if (where?.search) {
      queryBuilder.where(
        'item.name ILIKE :search OR item.description ILIKE :search OR item.category ILIKE :search OR item.searchTags ILIKE :search',
        { search: `%${where?.search}%` },
      );
    }

    queryBuilder
      .orderBy('item.order', 'ASC')
      .addOrderBy('item.createdAt', 'DESC')
      .skip(skip)
      .take(take);

    const [items, total] = await queryBuilder.getManyAndCount();
    const totalPages = Math.ceil(total / take);

    return {
      items,
      total,
      page: skip / take + 1,
      limit: take,
      totalPages,
    };
  }

  /** get by category */
  async findByCategory(
    category: string,
    pagination: any,
  ): Promise<{
    items: ItemEntity[];
    total: number;
    page: number;
    limit: number;
    totalPages: number;
  }> {
    const { page = 1, limit = 10 } = pagination;
    const skip = (page - 1) * limit;

    const [items, total] = await this.repo.findAndCount({
      where: { category },
      order: { order: 'ASC', createdAt: 'DESC' },
      skip,
      take: limit,
    });

    const totalPages = Math.ceil(total / limit);

    return {
      items,
      total,
      page,
      limit,
      totalPages,
    };
  }

  /** update */
  async update(
    id: string,
    body: ItemUpdateDto,
    user: UserDataDTO,
  ): Promise<ItemEntity> {
    return this.repo.manager.transaction(async (trans) => {
      const repo = trans.getRepository(ItemEntity);

      const existingItem = await repo.findOne({ where: { id } });
      if (!existingItem) {
        throw new NotFoundException(`Item with ID ${id} not found`);
      }

      const oldData = { ...existingItem };

      // Update fields
      if (body.name !== undefined) existingItem.name = body.name;
      if (body.thumbnail !== undefined) existingItem.thumbnail = body.thumbnail;
      if (body.category !== undefined) existingItem.category = body.category;
      if (body.description !== undefined)
        existingItem.description = body.description;
      if (body.price !== undefined) existingItem.price = body.price;
      if (body.originalPrice !== undefined)
        existingItem.originalPrice = body.originalPrice;
      if (body.stockQuantity !== undefined)
        existingItem.stockQuantity = body.stockQuantity;
      if (body.primaryColor !== undefined)
        existingItem.primaryColor = body.primaryColor;
      if (body.sku !== undefined) existingItem.sku = body.sku;
      if (body.flowerType !== undefined)
        existingItem.flowerType = body.flowerType;
      if (body.order !== undefined) existingItem.order = body.order;
      if (body.suitableOccasions !== undefined)
        existingItem.suitableOccasions = body.suitableOccasions;
      if (body.shelfLifeDays !== undefined)
        existingItem.shelfLifeDays = body.shelfLifeDays;
      if (body.careInstructions !== undefined)
        existingItem.careInstructions = body.careInstructions;
      if (body.status !== undefined) existingItem.status = body.status;
      if (body.isImported !== undefined)
        existingItem.isImported = body.isImported;
      if (body.origin !== undefined) existingItem.origin = body.origin;
      if (body.searchTags !== undefined)
        existingItem.searchTags = body.searchTags;
      if (body.isFeatured !== undefined)
        existingItem.isFeatured = body.isFeatured;
      if (body.isNew !== undefined) existingItem.isNew = body.isNew;
      if (body.relatedProducts !== undefined)
        existingItem.relatedProducts = body.relatedProducts;

      existingItem.updatedAt = new Date();
      existingItem.updatedBy = user.id;

      const updatedItem = await repo.save(existingItem);

      // Create action log
      const actionLogCreateDto: ActionLogCreateDto = {
        createdByName: user.username,
        description: `Cập nhật sản phẩm: ${updatedItem.name}`,
        dataOld: JSON.stringify(oldData),
        dataNew: JSON.stringify(updatedItem),
        type: enumData.ACTION_TYPES.U,
        functionType: 'ItemEntity',
        functionId: id,
      };

      await this.actionLogService.create(actionLogCreateDto, trans);

      return updatedItem;
    });
  }

  /** delete */
  async delete(id: string, user: UserDataDTO): Promise<void> {
    return this.repo.manager.transaction(async (trans) => {
      const repo = trans.getRepository(ItemEntity);

      const existingItem = await repo.findOne({ where: { id } });
      if (!existingItem) {
        throw new NotFoundException(`Item with ID ${id} not found`);
      }

      await repo.remove(existingItem);

      // Create action log
      const actionLogCreateDto: ActionLogCreateDto = {
        createdByName: user.username,
        description: `Xóa sản phẩm: ${existingItem.name}`,
        dataOld: JSON.stringify(existingItem),
        type: enumData.ACTION_TYPES.D,
        functionType: 'ItemEntity',
        functionId: id,
      };

      await this.actionLogService.create(actionLogCreateDto, trans);
    });
  }

  /** soft delete - update status to discontinued */
  async softDelete(id: string, user: UserDataDTO): Promise<ItemEntity> {
    return this.repo.manager.transaction(async (trans) => {
      const repo = trans.getRepository(ItemEntity);

      const existingItem = await repo.findOne({ where: { id } });
      if (!existingItem) {
        throw new NotFoundException(`Item with ID ${id} not found`);
      }

      const oldStatus = existingItem.status;
      existingItem.status = 'discontinued';
      existingItem.updatedAt = new Date();
      existingItem.updatedBy = user.id;

      const updatedItem = await repo.save(existingItem);

      // Create action log
      const actionLogCreateDto: ActionLogCreateDto = {
        createdByName: user.username,
        description: `Ngừng kinh doanh sản phẩm "${existingItem.name}"`,
        dataOld: JSON.stringify({ status: oldStatus }),
        dataNew: JSON.stringify({ status: 'discontinued' }),
        type: enumData.ACTION_TYPES.D,
        functionType: 'ItemEntity',
        functionId: id,
      };

      await this.actionLogService.create(actionLogCreateDto, trans);

      return updatedItem;
    });
  }

  /** get featured items */
  async getFeaturedItems(limit = 10): Promise<ItemEntity[]> {
    return this.repo.find({
      where: { isFeatured: true, status: 'available' },
      order: { order: 'ASC', createdAt: 'DESC' },
      take: limit,
    });
  }

  /** get new items */
  async getNewItems(limit = 10): Promise<ItemEntity[]> {
    return this.repo.find({
      where: { isNew: true, status: 'available' },
      order: { createdAt: 'DESC' },
      take: limit,
    });
  }

  /** update stock quantity */
  async updateStock(
    id: string,
    quantity: number,
    user: UserDataDTO,
  ): Promise<ItemEntity> {
    return this.repo.manager.transaction(async (trans) => {
      const repo = trans.getRepository(ItemEntity);

      const existingItem = await repo.findOne({ where: { id } });
      if (!existingItem) {
        throw new NotFoundException(`Item with ID ${id} not found`);
      }

      const oldQuantity = existingItem.stockQuantity;
      existingItem.stockQuantity = quantity;
      existingItem.updatedAt = new Date();
      existingItem.updatedBy = user.id;

      const updatedItem = await repo.save(existingItem);

      // Create action log
      const actionLogCreateDto: ActionLogCreateDto = {
        createdByName: user.username,
        description: `Cập nhật tồn kho sản phẩm "${existingItem.name}" từ ${oldQuantity} thành ${quantity}`,
        dataOld: JSON.stringify({ stockQuantity: oldQuantity }),
        dataNew: JSON.stringify({ stockQuantity: quantity }),
        type: enumData.ACTION_TYPES.U,
        functionType: 'ItemEntity',
        functionId: id,
      };

      await this.actionLogService.create(actionLogCreateDto, trans);

      return updatedItem;
    });
  }

  /** bulk update status */
  async bulkUpdateStatus(
    ids: string[],
    status: string,
    user: UserDataDTO,
  ): Promise<void> {
    await this.repo.manager.transaction(async (trans) => {
      const repo = trans.getRepository(ItemEntity);

      await repo.update(
        { id: { $in: ids } as any },
        {
          status,
          updatedAt: new Date(),
          updatedBy: user.id,
        },
      );

      // Create action log
      const actionLogCreateDto: ActionLogCreateDto = {
        createdByName: user.username,
        description: `Cập nhật trạng thái hàng loạt: ${ids.length} sản phẩm thành ${status}`,
        dataNew: JSON.stringify({ ids, status }),
        type: enumData.ACTION_TYPES.U,
        functionType: 'ItemEntity',
      };

      await this.actionLogService.create(actionLogCreateDto, trans);
    });
  }
}
