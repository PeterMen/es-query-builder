package org.peter.es.query.builder;


import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;

/**
 * query构造器基类
 * 
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public interface QueryBuilder {
    

    /**
     * query构造器
     * 
     * @param esQuery build后的query
     * @param myQuery 原始query
     * @param requestName 请求参数名称
     * @param requestValue 请求参数值
     * */
    void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String requestName, Object requestValue);

    String name();
}
