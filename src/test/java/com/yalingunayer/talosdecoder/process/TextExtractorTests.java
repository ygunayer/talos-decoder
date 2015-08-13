package com.yalingunayer.talosdecoder.process;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.yalingunayer.talosdecoder.dto.InputTextEntry;
import com.yalingunayer.talosdecoder.utils.CollectionUtils;

@RunWith(JUnit4.class)
public class TextExtractorTests {

    private TextExtractor extractor;

    @Before
    public synchronized void setUp() {
	if (extractor == null)
	    extractor = new TextExtractor();
    }

    @Test
    public void shouldExtractFromFile() throws IOException {
	final ClassLoader cl = getClass().getClassLoader();
	InputStream in = null;
	try {
	    in = cl.getResource("sample.txt").openStream();

	    InputTextEntry e1 = new InputTextEntry("TermDlg.FoundTexts.Butler.Dummy1", "Foo Bar 1",
		    "Foo bar 66 6F 6F 20 62 61 72 bla bla burrito");
	    InputTextEntry e2 = new InputTextEntry("TermDlg.FoundTexts.Butler.Dummy2", "Foo Bar 2",
		    "\u307B\u3052\u307B\u3052 66 6F 6F 20 62 61 72 \u3074\u3088\u3074\u3088");

	    Map<String, Collection<InputTextEntry>> expected = CollectionUtils.toMap(Arrays.asList(e1, e2),
		    item -> item.getKey());

	    Map<String, Collection<InputTextEntry>> output = CollectionUtils.toMap(extractor.extract(in),
		    item -> item.getKey());

	    Assert.assertEquals("Number of extracted entries must match", expected.size(), output.size());

	    InputTextEntry ee1 = expected.get("TermDlg.FoundTexts.Butler.Dummy1").iterator().next();
	    InputTextEntry ee2 = expected.get("TermDlg.FoundTexts.Butler.Dummy2").iterator().next();

	    InputTextEntry oe1 = output.get("TermDlg.FoundTexts.Butler.Dummy1").iterator().next();
	    InputTextEntry oe2 = output.get("TermDlg.FoundTexts.Butler.Dummy2").iterator().next();

	    Assert.assertEquals("Items must match", ee1, oe1);
	    Assert.assertEquals("Items must match", ee2, oe2);
	} finally {
	    IOUtils.closeQuietly(in);
	}
    }

    @Test
    public void shouldExtractFromString() {
	String input = "TermDlg.FoundTexts.Butler.Dummy1.Name=Foo Bar 1\n"
		+ "TermDlg.FoundTexts.Butler.Dummy1.Text=Foo bar 66 6F 6F 20 62 61 72 bla bla burrito\n"
		+ "TermDlg.FoundTexts.Butler.Dummy2.Name=Foo Bar 2\n"
		+ "TermDlg.FoundTexts.Butler.Dummy2.Text=\u307B\u3052\u307B\u3052 66 6F 6F 20 62 61 72 \u3074\u3088\u3074\u3088\n";

	InputTextEntry e1 = new InputTextEntry("TermDlg.FoundTexts.Butler.Dummy1", "Foo Bar 1",
		"Foo bar 66 6F 6F 20 62 61 72 bla bla burrito");
	InputTextEntry e2 = new InputTextEntry("TermDlg.FoundTexts.Butler.Dummy2", "Foo Bar 2",
		"\u307B\u3052\u307B\u3052 66 6F 6F 20 62 61 72 \u3074\u3088\u3074\u3088");

	Map<String, Collection<InputTextEntry>> expected = CollectionUtils.toMap(Arrays.asList(e1, e2),
		item -> item.getKey());

	Map<String, Collection<InputTextEntry>> output = CollectionUtils.toMap(extractor.extract(input),
		item -> item.getKey());

	Assert.assertEquals("Number of extracted entries must match", expected.size(), output.size());

	InputTextEntry ee1 = expected.get("TermDlg.FoundTexts.Butler.Dummy1").iterator().next();
	InputTextEntry ee2 = expected.get("TermDlg.FoundTexts.Butler.Dummy2").iterator().next();

	InputTextEntry oe1 = output.get("TermDlg.FoundTexts.Butler.Dummy1").iterator().next();
	InputTextEntry oe2 = output.get("TermDlg.FoundTexts.Butler.Dummy2").iterator().next();

	Assert.assertEquals("Items must match", ee1, oe1);
	Assert.assertEquals("Items must match", ee2, oe2);
    }
}
