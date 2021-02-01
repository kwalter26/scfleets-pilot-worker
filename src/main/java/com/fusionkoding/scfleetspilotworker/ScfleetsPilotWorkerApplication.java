package com.fusionkoding.scfleetspilotworker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@SpringBootApplication
public class ScfleetsPilotWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScfleetsPilotWorkerApplication.class, args);
    }

}
