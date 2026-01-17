package book;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ServerInfoLogger implements ApplicationListener<WebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        System.out.println("====================================");
        System.out.println("🚀 Core API Server started successfully");
        System.out.println("🌐 URL  : http://localhost:" + port);
        System.out.println("📦 ENV  : " + System.getProperty("spring.profiles.active"));
        System.out.println("====================================");
    }
}
