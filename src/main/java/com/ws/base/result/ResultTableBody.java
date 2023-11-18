package com.ws.base.result;

import com.ws.GlobalParam;
import com.ws.base.model.BaseModel;
import com.ws.cache.BaseColumnType;
import com.ws.enu.DefaultInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author GSF
 * <p>返回给表格用的数据格式</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResultTableBody<T> extends ResultBody<T> {

    List<BaseColumnType> columns;

    int total = 0;

    public ResultTableBody() {
    }

    public static <T> ResultTableBody<T> build(T data, int total, List<BaseColumnType> columns, String code, String message, boolean status) {
        ResultTableBody<T> resultTableBody = new ResultTableBody<>();
        resultTableBody.setData(data);
        resultTableBody.setCode(code);
        resultTableBody.setMessage(message);
        resultTableBody.setStatus(status);
        resultTableBody.setTotal(total);
        resultTableBody.setColumns(columns);
        return resultTableBody;
    }

    public static <T> ResultTableBody<T> successWithModelClazz(T data, int total, Class<? extends BaseModel> modelClazz) {
        return ResultTableBody.build(data, total, GlobalParam.modelColumnTypeCacheMap.get(modelClazz.getName()), DefaultInfo.SUCCESS.getResultCode(), DefaultInfo.SUCCESS.getResultMsg(), true);
    }

    public static <T> ResultTableBody<T> successWithControllerClazz(T data, int total, Class<?> controllerClazz) {
        return ResultTableBody.successWithModelClazz(data, total, GlobalParam.controllerModelGenericityCacheMap.get(controllerClazz.getName()));
    }

}
