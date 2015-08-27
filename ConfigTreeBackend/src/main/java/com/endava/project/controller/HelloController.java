package com.endava.project.controller;

import com.endava.project.service.ConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ServerAddress serverAddress;

    @Autowired
    ConfigService configService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        return "hello";
    }

    @RequestMapping(value = "/read/{configPath}", method = RequestMethod.GET)
    public ResponseEntity<?> printEntry(@PathVariable String configPath) {
        String response = configService.readByPath(configPath);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/mergedRead/{configPath}-{strategy}", method = RequestMethod.GET)
    public ResponseEntity<?> getMergedConfig(@PathVariable String configPath, @PathVariable String strategy) {
        String response = configService.getMergedConfig(configPath, strategy);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> insert(@RequestBody String json) {
        String response = configService.create(json);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Access-Control-Request-Method", "POST");
        httpHeaders.add("Content-Type", "text/plain");
        return new ResponseEntity<Object>(response, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody String json) {
        String response = configService.update(json, false);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Access-Control-Request-Method", "PUT");
        httpHeaders.add("Content-Type", "text/plain");
        return new ResponseEntity<Object>(response, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = "text/plain; charset=utf-8")
    public ResponseEntity<?> delete(@RequestBody String json) {
        String response = configService.delete(json);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Access-Control-Request-Method", "DELETE");
        httpHeaders.add("Content-Type", "text/plain");
        return new ResponseEntity<Object>(response, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/getChildren/{parentPath}", method = RequestMethod.GET)
    public ResponseEntity<?> getChildren(@PathVariable String parentPath) {
        String response = configService.getChildren(parentPath);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/updateParent", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParent(@RequestBody String json) {
        String response = configService.update(json, true);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Access-Control-Request-Method", "PUT");
        httpHeaders.add("Content-Type", "text/plain");
        return new ResponseEntity<Object>(response, httpHeaders, HttpStatus.OK);
    }
}