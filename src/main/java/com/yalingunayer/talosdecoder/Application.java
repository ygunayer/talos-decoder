package com.yalingunayer.talosdecoder;

import static com.yalingunayer.talosdecoder.input.TextSources.*;
import static com.yalingunayer.talosdecoder.output.TextSinks.*;
import static com.yalingunayer.talosdecoder.process.Filters.*;
import static com.yalingunayer.talosdecoder.process.Pipeline.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        // the DSL is as follows:
        // from(ITextSource)[.filter(IFilter...)].to(ITextSink).go();

        // from(filename).to(filename).go() -> input: file, output: file
        // from(filename).to(inMemory()).go() -> input: file, output: in-memory
        // from(filename).to(elastic()).go() -> input: file, output: elastic

        // from(file("classpath:texts.txt")).to(basic()).go();

        Properties props = new Properties();

        if (args.length > 1) {
            String propsFilePath = args[1];

            File file = new File(propsFilePath);
            if (!file.exists())
                throw new FileNotFoundException("No file found at " + propsFilePath);

            InputStream in = null;
            try {
                in = new FileInputStream(file);
                props.load(in);
            } finally {
                IOUtils.closeQuietly(in);
            }

            logger.info("Loaded configuration from {}", propsFilePath);
        }

        String elasticHostname = props.getProperty("elastic.hostname", "localhost");
        int elasticPort = Integer.valueOf(props.getProperty("elastic.port", "9300"));
        String indexName = System.getProperty("elastic.index", "talos");

        String gameLocale = props.getProperty("game.locale", "enu");
        String gamePath = System.getProperty("game.path");

        if (gamePath == null) {
            props.getProperty("game.path");
        }

        if (gamePath == null) {
            throw new RuntimeException("Game path must be set.");
        }

        if (!(new File(gamePath)).isDirectory()) {
            throw new FileNotFoundException("Specified path not found or is not a directory: " + gamePath);
        }

        // TODO allow the user to configure the pipeline by specifying the
        // input, output and filters
        from(game(gamePath, gameLocale)).filter(trim(), nl2br()).to(elastic(elasticHostname, elasticPort, indexName)).go();
    }
}
