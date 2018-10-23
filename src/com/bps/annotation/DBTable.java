package com.bps.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(TYPE)
@Retention(RUNTIME)
public @interface DBTable {
	String tableName() default "";

	String primaryKeyName() default "id";

	String defaultSort() default "id";

	String defaultDir() default "desc";

	//String flagName() default "flag";// 删除标志名称
}
