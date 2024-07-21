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
//            executeSqlScript("sql/movie.sql");
//            executeSqlScript("sql/user.sql");
//            executeSqlScript("sql/genre.sql");
//            executeSqlScript("sql/privilege.sql");
//            executeSqlScript("sql/role.sql");
//            executeSqlScript("sql/rating.sql");
//
//            executeSqlScript("sql/actor_movie.sql");
//            executeSqlScript("sql/director_movie.sql");
//            executeSqlScript("sql/movie_media.sql");
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
//                INSERT INTO Genre (id,name) VALUES
//                (1,'Action'),
//                (2,'Adventure'),
//                (3,'Comedy'),
//                (4,'Drama'),
//                (5,'Romance'),
//                (6,'Horror'),
//                (7,'Science_Fiction'),
//                (8,'Fantasy'),
//                (9,'Thriller'),
//                (10,'Mystery'),
//                (11,'Crime'),
//                (12,'Animation'),
//                (13,'Documentary'),
//                (14,'Historical'),
//                (15,'Biographical'),
//                (16,'Musical'),
//                (17,'War'),
//                (18,'Western'),
//                (19,'Spy_Espionage'),
//                (20,'Supernatural'),
//                (21,'Psychological'),
//                (22,'Family'),
//                (23,'Sports'),
//                (24,'Disaster'),
//                (25,'Romance_Comedy'),
//                (26,'Musical_Comedy'),
//                (27,'Martial_Arts'),
//                (28,'Noir'),
//                (29,'Satire'),
//                (30,'Mockumentary');
//                """;
//        databaseClient.sql(sql).fetch().rowsUpdated().block();
//    }
}
