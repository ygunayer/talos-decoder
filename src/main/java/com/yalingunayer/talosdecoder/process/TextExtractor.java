package com.yalingunayer.talosdecoder.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import com.yalingunayer.talosdecoder.dto.InputTextEntry;

/**
 * Extracts important texts from a given input stream, and decodes them where
 * necessary. The stream must contain a Java properties file string, just like
 * the game assets do.
 * 
 * By default, uses the prefix {@link DEFAULT_PREFIX} to filter out entries, and
 * {@link DEFAULT_CHARSET} as the character set.
 * 
 * @author ygunayer
 *
 */
public class TextExtractor {
    public static final String DEFAULT_PREFIX = "TermDlg.FoundTexts";
    public static final String DEFAULT_CHARSET = "utf-8";

    private String prefix;
    private Charset charset;

    /**
     * Initializes an extractor using the default prefix and character set.
     * 
     * @see {@link DEFAULT_PREFIX}
     * @see {@link DEFAULT_CHARSET}
     */
    public TextExtractor() {
	this(DEFAULT_PREFIX, DEFAULT_CHARSET);
    }

    /**
     * Initializes an extractor with the given prefix and character set.
     * 
     * @param prefix
     * @param charset
     */
    public TextExtractor(final String prefix, final String charset) {
	this.prefix = prefix;
	this.charset = Charset.forName(charset);
    }

    /**
     * Extracts text entries from a string. This is expected to be in a Java
     * properties file format.
     * 
     * @param input
     *            the input string
     * @return
     */
    public Collection<InputTextEntry> extract(final String input) {
	try {
	    return extract(IOUtils.toInputStream(input, charset));
	} catch (IOException e) {
	    throw new RuntimeException("Failed to convert the string into an input stream.", e);
	}
    }

    /**
     * Extracts text entries from an input stream. This is expected to contain a
     * Java properties string.
     * 
     * @param in
     *            the input stream
     * @return the set of text entries
     * @throws IOException
     */
    public Collection<InputTextEntry> extract(final InputStream in) throws IOException {
	InputStreamReader reader = null;
	try {
	    // the game stores texts in the same format as a Java properties
	    // file
	    // and the keys for the important texts are in the following format:
	    // TermDlg.FoundTexts.Alexandra.AI_Citizenship.Name
	    // TermDlg.FoundTexts.Alexandra.AI_Citizenship.Text
	    Properties props = new Properties();

	    // the Properties.load() method assumes Latin-1 encoding by default
	    // to overcome this we need to explicitly define a utf-8 reader and
	    // pass it to the load method
	    reader = new InputStreamReader(in, charset);
	    props.load(reader);

	    // @formatter:off
	    Set<String> keyNames = props.keySet()
    		    .stream()
    		    .map(k -> (String) k)
    		    .filter(k -> k.startsWith(prefix))
    		    .map(k -> k.substring(0, k.length() - 5))
    		    .collect(Collectors.toSet());
	    // @formatter:on

	    return keyNames.stream().map(key -> {
		String filename = props.getProperty(key + ".Name");
		String contents = props.getProperty(key + ".Text");
		return new InputTextEntry(key, filename, contents);
	    }).collect(Collectors.toSet());
	} finally {
	    IOUtils.closeQuietly(reader);
	}
    }
}
