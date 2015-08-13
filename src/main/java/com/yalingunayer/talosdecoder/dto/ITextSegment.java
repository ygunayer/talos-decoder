package com.yalingunayer.talosdecoder.dto;

import java.io.Serializable;

public interface ITextSegment extends Serializable {
    ITextSegment merge(ITextSegment other);
}