package deque;

import java.util.Iterator;

public interface Deque<T> {
    void addFirst(T item);
    void addLast(T item);
    //checks if the list is empty with the size condition
    //if size = 0 then must be empty else size == 0 will return false
    default boolean isEmpty() {
        return size() == 0;
    }
    int size();
    void printDeque();
    T removeFirst();
    T removeLast();
    T get(int index);
    Iterator<T> iterator();
    boolean equals(Object o);
}
