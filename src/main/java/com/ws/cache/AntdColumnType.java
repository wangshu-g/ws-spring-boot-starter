package com.ws.cache;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Field;

/**
 * @author GSF
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AntdColumnType extends BaseColumnType {

    private String dataIndex;
    private String key;

    public AntdColumnType() {
        super();
    }

    public AntdColumnType(Field field) {
        super(field);
        this.dataIndex = field.getName();
        this.key = field.getName();
    }

}
