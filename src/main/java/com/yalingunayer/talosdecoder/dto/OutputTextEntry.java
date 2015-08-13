package com.yalingunayer.talosdecoder.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.yalingunayer.talosdecoder.utils.CollectionUtils;

public class OutputTextEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private InputTextEntry input;
    private Collection<ITextSegment> segments;
    private String output;

    public OutputTextEntry() {
	as(Arrays.asList());
    }

    public OutputTextEntry(final InputTextEntry input) {
	this.input = input;
	as(Arrays.asList());
    }

    public OutputTextEntry(final InputTextEntry input, final Collection<ITextSegment> output) {
	this.input = input;
	as(output);
    }

    public static OutputTextEntry from(final InputTextEntry e) {
	return new OutputTextEntry(e);
    }

    public OutputTextEntry as(final Collection<ITextSegment> output) {
	this.segments = output;
	Collection<String> outputSegments = output.stream().map(segment -> {
	    // where's pattern matching when you need it
	    if (segment instanceof EncodedTextSegment)
		return ((EncodedTextSegment) segment).getDecodedText();
	    if (segment instanceof PlainTextSegment)
		return ((PlainTextSegment) segment).getText();
	    return "";
	}).collect(Collectors.toList());
	this.output = String.join(" ", outputSegments);
	return this;
    }

    public InputTextEntry getInput() {
	return input;
    }

    public Collection<ITextSegment> getSegments() {
	return segments;
    }

    public String getOutput() {
	return output;
    }

    @Override
    public String toString() {
	return "OutputTextEntry [input=" + input + ", output=" + output + "]";
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null)
	    return false;
	if (!this.getClass().equals(obj.getClass()))
	    return false;
	OutputTextEntry other = (OutputTextEntry) obj;
	return input.equals(other.input) && CollectionUtils.equals(segments, other.segments);
    }

}
