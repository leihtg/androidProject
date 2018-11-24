/**
 * 
 */
package com.anser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.anser.enums.ActionType;

/**
 * @author leihuating
 * @time 2018年1月18日 下午4:24:37
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessType {
	ActionType value();
}
