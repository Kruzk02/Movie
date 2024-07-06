package com.app.Config;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Configuration
public class DatabaseInitialization {

    private static final Logger log = LogManager.getLogger(DatabaseInitialization.class);
    @Autowired
    private DatabaseClient databaseClient;
//
//    @PostConstruct
//    public void initializeDatabase(){
//        try{
//            executeSqlScript("sql/actor.sql");
//            executeSqlScript("sql/director.sql");
//            executeSqlScript("sql/genre.sql");
//            executeSqlScript("sql/movie.sql");
//            executeSqlScript("sql/rating.sql");
//            executeSqlScript("sql/privilege.sql");
//            executeSqlScript("sql/role.sql");
//            executeSqlScript("sql/user.sql");
//
//            executeSqlScript("sql/actor_movie.sql");
//            executeSqlScript("sql/director_movie.sql");
//            executeSqlScript("sql/genre_movie.sql");
//            executeSqlScript("sql/role_privilege.sql");
//            executeSqlScript("sql/user_role.sql");
//            executeSqlScript("sql/verification_token.sql");
//        } catch (Exception e) {
//            log.error("Error initializing database: {}", e.getMessage());
//        }
//    }
//
//    private void executeSqlScript(String fileName){
//        Resource resource = new ClassPathResource(fileName);
//        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
//            String sqlScript = FileCopyUtils.copyToString(reader);
//            databaseClient.sql(sqlScript).fetch().rowsUpdated().block();
//            log.info("Successfully executed SQL script from file: {}", fileName);
//        } catch (Exception e) {
//            log.error("Error executing SQL script from file {}: {}", fileName, e.getMessage());
//        }
//    }
//
//    @PostConstruct
//    public void insertDataToTable(){
//        String sql = """
//                INSERT INTO Genre (name) VALUES
//                ('Action'),
//                ('Adventure'),
//                ('Comedy'),
//                ('Drama'),
//                ('Romance'),
//                ('Horror'),
//                ('Science_Fiction'),
//                ('Fantasy'),
//                ('Thriller'),
//                ('Mystery'),
//                ('Crime'),
//                ('Animation'),
//                ('Documentary'),
//                ('Historical'),
//                ('Biographical'),
//                ('Musical'),
//                ('War'),
//                ('Western'),
//                ('Spy_Espionage'),
//                ('Supernatural'),
//                ('Psychological'),
//                ('Family'),
//                ('Sports'),
//                ('Disaster'),
//                ('Romance_Comedy'),
//                ('Musical_Comedy'),
//                ('Martial_Arts'),
//                ('Noir'),
//                ('Satire'),
//                ('Mockumentary');
//                INSERT INTO Rating (rating) VALUES
//                (1.0),
//                (2.0),
//                (3.0),
//                (4.0),
//                (5.0);
//                """;
//        databaseClient.sql(sql).fetch().rowsUpdated().block();
//    }
}
