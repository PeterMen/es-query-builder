package org.peter.es.query.queryparam;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 聚合查询参数
 *
 * @author 王海涛
 * */
@Getter
@Setter
@SuperBuilder
public class AggregationParam {

    /** 聚合字段 */
    private String field;
    /** 子聚合函数,多个使用逗号隔开 */
    private String functions;
    /** 子聚合函数字段 */
    private String functionField;
    /** 想要返回的字段 */
    private String source;
    /** 想要返回多少条 */
    private Integer fetchSize;
    /** 排序 */
    private String order;
    /** nested聚合结果的过滤条件 */
    private Map<String, Object> filterJsonStr;

    public enum FUNCTION_ENUM {
        /**子聚合函数*/min,
        /**子聚合函数*/max,
        /**子聚合函数*/avg,
        /**子聚合函数*/sum }

    public enum ORDER {
        /**降序*/desc,
        /**升序*/asc}

    public Integer getFetchSize() {
        if(this.fetchSize == null){
            // 默认返回200条
            return 200;
        }
        return fetchSize;
    }

    public void setFunction(FUNCTION_ENUM functions) {
        this.functions = functions.name();
    }
    public void setOrder(ORDER order) {
        this.order = order.name();
    }

    public void setFunction(String function) {
        this.functions = function;
    }
    public void setOrder(String order) {
        this.order = order;
    }

    public String getFunctionField() {
        return StringUtils.isEmpty(functionField) ? field : functionField;
    }
}
