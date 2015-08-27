package com.endava.project.repository;

import com.mongodb.DBObject;

import java.util.List;

public interface ConfigRepository {

    DBObject readByPath(String path);

    DBObject readByName(String name);

    String create(DBObject dbObject);

    String update(DBObject oldConfig, DBObject newConfig);

    String delete(DBObject dbObject);

    List<DBObject> getChildren(String parentPath);

    DBObject getNameByPath(String path);

}
