package com.yalingunayer.talosdecoder.process;

public class Filters {
    public static HtmlLineBreakFilter nl2br() {
	return new HtmlLineBreakFilter();
    }

    public static WhitespaceTrimFilter trim() {
	return new WhitespaceTrimFilter();
    }
}
