package com.ws.table;

import com.ws.annotation.Column;
import com.ws.annotation.Data;
import com.ws.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@lombok.Data
public abstract class ModelInfo {

    private List<Field> fields;
    private List<String> names;
    private Class<?> metadata;
    private Data data;
    private String table;
    private String modelName;
    private String modelFullName;

    public ModelInfo() {

    }

    public ModelInfo(Class<?> clazz) {
        this.init(clazz);
    }

    public void init(Class<?> clazz) {
        this.data = clazz.getAnnotation(Data.class);
        this.table = initTableName(clazz);
        this.modelName = clazz.getSimpleName();
        this.modelFullName = clazz.getTypeName();
        this.initNames(clazz);
        this.initFields(clazz);
    }

    public void initFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        this.setFields(fields);
    }

    public void initNames(Class<?> clazz) {
        Data dataAnnotation = clazz.getAnnotation(Data.class);
        if (dataAnnotation != null) {
            this.setNames(List.of(dataAnnotation.names()));
        }
    }

    public String initTableName(Class<?> clazz) {
        Data dataAnnotation = clazz.getAnnotation(Data.class);
        String table = dataAnnotation.table().toLowerCase();
        if (StringUtil.isEmpty(table)) {
            table = clazz.getSimpleName().toLowerCase();
        }
        return table;
    }

}
