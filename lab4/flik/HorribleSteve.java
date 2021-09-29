package flik;

public class HorribleSteve {
    public static void main(String [] args) throws Exception {

        int i = 0;
        for (int j = 0; j < 500; i++, j++) {

            //testing to debug
            //this works comparing 128 to 128
            //returns true
            //maybe int issue?
            System.out.println(i == j);

            //error in this isSameNumber(i,j) comparison of object type not int type
            //cannot use == since they are integer objects not ints
            //gotta use equals()
            if (!Flik.isSameNumber(i, j)) {
                throw new Exception(
                        String.format("i:%d not same as j:%d ??", i, j));
            }
        }
        System.out.println("i is " + i);
    }
}
