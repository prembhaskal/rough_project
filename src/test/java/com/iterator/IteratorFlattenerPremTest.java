package com.iterator;

import com.iteratorx.CustomListIterator;
import com.iteratorx.Iterator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class IteratorFlattenerPremTest {

	private List<List<Integer>> listOfList;
	private List<Integer> list1;
	private List<Integer> list2;
	private List<Integer> list3;
	private List<Integer> list4;
	private List<Integer> list5;

	private IteratorFlattenerPrem<Integer> iteratorFlattenerPrem;
	private List<Integer> combinedList;

	@Before
	public void setUp() throws Exception {
		listOfList = new ArrayList<>();
		list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
		list2 = new ArrayList<>(Arrays.asList(4, 5, 6, 7));
		list3 = new ArrayList<>(Arrays.asList());
		list4 = new ArrayList<>(Arrays.asList(8, 9, 10));
		list5 = new ArrayList<>(Arrays.asList());

		listOfList.add(list1);
		listOfList.add(list2);
		listOfList.add(list3);
		listOfList.add(list4);
		listOfList.add(list5);

		combinedList = new ArrayList<>();
		combinedList.addAll(list1);
		combinedList.addAll(list2);
		combinedList.addAll(list3);
		combinedList.addAll(list4);
		combinedList.addAll(list5);

		List<Iterator<Integer>> iteratorList = new ArrayList<>();
		for (List<Integer> list : listOfList) {
			iteratorList.add(new CustomListIterator<Integer>(list.iterator()));
		}
		Iterator<Iterator<Integer>> nestedIterator = new CustomListIterator<>(iteratorList.iterator());

		iteratorFlattenerPrem = new IteratorFlattenerPrem<>(nestedIterator);
	}

	@Test
	public void testNextReturnsProperlyAllElements() throws Exception {
		for (Integer integer : combinedList) {
			assertEquals(integer, iteratorFlattenerPrem.next());
		}

		// after last element, exception is thrown
		try {
			iteratorFlattenerPrem.next();
			fail();
		}
		catch (NoSuchElementException e) {}
	}

	@Test
	public void testHasNextIsProper() throws Exception {

		assertTrue(iteratorFlattenerPrem.hasNext());

		for (int i = 0; i < combinedList.size(); i++) {
			assertTrue(iteratorFlattenerPrem.hasNext());
			iteratorFlattenerPrem.next();
		}

		assertFalse(iteratorFlattenerPrem.hasNext());
	}
	
	@Test
	public void testRemoveIsProper() {
		try {
			iteratorFlattenerPrem.remove();
			fail();
		} catch (IllegalStateException e) {}

		for (Integer num: combinedList) {
			assertEquals(num, iteratorFlattenerPrem.next());
			iteratorFlattenerPrem.remove();
		}

		// check that elements are actually removed.
		for (List<Integer> list : listOfList) {
			assertTrue(list.isEmpty());
		}

		try {
			iteratorFlattenerPrem.remove();
			fail();
		} catch (IllegalStateException e){}

	}



	
}