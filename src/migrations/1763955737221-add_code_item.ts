import { MigrationInterface, QueryRunner } from 'typeorm';

export class AddCodeItem1763955737221 implements MigrationInterface {
  name = 'AddCodeItem1763955737221';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(
      `ALTER TABLE "items" ADD "code" character varying(255) NOT NULL`,
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query(`ALTER TABLE "items" DROP COLUMN "code"`);
  }
}
