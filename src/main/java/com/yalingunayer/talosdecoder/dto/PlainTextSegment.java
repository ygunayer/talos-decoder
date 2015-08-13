package com.yalingunayer.talosdecoder.dto;

import org.apache.commons.codec.binary.StringUtils;

/**
 * Represents a basic text segment.
 * 
 * @author ygunayer
 *
 */
public class PlainTextSegment implements ITextSegment {
    private static final long serialVersionUID = 1L;

    protected String text;

    public PlainTextSegment(final String text) {
	this.text = text;
    }

    public String getText() {
	return text;
    }

    @Override
    public String toString() {
	return text;
    }

    @Override
    public PlainTextSegment merge(ITextSegment other) {
	if (getClass() != other.getClass())
	    throw new IllegalStateException("Incompatible segment types: " + getClass() + " <-> " + other.getClass());
	PlainTextSegment o = (PlainTextSegment) other;
	return new PlainTextSegment(text + o.text);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null)
	    return false;
	if (!this.getClass().equals(obj.getClass()))
	    return false;
	PlainTextSegment other = (PlainTextSegment) obj;
	return StringUtils.equals(text, other.text);
    }

}
