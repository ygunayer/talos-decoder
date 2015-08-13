package com.yalingunayer.talosdecoder.process;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class WhitespaceTrimFilterTests {

    private WhitespaceTrimFilter filter;

    @Before
    public void setUp() {
	if (filter == null)
	    filter = new WhitespaceTrimFilter();
    }

    @Test
    public void shouldTrimLineEndings() {
	String input = "\r\nfoo\nbar\n";
	String expected = "foo\nbar";
	String output = filter.apply(input);
	Assert.assertEquals("Line endings must be trimmed", expected, output);
    }

    @Test
    public void shouldTrimSpaces() {
	String input = "         foo\r\nbar   ";
	String expected = "foo\r\nbar";
	String output = filter.apply(input);
	Assert.assertEquals("Spaces must be trimmed", expected, output);
    }

    @Test
    public void shouldTrimTabSkips() {
	String input = "\t\tfoo\nbar\t";
	String expected = "foo\nbar";
	String output = filter.apply(input);
	Assert.assertEquals("Tab skips must be trimmed", expected, output);
    }
}
