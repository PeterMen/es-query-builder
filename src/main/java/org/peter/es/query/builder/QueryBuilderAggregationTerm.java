package org.peter.es.query.builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.InternalOrder;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.AggregationParam;


/**
 * 聚合查询构造器
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderAggregationTerm extends QueryBuilderAggregation {

    public String name(){
        return "AGGREGATION";
    }

    public AggregationParam getAggParam(Object paramValue){
        Asserts.check(paramValue instanceof AggregationParam, "paramValue type must be AggregationParam.class");
        return (AggregationParam) paramValue;
    }

    public AbstractAggregationBuilder getAbstractAggregationBuilder(MyQuery myQuery, AggregationParam aggParam, String field){
        // 设置排序
        BucketOrder order = InternalOrder.CompoundOrder.count(false);
        if(StringUtils.equals(aggParam.getOrder(), DESC)){
            order = InternalOrder.CompoundOrder.count(false);
        } else if(StringUtils.equals(aggParam.getOrder(), ASC)){
            order = InternalOrder.CompoundOrder.count(true);
        }

        // 设置聚合字段
        return AggregationBuilders.terms(field)
                .size(aggParam.getFetchSize())
                .field(myQuery.getESName(field))
                .order(order);
    }
}
