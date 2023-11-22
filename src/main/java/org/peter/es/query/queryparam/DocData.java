package org.peter.es.query.queryparam;

import lombok.Data;

@Data
public class DocData {

    public static final String JSON = "json";
    private String docId;

    private String routing;

    private String docData;

    private String docDataType = JSON;
}
