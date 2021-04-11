import java.util.Scanner;
/*
Question 2 ( 2.5%) Write a thread that continuously prints a message every 100 milliseconds while it is still alive.
The thread should be written in such a way that it can be terminated at any time by a control program (main).
 */
public class Assign2 {
    public static void main(String args[]) {

        new Assign2().assign2();
    }


    public void assign2() {
        Scanner in = new Scanner(System.in);
        MessageTimer et = new MessageTimer();
        System.out.println("Press enter to stop the thread");
        et.start();
        in.nextLine();
        et.terminate();

    }


    class MessageTimer extends Thread{
        private volatile boolean go = true;
        public void run(){
            while(go){
                try{
                    System.out.println("message printed..");
                    this.sleep(100);
                }catch(InterruptedException e){}
            }
        }
        public void terminate(){ go = false; }
    }

}
