package com.yalingunayer.talosdecoder.dto;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class OutputTextEntryTests {

    @Test
    public void matchingEntriesShouldEqual() {
	InputTextEntry e1 = new InputTextEntry("foo", "bar", "baz");
	InputTextEntry e2 = new InputTextEntry("foo", "bar", "baz");
	OutputTextEntry o1 = new OutputTextEntry(e1,
		Arrays.asList(new PlainTextSegment("abc"), new PlainTextSegment("def")));
	OutputTextEntry o2 = new OutputTextEntry(e2,
		Arrays.asList(new PlainTextSegment("abc"), new PlainTextSegment("def")));
	Assert.assertEquals("Matching entries should be equal", o1, o2);
    }

    @Test
    public void differentEntriesShouldNotEqual() {
	InputTextEntry e1 = new InputTextEntry("foo", "baz", "bar");
	InputTextEntry e2 = new InputTextEntry("foo", "bar", "baz");
	OutputTextEntry o1 = new OutputTextEntry(e1,
		Arrays.asList(new PlainTextSegment("def"), new PlainTextSegment("abc")));
	OutputTextEntry o2 = new OutputTextEntry(e2,
		Arrays.asList(new PlainTextSegment("abc"), new PlainTextSegment("def")));
	Assert.assertNotEquals("Different entries should not be equal", o1, o2);
    }
}
