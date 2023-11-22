package org.peter.es.query.builder;


import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScriptScoreFunctionBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.rescore.QueryRescoreMode;
import org.elasticsearch.search.rescore.QueryRescorerBuilder;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;

import java.util.HashMap;

/**
 * 二次排序构造器-无参数
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderRescoreId extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "RESCORE_ID";
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

      String reScoreId = getStringValue(requestValue);

      Script script = new Script(ScriptType.STORED, null, reScoreId, new HashMap<>());
      esQuery.setRescoreBuilder(new QueryRescorerBuilder(
              QueryBuilders.functionScoreQuery(new ScriptScoreFunctionBuilder(script))
      ).windowSize(200).setScoreMode(QueryRescoreMode.Max));
  }
}
