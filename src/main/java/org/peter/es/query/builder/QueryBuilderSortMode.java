package org.peter.es.query.builder;


import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;
import org.peter.es.query.queryparam.SortMode;

/**
 * sort构造器
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderSortMode extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "SORT_MODE";
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

      // 随机排序
      if(StringUtils.equals(getStringValue(requestValue), SortMode.RANDOM.name())){
          Script script = new Script("Math.random()");
          ScriptSortBuilder scriptSortBuilder = new ScriptSortBuilder(script, ScriptSortBuilder.ScriptSortType.NUMBER);
          esQuery.getSortBuilderList().add(scriptSortBuilder);
      }
  }
}
