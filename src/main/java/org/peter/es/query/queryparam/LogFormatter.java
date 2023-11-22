package org.peter.es.query.queryparam;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogFormatter {

    private String hostName;
    private String time;
    private String message;
    private String serviceTag;
}
