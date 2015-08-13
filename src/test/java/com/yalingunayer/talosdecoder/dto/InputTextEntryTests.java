package com.yalingunayer.talosdecoder.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class InputTextEntryTests {

    @Test
    public void matchingEntriesShouldEqual() {
	InputTextEntry e1 = new InputTextEntry("foo", "bar", "baz");
	InputTextEntry e2 = new InputTextEntry("foo", "bar", "baz");
	Assert.assertEquals("Matching entries should be equal", e1, e2);
    }

    @Test
    public void differentEntriesShouldNotEqual() {
	InputTextEntry e1 = new InputTextEntry("foo", "baz", "bar");
	InputTextEntry e2 = new InputTextEntry("foo", "bar", "baz");
	Assert.assertNotEquals("Different entries should not be equal", e1, e2);
    }
}
