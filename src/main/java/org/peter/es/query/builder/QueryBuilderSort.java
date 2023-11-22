package org.peter.es.query.builder;


import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;

/**
 * sort构造器
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderSort extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "SORT";
    }

  /**
   * sort构造器
   * 
   * @param esQuery solr query
   * @param requestName 请求参数名称
   * @param requestValue 请求参数值,空，则采用默认值
   * 
   * */
  @Override
  public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String requestName, Object requestValue) {

      String[] sortStrArray = getStringValue(requestValue).split(FIELD_SPLIT_STR);
      for (String str : sortStrArray) {

          // 获取参数对应的ES使用的字段名称
          String[] strArray = str.trim().split(" ");
          String esSortFieldName = myQuery.getESName(strArray[0]);
          
          if (StringUtils.equals(SortOrder.DESC.name(), strArray[1].trim().toUpperCase())) {
              esQuery.getSortBuilderList().add(new FieldSortBuilder(esSortFieldName).order(SortOrder.DESC));
          } else {
              esQuery.getSortBuilderList().add(new FieldSortBuilder(esSortFieldName).order(SortOrder.ASC));
          }
      }
  }
}
