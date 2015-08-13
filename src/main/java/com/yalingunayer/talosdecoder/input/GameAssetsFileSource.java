package com.yalingunayer.talosdecoder.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yalingunayer.talosdecoder.dto.InputTextEntry;
import com.yalingunayer.talosdecoder.process.TextExtractor;

public class GameAssetsFileSource implements ITextSource {
    public enum GameLocale {
	CHINESE_SIMPLIFIED("chs"),
	CHINESE_TRADITIONAL("cht"),
	GERMAN("deu"),
	ENGLISH("enu"),
	SPANISH("esp"),
	FRENCH("fra"),
	CROATIAN("hrv"),
	ITALIAN("itl"),
	JAPANESE("jpn"),
	KOREAN("kor"),
	POLISH("plk"),
	PORTUGESE("por"),
	RUSSIAN("rus");

	private String name;

	private GameLocale(final String name) {
	    this.name = name;
	}

	public static GameLocale forName(final String name) {
	    return Arrays.asList(values()).stream().filter(e -> e.name.equalsIgnoreCase(name)).findFirst()
		    .orElseThrow(() -> new StringIndexOutOfBoundsException("No locale found with name " + name));
	}
    };

    public static final GameLocale DEFAULT_LOCALE = GameLocale.ENGLISH;
    public static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");
    public static final String ASSET_FILE_PATH = "Content/Talos/All_244371.gro";
    public static final String TRANSLATION_FILE_ENTRY_NAME_FORMAT = "Content/Talos/Locales/%s/translation_All.txt";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Charset charset;
    private String assetFilePath;
    private GameLocale locale;

    public GameAssetsFileSource(String gameBasePath) {
	this(gameBasePath, DEFAULT_LOCALE, DEFAULT_CHARSET);
    }

    public GameAssetsFileSource(String gameBasePath, String locale) {
	this(gameBasePath, GameLocale.forName(locale), DEFAULT_CHARSET);
    }

    public GameAssetsFileSource(String gameBasePath, String locale, String charset) {
	this(gameBasePath, GameLocale.forName(locale), Charset.forName(charset));
    }

    public GameAssetsFileSource(String gameBasePath, GameLocale locale, Charset charset) {
	this.assetFilePath = Paths.get(gameBasePath, ASSET_FILE_PATH).toString();
	this.locale = locale;
	this.charset = charset;
    }

    public String getTranslationFilePath() {
	return String.format(TRANSLATION_FILE_ENTRY_NAME_FORMAT, locale.name);
    }

    private String readFromFile() throws IOException {
	File file = new File(assetFilePath);
	if (!file.isFile())
	    throw new FileNotFoundException("Game assets file not found on path " + assetFilePath);

	String path = getTranslationFilePath();
	logger.info("Reading texts from game assets located at {}, will look at the following path in the assets {}",
		assetFilePath, path);
	ZipFile zipFile = null;
	InputStream inEntry = null;
	InputStreamReader inReader = null;
	BufferedReader br = null;

	try {
	    zipFile = new ZipFile(file);
	    ZipEntry entry = zipFile.getEntry(path);
	    if (entry == null)
		throw new RuntimeException(
			"Failed to find the translation file at path " + path + " in file " + file.toString());
	    inEntry = zipFile.getInputStream(entry);
	    inReader = new InputStreamReader(inEntry, charset);
	    br = new BufferedReader(inReader);
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = br.readLine()) != null)
		sb.append(line + System.lineSeparator());
	    return sb.toString();
	} finally {
	    IOUtils.closeQuietly(br);
	    IOUtils.closeQuietly(inReader);
	    IOUtils.closeQuietly(inEntry);
	    IOUtils.closeQuietly(zipFile);
	}
    }

    @Override
    public Collection<InputTextEntry> getEntries() {
	try {
	    return new TextExtractor().extract(readFromFile());
	} catch (IOException e) {
	    throw new RuntimeException("Failed to read from game assets", e);
	}
    }
}
