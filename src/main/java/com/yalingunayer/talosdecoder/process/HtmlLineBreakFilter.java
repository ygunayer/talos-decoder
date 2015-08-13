package com.yalingunayer.talosdecoder.process;

import java.util.regex.Pattern;

public class HtmlLineBreakFilter implements IFilter {

    private Pattern pattern;

    public HtmlLineBreakFilter() {
	pattern = Pattern.compile("(\r\n|\n)");
    }

    @Override
    public String apply(String text) {
	if (text == null)
	    return text;
	return pattern.matcher(text).replaceAll("<br />");
    }

}
