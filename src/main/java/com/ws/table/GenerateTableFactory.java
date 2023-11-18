package com.ws.table;

import com.ws.enu.DataBaseType;
import com.ws.excepion.MessageException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author GSF
 */
public class GenerateTableFactory {

    private GenerateTableFactory() {
    }

    private static final GenerateTableFactory CLASS_FACTORY = new GenerateTableFactory();

    private static final Map<DataBaseType, Class<? extends GenerateTable>> DATA_BASE_TYPE_MAP_GENERATE_TABLE_CLASS = new HashMap<>();

    static {
        DATA_BASE_TYPE_MAP_GENERATE_TABLE_CLASS.put(DataBaseType.mysql, GenerateTableMysql.class);
        DATA_BASE_TYPE_MAP_GENERATE_TABLE_CLASS.put(DataBaseType.sqlServer, GenerateTableSqlServer.class);
        DATA_BASE_TYPE_MAP_GENERATE_TABLE_CLASS.put(DataBaseType.oracle, GenerateTableOracle.class);
    }

    public GenerateTable create(DataBaseType dataBaseType, Class<?> clazz) {
        Class<? extends GenerateTable> baseGenerateTable = DATA_BASE_TYPE_MAP_GENERATE_TABLE_CLASS.get(dataBaseType);
        GenerateTable generateTable = null;
        try {
            generateTable = baseGenerateTable.getDeclaredConstructor(Class.class).newInstance(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generateTable;
    }

    public GenerateTable create(DataBaseType dataBaseType, Class<?> clazz, Consumer<MessageException> message) {
        Class<? extends GenerateTable> baseGenerateTable = DATA_BASE_TYPE_MAP_GENERATE_TABLE_CLASS.get(dataBaseType);
        GenerateTable generateTable = null;
        try {
            generateTable = baseGenerateTable.getDeclaredConstructor(Class.class, Consumer.class).newInstance(clazz, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generateTable;
    }

    public static GenerateTableFactory getInstance() {
        return CLASS_FACTORY;
    }

}
