import { ApiProperty } from '@nestjs/swagger';
import { compare, hash } from 'bcrypt';
import { BeforeInsert, BeforeUpdate, Column, Entity, OneToOne } from 'typeorm';
import { BaseEntityCustom } from './base.entity';
import { UserDetailEntity } from './userDetail.entity';
@Entity(`users`)
export class UserEntity extends BaseEntityCustom {
  @Column({ length: 500 })
  username: string;

  @Column({ length: 500 })
  email: string;

  @Column({ length: 500 })
  password: string;

  @Column()
  avatar: string;

  @Column({
    nullable: true,
  })
  verifyAt: Date;

  @Column({
    nullable: true,
    default: false,
  })
  isAdmin: boolean;

  @Column({
    nullable: true,
  })
  verifyCode: string;

  @Column({
    default: false,
    nullable: true,
  })
  isActive: boolean;

  @Column({
    default: false,
    nullable: true,
  })
  isLoginWithGoogle: boolean;

  @Column({
    default: false,
    nullable: true,
  })
  isLoginWithFacebook: boolean;

  @Column({
    default: false,
  })
  isUpdateDetail: boolean;

  @Column({
    nullable: true,
  })
  verifyExpiredTime: Date;

  @Column({
    nullable: true,
    default: false,
  })
  isPremium: boolean;

  @Column({ default: 0 })
  totalPlanTripInMonth: number;

  @BeforeInsert()
  @BeforeUpdate()
  async hashPassword() {
    if (this.password && !this.password.startsWith('$2b$')) {
      const hashedPassword = await hash(this.password, 10);
      this.password = hashedPassword;
    }
  }

  comparePassword(candidate: string) {
    console.log('Candidate Password:', candidate);
    console.log('Stored Password:', this.password);
    return compare(candidate, this.password);
  }
  @OneToOne(() => UserDetailEntity, (userDetail) => userDetail.user)
  userDetail: Promise<UserDetailEntity>;

  /** current subscriptions  */
  @ApiProperty({
    description: 'Current subscriptions or related metadata for the user',
    type: 'object',
    additionalProperties: true,
    required: false,
  })
  @Column({ type: 'jsonb', nullable: true })
  subscriptions?: Record<string, any>;
}
