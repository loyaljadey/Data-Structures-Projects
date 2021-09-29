package deque;

import java.util.Iterator;

/**
 * @source lecture 2.2 and 2.3 and 4.2 and 6.3
 * @source cs61b sp19 proj1 slides
 * @author Michael Jia
 */
public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    //nested node class
    private class TypeNode {
        private T item;
        private TypeNode next;
        private TypeNode prev;
        TypeNode(T i, TypeNode p, TypeNode n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    //instance variables
    private TypeNode sentinel;
    private int size;

    //constructor
    public LinkedListDeque() {
        sentinel = new TypeNode(null, null, null);
        size = 0;
    }

    //assuming that item is never null
    //adding item to beginning of list
    @Override
    public void addFirst(T item) {
        //check if the list is empty first
        if (isEmpty()) {
            //creates new node and assigns pointers
            TypeNode newNode = new TypeNode(item, sentinel, sentinel);
            sentinel.next = newNode;
            sentinel.prev = newNode;
        } else {
            //creates new node and assigns pointers
            TypeNode firstNode = sentinel.next;
            TypeNode newNode = new TypeNode(item, sentinel, firstNode);
            sentinel.next = newNode;
            firstNode.prev = newNode;
        }
        //cache the size of the deque
        size++;
    }
    //assuming that item is never null
    //adding item to end of list
    @Override
    public void addLast(T item) {
        //check if the list is empty first
        if (isEmpty()) {
            //creates new node and assigns pointers
            TypeNode newNode = new TypeNode(item, sentinel, sentinel);
            sentinel.next = newNode;
            sentinel.prev = newNode;
        } else {
            //creates new node and assigns pointers
            TypeNode lastNode = sentinel.prev;
            TypeNode newNode = new TypeNode(item, lastNode, sentinel);
            sentinel.prev = newNode;
            lastNode.next = newNode;
        }
        //cache the size of the deque
        size++;
    }

    //returns the size of the deque not including the sentinel node
    @Override
    public int size() {
        return size;
    }

    //Prints the items in the deque from first to last, separated by a space.
    //Once all the items have been printed, print out a new line.
    @Override
    public void printDeque() {
        //string to print later
        String print = "";
        //keeping track of what node to look at for the pointer
        TypeNode looker = sentinel.next;
        while (looker.item != null) {
            print = print + looker.item + " ";
            //move the pointer to next node
            looker = looker.next;
        }
        //prints
        System.out.println(print);
        System.out.println();
    }

    //removes the first item from the deque
    //if no item exists then return null
    @Override
    public T removeFirst() {
        //if the deque is not empty then
        //set the item to a temp variable
        //remove the first item by reassignment of pointers
        //return item of removal
        //if the deque is empty, it returns null
        if (!isEmpty()) {
            size--;
            TypeNode removal = sentinel.next;
            sentinel.next.next.prev = sentinel;
            sentinel.next = sentinel.next.next;
            return removal.item;
        } else {
            return null;
        }
    }

    //removes the last item from the deque
    //if no item exists then return null
    @Override
    public T removeLast() {
        //if the deque is not empty then
        //set the item to a temp variable
        //remove the last item by reassignment of pointers
        //return item of removal
        //if the deque is empty, it returns null
        if (!isEmpty()) {
            size--;
            TypeNode removal = sentinel.prev;
            sentinel.prev.prev.next = sentinel;
            sentinel.prev = sentinel.prev.prev;
            return removal.item;
        } else {
            return null;
        }
    }

    //iterative get method
    @Override
    public T get(int index) {
        //if the deque is not empty and index is in bounds then
        //get the item through iteration
        //return item at index
        //if the deque is empty or the index is out of bounds, it returns null
        if (!isEmpty() && index < size && index >= 0) {
            TypeNode looker = sentinel.next;
            for (int i = 0; i < index; i++) {
                looker = looker.next;
            }
            return looker.item;
        } else {
            return null;
        }

    }

    //recursive get method
    public T getRecursive(int index) {
        //if the deque is not empty and index is in bounds then
        //get the item through recursion
        //return item at index
        //if the deque is empty or the index is out of bounds, it returns null
        if (!isEmpty() && index < size && index >= 0) {
            TypeNode node = sentinel.next;
            return getRecursiveHelper(index, node);
        } else {
            return null;
        }
    }
    //does the recursive part of the getRecursive method
    private T getRecursiveHelper(int index, TypeNode node) {
        if (index != 0) {
            node = node.next;
            index--;
            return getRecursiveHelper(index, node);
        } else {
            return node.item;
        }
    }

    //Returns whether or not the parameter o is equal to the Deque.
    // o is considered equal if it is a Deque and if it contains the same contents
    // (as goverened by the generic T’s equals method) in the same order.
    @Override
    public boolean equals(Object o) {
        if (o instanceof Deque) {
            Deque temp = (Deque) o;
            if (size() != temp.size()) {
                return false;
            }
            for (int i = 0; i < size(); i += 1) {
                if (!get(i).equals(temp.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    //makes an new iterator obj to use to look through the deque

    public Iterator<T> iterator() {
        return new LLDIterator();
    }

    //nested class for the iterator that implements the methods in an iterator from java.util
    private class LLDIterator implements Iterator<T> {
        private int pos;

        LLDIterator() {
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            return pos < size;
        }

        @Override
        public T next() {
            T returnItem = get(pos);
            pos += 1;
            return returnItem;
        }
    }

}
