package com.mrk.myordershop.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Transactional;

import com.mrk.myordershop.exception.EntityNotPersistedException;

@Target(value = { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Transactional(rollbackFor ={ EntityNotPersistedException.class, Exception.class})
public @interface PersistTransactional {
}
