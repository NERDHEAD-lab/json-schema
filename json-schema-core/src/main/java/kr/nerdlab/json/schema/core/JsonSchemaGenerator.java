package kr.nerdlab.json.schema.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.nerdlab.json.schema.annotation.SchemaDefinition;
import kr.nerdlab.json.schema.annotation.SchemaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.*;

/**
 * Generates a JSON Schema for a given Java class using Jackson annotations.
 * The generated schema can be used to validate JSON objects against the structure defined by the class.
 *
 * @see <a href="https://json-schema.org/">JSON Schema</a>
 * @see <a href="https://json-schema.org/specification">JSON Schema Specification</a>
 * @see <a href="http://json-schema.org/draft-07/schema#/">JSON Schema Draft 07</a>
 */
public class JsonSchemaGenerator {

    private static final Logger logger = LoggerFactory.getLogger(JsonSchemaGenerator.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static boolean writeSchemaToFile(Class<?> rootClass, String filePath) throws IOException {
        String schemaJson = generateSchema(rootClass);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(schemaJson);
            logger.info("JSON Schema successfully written to '{}'", filePath);
            return true;
        } catch (IOException e) {
            logger.error("Error writing JSON Schema to file '{}'", filePath, e);
            throw e;
        }
    }

    /**
     * Generates a JSON Schema for the given root class.
     * The root class must be annotated with @SchemaDefinition.
     *
     * @param rootClass the root class to generate the schema for
     * @return a JSON string representing the schema
     * @throws IOException              if an error occurs while generating the schema
     * @throws IllegalArgumentException if the root class is not annotated with @SchemaDefinition
     */
    public static String generateSchema(Class<?> rootClass) throws IOException {
        Map<String, Object> schema = new LinkedHashMap<>();
        Set<Class<?>> processedClasses = new HashSet<>();

        SchemaDefinition schemaDefinition = rootClass.getAnnotation(SchemaDefinition.class);
        if (schemaDefinition == null) {
            throw new IllegalArgumentException("Root class must be annotated with @SchemaDefinition");
        }

        schema.put("$schema", schemaDefinition.version().getUrl());
        schema.put("title", schemaDefinition.title());
        if (!schemaDefinition.description().isEmpty()) {
            schema.put("description", schemaDefinition.description());
        }
        schema.put("type", "object");
        schema.put("additionalProperties", schemaDefinition.additionalProperties());

        Map<String, Object> definitions = new LinkedHashMap<>();
        schema.put("definitions", definitions);

        processClass(rootClass, schema, definitions, processedClasses);

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(schema);
    }

    /**
     * Generates a sample JSON string based on default values in DTO annotations.
     */
    public static String generateSampleJsonString(Class<?> rootClass) throws IOException {
        try {
            Map<String, Object> sampleMap = createSampleObject(rootClass);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(sampleMap);
        } catch (Exception e) {
            throw new IOException("Failed to generate sample JSON", e);
        }
    }

    /**
     * Binds a JSON file to a schema URI by adding a '$schema' property to the JSON object.
     *
     * @param jsonFile  the JSON file to bind
     * @param schemaUri the URI of the schema to bind to the JSON file
     * @throws IOException if an error occurs while reading the JSON file or schema
     */
    public static void bindSchemaToJsonFile(File jsonFile, URI schemaUri) throws IOException {
        JsonNode originalJsonNode = mapper.readTree(jsonFile);
        ObjectNode newJsonNode = mapper.createObjectNode();

        newJsonNode.put("$schema", schemaUri.toString());

        originalJsonNode.fieldNames().forEachRemaining(key -> {
            if (key.equals("$schema")) {
                return;
            }

            newJsonNode.set(key, originalJsonNode.get(key));
        });

        mapper.writeValue(jsonFile, newJsonNode);
    }




    private static void processClass(Class<?> clazz, Map<String, Object> parentSchemaNode, Map<String, Object> definitions, Set<Class<?>> processedClasses) {
        if (processedClasses.contains(clazz)) {
            return;
        }
        processedClasses.add(clazz);

        Map<String, Object> classDefinition = new LinkedHashMap<>();
        if (parentSchemaNode == null) {
            classDefinition.put("type", "object");
            classDefinition.put("title", clazz.getSimpleName());
        }

        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> requiredFields = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(SchemaProperty.class)) {
                continue;
            }

            String propertyName = getPropertyName(field);
            SchemaProperty schemaProperty = field.getAnnotation(SchemaProperty.class);
            Map<String, Object> propertyNode = buildPropertyNode(field, schemaProperty, definitions, processedClasses);

            properties.put(propertyName, propertyNode);

