package org.peter.es.query.builder;


import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;

/**
 * page构造器 
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderPageSize extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "PAGE_SIZE";
    }

    /**
     * 设置默认返回条数
     * 
     * @param esQuery es query
     * @param requestName 请求参数名称
     * @param requestValue 请求参数值,空，则采用默认值
     * 
     * */
    @Override
    public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String requestName, Object requestValue){

        int iSize = getIntegerValue(requestValue);
        iSize = Math.min(iSize, 2000);
        esQuery.setSize(iSize);
    }
}
