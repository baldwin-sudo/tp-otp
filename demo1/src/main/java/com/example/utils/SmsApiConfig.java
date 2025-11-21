import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SmsApiConfig {

    @Bean
    public WebClient smsApiWebClient() {
        return WebClient.builder().build();
    }
}
