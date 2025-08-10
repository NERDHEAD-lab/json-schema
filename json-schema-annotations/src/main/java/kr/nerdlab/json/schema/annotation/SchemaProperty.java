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

    // --- Common Metadata Keywords ---

    /**
     * A short description of the property.
     */
    String description() default "";

    /**
     * Provides multiple examples of a valid value for the property.
     * @since Draft 2019-09
     */
    String[] examples() default {};

    /**
     * The default value for the property.
     */
    String defaultValue() default "";

    /**
     * Specifies that the property is read-only.
     * @since Draft-07
     */
    boolean readOnly() default false;

    /**
     * Specifies that the property is write-only.
     * @since Draft-07
     */
    boolean writeOnly() default false;

    /**
     * Indicates that the property is deprecated.
     * @since Draft 2020-12
     */
    boolean deprecated() default false;

    /**
     * A comment for the schema author, not for the end user. Does not affect validation.
     * @since Draft-07
     */
    String comment() default "";


    // --- Validation Keywords ---

    /**
     * Specifies if the property is required.
     * Default is false.
     */
    boolean required() default false;

    /**
     * Defines the format of the data (e.g., "date-time", "email", "uri").
     * Note: Since Draft 2019-09, this is an annotation by default and not a validation assertion.
     */
    String format() default "";

    // --- String Validation ---

    /**
     * Specifies a regular expression pattern that the property's string value must match.
     */
    String pattern() default "";

    /**
     * Specifies the minimum length of a string.
     */
    long minLength() default -1;

    /**
     * Specifies the maximum length of a string.
     */
    long maxLength() default -1;

    /**
     * Describes the encoding of the string content.
     * @since Draft-07
     */
    String contentEncoding() default "";

    /**
     * Describes the media type of the string content.
     * @since Draft-07
     */
    String contentMediaType() default "";

    // --- Numeric Validation ---

    /**
     * Specifies the minimum value for a numeric property.
     */
    double minimum() default Double.NaN;

    /**
     * Specifies the maximum value for a numeric property.
     */
    double maximum() default Double.NaN;

    /**
     * Specifies that a numeric property must be a multiple of this value.
     */
    double multipleOf() default Double.NaN;

    /**
     * Specifies an exclusive minimum value for a numeric property.
     * @since Draft-06
     */
    double exclusiveMinimum() default Double.NaN;

    /**
     * Specifies an exclusive maximum value for a numeric property.
     * @since Draft-06
     */
    double exclusiveMaximum() default Double.NaN;

    // --- Array Validation ---

    /**
     * Specifies the minimum number of items in an array.
     */
    long minItems() default -1;

    /**
     * Specifies the maximum number of items in an array.
     */
    long maxItems() default -1;

    /**
     * Specifies if all items in the array must be unique.
     */
    boolean uniqueItems() default false;
}