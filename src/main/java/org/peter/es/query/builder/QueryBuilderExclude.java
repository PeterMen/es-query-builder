package org.peter.es.query.builder;


import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;

/**
 * 返回字段构造器 
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderExclude extends BaseQueryBuilder implements QueryBuilder {

    public String name(){
        return "EXCLUDE";
    }

    /**
     * query构造器
     * 
     * @param esQuery solr query
     * @param paramName 请求参数名称
     * @param paramValue 请求参数值
     * 
     * */
    @Override
    public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String paramName, Object paramValue){

        String paramValueStr = getStringValue(paramValue);

        // 传入指定返回字段
        String[] excludeFieldArray = paramValueStr.replaceAll(" ", "").split(FIELD_SPLIT_STR);

        StringBuilder fieldStr = new StringBuilder();

        // 转换为ES内部使用的字段名称
        for(String excludeField : excludeFieldArray){

            String esName = myQuery.getESName(excludeField);
            fieldStr.append(esName).append(FIELD_SPLIT_STR);
        }

        fieldStr.substring(fieldStr.lastIndexOf(FIELD_SPLIT_STR), fieldStr.length());
        esQuery.setExcludes(  fieldStr.toString().split(FIELD_SPLIT_STR));
    }
}
