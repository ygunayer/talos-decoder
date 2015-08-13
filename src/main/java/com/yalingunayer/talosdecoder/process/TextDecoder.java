package com.yalingunayer.talosdecoder.process;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yalingunayer.talosdecoder.dto.EncodedTextSegment;
import com.yalingunayer.talosdecoder.dto.ITextSegment;
import com.yalingunayer.talosdecoder.dto.PlainTextSegment;

/**
 * Breaks down an input text into segments some of which might contain
 * hexadecimal texts decoded into a human readable format.
 * 
 * By default, uses {@link DEFAULT_PATTERN} for detecting hexadecimal texts and
 * {@link DEFAULT_CHARSET} as character set.
 * 
 * @author ygunayer
 *
 */
public class TextDecoder {
    /**
     * The default recognition pattern. Can handle the random white-spaces
     * scattered in-between characters.
     */
    public static final String DEFAULT_PATTERN = "([0-9A-F](\\s|\\(|[0-9A-F])(\\s|\\()?)+";

    /**
     * The default charset (UTF-8) for processed strings.
     */
    public static final String DEFAULT_CHARSET = "utf-8";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Pattern pattern;
    private Charset charset;

    /**
     * Initializes a decoder using the default charset and regex pattern.
     * 
     * @see {@link DEFAULT_CHARSET}
     * @see {@link DEFAULT_PATTERN}
     */
    public TextDecoder() {
	this(DEFAULT_PATTERN, DEFAULT_CHARSET);
    }

    /**
     * Initializes a decoder using the default charset and the specified regex
     * pattern.
     * 
     * @param pattern
     *            the regex pattern string
     * @see {@link DEFAULT_CHARSET}
     */
    public TextDecoder(String pattern) {
	this(pattern, DEFAULT_CHARSET);
    }

    /**
     * Initializes a decoder using the specified charset and regex pattern.
     * 
     * @param pattern
     * @param charset
     */
    public TextDecoder(String pattern, String charset) {
	this.pattern = Pattern.compile(pattern);
	this.charset = Charset.forName(charset);
    }

    /**
     * Decodes all occurrences of hexadecimal strings in a given string, and
     * extracts segments of plain and encoded text. Encoded text segments also
     * contain their decoded version.
     * 
     * @see {@link decode}
     * @see {@link PlainTextSegment}
     * @see {@link EncodedTextSegment}
     * 
     * @param input
     * @return
     * @throws DecoderException
     */
    public Collection<ITextSegment> process(final String input) {
	if (input == null)
	    return new Vector<ITextSegment>();
	Matcher matcher = pattern.matcher(input);
	StringBuffer sb = new StringBuffer();
	String encoded = null;
	String decoded = null;
	Vector<ITextSegment> segments = new Vector<ITextSegment>();
	while (matcher.find()) {
	    matcher.appendReplacement(sb, "");
	    segments.add(new PlainTextSegment(sb.toString().trim()));
	    sb.setLength(0);

	    try {
		encoded = matcher.group().trim();
		decoded = decode(encoded).trim();
		segments.add(new EncodedTextSegment(encoded, decoded));
	    } catch (DecoderException ex) {
		logger.error("Failed to decode text \"{}\", will act as if it was a clear text segment", encoded, ex);
		segments.add(new PlainTextSegment(encoded));
	    }
	}
	matcher.appendTail(sb);
	String tail = sb.toString();
	if (tail.length() > 0)
	    segments.add(new PlainTextSegment(tail));
	return merge(segments);
    }

    /**
     * Merges consecutive text segments of the same type, producing a cleaner
     * output.
     * 
     * @param segments
     * @return
     */
    public Collection<ITextSegment> merge(Collection<ITextSegment> segments) {
	if (segments.size() < 2)
	    return segments;

	Vector<ITextSegment> buffer = new Vector<ITextSegment>();
	Iterator<ITextSegment> it = segments.iterator();
	ITextSegment curr = it.next();
	ITextSegment next = null;
	while (it.hasNext()) {
	    next = it.next();
	    if (curr.getClass() == next.getClass())
		curr = curr.merge(next);
	    else {
		buffer.add(curr);
		curr = next;
	    }
	}
	buffer.add(curr);
	return buffer;
    }

    /**
     * Decodes a given hexadecimal string using Apache's {@link Hex}
     * 
     * @param input
     *            the hexadecimal input string
     * @return the decoded string
     * @throws DecoderException
     */
    public String decode(final String input) throws DecoderException {
	String clean = input.replaceAll("\\s|\\(", "");
	return new String(Hex.decodeHex(clean.toCharArray()), charset);
    }
}
