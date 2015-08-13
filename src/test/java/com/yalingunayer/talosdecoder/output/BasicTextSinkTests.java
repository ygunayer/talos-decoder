package com.yalingunayer.talosdecoder.output;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.yalingunayer.talosdecoder.dto.EncodedTextSegment;
import com.yalingunayer.talosdecoder.dto.InputTextEntry;
import com.yalingunayer.talosdecoder.dto.OutputTextEntry;
import com.yalingunayer.talosdecoder.dto.PlainTextSegment;
import com.yalingunayer.talosdecoder.utils.CollectionUtils;

@RunWith(JUnit4.class)
public class BasicTextSinkTests {

    private BasicTextSink sink;

    @Before
    public synchronized void setUp() {
	if (sink == null)
	    sink = new BasicTextSink();
    }

    @Test
    public void shouldProcessCorrectly() {
	InputTextEntry input = new InputTextEntry("foo", "bar", "Foo bar 66 6F 6F 20 62 61 72 bla bla burrito");
	OutputTextEntry output = new OutputTextEntry(input, Arrays.asList(new PlainTextSegment("Foo bar"),
		new EncodedTextSegment("66 6F 6F 20 62 61 72", "foo bar"), new PlainTextSegment("bla bla burrito")));

	Collection<InputTextEntry> in = Arrays.asList(input);
	Collection<OutputTextEntry> out = Arrays.asList(output);
	Collection<OutputTextEntry> actual = sink.process(in);

	Map<String, Collection<OutputTextEntry>> expected = CollectionUtils.toMap(out, e -> e.getInput().getKey());
	Map<String, Collection<OutputTextEntry>> result = CollectionUtils.toMap(actual, e -> e.getInput().getKey());

	Assert.assertEquals("Number of processed entries must match", expected.size(), result.size());

	OutputTextEntry ee = expected.get("foo").iterator().next();
	OutputTextEntry oe = result.get("foo").iterator().next();

	Assert.assertEquals("Items must match", ee, oe);
    }
}
