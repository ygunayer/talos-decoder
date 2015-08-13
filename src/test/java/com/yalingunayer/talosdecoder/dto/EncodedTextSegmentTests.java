package com.yalingunayer.talosdecoder.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EncodedTextSegmentTests {

    @Test
    public void shouldEqualWithSameContents() {
	EncodedTextSegment s1 = new EncodedTextSegment("foo", "bar");
	EncodedTextSegment s2 = new EncodedTextSegment("foo", "bar");
	Assert.assertEquals("Segments containing the same text should be equal", s1, s2);
    }

    @Test
    public void shouldNotEqualWithSameContentsButOfDifferentTypes() {
	EncodedTextSegment s1 = new EncodedTextSegment("foo", "bar");
	ITextSegment s2 = new PlainTextSegment("foo");
	Assert.assertNotEquals("Segments containing the same text but are not of same types should not be equal", s1,
		s2);
    }

    @Test
    public void shouldCreateNewInstanceWithMerge() {
	EncodedTextSegment s1 = new EncodedTextSegment("foo", "bar");
	EncodedTextSegment s2 = new EncodedTextSegment("bar", "baz");
	EncodedTextSegment output = (EncodedTextSegment) s1.merge(s2);
	EncodedTextSegment expected = new EncodedTextSegment("foobar", "barbaz");
	Assert.assertFalse("A new instance must be created when merged", s1 == output);
	Assert.assertFalse("A new instance must be created when merged", s2 == output);
	Assert.assertEquals("Texts should be merged correctly", expected, output);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotMergeWithDifferentSegmentType() {
	EncodedTextSegment s1 = new EncodedTextSegment("foo", "bar");
	PlainTextSegment s2 = new PlainTextSegment("foo");
	s1.merge(s2);
    }
}
