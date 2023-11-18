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
public class GenerateTableSqlServer extends GenerateTable {

    public GenerateTableSqlServer() {
        super();
    }

    public GenerateTableSqlServer(Class<? extends BaseModel> clazz) {
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
