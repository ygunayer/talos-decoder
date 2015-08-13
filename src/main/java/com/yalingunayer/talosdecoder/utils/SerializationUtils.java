package com.yalingunayer.talosdecoder.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializationUtils {
    // TODO singleton? smh
    private static ObjectMapper mapper;

    public synchronized static ObjectMapper mapper() {
	if (mapper == null) {
	    mapper = new ObjectMapper();
	    mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
	}
	return mapper;
    }
}
