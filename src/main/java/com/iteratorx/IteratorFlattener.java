package com.iteratorx;

public class IteratorFlattener<E> implements Iterator<E> {

    private Iterator<E> currentItr;
    private Iterator<Iterator<E>> cursorItr;

    public IteratorFlattener(Iterator<Iterator<E>> iterators) {
        if (iterators == null) throw new IllegalArgumentException("iterators is null");
        this.cursorItr = iterators;
    }

    private void setNextCurrent() {
        while (cursorItr.hasNext()) {
            currentItr = cursorItr.next();
            if(currentItr != null && currentItr.hasNext())
                break;
        }
    }

    @Override
    public boolean hasNext() {
        if (currentItr == null || !currentItr.hasNext()) {
            setNextCurrent();
        }
        return currentItr.hasNext();
    }

    @Override
    public E next() {
        return currentItr.next();
    }

    @Override
    public void remove() {
        if (currentItr != null) {
            currentItr.remove();
        }
    }
}


