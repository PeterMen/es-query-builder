package org.peter.es;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.peter.es.builder.FQ_RULE;
import org.peter.es.builder.SearchRequestBuilder;
import org.peter.es.dto.SearchResultDTO;
import org.peter.es.query.QueryBuilderFactory;
import org.peter.es.query.queryparam.AggregationParam;
import org.peter.es.query.queryparam.GeoParam;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.RangeAggregation;
import org.peter.es.response.EsResponseParser;

import java.io.IOException;
import java.util.Arrays;

public class BuilderExample {

    public static void main(String[] args) throws IOException {

        // 构造最终查询对象
        MyQuery myQuery = MyQuery.builder()
                .index("item") // 指定index
                .sort("salePrice desc")  // 指定排序条件
                .addOR(SearchRequestBuilder.ORBuilder.builder()
                        .fq("attrCode", "38883")
                        .fq("attrCode", "33,32,22")
                        .addAnd(SearchRequestBuilder.AndBuilder.builder()
                                .fq("code", "123").fq("size", 123))
                        .addAnd(SearchRequestBuilder.AndBuilder.builder()
                                .fq("code", "456").fq("size", 456)))  // 添加or逻辑
                .addAnd(SearchRequestBuilder.AndBuilder.builder()
                        .fq("code", "789").fq("size", 789)) // 添加and逻辑
                .fq("roomId", "5242", FQ_RULE.WILDCARD) // fq查询，指定fq规则为：wildcard
                .fq("listTime", "525652", FQ_RULE.GT) // fq查询，指定fq规则为：gt
                .reScoreId("4568225") // 指定二次排序模型的id
                .fq("size2", 4,  FQ_RULE.AND ) // fq查询，指定fq规则为：gt
                .iRowSize(20) // 指定返回条数
                .iStart(10)  // 指定起始行
                .include("title,subTitle")  // 指定返回字段
                .sKw("title^0.7,subTitle^0.3", "短袖") // 指定关键字查询
                .addAggregation(AggregationParam.builder().field("salePrice").build()) // 添加聚合查询
                .addAggregation(AggregationParam.builder().field("attrCode")
                        .functions(AggregationParam.FUNCTION_ENUM.avg.name()).build())
                .addRangeAgg(RangeAggregation.builder().field("salePrice2")
                        .rangeType(RangeAggregation.RangeType.general)
                        .ranges(Arrays.asList(new RangeAggregation.Range())).build())
                .sortMode("sdfsd")
                .routing("sdfsdf")
                .exclude("dddddddddd")
                .addGeo(new GeoParam())
                .build();

        System.out.println(myQuery.getQueryParam());
        System.out.println(myQuery.getNameMapping());
        SearchRequest searchRequest = QueryBuilderFactory.buildSearchRequest(myQuery);
        System.out.println(searchRequest.source());

        // 查询es
        RestHighLevelClient client = new RestHighLevelClient(null);
        SearchResponse s = client.search(searchRequest, RequestOptions.DEFAULT);
        // 解析返回结果
        SearchResultDTO sr = EsResponseParser.parseSearchResponse(s);
    }
}
