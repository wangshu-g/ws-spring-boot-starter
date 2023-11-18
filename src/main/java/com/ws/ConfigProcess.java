package com.ws;

import com.ws.annotation.Data;
import com.ws.annotation.EnableConfig;
import com.ws.base.controller.BaseController;
import com.ws.base.model.BaseModel;
import com.ws.base.service.BaseService;
import com.ws.cache.BaseColumnType;
import com.ws.cache.ModelInfoCache;
import com.ws.table.GenerateTable;
import com.ws.table.GenerateTableFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author GSF
 */
@Slf4j
public class ConfigProcess implements InitializingBean {

    private final ApplicationContext applicationContext;
    private final SqlSessionFactory sqlSessionFactory;
    private final EnableConfig enableConfig;

    public ConfigProcess(ApplicationContext applicationContext, SqlSessionFactory sqlSessionFactory) {
        this.applicationContext = applicationContext;
        this.sqlSessionFactory = sqlSessionFactory;
        this.enableConfig = GlobalParam.mainClazz.getAnnotation(EnableConfig.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.processCacheConfig();
        this.processTableConfig();
//        this.processOptionalMapper();
    }

    private void processTableConfig() throws SQLException, IOException, ClassNotFoundException {
        if (this.enableConfig.enableDataSourceExchange() && Objects.nonNull(this.enableConfig.modelPackage())) {
            List<String> targetDataSource = List.of(this.enableConfig.targetDataSource());
            log.info("需要建表和变动表的数据源: {}", targetDataSource);
            Map<String, DataSource> dataSourceMap = applicationContext.getBeansOfType(DataSource.class);
            if (!targetDataSource.contains("*")) {
                for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()) {
                    if (!targetDataSource.contains(entry.getKey())) {
                        dataSourceMap.remove(entry.getKey());
                    }
                }
            }
            Set<Class<? extends BaseModel>> currentPackageModelClazz = GlobalParam.getCurrentPackageModelClazz(this.enableConfig.modelPackage(), item -> Objects.nonNull(item.getAnnotation(Data.class)));
            for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()) {
                String k = entry.getKey();
                DataSource v = entry.getValue();
                log.info("数据源名字: {}", k);
                log.info("数据源信息: {}", v);
                Connection connection = null;
                try {
                    connection = v.getConnection();
                    for (Class<? extends BaseModel> modelClazz : currentPackageModelClazz) {
                        Data dataAnnotation = modelClazz.getAnnotation(Data.class);
                        GenerateTable generateTable = GenerateTableFactory.getInstance().create(dataAnnotation.dataBaseType(), modelClazz);
                        if (Objects.isNull(generateTable)) {
                            log.info("暂无对应数据库类型实现: {}", dataAnnotation.dataBaseType());
                        } else {
                            generateTable.createTable(connection);
                        }
                    }
                } catch (SQLException e) {
                    log.info("数据源: {}, 获取连接失败", k);
                } finally {
                    if (Objects.nonNull(connection)) {
                        connection.close();
                    }
                }
            }
        }
    }

    private void processCacheConfig() throws IOException, ClassNotFoundException {
        if (Objects.nonNull(this.enableConfig.modelPackage())) {
            Set<Class<? extends BaseModel>> modelClazz = GlobalParam.getCurrentPackageModelClazz(this.enableConfig.modelPackage(), item -> Objects.nonNull(item.getAnnotation(Data.class)));
            Map<String, ModelInfoCache> modelInfoCacheMap = new HashMap<>();
            Map<String, List<BaseColumnType>> modelColumnTypeCacheMap = new HashMap<>();
            Map<String, Class<? extends BaseModel>> serviceModelGenericityCacheMap = new HashMap<>();
            Map<String, Class<? extends BaseModel>> controllerModelGenericityCacheMap = new HashMap<>();
            for (Class<? extends BaseModel> clazz : modelClazz) {
                ModelInfoCache modelInfoCache = new ModelInfoCache(clazz);
                List<BaseColumnType> cacheColumns = modelInfoCache.getCacheColumns();
                modelInfoCacheMap.put(clazz.getName(), modelInfoCache);
                modelColumnTypeCacheMap.put(clazz.getName(), cacheColumns);
            }
            Map<String, BaseService> serviceMap = applicationContext.getBeansOfType(BaseService.class);
            Map<String, BaseController> controllerMap = applicationContext.getBeansOfType(BaseController.class);
            for (BaseService service : serviceMap.values()) {
                Class<? extends BaseModel> model = getServiceModel(service.getClass());
                if (Objects.nonNull(model)) {
                    serviceModelGenericityCacheMap.put(getProxyClazzFullName(service), model);
                }
            }
            for (BaseController controller : controllerMap.values()) {
                Class<? extends BaseModel> model = getControllerModel(controller.getClass());
                if (Objects.nonNull(model)) {
                    controllerModelGenericityCacheMap.put(getProxyClazzFullName(controller), model);
                }
            }
            GlobalParam.applicationContext = applicationContext;
            GlobalParam.modelClazz = modelClazz;
            GlobalParam.modelInfoCacheMap = modelInfoCacheMap;
            GlobalParam.modelColumnTypeCacheMap = modelColumnTypeCacheMap;
            GlobalParam.serviceModelGenericityCacheMap = serviceModelGenericityCacheMap;
            GlobalParam.controllerModelGenericityCacheMap = controllerModelGenericityCacheMap;
        }
    }

    public String getProxyClazzFullName(Object obj) {
        String name = obj.getClass().getName();
        if (name.contains("$$")) {
            return name.substring(0, name.indexOf("$$"));
        }
        return name;
    }

    public Class<? extends BaseModel> getServiceModel(Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        while (Objects.nonNull(type) && !(type instanceof ParameterizedType)) {
            type = ((Class<?>) type).getGenericSuperclass();
        }
        if (Objects.isNull(type)) {
            return null;
        }
        List<Type> actualTypeArguments = List.of(((ParameterizedType) type).getActualTypeArguments());
        if (actualTypeArguments.size() < 2) {
            return null;
        }
        return (Class<? extends BaseModel>) actualTypeArguments.get(1);
    }

    public Class<? extends BaseModel> getControllerModel(Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        while (Objects.nonNull(type) && !(type instanceof ParameterizedType)) {
            type = ((Class<?>) type).getGenericSuperclass();
        }
        if (Objects.isNull(type)) {
            return null;
        }
        List<Type> actualTypeArguments = List.of(((ParameterizedType) type).getActualTypeArguments());
        if (actualTypeArguments.size() < 2) {
            return null;
        }
        return (Class<? extends BaseModel>) actualTypeArguments.get(1);
    }

