package com.ws.cache;

import com.ws.annotation.Column;
import com.ws.utils.StringUtil;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author GSF
 */
@Data
public class BaseColumnType {

    public BaseColumnType() {

    }

    public BaseColumnType(Field field) {
        this.name = field.getName();
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            this.title = column.title();
            if (StringUtil.isEmpty(this.title)) {
                this.title = column.comment();
            }
        }
        if (StringUtil.isEmpty(this.title)) {
            this.title = this.name;
        }
        this.dataType = field.getType().getSimpleName();
    }

    private String name;
    private String title;
    private Integer order;
    private String dataType;

}
