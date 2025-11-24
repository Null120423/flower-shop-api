import { Injectable } from '@nestjs/common';
import { FileArchivalEntity } from 'src/entities/fire-archival.entity';
import { FileArchiveRepository } from 'src/repositories/master-data.repository';
import { FileArchiveCreateDto } from './dto/create.dto';

@Injectable()
export class FileArchiveService {
  constructor(private repo: FileArchiveRepository) {}

  // find all files by functionId
  async findByFunctionId(functionId: string) {
    return this.repo.find({ where: { functionId } });
  }

  async createFileArchive(body: FileArchiveCreateDto) {
    const { url, name, dataType, functionId } = body;
    const fileAr = new FileArchivalEntity();
    fileAr.url = url;
    fileAr.name = name;
    fileAr.dataType = dataType;
    fileAr.functionId = functionId;
    return this.repo.insert(fileAr);
  }
}
