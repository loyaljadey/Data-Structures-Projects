/** Class that prints the Collatz sequence starting from a given number.
 *  @author Michael Jia
 */
public class Collatz {
    public static void main(String[] args) {

        int n = 5;
        int i = 0;
        while (i == 0) {
            System.out.print(n + " ");

            n = nextNumber(n);

            if(n == 1) {
                i = 1;
            }
        }
        System.out.print(1 + " ");

    }
/**
 * bottom method to return the next number that should be outputted
 * */

    public static int nextNumber(int n){
        int nNext;

        if(n == 1) {
            nNext = n;
        }
        else if(n%2 == 0) {
            nNext = n/2;
        }
        else {
            nNext = 3*n+1;
        }

        return nNext;
    }

}

