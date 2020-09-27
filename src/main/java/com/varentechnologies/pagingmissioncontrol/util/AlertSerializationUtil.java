package com.varentechnologies.pagingmissioncontrol.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.varentechnologies.pagingmissioncontrol.entity.Alert;

import java.util.List;

public class AlertSerializationUtil {
    private AlertSerializationUtil() { }

    public static final String deserialize(List<Alert> alerts) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // the example had no quotes on field names, and had some non-default indentation
            mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);

            DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("    ", DefaultIndenter.SYS_LF);
            DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
            printer.indentObjectsWith(indenter);
            printer.indentArraysWith(indenter);

            return mapper.writer(printer).writeValueAsString(alerts);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
