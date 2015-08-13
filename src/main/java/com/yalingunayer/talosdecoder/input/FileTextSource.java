package com.yalingunayer.talosdecoder.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yalingunayer.talosdecoder.dto.InputTextEntry;
import com.yalingunayer.talosdecoder.process.TextExtractor;

public class FileTextSource implements ITextSource {

    private File file;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public FileTextSource(final File file) {
	this.file = file;
    }

    public File getFile() {
	return file;
    }

    @Override
    public Collection<InputTextEntry> getEntries() throws Exception {
	InputStream in = null;
	try {
	    in = new FileInputStream(file);
	    Collection<InputTextEntry> results = new TextExtractor().extract(in);
	    logger.info("Extracted {} entries from file {}", results.size(), file);
	    return results;
	} finally {
	    IOUtils.closeQuietly(in);
	}
    }
}
