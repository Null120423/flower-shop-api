package book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableJpaRepositories(basePackages = "book.storage.db.core.repository")
@EntityScan(basePackages = "book.storage.db.core.entity")
public class CoreApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(CoreApiApplication.class, args);
    }
}
