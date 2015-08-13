package com.yalingunayer.talosdecoder.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PlainTextSegmentTests {

    @Test
    public void shouldEqualWithSameContents() {
	ITextSegment s1 = new PlainTextSegment("foo");
	ITextSegment s2 = new PlainTextSegment("foo");
	Assert.assertEquals("Segments containing the same text should be equal", s1, s2);
    }

    @Test
    public void shouldNotEqualWithSameContentsButOfDifferentTypes() {
	ITextSegment s1 = new PlainTextSegment("foo");
	EncodedTextSegment s2 = new EncodedTextSegment("foo", "bar");
	Assert.assertNotEquals("Segments containing the same text but are not of same types should not be equal", s1,
		s2);
    }

    @Test
    public void shouldCreateNewInstanceWithMerge() {
	ITextSegment s1 = new PlainTextSegment("foo");
	PlainTextSegment s2 = new PlainTextSegment("bar");
	ITextSegment output = s1.merge(s2);
	ITextSegment expected = new PlainTextSegment("foobar");
	Assert.assertFalse("A new instance must be created when merged", s1 == output);
	Assert.assertFalse("A new instance must be created when merged", s2 == output);
	Assert.assertEquals("Texts should be merged correctly", expected, output);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotMergeWithDifferentSegmentType() {
	ITextSegment s1 = new PlainTextSegment("foo");
	EncodedTextSegment s2 = new EncodedTextSegment("foo", "bar");
	s1.merge(s2);
    }
}
