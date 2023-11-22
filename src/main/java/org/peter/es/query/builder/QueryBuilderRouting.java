package org.peter.es.query.builder;


import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;

/**
 * 设置查询路由
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderRouting extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "ROUTING";
    }

    /**
     * 设置查询路由
     * 
     * @param esQuery es query
     * @param requestName 请求参数名称
     * @param requestValue 请求参数值,空，则采用默认值
     * 
     * */
    @Override
    public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String requestName, Object requestValue){

        esQuery.setRouting(getStringValue(requestValue));
    }
}
