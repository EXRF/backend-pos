package exrf.pos.config;

import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Configuration
public class DotenvConfig implements PropertySourceLoader {
    @Override
    public String[] getFileExtensions() {
        return new String[]{".env"};
    }

    @Override
    public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
        Properties properties = new Properties();
        properties.load(resource.getInputStream());
        return Collections.singletonList(new PropertiesPropertySource(name, properties));
    }
    public static void loadFromRootDir(ConfigurableEnvironment environment) {
        Path path = Paths.get(".env");
        if (Files.exists(path)) {
            try (InputStream inputStream = Files.newInputStream(path)) {
                Properties properties = new Properties();
                properties.load(inputStream);

                environment.getPropertySources().addFirst(
                        new PropertiesPropertySource("rootEnvFile", properties)
                );
            } catch (IOException e) {
                throw new RuntimeException("Failed to load .env file from root directory", e);
            }
        }
    }



}
