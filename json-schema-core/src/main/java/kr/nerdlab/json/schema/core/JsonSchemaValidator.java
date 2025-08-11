package kr.nerdlab.json.schema.core;

import kr.nerdlab.json.schema.core.exception.JsonValidationException;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public interface JsonSchemaValidator {
    void validate(File data) throws JsonValidationException, IOException;
    void validate(Object data) throws JsonValidationException, IOException;
    void validate(File data, Class<?> schemaClass) throws JsonValidationException, IOException;
    void validate(File data, File schema) throws JsonValidationException, IOException;
    void validate(File data, URI schema) throws JsonValidationException, IOException;
}
