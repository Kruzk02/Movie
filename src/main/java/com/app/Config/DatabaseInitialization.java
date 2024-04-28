package com.app.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
public class DatabaseInitialization {

    @Autowired
    private DatabaseClient databaseClient;

    @PostConstruct
    public void initializeDatabase(){
        databaseClient.sql(
                "CREATE TABLE IF NOT EXISTS Director(" +
                "id SERIAL PRIMARY KEY," +
                "first_name VARCHAR(255) NOT NULL," +
                "last_name VARCHAR(255) NOT NULL," +
                "nationality VARCHAR(255) NOT NULL," +
                "birthDate DATE NOT NULL);"+

                "CREATE TABLE IF NOT EXISTS Actor("+
                "id SERIAL PRIMARY KEY,"+
                "first_name VARCHAR(255) NOT NULL,"+
                "last_name VARCHAR(255) NOT NULL,"+
                "nationality VARCHAR(255) NOT NULL,"+
                "birthDate DATE NOT NULL);"
        ).fetch().rowsUpdated().block();
    }
}
