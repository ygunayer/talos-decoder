package com.yalingunayer.talosdecoder.dto;

import org.apache.commons.codec.binary.StringUtils;

/**
 * Represents an encoded text segment. Also contains the decoded version of the
 * text.
 * 
 * @author ygunayer
 *
 */
public class EncodedTextSegment implements ITextSegment {
    private static final long serialVersionUID = 1L;

    private String encodedText;
    private String decodedText;

    public EncodedTextSegment(final String encodedText, final String decodedText) {
	this.encodedText = encodedText;
	this.decodedText = decodedText;
    }

    public String getDecodedText() {
	return decodedText;
    }

    @Override
    public ITextSegment merge(ITextSegment other) {
	if (getClass() != other.getClass())
	    throw new IllegalStateException("Incompatible segment types: " + getClass() + " <-> " + other.getClass());
	EncodedTextSegment o = (EncodedTextSegment) other;
	return new EncodedTextSegment(encodedText + o.encodedText, decodedText + o.decodedText);
    }

    @Override
    public String toString() {
	return super.toString() + " (decoded as: " + decodedText + ")";
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null)
	    return false;
	if (!this.getClass().equals(obj.getClass()))
	    return false;
	EncodedTextSegment other = (EncodedTextSegment) obj;
	return StringUtils.equals(encodedText, other.encodedText) && StringUtils.equals(decodedText, other.decodedText);
    }
}
