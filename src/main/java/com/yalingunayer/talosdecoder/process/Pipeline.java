package com.yalingunayer.talosdecoder.process;

import java.util.Collection;
import java.util.Vector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yalingunayer.talosdecoder.dto.InputTextEntry;
import com.yalingunayer.talosdecoder.dto.OutputTextEntry;
import com.yalingunayer.talosdecoder.input.ITextSource;
import com.yalingunayer.talosdecoder.output.ITextSink;

public class Pipeline {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ITextSource from;
    private ITextSink to;
    private Vector<IFilter> filters;

    private Pipeline(final ITextSource from) {
	this.from = from;
	this.filters = new Vector<IFilter>();
    }

    public static Pipeline from(ITextSource from) {
	return new Pipeline(from);
    }

    public Pipeline to(final ITextSink to) {
	this.to = to;
	return this;
    }

    public Pipeline filter(final IFilter... filters) {
	for (IFilter f : filters)
	    this.filters.add(f);
	return this;
    }

    private InputTextEntry applyFilters(InputTextEntry e) {
	String newContent = filters.stream().reduce(e.getContents(), (text, filter) -> filter.apply(text), (a, b) -> b);
	return new InputTextEntry(e.getKey(), e.getTitle(), newContent);
    }

    public Collection<OutputTextEntry> go() throws Exception {
	if (from == null)
	    throw new IllegalStateException("A source must be provided");

	if (to == null)
	    throw new IllegalStateException("A destination must be provided");

	// @formatter:off
	Collection<InputTextEntry> input = from.getEntries()
		.stream()
		.map(e -> applyFilters(e))
		.collect(Collectors.toList());
	// @formatter:on

	Collection<OutputTextEntry> output = to.process(input);
	logger.info("{} input entries were processed into {} output entries", input.size(), output.size());
	return output;
    }
}
