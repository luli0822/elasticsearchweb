package com.jahia.elasticsearch.sample.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jahia.elasticsearch.sample.security.EsYmlConfig;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * IOUtil class to read value from and write value to YAML file
 */
public class JahiaUtil {
	private static final String YAML_FILE_NAME = "app.yaml";
	
    private JahiaUtil() {}

    public static EsYmlConfig readYamlConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        return objectMapper.readValue(JahiaUtil.class.getClassLoader().getResourceAsStream(YAML_FILE_NAME), EsYmlConfig.class);
    }
    
    public static <T> void writeToYamlConfig(T object) throws IOException, URISyntaxException {
    	ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    	URL resource = JahiaUtil.class.getResource(YAML_FILE_NAME);
        objectMapper.writeValue(Paths.get(resource.toURI()).toFile(), object);
    }
    
    public static <T> boolean isNullOrEmpty(Collection<T> collection) {
    	return null == collection || collection.isEmpty();
    }
}
