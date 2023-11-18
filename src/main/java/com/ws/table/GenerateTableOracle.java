package com.ws.table;

import com.ws.base.model.BaseModel;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Field;
import java.sql.Connection;

/**
 * @author GSF
 */
@EqualsAndHashCode(callSuper = true)
@lombok.Data
public class GenerateTableOracle extends GenerateTable {

    public GenerateTableOracle() {
        super();
    }

    public GenerateTableOracle(Class<? extends BaseModel> clazz) {
        super(clazz);
    }

    @Override
    public void createTable(Connection connection) {
    }

    @Override
    public String getJdbcType(Field field) {
        return null;
    }

    @Override
    public int getDefaultLength(Field field) {
        return 0;
    }

}
