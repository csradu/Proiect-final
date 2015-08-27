/**
 * This file is part of the Endava Graduates training program
 * Created by Calin Radu 8/25/2015
 */
package com.endava.project.service;

import com.endava.project.repository.impl.ConfigRepositoryImpl;
import com.endava.project.service.impl.ConfigServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@PrepareForTest(ConfigServiceImpl.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class ConfigServiceImplTestCase {

    @InjectMocks
    @Autowired
    private ConfigServiceImpl configService;

    @Mock
    private ConfigRepositoryImpl configRepository;

    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setUpClass() {
        objectMapper = new ObjectMapper();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        configService = null;
    }

    //-----------------------CREATE TESTS-------------------------
    @Test
    public void testValidJsonCreate() {
        Mockito.when(configRepository.create((DBObject) Mockito.anyObject())).thenReturn("Node successfully created!");
        String actual = configService.create("{'key' : 'value', '_name' : 'test', 'properties':'testProps'}");
        Map<String, String> actualMap;
        actualMap = (HashMap) JSON.parse(actual);
        assert actualMap.get("Error").equals("false");
        assert actualMap.get("Message").equals("Node successfully created!");
    }

    @Test
    public void testInvalidJsonCreate() {
        Mockito.when(configRepository.create((DBObject) Mockito.anyObject())).thenReturn("Node successfully created!");
        String actual = configService.create("InvalidJson");
        Map<String, String> actualMap;
        actualMap = (HashMap) JSON.parse(actual);
        assert actualMap.get("ErrorMessage").equals("Trying to insert an invalid node!");
        assert actualMap.get("Error").equals("true");
    }

    @Test
    public void testNullCreate() {
        Mockito.when(configRepository.create((DBObject) Mockito.anyObject())).thenReturn("Node successfully created!");
        String actual = configService.create(null);
        Map<String, String> actualMap;
        actualMap = (HashMap) JSON.parse(actual);
        assert actualMap.get("ErrorMessage").equals("Trying to insert a null node!");
        assert actualMap.get("Error").equals("true");
    }

    @Test
    public void testEmptyCreate() {
        Mockito.when(configRepository.create((DBObject) Mockito.anyObject())).thenReturn("Node successfully created!");
        String actual = configService.create("{'key' : ''}");
        Map<String, String> actualMap;
        actualMap = (HashMap) JSON.parse(actual);
        assert actualMap.get("ErrorMessage").equals("One of the mandatory fields is empty!");
        assert actualMap.get("Error").equals("true");
    }


    //-----------------------READ BY PATH TESTS-------------------------
    @Test
    public void testNormalResponseReadByPath() throws JsonProcessingException {
        Mockito.when(configRepository.readByPath(Mockito.anyString())).thenReturn(new BasicDBObject("key", "value"));
        String actual = configService.readByPath("1");
        Map<String, String> actualMap;
        actualMap = (HashMap) JSON.parse(actual);
        assert actualMap.get("key").equals("value");
        assert actualMap.get("Error").equals("false");
    }

    @Test
    public void testNullResponseReadByPath() {
        Mockito.when(configRepository.readByPath(Mockito.anyString())).thenReturn(null);
        String actual = configService.readByPath("1");
        Map<String, String> actualMap;
        actualMap = (HashMap) JSON.parse(actual);
        assert actualMap.get("ErrorMessage").equals("The node does not exist!");
        assert actualMap.get("Error").equals("true");
    }


    //-----------------------UPDATE TESTS-------------------------

    @Test
    public void testValidJsonUpdate() {
        Mockito.when(configRepository.update((DBObject) Mockito.anyObject(), (DBObject) Mockito.anyObject())).
                thenReturn("Node successfully updated!");
        String actual = configService.update("{'key' : 'value', '_name' : 'testName', 'properties' : 'testProps'}", false);
        Map<String, String> actualMap;
        actualMap = (HashMap) JSON.parse(actual);
        assert actualMap.get("Error").equals("false");
        assert actualMap.get("Message").equals("Node successfully updated!");
    }

    @Test
    public void testInvalidJsonUpdate() {
        Mockito.when(configRepository.update((DBObject) Mockito.anyObject(), (DBObject) Mockito.anyObject())).
                thenReturn("Node successfully updated!");
        String actual = configService.update("InvalidJson", false);
        Map<String, String> actualMap;
        actualMap = (HashMap) JSON.parse(actual);
        assert actualMap.get("ErrorMessage").equals("Trying to update an invalid node!");
        assert actualMap.get("Error").equals("true");
    }

    @Test
    public void testNullUpdate() {
        Mockito.when(configRepository.update((DBObject) Mockito.anyObject(), (DBObject) Mockito.anyObject())).
                thenReturn("Node successfully updated!");
        String actual = configService.update(null, false);
        Map<String, String> actualMap;
        actualMap = (HashMap) JSON.parse(actual);
        assert actualMap.get("ErrorMessage").equals("Trying to update a null node!");
        assert actualMap.get("Error").equals("true");
    }


    //-----------------------DELETE TESTS-------------------------

    @Test
    public void testValidJsonDelete() {
        Mockito.when(configRepository.delete((DBObject) Mockito.anyObject())).thenReturn("Node successfully deleted!");
        String actual = configService.delete("{'key' : 'value', '_path' : '1.1'}");
        Map<String, String> actualMap;
        actualMap = (HashMap) JSON.parse(actual);
        assert actualMap.get("Error").equals("false");
        assert actualMap.get("Message").equals("Node successfully deleted!");
        ;
    }

    @Test
    public void testInvalidJsonDelete() {
        Mockito.when(configRepository.delete((DBObject) Mockito.anyObject())).thenReturn("Node successfully deleted!");
        String actual = configService.delete("InvalidJson");
        Map<String, String> actualMap;
        actualMap = (HashMap) JSON.parse(actual);
        assert actualMap.get("ErrorMessage").equals("Trying to delete an invalid node!");
        assert actualMap.get("Error").equals("true");
    }

    @Test
    public void testNullDelete() {
        Mockito.when(configRepository.delete((DBObject) Mockito.anyObject())).thenReturn("Node successfully deleted!");
        String actual = configService.delete(null);
        Map<String, String> actualMap;
        actualMap = (HashMap) JSON.parse(actual);
        assert actualMap.get("ErrorMessage").equals("Trying to delete null!");
        assert actualMap.get("Error").equals("true");
    }


}
