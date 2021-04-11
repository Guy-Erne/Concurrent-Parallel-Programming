

/*
Concurrent Programming MSCBD Assignment 1 ( 10%) Please complete the problems listed below.
This assignment forms part of the assessment for this module and you are required
to upload your solution in the template file available on Moodle.
You need to submit only java files, any other format will not be corrected.
If code is shared with other students you will loose the assignment grade and
will be reported to college.

Question 1 ( 2.5%) Write a thread that tosses a coin
1000 times and computes the frequency of heads and tails.
A coin can be modelled by a random number generator that generates random numbers in the range 0..1.





 */
public class Assign1 {

    public static void main(String[] args) {
        Thread p = new CoinFlip( 1000);
        p.start();
    }
}

class CoinFlip extends Thread {
    private int head=0;
    private int tail=0;
    private int n;

    CoinFlip( int p) {
        n = p;
    }

    public void run() {
        for (int i = 0; i < n; i++) {
            if(flipCoin()==0)
            {
                head++;
            }
            else {
                tail++;
            }
        }
        System.out.println("frequency of heads:"+head);
        System.out.println("frequency of tails:"+tail);

    }

    private int flipCoin() {
        int result;
        if (Math.random() < 0.5){
            result=0;
        }else{
            result=1;
        }
        return result;
    }

}