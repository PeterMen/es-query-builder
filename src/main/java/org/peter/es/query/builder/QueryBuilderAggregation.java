package org.peter.es.query.builder;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.peter.es.query.QueryBuilderFactory;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.AggregationParam;
import org.peter.es.query.queryparam.QueryParamWrapper;

import java.util.Map;


/**
 * 聚合查询构造器
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public abstract class QueryBuilderAggregation extends BaseQueryBuilder implements QueryBuilder {

    protected static final String DESC = "desc";
    protected static final String ASC = "asc";
    public static final String FILTER = "filter_";
    public static final String TOP_HITS = "top_hits_";

    private enum FUNCTION_ENUM {
        /**子聚合函数*/MIN,
        /**子聚合函数*/MAX,
        /**子聚合函数*/AVG,
        /**子聚合函数*/SUM
    }


    /**
    * query构造器，参数示例：{
                               "field":"communityId",
                               "function":"min",
                               "functionField":"showPrice",
                               "source":"lat,lng,communityName"
                               }
    *
    * */
    @Override
    public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String paramName, Object paramValue) {

        AggregationParam aggParam = getAggParam(paramValue);
        String field = aggParam.getField();
        if(StringUtils.isNotEmpty(field)){
            String[] fields = field.split(FIELD_SPLIT_STR);
            for(String currentField : fields){
                esQuery.addAggregationBuilders(getAggregationBuilder(myQuery, aggParam, currentField));
            }
        }
    }

    private AbstractAggregationBuilder getAggregationBuilder(MyQuery myQuery, AggregationParam aggParam, String field) {
        // 是否nested结构字段
        boolean isNested = myQuery.isNested(field);

        // 设置聚合字段
        AbstractAggregationBuilder aggregationBuilder = getAbstractAggregationBuilder(myQuery, aggParam, field);

        // 处理子聚合函数
        if(StringUtils.isNotBlank(aggParam.getFunctions())){
            String[] functions = aggParam.getFunctions().split(FIELD_SPLIT_STR);
            for(String function : functions){
                aggregationBuilder.subAggregation(getFunctionAggregation(myQuery, aggParam.getFunctionField(), function));
            }
        }

        // 聚合数据的返回字段
        if(StringUtils.isNotEmpty(aggParam.getSource())){
            aggregationBuilder.subAggregation(getTopHitsAggregation(aggParam));
        }

        // 设置filter
        if(MapUtils.isNotEmpty(aggParam.getFilterJsonStr())){
            aggregationBuilder = AggregationBuilders.filter(FILTER + field,
                    getFilter(myQuery, aggParam.getFilterJsonStr(), isNested))
                    .subAggregation(aggregationBuilder);
        }

        // 如果是 nested 字段聚合
        if(isNested){
            String nestedPath = field.substring(0, field.lastIndexOf(NESTED_SPLIT_CHAR));
            aggregationBuilder = AggregationBuilders.nested(field, nestedPath).subAggregation(aggregationBuilder);
        }
        return aggregationBuilder;
    }


    /**
     * 处理子聚合函数，如：max、min、avg等
     * */
    private AbstractAggregationBuilder getFunctionAggregation(MyQuery myQuery, String functionField, String function) {
        // 设置子聚合函数
        if(FUNCTION_ENUM.MIN.name().toLowerCase().equals(function)){
            return AggregationBuilders.min(functionField+function).field(myQuery.getESName(functionField));
        } else if(FUNCTION_ENUM.MAX.name().toLowerCase().equals(function)){
            return AggregationBuilders.max(functionField+function).field(myQuery.getESName(functionField));
        } else if(FUNCTION_ENUM.SUM.name().toLowerCase().equals(function)){
            return AggregationBuilders.sum(functionField+function).field(myQuery.getESName(functionField));
        } else if(FUNCTION_ENUM.AVG.name().toLowerCase().equals(function)){
            return AggregationBuilders.avg(functionField+function).field(myQuery.getESName(functionField));
        }
        return null;
    }

    /**
     * 聚合数据的返回字段
     * */
    private AbstractAggregationBuilder getTopHitsAggregation(AggregationParam aggParam) {
        // 设置聚合数据的返回字段
        return AggregationBuilders.topHits(TOP_HITS + aggParam.getField())
                .fetchSource(aggParam.getSource().split(FIELD_SPLIT_STR), null).size(1);
    }

    /**
     * 构造 aggregation filter
     * */
    private BoolQueryBuilder getFilter(MyQuery myQuery, Map<String, Object> queryParamsJson, boolean isNested){
        QueryParamWrapper esQuery = new QueryParamWrapper();
        MyQuery newQuery = new MyQuery();
        newQuery.setQueryParam(queryParamsJson);
        newQuery.setNameMapping(myQuery.getNameMapping());
        newQuery.setQueryBuilderMapping(myQuery.getQueryBuilderMapping());
        newQuery.setIndex(myQuery.getIndex());
        // 调用查询条件构造工厂构造 aggregation filter
        QueryBuilderFactory.buildProvidedQuery(newQuery, esQuery);

        if(isNested){
            // 如果是nested结构字段，则聚合filter不能重复使用nested query builder,
            // 需要把nested query转成非nested
            return cancelNestedQuery(esQuery.getBoolQueryBuilder());
        } else {
            return esQuery.getBoolQueryBuilder();
        }

    }

    /**
     * 如果是nested结构字段，则聚合filter不能重复使用nested query builder,
     * 需要把nested query转成非nested
     * */
    private BoolQueryBuilder cancelNestedQuery(BoolQueryBuilder boolQueryBuilder) {
        BoolQueryBuilder newQB = new BoolQueryBuilder();
        boolQueryBuilder.filter().forEach(queryBuilder -> {
            if(queryBuilder instanceof NestedQueryBuilder){
                newQB.filter(((NestedQueryBuilder)queryBuilder).query());
            } else if(queryBuilder instanceof BoolQueryBuilder){
                newQB.filter(cancelNestedQuery(((BoolQueryBuilder)queryBuilder)));
            } else {
                newQB.filter(queryBuilder);
            }
        });
        boolQueryBuilder.should().forEach(queryBuilder -> {
            if(queryBuilder instanceof NestedQueryBuilder){
                newQB.should(((NestedQueryBuilder)queryBuilder).query());
            } else if(queryBuilder instanceof BoolQueryBuilder){
                newQB.should(cancelNestedQuery(((BoolQueryBuilder)queryBuilder)));
            } else {
                newQB.should(queryBuilder);
            }
        });
        return newQB;
    }

    protected abstract AggregationParam getAggParam(Object paramValue);

    protected abstract AbstractAggregationBuilder getAbstractAggregationBuilder(MyQuery myQuery, AggregationParam aggParam, String field);
}
