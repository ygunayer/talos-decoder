package com.yalingunayer.talosdecoder.output;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yalingunayer.talosdecoder.dto.InputTextEntry;
import com.yalingunayer.talosdecoder.dto.OutputTextEntry;
import com.yalingunayer.talosdecoder.utils.SerializationUtils;

public class SingleFileTextSink extends BasicTextSink {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private File file;

    public SingleFileTextSink(final File file) {
	if (file == null)
	    throw new RuntimeException("file cannot be null");
	if (file.isDirectory())
	    throw new RuntimeException("A directory exists on path " + file);
	this.file = file;
    }

    @Override
    public Collection<OutputTextEntry> process(Collection<InputTextEntry> entries) {
	try {
	    Collection<OutputTextEntry> results = super.process(entries);
	    String outputString = SerializationUtils.mapper().writeValueAsString(results);
	    FileUtils.write(file, outputString);
	    logger.debug("Successfully wrote {} entries as JSON to file {}", entries.size(), file);
	    return results;
	} catch (Exception ex) {
	    throw new RuntimeException("Failed to write entries to JSON file at " + file, ex);
	}
    }
}
