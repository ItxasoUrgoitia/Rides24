package businessLogic;

import java.util.List;
import java.util.NoSuchElementException;

public class ExtendedIteratorImpl<T> implements ExtendedIterator<T> {
    private List<T> list;
    private int index;

    public ExtendedIteratorImpl(List<T> list) { 
        this.list = list;
        goFirst();
    }

    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        return list.get(index++);
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public T previous() {
        if (!hasPrevious()) throw new NoSuchElementException();
        index--;
        return list.get(index);
    }

    @Override
    public void goFirst() {
        index = 0;
    }

    @Override
    public void goLast() {
        //index = list.isEmpty() ? 0 : list.size() - 1;
    	index = list.size();
    }
}
