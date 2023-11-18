package com.ws.cache;

import com.ws.annotation.Column;
import com.ws.table.ModelInfo;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author GSF
 */
@EqualsAndHashCode(callSuper = true)
@lombok.Data
public class ModelInfoCache extends ModelInfo {

    public ModelInfoCache(Class<?> clazz) {
        super(clazz);
        this.initFields(clazz);
    }

    @Override
    public void initFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Objects.nonNull(field.getAnnotation(Column.class))) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        this.setFields(fields);
    }

    public List<BaseColumnType> getCacheColumns() {
        if (Objects.isNull(this.getData())) {
            return null;
        }
        List<BaseColumnType> columns = new ArrayList<>(this.getFields().size());
        this.getFields().forEach(item -> {
            if (Objects.nonNull(item.getAnnotation(Column.class))) {
                columns.add(BaseColumnTypeFactory.getInstance().create(this.getData().columnType(), item));
            }
        });
        return columns;
    }

}
