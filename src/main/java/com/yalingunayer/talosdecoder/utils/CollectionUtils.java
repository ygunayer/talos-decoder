package com.yalingunayer.talosdecoder.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public final class CollectionUtils {
    public static <T> boolean equals(Collection<? extends T> c1, Collection<? extends T> c2) {
	if (c1.size() != c2.size())
	    return false;
	Iterator<?> e1 = c1.iterator();
	while (e1.hasNext()) {
	    if (!c2.contains(e1.next()))
		return false;
	}
	return true;
    }

    public static <K, V> Map<K, Collection<V>> toMap(final Collection<? extends V> items, final Function<V, K> mapper) {
	Map<K, Collection<V>> buffer = new HashMap<K, Collection<V>>();
	// TODO this isn't probably thread-safe
	items.stream().forEach(item -> {
	    K key = mapper.apply(item);
	    Collection<V> c = buffer.getOrDefault(key, new ArrayList<V>());
	    c.add(item);
	    buffer.put(key, c);
	});
	return buffer;
    }
}
