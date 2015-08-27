/**
 * This file is part of the Endava Graduates training program
 * Created by Calin Radu 8/13/2015
 */
package com.endava.project.config;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoBeanConfig {
    @Autowired
    MongoClient mongoClient;

    @Bean
    public DB getDB() {
        return mongoClient.getDB("test");
    }

    @Bean
    public DBCollection getDBCollection()    {
        return getDB().getCollection("testCol");
    }
}
