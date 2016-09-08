package com.mrk.myordershop.notify.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Notify {
	NotifyType tier() default NotifyType.neworder;

	int position() default 0;
	int currentUserPosition() default -1;
}
