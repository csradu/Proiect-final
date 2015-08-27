package com.endava.project.service;

public interface ConfigService {

    String readByPath(String path);

    String create(String json);

    String update(String json, boolean parentUpdate);

    String delete(String json);

    String getChildren(String parentPath);

    String getMergedConfig(String configPath, String strategy);

}
