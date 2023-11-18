package com.ws.base.service;

import com.ws.GlobalParam;
import com.ws.base.mapper.BaseDataMapper;
import com.ws.base.model.BaseModel;
import com.ws.cache.ModelInfoCache;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public abstract class AbstractBaseDataServiceWithCache<M extends BaseDataMapper<T>, T extends BaseModel> extends AbstractBaseDataService<M, T> {

    @Override
    public Class<T> getModelClazz() {
        Class<? extends BaseModel> modelClazz = GlobalParam.serviceModelGenericityCacheMap.get(this.getClass().getName());
        if (Objects.isNull(modelClazz)) {
            log.warn("未获取到该类泛型实体类的缓存,返回默认获取方式: {}", this.getClass().getName());
            return super.getModelClazz();
        }
        return (Class<T>) modelClazz;
    }

    @Override
    public List<Field> getModelFields() {
        Class<T> modelClazz = this.getModelClazz();
        if (Objects.isNull(modelClazz)) {
            log.warn("未获取到该类泛型实体类的缓存,返回默认获取方式: {}", this.getClass().getName());
            return super.getModelFields();
        }
        ModelInfoCache modelInfoCache = GlobalParam.modelInfoCacheMap.get(modelClazz.getName());
        if (Objects.isNull(modelInfoCache)) {
            log.warn("未获取到该类泛型实体类的属性缓存,返回默认获取方式: {}", this.getClass().getName());
            return super.getModelFields();
        }
        return modelInfoCache.getFields();
    }

}
