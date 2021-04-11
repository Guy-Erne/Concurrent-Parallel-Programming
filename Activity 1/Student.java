import java.util.Random;

public class Driver 
{
		
	public static void main(String args[])
	{
		//Create an array of integers 1 to 30
		int[] arr = new int[30];
		
		for(int i=0;i<30;i++)
			arr[i] = i+1;
		
		// Now, shuffle the array
		
		Random rand = new Random(); 
		
		for(int i=0;i<30;i++)
		{
			// Generate a random number j in 0-29
			int j = rand.nextInt(30);
			
			// Swap arr[i] and arr[j]
			int k = arr[i];
			arr[i] = arr[j];
			arr[j] = k;
		}
		
		// Display the shuffled array
		for(int i=0;i<30;i++)
			System.out.print(arr[i]+" ");
		
		// Now, begin three threads, and sort the array 
		
		Sort ss = new Sort(arr, "student");
		ss.start();
		
		Sort bs = new Sort(arr, "lecture");
		bs.start();
		
		Sort ms = new Sort(arr, "visitor");
		ms.start();
		
		Sort results = new Sort(arr, "results");
		results.start();
		
	}

}
