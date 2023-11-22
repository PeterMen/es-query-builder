package org.peter.es.query.queryparam;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.peter.es.builder.SearchRequestBuilder;
import org.peter.es.query.builder.QueryBuilderName;

import java.util.Map;

/**
 * 查询请求参数名定义
 * T的类型为JSONObject或JSONArray
 * @author 王海涛
 * */
@Data
public class MyQuery {

    /**
     * 查询参数
     * */
    private Map<String, Object> queryParam;
    /**
     * 查询标识
     * */
    private String index;

    private Map<String, String> nameMapping;
    private Map<String, QueryBuilderName> queryBuilderMapping;


    /**
     * key解析之后放入boolQueryBuilder的类型，默认是must,
     * must should
     * */
    public static final String MINIMUM_SHOULD_MATCH = "_MINIMUM_SHOULD_MATCH";
    /**
     * ES name
     * */
    public static final String ES_NAME = "_ES_NAME";

    /**
     * ES name
     * */
    public static final String NESTED = "_NESTED";

    /**
     * nested结构字段分隔符
     * */
    public static final char NESTED_SPLIT_CHAR = '.';

    /**
     * nested结构字段分隔符
     * */
    public static final char SPLIT_CHAR = '_';

    /**
     * 多字段值分隔符
     * */
    public static final char FIELD_SPLIT_CHAR = ',';

    /**
     * 多字段值分隔符
     * */
    public static final String FIELD_SPLIT_STR= ",";

    /**
     * json数组首字符
     * */
    public static final String JSON_ARRAY_PREFIX = "[";

    public static final String SNOW = "*";

    /**
     * 获取ES内部使用的字段名称
     * @param paramName 请求参数名称
     * @return ES使用的key名称
     * */
    public String getESName(String paramName){
        String esName = nameMapping.get(paramName + "_" + this.index + ES_NAME);
        if(StringUtils.isEmpty(esName)){
            esName = paramName;
        }
        return esName;
    }


    /**
     * 判断是否nested结构查询,默认是非nested
     * @param esName 参数映射后的ES字段名
     * @return 是否为nested结构查询
     * */
    public Boolean isNested(String esName){
        if(esName.indexOf(NESTED_SPLIT_CHAR) != -1 && StringUtils.isNotEmpty(nameMapping.get(esName+SPLIT_CHAR+this.index+NESTED))){
            return Boolean.valueOf(nameMapping.get(esName+SPLIT_CHAR+this.index+NESTED));
        }
        return false;
    }

    /**
     * 根据规则，获取nestedPath
     * */
    public String getNestedPath(String esName){
        return esName.substring(0, esName.lastIndexOf(NESTED_SPLIT_CHAR));
    }

    /**
     * 读取bool拼接关系， 默认是must,
     * must should
     * */
    public int getMininumShouldMatch(String paramName){
        String num = nameMapping.get(paramName + SPLIT_CHAR + this.index + MINIMUM_SHOULD_MATCH);
        if(StringUtils.isEmpty(num)){
            return 1;
        }
        return Integer.valueOf(num);
    }

    public static SearchRequestBuilder builder(){
        return new SearchRequestBuilder();
    }
}
