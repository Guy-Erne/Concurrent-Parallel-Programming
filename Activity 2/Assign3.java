
/*

Question 3 ( 5 % ) Write a program that employs 2 threads that each toss their
 own die (modelled by a random number generator that generates random numbers in the
  range 1..6) a given number of times. In both cases the result of each toss is stored in a shared array.
   The array is deemed to be large enough to store the result of every throw and each thread should only
   write to its own array segment. Once the threads have completed their work then the main program
   counts the frequency of each number thrown and prints it on the screen.
 */

import java.util.Random;

 class Dice extends Thread {
    private Random random;
    private int rolls;
    private String name;
    public int rollsOutput[];

    public Dice(int rolls,int[] arr) {
        random = new Random();
        this.rolls = rolls;
        rollsOutput=arr;
    }

    public void run() {
        for (int i = 0; i < this.rolls; i++) {
           int output= random.nextInt(6) + 1;
           rollsOutput[i]=output;
        }
    }
}
   public class Assign3 {
        public static void main(String[] args) {
            int noOfRolls=10; //this is the number of rolls
            int[][] rollsOutputInMain=new int[2][noOfRolls]; //shared array.
            Dice d1 = new Dice(noOfRolls,rollsOutputInMain[0]);
            Dice d2 = new Dice(noOfRolls,rollsOutputInMain[1]);
            d1.start();
            d2.start();
            countDice(rollsOutputInMain[0],"dice 1");
            countDice(rollsOutputInMain[1],"dice 2");
        }

        public static void countDice(int[] arr,String threadName)
        {
            int one=0;
            int two=0;
            int three=0;
            int four=0;
            int five=0;
            int six=0;

            for(int i=0;i<arr.length;i++)
            {
                switch (arr[i]) {
                    case 1:
                        one++;
                        break;
                    case 2:
                        two++;
                        break;
                    case 3:
                        three++;
                        break;
                    case 4:
                        four++;
                        break;
                    case 5:
                        five++;
                        break;
                    case 6:
                        six++;
                        break;
                }
            }

            System.out.println("The frequency of each number for "+threadName+" is as follows:");
            System.out.println("one:"+one);
            System.out.println("two:"+two);
            System.out.println("three:"+three);
            System.out.println("four:"+four);
            System.out.println("five:"+five);
            System.out.println("six:"+six);
        }
    }



