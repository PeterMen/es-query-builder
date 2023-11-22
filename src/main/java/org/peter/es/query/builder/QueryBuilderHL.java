package org.peter.es.query.builder;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;

/**
 * 高亮字段构造器
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderHL extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "HL";
    }

    /**
     * query构造器
     * 
     * @param esQuery esQuery query
     * @param requestName 请求参数名称
     * @param requestValue 请求参数值
     * 
     * */
    @Override
    public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String requestName, Object requestValue) {

        String requestValueStr = getStringValue(requestValue);

        // 获取默认的高亮字段
        String[] highLightFieldArrays = requestValueStr.split(FIELD_SPLIT_STR);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for(String s : highLightFieldArrays){
            highlightBuilder.field(s);
        }
        esQuery.setHighlightBuilder(highlightBuilder);
    }
}
