package com.iterator;

import com.iteratorx.Iterator;

public class IteratorFlattenerPrem<E> implements Iterator<E> {

	private final Iterator<Iterator<E>> nestedIterator;

	private Iterator<E> currentIterator = null;

	public IteratorFlattenerPrem(Iterator<Iterator<E>> nestedIterator) {
		this.nestedIterator = nestedIterator;

		if (!nestedIterator.hasNext()) {
			throw new IllegalArgumentException("no iterators");
		}
		currentIterator = nestedIterator.next();
	}

	@Override
	public boolean hasNext() {
		resetNextIterator();
		return currentIterator.hasNext();
	}

	private void resetNextIterator() {
		if (currentIterator.hasNext()) {
			return;
		}

		if (!nestedIterator.hasNext())
			return;


		while (nestedIterator.hasNext()) {
			currentIterator = nestedIterator.next();
			if (currentIterator.hasNext())
				return;
		}
	}

	@Override
	public E next() {
		resetNextIterator();
		return currentIterator.next();
	}

	@Override
	public void remove() {
		currentIterator.remove();
	}

}
