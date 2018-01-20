package com.anser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.anser.enums.ScopeType;

/**
 * 作用域
 * 
 * @author lht
 * @time 2018年1月20日 上午11:37:41
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
	ScopeType value() default ScopeType.singleton;
}
