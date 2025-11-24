import { TransactionEntity } from 'src/entities';
import { CustomRepository } from 'src/typeorm/typeorm-decorater';
import { Repository } from 'typeorm';

@CustomRepository(TransactionEntity)
export class TransactionRepository extends Repository<TransactionEntity> {}
