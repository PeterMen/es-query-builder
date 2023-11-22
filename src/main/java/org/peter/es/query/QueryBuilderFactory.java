package org.peter.es.query;

import org.apache.commons.collections4.MapUtils;
import org.apache.http.util.Asserts;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;
import org.peter.es.query.builder.*;

import java.util.*;

/**
 * 构造器工厂
 * 目前支持的query构造器 key:Q, FQ, FACET, GEO, MULTIPLE_FQ, SORT, HL, GROUP, FL, EDISMAX, POLYGON, PAGE_START, PAGE_SIZE, DF
 * 
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderFactory {

    public static final String QUERY_BUILDER = "_QUERY_BUILDER";
    
    /**
     * 查询偏好
     * */
    public static final String QUERY_SHARD_PREFERENCE_LOCAL = "_local";

    private static final Map<QueryBuilderName, QueryBuilder> queryBuilderMap = new HashMap<>();
    static {
        queryBuilderMap.put(QueryBuilderName.FQ, new QueryBuilderFQ());
        queryBuilderMap.put(QueryBuilderName.RANGE_AGGREGATION, new QueryBuilderAggregationRange());
        queryBuilderMap.put(QueryBuilderName.AGGREGATION, new QueryBuilderAggregationTerm());
        queryBuilderMap.put(QueryBuilderName.EXCLUDE, new QueryBuilderExclude());
        queryBuilderMap.put(QueryBuilderName.INCLUDE, new QueryBuilderFL());
        queryBuilderMap.put(QueryBuilderName.GEO, new QueryBuilderGeo());
        queryBuilderMap.put(QueryBuilderName.HL, new QueryBuilderHL());
        queryBuilderMap.put(QueryBuilderName.ID, new QueryBuilderIds());
        queryBuilderMap.put(QueryBuilderName.MULTIPLE_OR, new QueryBuilderMultipleOR());
        queryBuilderMap.put(QueryBuilderName.MULTIPLE_AND, new QueryBuilderMultipleAND());
        queryBuilderMap.put(QueryBuilderName.PAGE_SIZE, new QueryBuilderPageSize());
        queryBuilderMap.put(QueryBuilderName.PAGE_START, new QueryBuilderPageStart());
        queryBuilderMap.put(QueryBuilderName.POLYGON, new QueryBuilderPolygon());
        queryBuilderMap.put(QueryBuilderName.PREFIX, new QueryBuilderPrefix());
        queryBuilderMap.put(QueryBuilderName.Q, new QueryBuilderQ());
        queryBuilderMap.put(QueryBuilderName.RANGE_END, new QueryBuilderRangeEnd());
        queryBuilderMap.put(QueryBuilderName.RANGE_START, new QueryBuilderRangeStart());
        queryBuilderMap.put(QueryBuilderName.RESCORE, new QueryBuilderReScore());
        queryBuilderMap.put(QueryBuilderName.RESCORE_ID, new QueryBuilderRescoreId());
        queryBuilderMap.put(QueryBuilderName.ROUTING, new QueryBuilderRouting());
        queryBuilderMap.put(QueryBuilderName.SORT, new QueryBuilderSort());
        queryBuilderMap.put(QueryBuilderName.SORT_MODE, new QueryBuilderSortMode());
        queryBuilderMap.put(QueryBuilderName.WILDCARD, new QueryBuilderWildcard());
    }

    private static QueryBuilder getQueryBuilder(QueryBuilderName qbName){
        Asserts.check(queryBuilderMap.containsKey(qbName), "query builder config not found!");
        return queryBuilderMap.get(qbName);
    }

    /**
     * 封装ES Query
     * 
     * @param myQuery 业务标示
     * @return 查询对象构造器
     **/
    public static SearchRequest buildSearchRequest(MyQuery myQuery) {

        // 创建查询参数对象
        QueryParamWrapper esQuery = new QueryParamWrapper();

        // 循环遍历查询参数，解析query
        buildProvidedQuery(myQuery, esQuery);

        //create search request for specific indexAlias
        SearchRequest esSearchRequest = new SearchRequest();
        //设置查询偏好
        esSearchRequest.preference(QUERY_SHARD_PREFERENCE_LOCAL);
        esSearchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
        esSearchRequest.source(esQuery.getSearchSourceBuilder());
        esSearchRequest.routing(esQuery.getRouting());
        return esSearchRequest;
    }

    /**
     * 封装ES Query
     *
     * @param myQuery deleteByQuery
     * @return 查询对象构造器
     **/
    public DeleteByQueryRequest buildDeleteRequest(MyQuery myQuery) {

        // 创建查询参数对象
        QueryParamWrapper esQuery = new QueryParamWrapper();

        // 循环遍历查询参数，解析query
        buildProvidedQuery(myQuery, esQuery);

        //create search request for specific indexAlias
        DeleteByQueryRequest deleteRequest = new DeleteByQueryRequest();
        deleteRequest.setQuery(esQuery.getBoolQueryBuilder());
        return deleteRequest;
    }

    /**
     * 构造指定的query
     * boolQuery都用and关系连接
     * */
    public static void buildProvidedQuery(MyQuery myQuery, QueryParamWrapper esQuery) {
        if(myQuery.getQueryParam() == null) return;
        Iterator<Map.Entry<String, Object>> iterator  = myQuery.getQueryParam().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> item = iterator.next();

            String paramName = item.getKey();
            Object paramValue = item.getValue();

            // 获取ES对应的key名称和query构造器
            QueryBuilderName queryBuilderType = MapUtils.getObject(myQuery.getQueryBuilderMapping(),
                    paramName + "_" + myQuery.getIndex() + QUERY_BUILDER, QueryBuilderName.FQ);

            QueryBuilder baseQueryBuilder = getQueryBuilder(queryBuilderType);
            baseQueryBuilder.buildQuery(myQuery, esQuery, paramName, paramValue);
        }
    }
}
