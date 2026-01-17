package book.storage.db.core.runner;

import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("migration")
public class FlywayRunner implements ApplicationRunner {

    private final Flyway flyway;

    public FlywayRunner(Flyway flyway) {
        this.flyway = flyway;
    }

    @Override
    public void run(ApplicationArguments args) {
        flyway.migrate();
    }
}
