package com.ws.base.controller;

import com.ws.base.mapper.BaseDataMapper;
import com.ws.base.model.BaseModel;
import com.ws.base.result.ResultBody;
import com.ws.base.result.ResultTableBody;
import com.ws.base.service.AbstractBaseDataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author GSF
 * <p>BaseControllerImpl</p>
 */
public abstract class AbstractBaseDataControllerTableRestful<S extends AbstractBaseDataService<? extends BaseDataMapper<T>, T>, T extends BaseModel> extends AbstractBaseDataControllerRestful<S, T> {

    /**
     * <p>查询列表</p>
     *
     * @author wangshuhunyin
     **/
    @RequestMapping("/getList")
    @ResponseBody
    public ResultBody<List<Map<String, Object>>> getList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        Map<String, Object> params = this.getRequestParams(request);
        return ResultTableBody.successWithControllerClazz(this.getService().getList(params), this.getService().getTotal(params), this.getClass());
    }

    /**
     * <p>查询列表</p>
     *
     * @author wangshuhunyin
     **/
    @RequestMapping("/getNestList")
    @ResponseBody
    public ResultBody<List<T>> getNestList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        Map<String, Object> params = this.getRequestParams(request);
        return ResultTableBody.successWithControllerClazz(this.getService().getNestList(params), this.getService().getTotal(params), this.getClass());
    }

}
