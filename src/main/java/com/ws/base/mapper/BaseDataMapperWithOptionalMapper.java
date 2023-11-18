package com.ws.base.mapper;

import com.ws.base.model.BaseModel;

public interface BaseDataMapperWithOptionalMapper<T extends BaseModel> extends BaseDataMapper<T>, OptionalMapper<T> {


}
