package com.yalingunayer.talosdecoder.output;

import java.io.File;

public class TextSinks {

    public static BasicTextSink basic() {
	return new BasicTextSink();
    }

    public static ElasticSearchTextSink elastic(String hostname, int port) {
	return new ElasticSearchTextSink(hostname, port);
    }

    public static SingleFileTextSink json(final File file) {
	return new SingleFileTextSink(file);
    }

    public static FolderTextSink folder(final File parentFolder) {
	return new FolderTextSink(parentFolder);
    }

}
