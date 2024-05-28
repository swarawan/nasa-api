package id.swarawan.asteroid.config.openfeign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.coyote.BadRequestException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Configuration
public class OpenFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder() {
            @Override
            public Exception decode(String s, Response response) {
                HttpStatus status = HttpStatus.valueOf(response.status());
                switch (status) {
                    case NOT_FOUND:
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
                    default:
                        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error on requesting an API");
                }
            }
        };
    }
}
