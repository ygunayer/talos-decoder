package com.yalingunayer.talosdecoder.output;

import java.util.Collection;

import com.yalingunayer.talosdecoder.dto.InputTextEntry;
import com.yalingunayer.talosdecoder.dto.OutputTextEntry;

public abstract class PostProcessingTextSink<T> extends BasicTextSink {

    protected abstract T postProcess(OutputTextEntry entry) throws Exception;

    @Override
    public Collection<OutputTextEntry> process(Collection<InputTextEntry> entries) {
	Collection<OutputTextEntry> results = super.process(entries);
	results.forEach(e -> {
	    try {
		postProcess(e);
	    } catch (Exception ex) {
		throw new RuntimeException("An error has occurred while post-processing entry " + e, ex);
	    }
	});
	return results;
    }
}
