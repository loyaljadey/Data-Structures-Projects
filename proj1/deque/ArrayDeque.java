package deque;

import java.util.Iterator;

/**
 * @source lecture 2.4 and 2.5 and 4.2 and 6.3
 * @source cs61b sp19 proj1 slides
 * @author Michael Jia
 */
public class ArrayDeque<T> implements Deque<T>, Iterable<T> {

    //instance variables
    private T[] sentinel;
    private int size;
    private int nextFirst;
    private int nextLast;

    //constructor makes a array with a starting size of 8
    public ArrayDeque() {
        sentinel = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    //assume item is never null
    //adding item to beginning of the array
    @Override
    public void addFirst(T item) {
        //checks if this is the first item to be added
        //if not checks if the next spot is full
        if (size() == sentinel.length) {
            //else case: all full cannot add more items in front
            //must resize array
            resize(1);
        }
        sentinel[nextFirst] = item;
        nextFirst = (nextFirst - 1 + sentinel.length) % (sentinel.length);
        //caching the size of array
        size++;
    }

    //assume item is never null
    //adding item to end of the array
    @Override
    public void addLast(T item) {
        //checks if this is the first item to be added
        //if not checks if the next spot is full
        if (size() == sentinel.length) {
            //else case: all full cannot add more items in front
            //must resize array
            resize(1);
        }
        sentinel[nextLast] = item;
        nextLast = (nextLast + 1 + sentinel.length) % (sentinel.length);
        //caching the size of array
        size++;
    }

    //returns the size variable
    @Override
    public int size() {
        return size;
    }

    //prints out the array from the beginning to the end
    @Override
    public void printDeque() {
        //keeping track of the start and the end indices
        int start = nextFirst + 1;
        int end = nextLast - 1;
        //string to print later
        String print = "";
        int times = 0;
        int i = start;
        while (times < size) {
            //if the index is out of bounds then change it
            if (i >= sentinel.length) {
                i = (i + sentinel.length) % (sentinel.length);
            }
            //concatenate the string
            print = print + sentinel[i] + " ";
            //move forward the looking index
            i++;
            //move forward the print counter
            times++;
        }
        //prints
        System.out.println(print);
        System.out.println();
    }

    //removes the first item in the array
    @Override
    public T removeFirst() {
        //checks if this is the first item to be removed
        //if so, return null
        //if not checks the index value first to make sure the call to next is not out of bounds
        //temporarily store it
        //set to null
        //reset nextfirst index
        //change size
        //return item
        if (size == 0) {
            return null;
        } else {
            //else case: more than quarter the array is empty then resize
            //must resize array
            if ((double) size() / sentinel.length <= 0.25 && sentinel.length > 8) {
                resize(-1);
            }
            int first = ((nextFirst + 1) + sentinel.length) % (sentinel.length);
            T item = sentinel[first];
            sentinel[first] = null;
            nextFirst = first;
            size--;
            return item;
        }
    }
    @Override
    public T removeLast() {
        //checks if this is the first item to be removed
        //if so, return null
        //if not checks the index value last to make sure the call to next is not out of bounds
        //temporarily store it
        //set to null
        //reset nextlast index
        //change size
        //return item
        if (size == 0) {
            return null;
        } else {
            //else case: more than quarter the array is empty then resize
            //must resize array
            if ((double) size() / sentinel.length <= 0.25 && sentinel.length > 8) {
                resize(-1);
            }
            int last = ((nextLast - 1) + sentinel.length) % (sentinel.length);
            T item = sentinel[last];
            sentinel[last] = null;
            nextLast = last;
            size--;
            return item;
        }
    }

    private void resize(int i) {
        T[] temp;
        //resizing if full
        if (i > 0) {
            temp = (T[]) new Object[size() * 2];
            for (int j = 0; j < size(); j++) {
                temp[j] = get(j);
            }
            nextFirst = temp.length - 1;
            nextLast = (size() + temp.length) % (temp.length);
        } else {
            //resizing if quarter of length
            temp = (T[]) new Object[sentinel.length / 4];
            for (int j = 0; j < size(); j++) {
                temp[j] = get(j);
            }
            nextFirst = temp.length - 1;
            nextLast = (size() + temp.length) % (temp.length);
        }
        sentinel = temp;
    }



    //returns the object at index
    //doesn't matter if the array is empty will return null anyways
    @Override
    public T get(int index) {
        index = nextFirst + 1 + index;
        index = (index + sentinel.length) % (sentinel.length);
        return sentinel[index];
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
        return new ADIterator();
    }

    //nested class for the iterator that implements the methods in an iterator from java.util
    private class ADIterator implements Iterator<T> {
        private int pos;

        ADIterator() {
            pos = 0;
        }

        public boolean hasNext() {
            return pos < size;
        }

        public T next() {
            T returnItem = get(pos);
            pos += 1;
            return returnItem;
        }
    }

}

