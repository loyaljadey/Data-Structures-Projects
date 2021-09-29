package deque;

import org.junit.Test;

/** Performs some basic linked list tests. */
public class ArrayDequeTest {

    /** MY OWN TESTS */

    @Test
    /** Test the creation of a AD. */
    public void ADequeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
    }

    @Test
    /** Test the addition of something to AD */
    public void ADequeTestadd() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addLast(5);
        ad1.addLast(7);
        ad1.addLast(9);
        ad1.addFirst(3);
        ad1.addFirst(3);
        ad1.addFirst(3);
        ad1.addFirst(3);
        ad1.addFirst(3);
        ad1.addFirst(3);
    }

    @Test
    /** Test the printing of the AD */
    public void ADequeTestprint() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addLast(5);
        ad1.addLast(7);
        ad1.addLast(9);
        ad1.addFirst(3);
        ad1.addFirst(1);
        ad1.printDeque();
    }

    @Test
    /** Test the removal of items in the AD */
    public void ADequeTestremove() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.addLast(5);
        ad1.addLast(7);
        ad1.addFirst(3);
        ad1.addFirst(1);
        ad1.addLast(9);
        ad1.addLast(11);
        ad1.addLast(13);
        ad1.addLast(15);
        System.out.println(ad1.removeFirst());
        System.out.println(ad1.removeLast());
        System.out.println(ad1.removeLast());
        System.out.println(ad1.removeLast());
    }

    @Test
    /** Test the getting of items in the AD */
    public void ADequeTestget() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.addLast(5);
        ad1.addLast(7);
        ad1.addLast(9);
        ad1.addFirst(3);
        ad1.addFirst(1);
        System.out.println(ad1.get(0));
        System.out.println(ad1.get(1));
        System.out.println(ad1.get(2));
        System.out.println(ad1.get(3));
        System.out.println(ad1.get(4));
        System.out.println(ad1.get(5));
        System.out.println(ad1.get(-5));
        System.out.println(ad1.get(-1));
    }

    @Test
    /** Test the resize of the AD */
    public void ADequeTestresize() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.addLast(5);
        ad1.addLast(7);
        ad1.addLast(9);
        ad1.addLast(5);
        ad1.addLast(7);
        ad1.addLast(9);
        ad1.addLast(5);
        ad1.addLast(7);
        ad1.addLast(9);
        ad1.addLast(5);
        ad1.addLast(7);
        ad1.addLast(9);
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
    }

    @Test
    /** Test the resize of the AD */
    public void ADequeTestresize2() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.addLast(5);
        ad1.addLast(7);
        ad1.addFirst(9);
        ad1.addFirst(5);
        ad1.addFirst(7);
        ad1.addFirst(9);
        ad1.addFirst(5);
        ad1.addFirst(7);
        ad1.addFirst(9);
        ad1.addFirst(5);
        ad1.addFirst(7);
        ad1.addFirst(9);
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
    }

    @Test
    /** Test the resize of the AD */
    public void ADequeTestresize3() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.addLast(7);
        ad1.addFirst(5);
        ad1.addLast(7);
        ad1.addFirst(5);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
    }

    @Test
    /** Test the resize of the AD */
    public void ADequeTestresize4() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.addLast(7);
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
    }

    @Test
    /** Test the resize of the AD */
    public void grade() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.addFirst(0);
        ad1.addFirst(1);
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.addFirst(4);
        ad1.removeFirst();
        ad1.get(0);
        ad1.addLast(6);
        ad1.addLast(7);
    }

    @Test
    /** Test the resize of the AD */
    public void grade1() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.addFirst(0);
        ad1.addFirst(1);
        ad1.removeFirst();
        ad1.addFirst(3);
        ad1.addFirst(4);
    }

    @Test
    /** Test the resize of the AD */
    public void grade2() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.isEmpty();
        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.removeLast();
        ad1.addFirst(4);
        ad1.removeLast();
        ad1.addFirst(6);
        ad1.isEmpty();
        ad1.removeLast();
        ad1.addFirst(9);
    }

    @Test
    /** Test the resize of the AD */
    public void grade3() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.addFirst(0);
        ad1.addFirst(1);
        ad1.removeFirst();
        ad1.addLast(3);
    }

    @Test
    /** Test the resize of the AD */
    public void grade4() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.addFirst(0);
        ad1.removeFirst();
        ad1.addLast(2);
        ad1.addFirst(3);
        System.out.println(ad1.get(0));
        ad1.removeLast();
        ad1.removeLast();
        ad1.addFirst(7);
        ad1.removeFirst();
        ad1.addLast(9);
        ad1.removeLast();
        ad1.addLast(11);
        ad1.addLast(12);
        System.out.println(ad1.get(0));
    }

    @Test
    /** Test the resize of the AD */
    public void grade5() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        ad1.addFirst(0);
        ad1.addFirst(1);
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.addFirst(4);
        ad1.removeFirst();
    }

}

