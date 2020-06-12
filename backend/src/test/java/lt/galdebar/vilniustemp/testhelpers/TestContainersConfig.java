package lt.galdebar.vilniustemp.testhelpers;

import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@TestConfiguration
public class TestContainersConfig {


    @ClassRule
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer();

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            mongoDBContainer.start();
            TestPropertyValues.of(
                    "spring.data.mongodb.host=" + mongoDBContainer.getHost(),
                    "spring.data.mongodb.port=" + mongoDBContainer.getMappedPort(27017)
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
