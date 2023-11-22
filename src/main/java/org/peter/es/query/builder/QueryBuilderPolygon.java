package org.peter.es.query.builder;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.query.GeoPolygonQueryBuilder;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.QueryParamWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 图形搜索构造器 
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderPolygon extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "POLYGON";
    }

    private static final String POINT_DELIMITER = "\\|";

    /**
     * 图形搜索构造器 
     * eg:query.addFilterQuery("{!field f=xf_baidu_location}Intersects(POLYGON((-10 30, -40 40, -10 -20, 40 20, 0 0, -10 30)))");
     *
     * @param esQuery es query
     * @param requestName 请求参数名称
     * @param requestValue 请求参数值
     *
     * */
    @Override
    public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String requestName, Object requestValue){

        // 获取ES对应的字段名
        String esName = myQuery.getESName(requestName);

        List<String> pointArray = getListStrings(requestValue);
        List<GeoPoint> points = new ArrayList<>();
        for(String pointStr : pointArray){
            String[] point = pointStr.trim().split(POINT_DELIMITER);
            points.add(new GeoPoint(Double.valueOf(point[0]), Double.valueOf(point[1])));
        }
        GeoPolygonQueryBuilder geoPolygonFilterBuilder = new GeoPolygonQueryBuilder(esName, points);
        esQuery.getBoolQueryBuilder().filter(geoPolygonFilterBuilder);
    }
}
