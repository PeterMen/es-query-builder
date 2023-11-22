package org.peter.es.query.builder;


import org.apache.http.util.Asserts;
import org.peter.es.query.QueryBuilderFactory;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;
import java.util.Map;

/**
 * 复合FQ解析器--and
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderMultipleAND extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "MULTIPLE_AND";
    }

    /**
     * 复合FQ解析器：value 格式为json
     *
     * @param esQuery solr query
     * @param paramName 请求参数名称
     * @param paramValue 请求参数值
     *
     * */
    @Override
    public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String paramName, Object paramValue){

        Asserts.notNull(paramValue, "paramValue must not be null");
        Asserts.check(paramValue instanceof Map, "paramValue type must be Map.class");
        Map<String, Object> paramMap = (Map<String, Object>) paramValue;


        // 调用构造器工厂进行query build
        QueryParamWrapper subEsQuery = new QueryParamWrapper();
        MyQuery newQuery = new MyQuery();
        newQuery.setQueryParam(paramMap);
        newQuery.setNameMapping(myQuery.getNameMapping());
        newQuery.setQueryBuilderMapping(myQuery.getQueryBuilderMapping());
        newQuery.setIndex(myQuery.getIndex());
        QueryBuilderFactory.buildProvidedQuery(newQuery, subEsQuery);
        ignoreNullFilter(esQuery, subEsQuery);
    }

    private void ignoreNullFilter(QueryParamWrapper esQuery, QueryParamWrapper subEsQuery) {
        if(!subEsQuery.getBoolQueryBuilder().filter().isEmpty()){
            esQuery.getBoolQueryBuilder().filter(subEsQuery.getBoolQueryBuilder());
        }
    }
}
