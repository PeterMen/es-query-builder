package org.peter.es.query.builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.peter.es.query.queryparam.MyQuery;
import org.peter.es.query.queryparam.GeoParam;
import org.peter.es.query.queryparam.QueryParamWrapper;


/**
 * 地理位置搜索构造器
 *
 * @author 七星
 * @date 2018年02月02日
 * @version 1.0
 */
public class QueryBuilderGeo extends BaseQueryBuilder implements QueryBuilder {

    @Override
    public String name() {
        return "GEO";
    }

  /**
   * query构造器
   * 
   * @param esQuery solr query
   * @param paramName 请求参数名称
   * @param paramValue 请求参数值
   * */
  @Override
  public void buildQuery(MyQuery myQuery, QueryParamWrapper esQuery, String paramName, Object paramValue){

      Asserts.notNull(paramValue, "paramValue must not be null");
      Asserts.check(paramValue instanceof GeoParam, "paramValue type must be GeoParam.class");
      GeoParam paramValueGeo = (GeoParam) paramValue;

      // 获取参数对应的es名称
      String geoFieldName = myQuery.getESName(paramName);

      //经纬度，fLat:纬度，fLng:经度, distance:半径
      if(paramValueGeo.getDistanceMeters() != null){
          GeoDistanceQueryBuilder geoQuery = QueryBuilders.geoDistanceQuery(geoFieldName)
                  .point(paramValueGeo.getLat(), paramValueGeo.getLng())
                  .distance(paramValueGeo.getDistanceMeters(), DistanceUnit.METERS);
          esQuery.getBoolQueryBuilder().filter(geoQuery);
      }

      // 设置排序
      if(StringUtils.isNotBlank(paramValueGeo.getiGeoSort())) {
          GeoDistanceSortBuilder sortBuilder = SortBuilders.geoDistanceSort(geoFieldName,
                          paramValueGeo.getLat(), paramValueGeo.getLng())
                  .unit(DistanceUnit.METERS)
                  .order(StringUtils.equals(paramValueGeo.getiGeoSort().toUpperCase(), SortOrder.DESC.name()) ? SortOrder.DESC : SortOrder.ASC );
          esQuery.getSortBuilderList().add(sortBuilder);
      }
  }
}
