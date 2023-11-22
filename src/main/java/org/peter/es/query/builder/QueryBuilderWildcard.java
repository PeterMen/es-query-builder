package org.peter.es.query.builder;


import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;

/**
 * 通配符构造器
 * 
 * @author 七星
 * @date 2017年03月02日
 * @version 1.0
 */
public class QueryBuilderWildcard extends BaseQueryBuilder implements QueryBuilder {
    
    public static final String name = "WILDCARD";

    @Override
    public String name() {
        return "WILDCARD";
    }

    /**
     * Q构造器
     * 
     * @param esQuery solr query
     * @param paramName 请求参数名称
     * @param paramValue 请求参数值,空，则采用默认值
     * 
     * */
    @Override
    public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String paramName, Object paramValue) {

        String paramValueStr = getStringValue(paramValue);
        // 设置搜索域
        String searchField = myQuery.getESName(paramName);
        if(searchField.indexOf(FIELD_SPLIT_CHAR) == -1){
            // 判断是否nested结构查询
            if(myQuery.isNested(searchField)){
                // nested 结构字段查询
                esQuery.getBoolQueryBuilder().filter(QueryBuilders.nestedQuery(getNestedPath(searchField),
                        getCaseInSensitiveQuery(searchField, paramValueStr), ScoreMode.Total));
            } else {
                // 非nested结构查询
                // 单字段搜索域
                esQuery.getBoolQueryBuilder().filter(getCaseInSensitiveQuery(searchField, paramValueStr));
            }

        } else {
            // 多字段搜索域
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            for(String s : searchField.split(FIELD_SPLIT_STR)){
                // 判断是否nested结构查询
                if(myQuery.isNested(s)){
                    boolQueryBuilder.should(QueryBuilders.nestedQuery(getNestedPath(s), getCaseInSensitiveQuery(s, paramValueStr), ScoreMode.Total));
                } else {
                    // 非nested结构字段查询
                    boolQueryBuilder.should(getCaseInSensitiveQuery(s, paramValueStr));
                }
            }
            esQuery.getBoolQueryBuilder().filter(boolQueryBuilder);
        }

    }
    /**
     * 不区分大小写
     * */
    private AbstractQueryBuilder getCaseInSensitiveQuery(String searchField, String searchValue){
        if(containsLetter(searchValue)){
            return QueryBuilders.boolQuery()
                    .should(new WildcardQueryBuilder(searchField, SNOW +searchValue.toLowerCase()+SNOW))
                    .should(new WildcardQueryBuilder(searchField, SNOW+searchValue.toUpperCase()+SNOW));
        } else {
            return new WildcardQueryBuilder(searchField, SNOW+searchValue+SNOW);
        }
    }

    /**
     * 是否包含纯字母
     * @param str 搜索关键字
     * @return
     */
    private boolean containsLetter(String str) {
        char[] chars = str.toCharArray();
        boolean isSmallLetter;
        boolean isBigLetter;
        for (int i = 0; i < chars.length; i++) {
            isBigLetter = (chars[i] >= 'A' && chars[i] <= 'Z');
            isSmallLetter = (chars[i] >= 'a' && chars[i] <= 'z');
            if (isSmallLetter || isBigLetter) {
                return true;
            }
        }
        return false;
    }
}
