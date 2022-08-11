package com.tc.execution.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JsonMapper {

    private static final Logger LOG = LoggerFactory.getLogger(JsonMapper.class);
    private static final ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

    }


    public JsonMapper(){}

    public <T> T fromJsonWithOutAnnotation(String json, TypeReference<T> type) {
        try {
            objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
            T t = (T) objectMapper.readValue(json, type);
            objectMapper.configure(MapperFeature.USE_ANNOTATIONS, true);
            return t;
        } catch (IOException e){
            LOG.error("unable to parse json String {}",json);
            throw new RuntimeException(e);
        }

    }
    public <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return (T) objectMapper.readValue(json, type);
        } catch (IOException e){
            LOG.error("unable to parse json String {}",json);
            throw new RuntimeException(e);
        }

    }

    public <T> T fromJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            LOG.error("Failed to map data from json",e);
            return null;
        }
    }

    public <T> T fromJsonFile(File file, TypeReference<T> t) {
        try {
            return (T) objectMapper.readValue(file,t);
        } catch (IOException e) {
            LOG.error("unable to parse json String {}",file);
            throw new RuntimeException(e);

        }
    }

    public String toJson(Object object) {
        try {
            return  objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}