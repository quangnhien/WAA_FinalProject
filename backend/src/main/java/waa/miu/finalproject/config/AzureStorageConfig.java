package waa.miu.finalproject.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureStorageConfig {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    @Bean
    public BlobContainerClient blobContainerClient() {
        return new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();
    }
}
