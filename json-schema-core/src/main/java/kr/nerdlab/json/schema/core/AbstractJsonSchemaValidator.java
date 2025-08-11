package kr.nerdlab.json.schema.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.nerdlab.json.schema.core.exception.JsonValidationException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Set;

public abstract class AbstractJsonSchemaValidator implements JsonSchemaValidator {
    private static final Set<String> ALLOWED_SCHEMES = Set.of("https","http","file");

    private final ObjectMapper mapper;
    private final JsonSchemaGenerator generator;

    public AbstractJsonSchemaValidator(ObjectMapper mapper, JsonSchemaGenerator generator) {
        this.mapper = mapper;
        this.generator = generator;
    }

    abstract void validate(JsonNode dataNode, JsonNode schemaNode) throws JsonValidationException;

    @Override
    public void validate(File data) throws JsonValidationException, IOException {
        JsonNode dataNode = mapper.readTree(data);

        // Ensure root is an object
        final JsonNode schemaField = dataNode.get("$schema");
        if (schemaField == null || !schemaField.isTextual() || schemaField.asText().isBlank()) {
            throw new IllegalArgumentException(
                    "JSON instance does not contain a valid textual '$schema' property. " +
                            "Use validate(data, schemaFile/url) to supply a schema explicitly.");
        }

        // Extract $schema property
        if (!dataNode.has("$schema")) {
            throw new IllegalArgumentException("JSON data does not contain a '$schema' property.");
        }

        final String schemaText = dataNode.get("$schema").asText();
        final URI schemaUri;
        try {
            schemaUri = URI.create(schemaText).normalize();
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException("Invalid $schema URI: " + schemaText, iae);
        }

        if (!schemaUri.isAbsolute() || !ALLOWED_SCHEMES.contains(schemaUri.getScheme())) {
            throw new IllegalArgumentException("Unsupported $schema URI: " + schemaUri);
        }
        final JsonNode schemaNode = mapper.readTree(URL.of(schemaUri, null));

        validate(dataNode, schemaNode);
    }

    @Override
    public void validate(File data, Class<?> schemaClass) throws JsonValidationException, IOException {

    }

    @Override
    public void validate(File data, File schema) throws JsonValidationException, IOException {

    }

    @Override
    public void validate(File data, URI schema) throws JsonValidationException, IOException {

    }
}
