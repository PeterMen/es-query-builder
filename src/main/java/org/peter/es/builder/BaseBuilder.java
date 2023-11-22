package org.peter.es.builder;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.peter.es.query.builder.QueryBuilderName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class BaseBuilder {

    protected static final String ES_NAME = "_ES_NAME";
    protected static final String QUERY_BUILDER = "_QUERY_BUILDER";

    protected Map<String, Object> queryParam = new HashMap<>();
    protected List<ImmutablePair<String, QueryBuilderName>> queryBuilder = new ArrayList<>();
    protected List<ImmutablePair<String, String>> nameMapping = new ArrayList<>();

    protected void setNameMapping(String name, String esName){
        nameMapping.add(ImmutablePair.of(name, esName));
    }

    protected void setQueryBuilder(String name, QueryBuilderName query){
        queryBuilder.add(ImmutablePair.of(name, query));
    }

    protected void addQueryBuilder(List<ImmutablePair<String, QueryBuilderName>> t){
        queryBuilder.addAll(t);
    }

    protected void addNameMapping(List<ImmutablePair<String, String>> t){
        nameMapping.addAll(t);
    }

    /**
     * 所有可用的查询解析器名称
     * */
    public enum QUERY{ FQ, PAGE_START, PAGE_SIZE, ROUTING,
        POLYGON, SORT, SORT_MODE, FL, HL,AGGREGATION, RANGE_AGGREGATION, GEO, MULTIPLE_OR,
        Q, MULTIPLE_AND, WILDCARD, RANGE_START, RANGE_END, ID, RESCORE_ID, RESCORE}



}
