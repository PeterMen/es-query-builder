package org.peter.es.query.builder;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;

/**
 * query构造器--范围结束
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderRangeEnd extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "RANGE_END";
    }

    /**
     * query构造器--范围结束
     * 
     * @param esQuery  query
     * @param paramName 请求参数名称
     * @param paramValue 请求参数值
     * */
    @Override
    public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String paramName, Object paramValue){

        BoolQueryBuilder boolqueryBuilder = esQuery.getBoolQueryBuilder();
        String paramValueStr = getStringValue(paramValue);

        // 取es映射名称
        String esName = myQuery.getESName(paramName);
        if(myQuery.isNested(esName)){
            boolqueryBuilder.filter(QueryBuilders.nestedQuery(getNestedPath(esName),
                    new RangeQueryBuilder(esName).lte(paramValueStr), ScoreMode.Total));
        } else {
            boolqueryBuilder.filter(new RangeQueryBuilder(esName).lte(paramValueStr));
        }
    }
}
