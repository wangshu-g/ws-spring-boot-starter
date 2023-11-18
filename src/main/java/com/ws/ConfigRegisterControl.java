package com.ws;

import com.ws.annotation.EnableConfig;
import com.ws.exception.IExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

public class ConfigRegisterControl implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Class<?> mainClazz = ((StandardAnnotationMetadata) importingClassMetadata).getIntrospectedClass();
        GlobalParam.mainClazz = mainClazz;
        EnableConfig annotation = mainClazz.getAnnotation(EnableConfig.class);
        if (annotation.enableExceptionHandle()) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
            builder.getBeanDefinition().setBeanClass(IExceptionHandler.class);
            registry.registerBeanDefinition("iExceptionHandler", builder.getBeanDefinition());
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
        builder.getBeanDefinition().setBeanClass(ConfigProcess.class);
        registry.registerBeanDefinition("configProcess", builder.getBeanDefinition());
    }

}
