package com.safetynet.safetynetalerts;

import com.safetynet.safetynetalerts.repositories.Database;
import com.safetynet.safetynetalerts.services.json.JSONDataReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SafetyNetAlertsApplication {

    public static void main(String[] args) {

        SpringApplication.run(SafetyNetAlertsApplication.class, args);
    }

    @Bean
    public Database jsonDatabase(@Value("${json.path:#{null}}") String path) {

        return new Database(JSONDataReader.readJsonFile(path));
    }

}
