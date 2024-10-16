package clinic;

public class List<E> implements Iterable<E> {
    private E[] objects;
    private int size;
    private static final int INITIAL_CAPACITY = 4;

    @SuppressWarnings("unchecked")
    public List() {
        this.objects = (E[]) new Object[INITIAL_CAPACITY];
        this.size = 0;
    }

    private int find(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    private void grow() {
        E[] newObjects = (E[]) new Object[objects.length * 2];
        for (int i = 0; i < size; i++) {
            newObjects[i] = objects[i];
        }
        objects = newObjects;
    }

    public boolean contains(E e) {
        return find(e) != -1;
    }

    public void add(E e) {
        if (size >= objects.length) {
            grow();
        }
        objects[size++] = e;
    }

    public void remove(E e) {
        int index = find(e);
        if (index != -1) {
            objects[index] = objects[size - 1];
            objects[size - 1] = null;
            size--;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    public E get(int index) {
        if (index >= size || index < 0) {
            throw new RuntimeException("Index: " + index + ", Size: " + size);
        }
        return objects[index];
    }

    public void set(int index, E e) {
        if (index >= size || index < 0) {
            throw new RuntimeException("Index: " + index + ", Size: " + size);
        }
        objects[index] = e;
    }

    public int indexOf(E e) {
        return find(e);
    }

    private class ListIterator implements Iterator<E> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() { //return false if it’s empty or end of list
            return currentIndex < size;
        }

        @Override
        public E next() { //return the next object in the list
            if (!hasNext()) {
                throw new RuntimeException("No more elements.");
            }
            return objects[currentIndex++];
        }
    }
}