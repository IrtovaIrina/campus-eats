package ru.campuseats.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.campuseats.repository.UserRepository;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {

    private final UserRepository userRepository;
    private final DataSource dataSource;

    @Bean
    public OpenAPI campusEatsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CampusEats REST API")
                        .version("1.0.0")
                        .description("Сервис предзаказа еды в точках питания кампуса без очередей")
                        .contact(new Contact()
                                .name("CampusEats Team")
                                .email("admin@campus.ru")));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Bean
    public CommandLineRunner initDatabaseFromSql() {
        return args -> {
            if (userRepository.count() == 0) {
                log.info("База данных пуста. Выполнение SQL-скрипта инициализации classpath:data.sql...");
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                        false, false, "UTF-8", new ClassPathResource("data.sql")
                );
                populator.execute(dataSource);
                log.info("SQL-скрипт data.sql успешно выполнен (заполнены все 13 таблиц).");
            } else {
                log.info("База данных уже содержит данные — пропуск выполнения data.sql.");
            }
        };
    }
}