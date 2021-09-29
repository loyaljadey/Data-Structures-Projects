package deque;

import java.util.Comparator;

/**
 * @source lecture 4.2
 * @source cs61b sp19 proj1 slides
 * @author Michael Jia
 */
public class MaxArrayDeque<T> extends ArrayDeque<T> {

    //instance variables
    private Comparator<T> comp;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        comp = c;
    }

    public T max() {
        if (size() == 0) {
            return null;
        } else {
            T max = get(0);
            for (int i = 0; i < size(); i += 1) {
                if (comp.compare(max, get(i)) < 0) {
                    max = get(i);
                }
            }
            return max;
        }
    }

    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        } else {
            T max = get(0);
            for (int i = 0; i < size(); i += 1) {
                if (c.compare(max, get(i)) < 0) {
                    max = get(i);
                }
            }
            return max;
        }
    }
}
