package org.peter.es.query.builder;


import org.apache.http.util.Asserts;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScriptScoreFunctionBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.rescore.QueryRescoreMode;
import org.elasticsearch.search.rescore.QueryRescorerBuilder;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;
import org.peter.es.query.queryparam.ReScore;

/**
 * 二次排序构造器-包含参数
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderReScore extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "RESCORE";
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

      Asserts.notNull(requestValue, "requestValue must not be null!");
      ReScore reScore = (ReScore) requestValue;
      Asserts.notEmpty(reScore.getReScoreId(), "reScoreId不能为空");

      Script script = new Script(ScriptType.STORED, null, reScore.getReScoreId(), reScore.getParams());
      esQuery.setRescoreBuilder(new QueryRescorerBuilder(
              QueryBuilders.functionScoreQuery(new ScriptScoreFunctionBuilder(script))
      ).windowSize(reScore.getWindowSize()).setScoreMode(QueryRescoreMode.Max));
  }
}
