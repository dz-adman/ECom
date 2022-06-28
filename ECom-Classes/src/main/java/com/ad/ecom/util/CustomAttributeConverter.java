package com.ad.ecom.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CustomAttributeConverter implements AttributeConverter<List<Long>, String> {

    private final Logger LOGGER = LogManager.getLogger(CustomAttributeConverter.class);

    @Override
    public String convertToDatabaseColumn(List<Long> attrs) {
        return attrs == null ? null : String.join(",", attrs.toArray(new CharSequence[attrs.size()]));
    }

    @Override
    public List<Long> convertToEntityAttribute(String listVal) {
        try {
            return listVal == null || listVal.isEmpty() || listVal.isBlank() ? Collections.EMPTY_LIST : Arrays.stream(listVal.split(",")).map(Long::valueOf).collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.info("Exception while converting String to List");
            ex.printStackTrace();
        } finally {
            return Collections.EMPTY_LIST;
        }
    }
}
