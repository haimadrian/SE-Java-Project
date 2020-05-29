package org.spa.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * @author Haim Adrian
 * @since 28-May-20
 */
public class JsonUtils {
   private static final ObjectMapper mapper = createObjectMapper();

   /**
    * Use this method to create and initialize an object mapper for our standards.
    * @return A new object mapper ready to work with Jsons.
    */
   public static ObjectMapper createObjectMapper() {
      ObjectMapper objectMapper = new ObjectMapper();

      //to enable standard indentation ("pretty-printing"):
      objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

      //to allow serialization of "empty" POJOs (no properties to serialize)
      objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

      //to ignore null property
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

      objectMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

      // Do not fail due to case sensitive properties.. These might be modified now and then...
      objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);

      //to turn off auto detection
      objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.NONE).withGetterVisibility(JsonAutoDetect.Visibility.NONE).withSetterVisibility(JsonAutoDetect.Visibility.NONE).withCreatorVisibility(JsonAutoDetect.Visibility.NONE).withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));

      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      return objectMapper;
   }

   /**
    * Read an object of the given type from the JSON in the given string
    *
    * @param <T> The type of the object to return
    * @param string The string containing the JSON
    * @param valueType The type to convert to
    * @return An object of type T
    * @throws IOException
    */
   public static <T> T readValue(String string, Class<T> valueType) throws IOException {
      return (T) mapper.readValue(string, valueType);
   }

   /**
    * Read an object of type T from the JSON in the given reader
    *
    * @param <T> The object type to return
    * @param reader The reader to read the JSON from
    * @param valueType The type of the value
    * @return an object of type T
    * @throws IOException
    */
   @SuppressWarnings("unchecked")
   public static <T> T readValue(Reader reader, Class<?> valueType) throws IOException {
      return (T) mapper.readValue(reader, valueType);
   }

   /**
    * Serialize the given object to a string
    *
    * @param object The object to serialize
    * @param <T> The object type
    * @return A string containing the object serialized
    * @throws JsonProcessingException
    */
   public static <T> String writeValueAsString(T object) throws JsonProcessingException {
      return mapper.writeValueAsString(object);
   }

   /**
    * Serialize the given object to a writer
    *
    * @param object The object to serialize
    * @param <T> The object type
    * @throws IOException
    */
   public static <T> void writeValue(Writer writer, T object) throws IOException {
      mapper.writeValue(writer, object);
   }
}
