package com.yalingunayer.talosdecoder.process;

import java.util.regex.Pattern;

public class WhitespaceTrimFilter implements IFilter {
    private static final Pattern pattern = Pattern.compile("^\\s+|\\s+$");

    @Override
    public String apply(String text) {
	if (text == null)
	    return null;
	return pattern.matcher(text).replaceAll("");
    }

}
