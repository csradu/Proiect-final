/**
 * This file is part of the Endava Graduates training program
 * Created by Calin Radu 8/13/2015
 */
package com.endava.project.repository.impl;

import com.endava.project.repository.ConfigRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.regex.Pattern;

public class ConfigRepositoryImpl implements ConfigRepository {

    @Autowired
    DBCollection dbCollection;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public DBObject readByPath(String path) {
        BasicDBObject query = new BasicDBObject();
        query.put("_path", path);
        DBObject dbObject = dbCollection.findOne(query);
        if (dbObject != null) {
            dbObject.removeField("_id");
        }
        return dbObject;
    }

    @Override
    public DBObject readByName(String name) {
        BasicDBObject query = new BasicDBObject();
        query.put("_name", name);
        DBObject dbObject = dbCollection.findOne(query);
        return dbObject;
    }

    @Override
    public String create(DBObject dbObject) {
        String parent = (String) dbObject.get("_parentPath");
        dbObject.removeField("_parentPath");
        List<DBObject> children = getChildren(parent);
        int max = 0;
        for (DBObject it : children) {
            String path = (String) it.get("_path");
            String[] parts = path.split("\\.");
            if (Integer.parseInt(parts[parts.length - 1]) > max) {
                max = Integer.parseInt(parts[parts.length - 1]);
            }
        }
        dbObject.put("_path", parent + "." + (max + 1));
        dbCollection.insert(dbObject);
        return "Node successfully created!";
    }

    @Override
    public String update(DBObject oldConfig, DBObject newConfig) {
        dbCollection.update(oldConfig, newConfig);
        return "Node successfully updated!";
    }

    @Override
    public String delete(DBObject dbObject) {
        String path = (String) dbObject.get("_path");
        BasicDBObject query = new BasicDBObject();
        Pattern regex = Pattern.compile("^" + path); //not ok
        query.put("_path", regex);
        dbCollection.remove(query);

        return "Node successfully deleted!";
    }

    @Override
    public List<DBObject> getChildren(String parentPath) {
        BasicDBObject query = new BasicDBObject();
        Pattern regex = Pattern.compile("^" + parentPath + "[\\.][0-9]{1,5}$");
        query.put("_path", regex);
        List<DBObject> children = dbCollection.find(query).toArray();
        for (DBObject d : children) {
            d.removeField("_id");
        }
        return children;
    }

    @Override
    public DBObject getNameByPath(String path) {
        BasicDBObject query = new BasicDBObject();
        query.put("_path", path);
        DBObject dbObject = dbCollection.findOne(query);
        dbObject.removeField("_id");
        return dbObject;
    }
}
