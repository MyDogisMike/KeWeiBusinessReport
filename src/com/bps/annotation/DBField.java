package com.bps.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.annotation.meta.TypeQualifier;
/**
 * @author andalee
 */
@Retention(RUNTIME)
@Target(FIELD)
@TypeQualifier(applicableTo = String.class)
public @interface DBField {

	String field() default "";

	Class type() default Object.class;

	String id = "id";

	String Integer = "int";
}
