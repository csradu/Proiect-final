/**
 * This file is part of the Endava Graduates training program
 * Created by Calin Radu 8/13/2015
 */
package com.endava.project.service.impl;

import com.endava.project.repository.ConfigRepository;
import com.endava.project.service.ConfigService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigServiceImpl implements ConfigService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ConfigRepository configRepository;

    @Override
    public String create(String json) {
        String response;
        if (json != null) {
            try {
                if (validate(json)) {
                    DBObject dbObject = (DBObject) JSON.parse(json);
                    if (readByName(dbObject.get("_name").toString()) != null) {
                        response = generateErrorResponse("A node with this name already exists!");
                    } else {
                        response = generateSuccessResponse(configRepository.create(dbObject));
                    }
                } else {
                    response = generateErrorResponse("One of the mandatory fields is empty!");
                }
            } catch (JSONParseException e) {
                response = generateErrorResponse("Trying to insert an invalid node!");
            }
        } else {
            response = generateErrorResponse("Trying to insert a null node!");
        }
        return response;
    }

    @Override
    public String readByPath(String path) {
        String response;
        DBObject dbObject = configRepository.readByPath(path);
        if (dbObject != null) {
            try {
                dbObject.put("Error", "false");
                response = objectMapper.writeValueAsString(dbObject);
            } catch (JsonProcessingException e) {
                response = generateErrorResponse("Invalid node read from database!");
            }
        } else {
            response = generateErrorResponse("The node does not exist!");
        }
        return response;
    }

    @Override
    public String update(String json, boolean parentUpdate) {
        String response;
        if (json != null) {
            try {
                if(validate(json)) {
                    DBObject newConfig = (DBObject) JSON.parse(json);
                    if (readByName(newConfig.get("_name").toString()) != null) {
                        response = generateErrorResponse("A node with this name already exists!");
                    } else {
                        DBObject oldConfig = configRepository.readByPath((String) newConfig.get("_path"));
                        response = generateSuccessResponse(configRepository.update(oldConfig, newConfig));
                    }
                } else {
                    response = generateErrorResponse("One of the mandatory fields is empty!");
                }
            } catch (JSONParseException e) {
                response = generateErrorResponse("Trying to update an invalid node!");
            }
        } else {
            response = generateErrorResponse("Trying to update a null node!");
        }
        return response;
    }


    @Override
    public String delete(String json) {
        String response;
        if (json != null) {
            try {
                DBObject dbObject = (DBObject) JSON.parse(json);
                if(dbObject.get("_path").toString().equals("1")) {
                    response = generateErrorResponse("Cannot delete root node");
                } else {
                    response = generateSuccessResponse(configRepository.delete(dbObject));
                }
            } catch (JSONParseException e) {
                response = generateErrorResponse("Trying to delete an invalid node!");
            }
        } else {
            response = generateErrorResponse("Trying to delete null!");
        }
        return response;
    }

    @Override
    public String getChildren(String parentPath) {
        List<DBObject> children = configRepository.getChildren(parentPath);
        String json = "";
        try {
            json = objectMapper.writeValueAsString(children);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    @Override
    public String getMergedConfig(String configPath, String strategy) {
        HashMap<String, String> mergedConfig = new HashMap<String, String>();
        StringBuilder pathBuilder = new StringBuilder();
        String[] nodes = configPath.split("\\.");
        if (strategy.equals("BOTTOM_UP")) {
            for (String s : nodes) {
                pathBuilder.append(s);
                DBObject dbObject = configRepository.readByPath(pathBuilder.toString());
                List<DBObject> objects = (List<DBObject>) dbObject.get("properties");
                for (DBObject d : objects) {
                    mergedConfig.put(d.toMap().get("property").toString(), d.toMap().get("value").toString());
                }
                pathBuilder.append(".");
            }
        } else if (strategy.equals("TOP_DOWN")) {
            pathBuilder.append(configPath);
            while (pathBuilder.length() != 0) {
                DBObject dbObject = configRepository.readByPath(pathBuilder.toString());
                List<DBObject> objects = (List<DBObject>) dbObject.get("properties");
                for (DBObject d : objects) {
                    mergedConfig.put(d.toMap().get("property").toString(), d.toMap().get("value").toString());
                }
                if (pathBuilder.length() == 1) {
                    pathBuilder.delete(pathBuilder.length() - 1, pathBuilder.length());
                } else {
                    pathBuilder.delete(pathBuilder.length() - 2, pathBuilder.length());
                }
            }
        } else {
            return null;
        }
        String json = "";
        try {
            json = objectMapper.writeValueAsString(mergedConfig);
        } catch (JsonProcessingException e) {

        }
        return json;
    }

    //----------------Util methods
    private String generateErrorResponse(String message) {
        String response;
        Map<String, String> mapResponse = new HashMap<String, String>();
        mapResponse.put("Error", "true");
        mapResponse.put("ErrorMessage", message);
        try {
            response = objectMapper.writeValueAsString(mapResponse);
        } catch (JsonProcessingException e2) {
            response = "";
        }
        return response;
    }

    private String generateSuccessResponse(String message) {
        String response;
        Map<String, String> mapResponse = new HashMap<String, String>();
        mapResponse.put("Message", message);
        mapResponse.put("Error", "false");
        try {
            response = objectMapper.writeValueAsString(mapResponse);
        } catch (JsonProcessingException e) {
            response = "";
        }
        return response;
    }

    private DBObject readByName(String name) {
        return configRepository.readByName(name);
    }

    private boolean validate(String json) throws JSONParseException {
        DBObject dbObject = (DBObject) JSON.parse(json);
        if (!dbObject.containsField("_name") || !dbObject.containsField("properties")) {
            return false;
        }
        for (String s : dbObject.keySet()) {
            if (dbObject.get(s) == null || dbObject.get(s).toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
