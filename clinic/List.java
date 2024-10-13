package clinic;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic list class for managing objects of type E. The list supports dynamic resizing,
 * searching, and basic operations like adding and removing objects.
 *
 * @param <E> the type of elements held in this list.
 */
public class List<E> implements Iterable<E> {
    private E[] objects;  // Array to hold objects of type E
    private int size;  // Current number of elements in the array
    private static final int INITIAL_CAPACITY = 4;  // Initial capacity of the array
    private static final int NOT_FOUND = -1;  // Constant for when an object is not found

    /**
     * Default constructor to initialize the list with an initial capacity.
     */
    @SuppressWarnings("unchecked")
    public List() {
        this.objects = (E[]) new Object[INITIAL_CAPACITY];  // Generic array creation
        this.size = 0;
    }

    /**
     * Finds the index of a given element in the list.
     *
     * @param e the element to search for.
     * @return the index of the element if found, or -1 if not found.
     */
    private int find(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                return i;
            }
        }
        return NOT_FOUND;  // If element is not found
    }

    /**
     * Expands the size of the array by doubling it when it's full.
     */
    @SuppressWarnings("unchecked")
    private void grow() {
        E[] newObjects = (E[]) new Object[objects.length * 2];
        System.arraycopy(objects, 0, newObjects, 0, objects.length);
        objects = newObjects;
    }

    /**
     * Checks if the list contains a given element.
     *
     * @param e the element to check.
     * @return true if the element is in the list, false otherwise.
     */
    public boolean contains(E e) {
        return find(e) != NOT_FOUND;
    }

    /**
     * Adds a new element to the list. Expands the list if the array is full.
     *
     * @param e the element to add.
     */
    public void add(E e) {
        if (size >= objects.length) {
            grow();  // Grow the array if it's full
        }
        objects[size++] = e;  // Add the element and increase size
    }

    /**
     * Removes a specified element from the list, replacing it with the last element
     * in the list to maintain contiguous data.
     *
     * @param e the element to remove.
     */
    public void remove(E e) {
        int index = find(e);
        if (index != NOT_FOUND) {
            objects[index] = objects[size - 1];  // Replace with the last element
            objects[size - 1] = null;  // Null out the last spot
            size--;
        }
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return the size of the list.
     */
    public int size() {
        return size;
    }

    /**
     * Returns the element at the given index.
     *
     * @param index the index of the element.
     * @return the element at the specified index.
     */
    public E get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return objects[index];
    }

    /**
     * Replaces the element at the given index with the provided element.
     *
     * @param index the index to replace the element at.
     * @param e the element to set.
     */
    public void set(int index, E e) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        objects[index] = e;
    }

    /**
     * Returns the index of the specified element, or -1 if the element is not found.
     *
     * @param e the element to find the index of.
     * @return the index of the element, or -1 if not found.
     */
    public int indexOf(E e) {
        return find(e);
    }

    /**
     * Returns an iterator over the elements in this list.
     *
     * @return an iterator.
     */
    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    /**
     * Private inner class implementing the Iterator interface for the List.
     */
    private class ListIterator implements Iterator<E> {
        private int currentIndex = 0;

        /**
         * Returns true if there are more elements to iterate.
         *
         * @return true if there are more elements, false otherwise.
         */
        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        /**
         * Returns the next element in the list.
         *
         * @return the next element.
         * @throws NoSuchElementException if there are no more elements.
         */
        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return objects[currentIndex++];
        }
    }
}
