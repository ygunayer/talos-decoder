package com.yalingunayer.talosdecoder.process;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.yalingunayer.talosdecoder.dto.EncodedTextSegment;
import com.yalingunayer.talosdecoder.dto.ITextSegment;
import com.yalingunayer.talosdecoder.dto.PlainTextSegment;

@RunWith(JUnit4.class)
public class TextDecoderTests {

    private TextDecoder decoder;

    @Before
    public synchronized void setUp() {
	if (decoder == null)
	    decoder = new TextDecoder();
    }

    @Test
    public void shouldDecodeWellFormedString() throws Exception {
	String input = "66 6F 6F 20 62 61 72";
	String expected = "foo bar";
	String output = decoder.decode(input);
	Assert.assertEquals("Correctly decode well-formed hex string", expected, output);
    }

    @Test
    public void shouldDecodeMalformedString() throws Exception {
	String input = " 666F 6 F20 626   172   ";
	String expected = "foo bar";
	String output = decoder.decode(input);
	Assert.assertEquals("Correctly decode malformed hex string", expected, output);
    }

    @Test
    public void shouldDecodeWellFormedInput() throws Exception {
	String input = "Foo bar 66 6F 6F 20 62 61 72 bla bla burrito";
	// expected segments are:
	// Foo bar, 66 6F 6F 20 62 61 72 (decoded as foo bar), bla bla burrito
	List<ITextSegment> expected = Arrays.asList(new PlainTextSegment("Foo bar"),
		new EncodedTextSegment("66 6F 6F 20 62 61 72", "foo bar"), new PlainTextSegment("bla bla burrito"));

	Collection<ITextSegment> output = decoder.process(input);
	Assert.assertEquals("Number of text segments must match", expected.size(), output.size());

	Iterator<ITextSegment> ie = expected.iterator();
	Iterator<ITextSegment> io = output.iterator();
	while (ie.hasNext())
	    Assert.assertEquals("All segments must match", ie.next(), io.next());
    }

    @Test
    public void shouldDecodeUnicodeString() throws Exception {
	String input = "\u307B\u3052\u307B\u3052 66 6F 6F 20 62 61 72 \u3074\u3088\u3074\u3088";
	List<ITextSegment> expected = Arrays.asList(new PlainTextSegment("\u307B\u3052\u307B\u3052"),
		new EncodedTextSegment("66 6F 6F 20 62 61 72", "foo bar"),
		new PlainTextSegment("\u3074\u3088\u3074\u3088"));

	Collection<ITextSegment> output = decoder.process(input);
	Assert.assertEquals("Number of text segments must match", expected.size(), output.size());

	Iterator<ITextSegment> ie = expected.iterator();
	Iterator<ITextSegment> io = output.iterator();
	while (ie.hasNext())
	    Assert.assertEquals("All segments must match", ie.next(), io.next());
    }

    @Test
    public void shouldDecodeActualStringFromGame() throws Exception {
	String input = "Thus the angels departed, having delivered their message, and I awoke in the fields of our fair land, 746F2 0666F726D 207468652067 6F6C 64656 E206 1726D6F75(72206F6 620736369 656E6365";
	List<ITextSegment> expected = Arrays.asList(
		new PlainTextSegment(
			"Thus the angels departed, having delivered their message, and I awoke in the fields of our fair land,"),
		new EncodedTextSegment(
			"746F2 0666F726D 207468652067 6F6C 64656 E206 1726D6F75(72206F6 620736369 656E6365",
			"to form the golden armour of science"));

	Collection<ITextSegment> output = decoder.process(input);
	Assert.assertEquals("Number of text segments must match", expected.size(), output.size());

	Iterator<ITextSegment> ie = expected.iterator();
	Iterator<ITextSegment> io = output.iterator();
	while (ie.hasNext())
	    Assert.assertEquals("All segments must match", ie.next(), io.next());
    }

    @Test
    public void shouldMergeSegmentsOfSameType() {
	Collection<ITextSegment> input = Arrays.asList(new PlainTextSegment("foo"), new PlainTextSegment("bar"),
		new EncodedTextSegment("foo bar", "baz bar"), new EncodedTextSegment("baz bar", "foo bar"));
	Collection<ITextSegment> expected = Arrays.asList(new PlainTextSegment("foobar"),
		new EncodedTextSegment("foo barbaz bar", "baz barfoo bar"));
	Collection<ITextSegment> merged = decoder.merge(input);
	Assert.assertEquals("Segments of same types must be merged correctly", expected, merged);
    }

    @Test
    public void shouldNotMergeSegmentsOfDifferentTypes() {
	Collection<ITextSegment> input = Arrays.asList(new PlainTextSegment("foo"),
		new EncodedTextSegment("baz", "bur"), new PlainTextSegment("bar"));
	Collection<ITextSegment> expected = Arrays.asList(new PlainTextSegment("foo"),
		new EncodedTextSegment("baz", "bur"), new PlainTextSegment("bar"));
	Collection<ITextSegment> merged = decoder.merge(input);
	Assert.assertEquals("Segments of same types must be merged correctly", expected, merged);
    }

}
