package org.peter.es.query.builder;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;

/**
 * query构造器--前缀匹配
 *
 * @author 七星
 * @date 2018年11月06日
 * @version 1.0
 */
public class QueryBuilderPrefix extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "PREFIX";
    }

    /**
     * query构造器--前缀匹配
     * 
     * @param esQuery  query
     * */
    @Override
    public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String requestName, Object requestValue){

        String paramValueStr = getStringValue(requestValue);
        BoolQueryBuilder boolqueryBuilder = esQuery.getBoolQueryBuilder();

        // 取es映射名称
        String esName = myQuery.getESName(requestName);
        if(myQuery.isNested(esName)){
            boolqueryBuilder.filter(QueryBuilders.nestedQuery(getNestedPath(esName),
                    new PrefixQueryBuilder(esName, paramValueStr), ScoreMode.Total));
        } else {
            boolqueryBuilder.filter(new PrefixQueryBuilder(esName, paramValueStr));
        }
    }
}
