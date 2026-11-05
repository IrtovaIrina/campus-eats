package ru.campuseats;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "CampusEats API — Платформа предзаказа еды в кампусе",
                version = "1.0.0",
                description = "REST API сервиса заказа еды без очередей для студентов, точек питания и администрации университета",
                contact = @Contact(name = "CampusEats Dev")
        )
)
public class CampusEatsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusEatsApplication.class, args);
    }
}
