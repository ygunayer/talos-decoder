package com.yalingunayer.talosdecoder.output;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yalingunayer.talosdecoder.dto.OutputTextEntry;

public class FolderTextSink extends PostProcessingTextSink<File> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private File parentFolder;

    public FolderTextSink(final File parentFolder) {
	if (parentFolder == null)
	    throw new RuntimeException("parentFolder cannot be null");
	if (!parentFolder.isDirectory())
	    throw new RuntimeException(parentFolder + " is not a directory.");
	this.parentFolder = parentFolder;
    }

    public File generateFilename(final OutputTextEntry entry) {
	return new File(parentFolder, entry.getInput().getTitle());
    }

    @Override
    protected File postProcess(OutputTextEntry entry) throws Exception {
	File file = generateFilename(entry);
	FileUtils.write(file, entry.getOutput());
	logger.debug("Successfully wrote entry {} to file {}", entry, file);
	return file;
    }
}
