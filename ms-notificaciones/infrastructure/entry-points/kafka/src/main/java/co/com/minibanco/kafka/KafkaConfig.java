package co.com.minibanco.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    @Bean
    public DefaultErrorHandler manejadorDeErrores(KafkaTemplate<?, ?> template) {
        return new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(template),   // si se agotan los reintentos: al DLT
                new FixedBackOff(1000L, 3));                   // 3 reintentos, esperando 1 segundo entre cada uno
    }
}