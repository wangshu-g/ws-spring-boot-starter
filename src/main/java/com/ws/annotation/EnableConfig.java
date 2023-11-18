package com.ws.annotation;

import com.ws.ConfigRegisterControl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author GSF
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ConfigRegisterControl.class)
public @interface EnableConfig {

    String[] modelPackage() default {""};

    String[] targetDataSource() default {"*"};

    boolean enableDataSourceExchange() default false;

    boolean enableExceptionHandle() default true;

}
