package com.ws.base.service;

import com.ws.base.model.BaseModel;

public abstract class AbstractOptionalService<M extends OptionalService<T>, T extends BaseModel> implements OptionalService<T> {

    public abstract M getMapper();

}
