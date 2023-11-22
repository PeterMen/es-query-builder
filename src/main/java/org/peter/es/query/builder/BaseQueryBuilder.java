package org.peter.es.query.builder;

import org.apache.http.util.Asserts;

import java.util.List;

/**
 * 解析器基类
 *
 * @author 王海涛
 * */
public abstract class BaseQueryBuilder {

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
     * 根据规则，获取nestedPath
     * */
    protected String getNestedPath(String esName){
        return esName.substring(0, esName.lastIndexOf(NESTED_SPLIT_CHAR));
    }

    protected String getStringValue(Object paramValue) {
        Asserts.notNull(paramValue, "paramValue must not be null!");
        return String.valueOf(paramValue);
    }

    protected Integer getIntegerValue(Object paramValue) {
        Asserts.notNull(paramValue, "paramValue must not be null!");
        Asserts.check(paramValue instanceof Integer, "paramValue type must be Integer.class");
        return (Integer) paramValue;
    }

    protected static List<String> getListStrings(Object requestValue) {
        Asserts.notNull(requestValue, "paramValue must not be null");
        Asserts.check(requestValue instanceof List, "paramValue type must be List.class");
        List<String> list = (List<String>) requestValue;
        return list;
    }
}
