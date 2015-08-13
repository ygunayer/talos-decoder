package com.yalingunayer.talosdecoder.dto;

import java.io.Serializable;
import java.util.Collection;

public class FlatOutputTextEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    public String title;
    public String key;
    public String input;
    public String output;
    public Collection<ITextSegment> segments;

    public FlatOutputTextEntry(OutputTextEntry e) {
	this.title = e.getInput().getTitle();
	this.key = e.getInput().getKey();
	this.input = e.getInput().getContents();
	this.output = e.getOutput();
	this.segments = e.getSegments();
    }

}