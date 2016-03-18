package modbusemu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import modbusemu.exception.PropertiesLoadingException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class PropertiesLoader.
 */
public final class PropertiesLoader {

    private PropertiesLoader() {
    }

    /**
     * Reads a set of properties from a file.
     * 
     * @param fileName the name of file with properties
     * @return the set of properties
     * @throws PropertiesLoadingException properties loading failed due to io aspects
     */
    public static Properties getProperties(String fileName) throws PropertiesLoadingException {
        final Properties props = new Properties();

        try {
            final InputStream input = new FileInputStream(fileName);
            props.load(input);
        } catch (IOException e) {
            throw new PropertiesLoadingException();
        }

        return props;
    }

    /**
     * Loads an HashMap from a JSON file.
     * 
     * @param fileName the name of the file with properties
     * @return the set of properties
     * @throws PropertiesLoadingException properties loading failed due to io or json
     *             aspects
     */
    public static HashMap<String, Integer> getJSONMap(String fileName) throws PropertiesLoadingException {
        HashMap<String, Integer> addressMap = null;

        try {
            final TypeReference<HashMap<String, Integer>> typeRef = new TypeReference<HashMap<String, Integer>>() {
            };
            final ObjectMapper mapper = new ObjectMapper();
            addressMap = mapper.readValue(new File(fileName), typeRef);
        } catch (JsonParseException e) {
            throw new PropertiesLoadingException();
        } catch (JsonMappingException e) {
            throw new PropertiesLoadingException();
        } catch (IOException e) {
            throw new PropertiesLoadingException();
        }

        return addressMap;
    }
}
