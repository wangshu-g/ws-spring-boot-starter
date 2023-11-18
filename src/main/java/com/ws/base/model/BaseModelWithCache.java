package com.ws.base.model;

import com.ws.GlobalParam;

import java.lang.reflect.Field;
import java.util.List;

public class BaseModelWithCache extends BaseModel {

    @Override
    public List<Field> obtainFields() {
        return GlobalParam.modelInfoCacheMap.get(this.getClass().getName()).getFields();
    }

}
