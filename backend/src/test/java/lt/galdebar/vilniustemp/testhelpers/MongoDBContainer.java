package lt.galdebar.vilniustemp.testhelpers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.testcontainers.containers.GenericContainer;

public class MongoDBContainer extends GenericContainer<MongoDBContainer> {

    @Value("${spring.data.mongodb.port:27017}")
    private int MONGODB_PORT;
    @Value("${spring.data.mongodb.database:vilniustempdb}")
    private String MONGO_DB_NAME;
    @Value("${spring.data.mongodb.username:mongouser}")
    private String MONGO_USERNAME;
    @Value("${spring.data.mongodb.passworod:someverysecretpassword}")
    private String MONGO_PASSWORD;
    private static final String DEFAULT_IMAGE_AND_TAG = "mongo:4.0";


    MongoDBContainer() {
        this(DEFAULT_IMAGE_AND_TAG);
    }

    private MongoDBContainer(@NotNull String image) {
        super(image);
        addExposedPort(27017);
        withEnv("NAME", "vilniustempdb");
        withEnv("MONGO_INITDB_ROOT_USERNAME", "mongouser");
        withEnv("MONGO_INITDB_ROOT_PASSWORD", "someverysecretpassword");
    }

    @NotNull
    public Integer getPort() {
        return getMappedPort(MONGODB_PORT);
    }
}
