package org.peter.es.query.builder;

import org.elasticsearch.index.query.IdsQueryBuilder;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;

import java.util.List;

/**
 * @author 王海涛
 * @Description QueryBuilderIds
 * @date Created on 2018/10/16
 */
public class QueryBuilderIds extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "ID";
    }

    @Override
    public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String requestName, Object requestValue) {

        List<String> list = getListStrings(requestValue);

        if(list == null || list.size() == 0){
            return;
        }
        Integer size = list.size();
        String[] ids = list.toArray(new String[size]);
        esQuery.setSize(size);
        IdsQueryBuilder idsQueryBuilder = new IdsQueryBuilder();
        idsQueryBuilder.addIds(ids);
        esQuery.setIdsQueryBuilder(idsQueryBuilder);
    }
}
