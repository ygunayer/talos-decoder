package com.yalingunayer.talosdecoder.output;

import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yalingunayer.talosdecoder.dto.ITextSegment;
import com.yalingunayer.talosdecoder.dto.InputTextEntry;
import com.yalingunayer.talosdecoder.dto.OutputTextEntry;
import com.yalingunayer.talosdecoder.process.TextDecoder;

public class BasicTextSink implements ITextSink {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Collection<OutputTextEntry> process(Collection<InputTextEntry> entries) {
	return entries.stream().map(input -> {
	    Collection<ITextSegment> output;
	    try {
		logger.info("Processing {}", input.getKey());
		output = new TextDecoder().process(input.getContents());
		return new OutputTextEntry(input, output);
	    } catch (Exception e) {
		logger.error("Failed to process {}", input, e);
		return null;
	    }
	}).filter(s -> s != null).collect(Collectors.toList());
    }

}
