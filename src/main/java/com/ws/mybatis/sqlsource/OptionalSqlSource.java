package com.ws.mybatis.sqlsource;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import java.util.List;

public class OptionalSqlSource implements SqlSource {

    private final Configuration configuration;

    public OptionalSqlSource(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(this.configuration, (String) parameterObject, List.of(), parameterObject);
    }

}
