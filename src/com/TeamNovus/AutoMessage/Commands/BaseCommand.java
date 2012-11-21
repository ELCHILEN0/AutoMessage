package com.TeamNovus.AutoMessage.Commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BaseCommand {

	String[] aliases();
	
	String description();
	
	String usage();
	
	int min() default 0;
	
	int max() default -1;
	
	boolean hidden() default false;
	
}
