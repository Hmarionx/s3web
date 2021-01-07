package fr.pe.polygone.s3web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;

@SpringBootApplication(exclude = ContextStackAutoConfiguration.class)
public class S3webApplication {

    public static void main(String[] args) {
        SpringApplication.run(S3webApplication.class, args);
    }

}
