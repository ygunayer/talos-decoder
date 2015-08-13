package com.yalingunayer.talosdecoder.input;

import java.io.File;

public class TextSources {

    private static final ClassLoader cl;

    static {
	cl = TextSources.class.getClassLoader();
    }

    public static FileTextSource file(final String filename) {
	if (filename == null)
	    throw new RuntimeException("Filename cannot be null");
	String fullPath = filename;
	if (filename.startsWith("classpath:"))
	    fullPath = cl.getResource(filename.substring(10)).getFile();
	return file(new File(fullPath));
    }

    public static FileTextSource file(final File file) {
	if (file == null)
	    throw new RuntimeException("File cannot be null");
	return new FileTextSource(file);
    }

    public static GameAssetsFileSource game(final String gamePath, final String gameLocale) {
	return new GameAssetsFileSource(gamePath, gameLocale);
    }
}
