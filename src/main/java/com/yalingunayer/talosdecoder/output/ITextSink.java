package com.yalingunayer.talosdecoder.output;

import java.util.Collection;

import com.yalingunayer.talosdecoder.dto.InputTextEntry;
import com.yalingunayer.talosdecoder.dto.OutputTextEntry;

public interface ITextSink {
    Collection<OutputTextEntry> process(Collection<InputTextEntry> entries);
}
