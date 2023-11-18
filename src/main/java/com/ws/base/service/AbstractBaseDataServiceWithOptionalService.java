package com.ws.base.service;

import com.ws.base.mapper.BaseDataMapperWithOptionalMapper;
import com.ws.base.model.BaseModel;

public abstract class AbstractBaseDataServiceWithOptionalService<M extends BaseDataMapperWithOptionalMapper<T>, T extends BaseModel> extends AbstractBaseDataService<M, T> implements OptionalService<T> {

}
