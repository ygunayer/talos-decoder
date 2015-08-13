package com.yalingunayer.talosdecoder.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CollectionUtilsTests {
    @Test
    public void shouldNotMatchCollectionsOfDifferentSizes() {
	List<String> in1 = Arrays.asList("foo", "bar");
	List<String> in2 = Arrays.asList("foo", "bar", "baz");
	boolean output = CollectionUtils.equals(in1, in2);
	Assert.assertFalse("Collections of different sizes should not match", output);
    }

    @Test
    public void shouldNotMatchCollectionsOfDifferentItems() {
	List<String> in1 = Arrays.asList("foo", "bar");
	List<String> in2 = Arrays.asList("foo", "baz");
	boolean output = CollectionUtils.equals(in1, in2);
	Assert.assertFalse("Collections of different items should not match", output);
    }

    @Test
    public void shouldMatchCollectionsWithSameItemsButOfDifferentTypes() {
	List<String> in1 = Arrays.asList("foo", "bar");
	Set<String> in2 = new HashSet<String>(in1);
	boolean output = CollectionUtils.equals(in1, in2);
	Assert.assertTrue("Collections with same items but of different types should match", output);
    }

    @Test
    public void shouldMapItemsProperly() {
	List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
	List<Integer> odds = Arrays.asList(1, 3, 5);
	List<Integer> evens = Arrays.asList(2, 4, 6);

	Map<Integer, Collection<Integer>> map = CollectionUtils.toMap(numbers, x -> x % 2);
	Collection<Integer> outOdds = map.get(1);
	Collection<Integer> outEvens = map.get(0);
	boolean oddsMatch = CollectionUtils.equals(odds, outOdds);
	boolean evensMatch = CollectionUtils.equals(evens, outEvens);

	Assert.assertTrue("Collections should be mapped properly", oddsMatch);
	Assert.assertTrue("Collections should be mapped properly", evensMatch);
    }
}
