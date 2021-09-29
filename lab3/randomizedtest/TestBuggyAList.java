package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE

    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> test = new AListNoResizing<>();
        BuggyAList<Integer> wrongtest = new BuggyAList<>();
        test.addLast(4);
        test.addLast(5);
        test.addLast(6);
        wrongtest.addLast(4);
        wrongtest.addLast(5);
        wrongtest.addLast(6);
        assertEquals(test.removeLast(), wrongtest.removeLast());
        assertEquals(test.removeLast(), wrongtest.removeLast());
        assertEquals(test.removeLast(), wrongtest.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> K = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                System.out.println("addLastL(" + randVal + ")");
                K.addLast(randVal);
                System.out.println("addLastK(" + randVal + ")");

            } else if (operationNumber == 1) {
                // size
                int sizeL = L.size();
                System.out.println("size: " + sizeL);
                int sizeK = K.size();
                System.out.println("size: " + sizeK);
                assertEquals(sizeL,sizeK);

            } else if (operationNumber == 2) {
                //getLast
                if(L.size() != 0 && K.size() != 0){
                    assertEquals(L.getLast(),K.getLast());
                }

            } else if (operationNumber == 3){
                if(L.size() != 0){
                    L.removeLast();
                    System.out.println("removeLastL()");
                    K.removeLast();
                    System.out.println("removeLastK()");
                }
            }
        }
    }

}
