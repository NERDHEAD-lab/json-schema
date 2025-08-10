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
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SchemaProperty {

    /**
     * A short description of the property.
     */
    String description() default "";

    /**
     * An example of a valid value for the property.
     */
    String example() default "";

    /**
     * The default value for the property.
     */
    String defaultValue() default "";

    /**
     * Specifies if the property is required.
     * Default is false.
     */
    boolean required() default false;

    /**
     * Specifies a regular expression pattern that the property's string value must match.
     */
    String pattern() default "";

    /**
     * Defines the format of the data (e.g., "date-time", "email", "uri").
     */
    String format() default "";

    /**
     * Specifies the minimum value for a numeric property.
     */
    double minimum() default Double.NaN;

    /**
     * Specifies the maximum value for a numeric property.
     */
    double maximum() default Double.NaN;
}

