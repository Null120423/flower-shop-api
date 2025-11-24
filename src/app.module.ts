import { MiddlewareConsumer, Module, NestModule } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { LogContextMiddleware } from './middleware/log-context.middleware';
import { AuthModule } from './modules/auth/auth.module';
import { EmailModule } from './modules/email/email.module';
import { ItemModule } from './modules/item/item.module';
import { PostModule } from './modules/post/post.module';
import { SepayModule } from './modules/se-pay/sepay.module';
import { TransactionModule } from './modules/transactions/transaction.module';
import { UploadModule } from './modules/upload/upload.module';
import { UserModule } from './modules/user/user.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    AuthModule,
    PostModule,
    EmailModule,
    UserModule,
    SepayModule,
    TransactionModule,
    UploadModule,
    ItemModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule implements NestModule {
  configure(consumer: MiddlewareConsumer) {
    // Apply the middleware to all routes
    consumer.apply(LogContextMiddleware).forRoutes('*');
  }
}