            if (schemaProperty.required()) {
                requiredFields.add(propertyName);
            }
        }

        Map<String, Object> targetNode = (parentSchemaNode != null) ? parentSchemaNode : classDefinition;
        targetNode.put("properties", properties);
        if (!requiredFields.isEmpty()) {
            targetNode.put("required", requiredFields);
        }

        if (parentSchemaNode == null) {
            definitions.put(clazz.getSimpleName(), classDefinition);
        }
    }

    private static Map<String, Object> buildPropertyNode(Field field, SchemaProperty schemaProperty, Map<String, Object> definitions, Set<Class<?>> processedClasses) {
        Map<String, Object> propertyNode = new LinkedHashMap<>();

        if (!schemaProperty.description().isEmpty()) propertyNode.put("description", schemaProperty.description());
        if (!schemaProperty.defaultValue().isEmpty()) propertyNode.put("default", schemaProperty.defaultValue());
        if (!(schemaProperty.examples().length == 0)) propertyNode.put("examples", schemaProperty.examples());
        if (!schemaProperty.pattern().isEmpty()) propertyNode.put("pattern", schemaProperty.pattern());
        if (!schemaProperty.format().isEmpty()) propertyNode.put("format", schemaProperty.format());
        if (!Double.isNaN(schemaProperty.minimum())) propertyNode.put("minimum", schemaProperty.minimum());
        if (!Double.isNaN(schemaProperty.maximum())) propertyNode.put("maximum", schemaProperty.maximum());

        Class<?> fieldType = guessFieldType(field);

        if (List.class.isAssignableFrom(fieldType)) {
            propertyNode.put("type", "array");
            Class<?> itemType = guessListGenericType(field);

            Map<String, String> items = new LinkedHashMap<>();
            items.put("$ref", "#/definitions/" + itemType.getSimpleName());
            propertyNode.put("items", items);
            processClass(itemType, null, definitions, processedClasses);
        } else if (Map.class.isAssignableFrom(fieldType)) {
            propertyNode.put("type", "object");
        } else if (Number.class.isAssignableFrom(fieldType)) {
            propertyNode.put("type", "number");
        } else if (Boolean.class.isAssignableFrom(fieldType) || fieldType == boolean.class) {
            propertyNode.put("type", "boolean");
        } else {
            propertyNode.put("type", "string");
        }

        return propertyNode;
    }

    private static Map<String, Object> createSampleObject(Class<?> clazz) throws Exception {
        Map<String, Object> objectMap = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            String propertyName = getPropertyName(field);
            if (field.isAnnotationPresent(SchemaProperty.class)) {
                SchemaProperty schemaProperty = field.getAnnotation(SchemaProperty.class);
                String defaultValue = schemaProperty.defaultValue();

                Class<?> fieldType = guessFieldType(field);

                if (!defaultValue.isEmpty()) {
                    objectMap.put(propertyName, defaultValue);
                } else {
                    if (List.class.isAssignableFrom(fieldType)) {
                        Class<?> itemType = guessListGenericType(field);

                        List<Object> sampleList = new ArrayList<>();
                        sampleList.add(createSampleObject(itemType));
                        objectMap.put(propertyName, sampleList);
                    } else if (Map.class.isAssignableFrom(fieldType) && !(schemaProperty.examples().length == 0)) {
                        try {
                            // @formatter:off
                            Map<String, Object> exampleMap = mapper.readValue(schemaProperty.examples()[0], new TypeReference<>() {});
                            // @formatter:on
                            objectMap.put(propertyName, exampleMap);
                        } catch (IOException e) {
                            logger.warn("Could not parse example JSON for field '{}': {}", field.getName(), schemaProperty.examples()[0], e);
                        }
                    }
                }
            }
        }
        return objectMap;
    }

    private static Class<?> guessFieldType(Field field) {
        Class<?> fieldType = field.getType();
        if (field.isAnnotationPresent(JsonDeserialize.class)) {
            JsonDeserialize jsonDeserialize = field.getAnnotation(JsonDeserialize.class);
            if (jsonDeserialize.as() != Void.class) {
                fieldType = jsonDeserialize.as();
            }
        }
        return fieldType;
    }

    private static Class<?> guessListGenericType(Field field) {
        ParameterizedType listType = (ParameterizedType) field.getGenericType();
        Class<?> itemType = (Class<?>) listType.getActualTypeArguments()[0];
        if (field.isAnnotationPresent(JsonDeserialize.class)) {
            JsonDeserialize jsonDeserialize = field.getAnnotation(JsonDeserialize.class);
            if (jsonDeserialize.contentAs() != Void.class) {
                itemType = jsonDeserialize.contentAs();
            }
        }
        return itemType;
    }

    private static String getPropertyName(Field field) {
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        if (jsonProperty != null && !jsonProperty.value().isEmpty()) {
            return jsonProperty.value();
        }
        // Convert camelCase to lower-dash-case
        return field.getName().replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
    }
}