//    public Class<? extends BaseModel> getMapperModel(Class<?> clazz) {
//        Class<?> modelClazz = null;
//        while (Objects.isNull(modelClazz) && Objects.nonNull(clazz)) {
//            Optional<Type> first = Arrays.stream(clazz.getGenericInterfaces()).filter(item -> {
//                Type rawType = ((ParameterizedType) item).getRawType();
//                return rawType.equals(BaseDataMapper.class) || rawType.equals(OptionalMapper.class) || rawType.equals(BaseDataMapperWithOptionalMapper.class);
//            }).findFirst();
//            if (first.isPresent()) {
//                modelClazz = (Class<?>) List.of(((ParameterizedType) first.get()).getActualTypeArguments()).get(0);
//            } else {
//                Optional<Type> extendMapper = Arrays.stream(clazz.getGenericInterfaces()).filter(item -> {
//                    return BaseMapper.class.isAssignableFrom((Class<?>) item);
//                }).findFirst();
//                if (extendMapper.isPresent()) {
//                    clazz = (Class<?>) extendMapper.get();
//                }
//                clazz = null;
//            }
//        }
//        return (Class<? extends BaseModel>) modelClazz;
//    }
//
//    private void processOptionalMapper() {
//        Configuration configuration = sqlSessionFactory.getConfiguration();
//        Map<String, BaseMapper> mapperBeans = this.applicationContext.getBeansOfType(BaseMapper.class);
//        for (BaseMapper mapperBean : mapperBeans.values()) {
//            if (mapperBean instanceof OptionalMapper<?>) {
//                Class<?> mapperClazz = mapperBean.getClass().getInterfaces()[0];
//                Class<? extends BaseModel> mapperModel = getMapperModel(mapperClazz);
//                MappedStatement.Builder executeSqlMappedStatement = getExecuteSqlMappedStatement(mapperClazz, configuration);
//                MappedStatement.Builder sqlSaveMappedStatement = getSqlSaveMappedStatement(mapperClazz, configuration);
//                configuration.addMappedStatement(executeSqlMappedStatement.build());
//                configuration.addMappedStatement(sqlSaveMappedStatement.build());
//            }
//        }
//    }
//
//    public MappedStatement.Builder getExecuteSqlMappedStatement(Class<?> mapperClazz, Configuration configuration) {
//        String id = StringUtil.concat(mapperClazz.getName(), ".", OptionalMethod.executeSql.name());
//        SqlSource sqlSource = parameterObject -> new BoundSql(configuration, (String) parameterObject, List.of(), parameterObject);
//        return new MappedStatement.Builder(configuration, id, sqlSource, SqlCommandType.SELECT);
//    }
//
//    public MappedStatement.Builder getSqlSaveMappedStatement(Class<?> mapperClazz, Configuration configuration) {
//        String id = StringUtil.concat(mapperClazz.getName(), ".", OptionalMethod.sqlSave.name());
//        SqlSource sqlSource = new SqlSource() {
//            @Override
//            public BoundSql getBoundSql(Object parameterObject) {
//                return new BoundSql(configuration, (String) parameterObject, List.of(), parameterObject);
//            }
//        };
//        return new MappedStatement.Builder(configuration, id, sqlSource, SqlCommandType.INSERT);
//    }

}
