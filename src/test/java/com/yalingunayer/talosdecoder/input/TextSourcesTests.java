package com.yalingunayer.talosdecoder.input;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TextSourcesTests {
    @Test
    public void file_shouldReadFromClasspathCorrectly() {
	final ClassLoader cl = getClass().getClassLoader();
	File expected = new File(cl.getResource("sample.txt").getFile());
	File result = TextSources.file("classpath:sample.txt").getFile();
	Assert.assertEquals("Files must match", expected, result);
    }
}
