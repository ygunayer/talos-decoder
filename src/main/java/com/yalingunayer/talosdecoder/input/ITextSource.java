package com.yalingunayer.talosdecoder.input;

import java.util.Collection;

import com.yalingunayer.talosdecoder.dto.InputTextEntry;

public interface ITextSource {
    Collection<InputTextEntry> getEntries() throws Exception;
}
