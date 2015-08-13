package com.yalingunayer.talosdecoder.dto;

import java.io.Serializable;

import org.apache.commons.codec.binary.StringUtils;

public class InputTextEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String key;
    private String title;
    private String contents;

    public InputTextEntry() {
    }

    public InputTextEntry(String key, String title, String contents) {
	this.key = key;
	this.title = title;
	this.contents = contents;
    }

    public String getKey() {
	return key;
    }

    public String getTitle() {
	return title;
    }

    public String getContents() {
	return contents;
    }

    @Override
    public String toString() {
	return "InputTextEntry [key=" + key + ", title=" + title + ", contents=" + contents + "]";
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null)
	    return false;
	if (!this.getClass().equals(obj.getClass()))
	    return false;
	InputTextEntry other = (InputTextEntry) obj;
	return StringUtils.equals(key, other.key) && StringUtils.equals(title, other.title)
		&& StringUtils.equals(contents, other.contents);
    }

}
