package com.iteratorx;

public class CustomListIterator<E> implements Iterator<E> {

	private final java.util.Iterator<E> javaIterator;

	public CustomListIterator(java.util.Iterator<E> javaIterator) {
		this.javaIterator = javaIterator;
	}

	@Override
	public boolean hasNext() {
		return javaIterator.hasNext();
	}

	@Override
	public E next() {
		return javaIterator.next();
	}

	@Override
	public void remove() {
		javaIterator.remove();
	}
}
