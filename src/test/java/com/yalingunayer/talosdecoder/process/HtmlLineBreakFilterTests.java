package com.yalingunayer.talosdecoder.process;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HtmlLineBreakFilterTests {

    private HtmlLineBreakFilter filter;

    @Before
    public void setUp() {
	if (filter == null)
	    filter = new HtmlLineBreakFilter();
    }

    @Test
    public void shouldFilterOutUnixStyleEndings() {
	String input = "foo\nbar";
	String expected = "foo<br />bar";
	String output = filter.apply(input);
	Assert.assertEquals("Unix-style line endings must be filtered out", expected, output);
    }

    @Test
    public void shouldFilterOutWindowsStyleEndings() {
	String input = "foo\r\nbar";
	String expected = "foo<br />bar";
	String output = filter.apply(input);
	Assert.assertEquals("Windows-style line endings must be filtered out", expected, output);
    }
}
