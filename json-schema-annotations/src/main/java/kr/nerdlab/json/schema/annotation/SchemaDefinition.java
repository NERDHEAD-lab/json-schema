package kr.nerdlab.json.schema.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <br>
 * Annotation to define properties of a schema for a Java class.
 * This annotation can be used to provide metadata about the fields of a class
 * that will be used in JSON Schema generation.
 *
 * @see SchemaProperty
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SchemaDefinition {

    /**
     * The specification version of the JSON Schema.
     * This is typically set to <a href="http://json-schema.org/draft-07/schema#">http://json-schema.org/draft-07/schema#</a>.
     */
    SchemaVersion version() default SchemaVersion.DRAFT_07;

    /**
     * A title for the schema.
     */
    String title();

    /**
     * A description of the schema.
     */
    String description() default "";

    /**
     * Indicates if the schema allows additional properties that are not explicitly defined.
     */
    boolean additionalProperties() default false;
}
