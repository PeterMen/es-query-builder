package org.peter.es.query.builder;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.IpRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.AggregationParam;
import org.peter.es.query.queryparam.RangeAggregation;


/**
 * 聚合查询构造器
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderAggregationRange extends QueryBuilderAggregation {

    @Override
    public String name(){
        return "RANGE_AGGREGATION";
    }

    public RangeAggregation getAggParam(Object paramValue){
        Asserts.check(paramValue instanceof RangeAggregation, "paramValue type must be RangeAggregation.class");
        return (RangeAggregation) paramValue;
    }

    public AbstractAggregationBuilder getAbstractAggregationBuilder(MyQuery myQuery, AggregationParam aggParam, String field) {

        RangeAggregation agg = (RangeAggregation) aggParam;
        Asserts.check(CollectionUtils.isNotEmpty(agg.getRanges()), "ranges不能为空");
        Asserts.notNull(agg.getRangeType(), "rangeType不支持");

        if(agg.getRangeType() == RangeAggregation.RangeType.general){
            // 通用聚合查询
            RangeAggregationBuilder aggregationBuilder = AggregationBuilders.range(field)
                    .field(myQuery.getESName(field));
            agg.getRanges().stream()
                    .filter(r-> StringUtils.isNotBlank(r.getFrom()) && StringUtils.isNotBlank(r.getTo()))
                    .forEach(range -> {
                if(StringUtils.isNotEmpty(range.getKey())){
                    aggregationBuilder.addRange(range.getKey(), Double.parseDouble(range.getFrom()),
                            Double.parseDouble(range.getTo()));
                } else {
                    aggregationBuilder.addRange(Double.parseDouble(range.getFrom()), Double.parseDouble(range.getTo()));
                }
            });
            return aggregationBuilder;
        } else if(agg.getRangeType() == RangeAggregation.RangeType.date_range){
            DateRangeAggregationBuilder aggregationBuilder = AggregationBuilders.dateRange(field)
                    .field(myQuery.getESName(field))
                    .format(agg.getFormat());
            agg.getRanges().forEach(range -> {
                if(StringUtils.isNotEmpty(range.getKey())){
                    aggregationBuilder.addRange(range.getKey(), range.getFrom(), range.getTo());
                } else {
                    aggregationBuilder.addRange(range.getFrom(), range.getTo());
                }
            });
            return aggregationBuilder;
        } else if(agg.getRangeType() == RangeAggregation.RangeType.ip_range){
            IpRangeAggregationBuilder aggregationBuilder = AggregationBuilders.ipRange(field)
                    .field(myQuery.getESName(field));
            agg.getRanges().forEach(range -> {
                if(StringUtils.isNotEmpty(range.getKey())){
                    aggregationBuilder.addRange(range.getKey(), range.getFrom(), range.getTo());
                } else {
                    aggregationBuilder.addRange(range.getFrom(), range.getTo());
                }
            });
            return aggregationBuilder;
        }

        return null;
    }
}
