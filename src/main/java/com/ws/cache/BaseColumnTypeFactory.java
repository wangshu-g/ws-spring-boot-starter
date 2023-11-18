package com.ws.cache;

import com.ws.enu.ColumnType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GSF
 */
@Slf4j
public class BaseColumnTypeFactory {

    private BaseColumnTypeFactory() {
    }

    private static BaseColumnTypeFactory factory = new BaseColumnTypeFactory();

    private static Map<ColumnType, Class<? extends BaseColumnType>> map = new HashMap<>();

    static {
        map.put(ColumnType.antd, AntdColumnType.class);
    }

    public BaseColumnType create(ColumnType columnType, Field field) {
        Class<? extends BaseColumnType> baseColumnClass = map.get(columnType);
        BaseColumnType baseColumnType = null;
        try {
            baseColumnType = baseColumnClass.getDeclaredConstructor(Field.class).newInstance(field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseColumnType;
    }

    public static BaseColumnTypeFactory getInstance() {
        return factory;
    }

}
