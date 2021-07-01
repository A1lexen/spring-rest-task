package co.inventorsoft.spring_rest_task;


import co.inventorsoft.spring_rest_task.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class SpringRestTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRestTaskApplication.class, args);
    }

}
