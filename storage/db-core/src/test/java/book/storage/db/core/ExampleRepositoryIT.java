package book.storage.db.core;

import static org.assertj.core.api.Assertions.assertThat;

import book.storage.db.CoreDbContextTest;

import book.storage.db.core.repository.UserRepository;
import org.junit.jupiter.api.Test;

public class ExampleRepositoryIT extends CoreDbContextTest {

    private final UserRepository exampleRepository;

    public ExampleRepositoryIT(UserRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @Test
    public void testShouldBeSavedAndFound() {
        ExampleEntity saved = exampleRepository.save(new ExampleEntity("SPRING_BOOT"));
        assertThat(saved.getExampleColumn()).isEqualTo("SPRING_BOOT");

        ExampleEntity found = exampleRepository.findById(saved.getId()).get();
        assertThat(found.getExampleColumn()).isEqualTo("SPRING_BOOT");
    }

}
