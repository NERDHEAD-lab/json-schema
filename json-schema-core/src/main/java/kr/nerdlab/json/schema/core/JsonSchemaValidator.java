package kr.nerdlab.json.schema.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class JsonSchemaValidator {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Validates a JSON file against a schema generated from a DTO class.
     *
     * @param jsonFile the JSON file to validate
     * @param dtoClass the DTO class to generate
     * @return a set of error messages if validation fails, or an empty set if valid
     * @throws IOException              if an error occurs while reading the JSON file or generating the schema
     * @throws IllegalArgumentException if the DTO class is not annotated with @SchemaDefinition
     */
    public static Set<String> validate(File jsonFile, Class<?> dtoClass) throws IOException {
        JsonNode jsonNode = mapper.readTree(jsonFile);
        JsonNode schemaNode = mapper.readTree(JsonSchemaGenerator.generateSchema(dtoClass));
        return validate(jsonNode, schemaNode);
    }

    /**
     * Validates a JSON file against a specified schema file.
     *
     * @param jsonFile   the JSON file to validate
     * @param schemaFile the JSON schema file to validate against
     * @return a set of error messages if validation fails, or an empty set if valid
     * @throws IOException              if an error occurs while reading the JSON file or schema file
     * @throws IllegalArgumentException if the schema file does not contain a valid JSON schema
     */
    public static Set<String> validate(File jsonFile, File schemaFile) throws IOException {
        JsonNode jsonNode = mapper.readTree(jsonFile);
        JsonNode schemaNode = mapper.readTree(schemaFile);
        return validate(jsonNode, schemaNode);
    }

    /**
     * Validates a JSON file using the '$schema' property inside it.
     *
     * @param jsonFile the JSON file to validate
     * @return a set of error messages if validation fails, or an empty set if valid
     * @throws IOException              if an error occurs while reading the JSON file or schema
     * @throws IllegalArgumentException if the JSON file does not contain a '$schema' property
     */
    public static Set<String> validate(File jsonFile) throws IOException {
        JsonNode jsonNode = mapper.readTree(jsonFile);
        if (jsonNode.get("$schema") == null) {
            throw new IllegalArgumentException("JSON file does not contain a '$schema' property.");
        }
        // Uri value
        String schemaPath = jsonNode.get("$schema").asText();
        // TODO: support remote schema validation
        if (schemaPath.startsWith("http://") || schemaPath.startsWith("https://")) {
            throw new UnsupportedOperationException("Remote schema validation is not supported. Please use a local schema file.");
        }
//        File schemaFile = new File(schemaPath.replace("file://", ""));
        URI schemaUri = URI.create(schemaPath);
        File schemaFile = new File(schemaUri);
        return validate(jsonNode, mapper.readTree(schemaFile));
    }

    private static Set<String> validate(JsonNode jsonNode, JsonNode schemaNode) {
        Set<String> errors = new HashSet<>();
        validateNode(jsonNode, schemaNode, "#", errors);
        return errors;
    }

    private static void validateNode(JsonNode node, JsonNode schema, String path, Set<String> errors) {
        if (schema.has("required")) {
            for (JsonNode requiredFieldNode : schema.get("required")) {
                String requiredField = requiredFieldNode.asText();
                if (!node.has(requiredField)) {
                    errors.add(String.format("'%s': required property '%s' is missing", path, requiredField));
                }
            }
        }

        node.fieldNames().forEachRemaining(key -> {
            JsonNode value = node.get(key);
            String currentPath = path.equals("#") ? key : path + "/" + key;

            if (schema.has("properties") && schema.get("properties").has(key)) {
                JsonNode propertySchema = schema.get("properties").get(key);

                String expectedType = propertySchema.has("type") ? propertySchema.get("type").asText() : "any";
                String actualType = getJsonNodeType(value);
                if (!"any".equals(expectedType) && !expectedType.equals(actualType)) {
                    errors.add(String.format("'%s': invalid type. Expected '%s' but found '%s'", currentPath, expectedType, actualType));
                    return;
                }

                if (propertySchema.has("pattern") && value.isTextual()) {
                    String pattern = propertySchema.get("pattern").asText();
                    if (!Pattern.matches(pattern, value.asText())) {
                        errors.add(String.format("'%s': string value '%s' does not match pattern '%s'", currentPath, value.asText(), pattern));
                    }
                }

                if (value.isObject()) {
                    validateNode(value, propertySchema, currentPath, errors);
                } else if (value.isArray() && propertySchema.has("items")) {
                    int i = 0;
                    for (JsonNode item : value) {
                        validateNode(item, propertySchema.get("items"), currentPath + "/" + i, errors);
                        i++;
                    }
                }
            }
        });
    }

    private static String getJsonNodeType(JsonNode node) {
        if (node.isObject()) return "object";
        if (node.isArray()) return "array";
        if (node.isTextual()) return "string";
        if (node.isNumber()) return "number";
        if (node.isBoolean()) return "boolean";
        if (node.isNull()) return "null";
        return "unknown";
    }
}
