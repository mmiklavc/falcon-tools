package com.michaelmiklavcic.falconer.test.entity;

import java.io.*;

import com.fasterxml.jackson.databind.*;

public class EntityConfigLoader {

    private static EntityConfigLoader loader;
    private ObjectMapper mapper;

    private EntityConfigLoader(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public static EntityConfigLoader getInstance() {
        if (loader == null) {
            loader = new EntityConfigLoader(new ObjectMapper());
        }
        return loader;
    }

    public EntityConfig load(File config) throws IOException {
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        EntityConfig ec = mapper.readValue(config, EntityConfig.class);
//        String parent = ec.getParent();
//        if (parent != null) {
//            ec.merge(load(new File(config.getParent(), parent)));
//        }
        return ec;
    }

}
