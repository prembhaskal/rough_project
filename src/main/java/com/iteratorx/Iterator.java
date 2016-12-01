package com.iteratorx;

public interface Iterator<E> {

    /**
     * @return true if the iterator has more elements.
     */
    boolean hasNext();

    /**
     * @return the next element in the iteration.
     * @exception NoSuchElementException iteration has no more elements.
     */
    E next();

    /**
     * Removes from the underlying collection the last element returned
     * by the iterator (optional operation).  This method can be called
     * only once per call to next().
     *
     * @exception IllegalStateException if the next() method has not
     * yet been called, or the remove() method has already been called
     * after the last call to the next() method.
     */
    void remove();
}