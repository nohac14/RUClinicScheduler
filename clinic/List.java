package clinic;

import java.util.Iterator;

public class List<E> implements Iterable<E> {
    private E[] objects;
    private int size;
    private static final int INITIAL_CAPACITY = 4;

    @SuppressWarnings("unchecked")
    public List() {
        this.objects = (E[]) new Object[INITIAL_CAPACITY];  // Initial capacity set to 4
        this.size = 0;
    }

    // Private helper method to find an element and return its index, or -1 if not found
    private int find(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    // Private helper method to grow the internal array when capacity is reached
    @SuppressWarnings("unchecked")
    private void grow() {
        E[] newObjects = (E[]) new Object[objects.length * 2];  // Double the capacity
        for (int i = 0; i < size; i++) {
            newObjects[i] = objects[i];
        }
        objects = newObjects;
    }

    // Method to check if the list contains a particular element
    public boolean contains(E e) {
        return find(e) != -1;
    }

    // Method to add a new element to the list
    public void add(E e) {
        if (size >= objects.length) {
            grow();  // Grow the array if necessary
        }
        objects[size++] = e;
    }

    // Method to remove an element by value
    public void remove(E e) {
        int index = find(e);
        if (index != -1) {
            objects[index] = objects[size - 1];  // Replace the element with the last element
            objects[size - 1] = null;  // Nullify the last element
            size--;
        }
    }

    // Method to remove an element by index
    public E remove(int index) {
        if (index >= size || index < 0) {
            throw new RuntimeException("Index: " + index + ", Size: " + size);
        }
        E removedElement = objects[index];
        for (int i = index; i < size - 1; i++) {
            objects[i] = objects[i + 1];  // Shift elements to the left
        }
        objects[size - 1] = null;
        size--;
        return removedElement;
    }

    // Method to check if the list is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Method to get the current size of the list
    public int size() {
        return size;
    }

    // Method to get the element at a particular index
    public E get(int index) {
        if (index >= size || index < 0) {
            throw new RuntimeException("Index: " + index + ", Size: " + size);
        }
        return objects[index];
    }

    // Method to set an element at a particular index
    public void set(int index, E e) {
        if (index >= size || index < 0) {
            throw new RuntimeException("Index: " + index + ", Size: " + size);
        }
        objects[index] = e;
    }

    // Method to find the index of a particular element
    public int indexOf(E e) {
        return find(e);
    }

    // Method to return an iterator over the list elements
    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    // Inner class to implement the Iterator<E> for this list
    private class ListIterator implements Iterator<E> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new RuntimeException("No more elements.");
            }
            return objects[currentIndex++];
        }
    }
}
