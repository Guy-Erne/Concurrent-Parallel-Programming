package question2;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class TestParallelStreams {

	public static void main(String[] args) {
		Predicate<Integer> pred = i -> (i % 2 == 0);

		MyArrayList<Integer> arrlist = new MyArrayList<Integer>();
		arrlist.add(10); 
		arrlist.add(20); 
		arrlist.add(40); 
		arrlist.add(60); 
		arrlist.add(70); 
		arrlist.add(80); 
		arrlist.add(90); 

		MyArrayList<Integer> tempList = new MyArrayList<Integer>();
		tempList.add(90); 
		tempList.add(50); 
		
		boolean forAll = tempList.forAll(pred);
		System.out.println("Check for all the data : " + forAll);


		boolean exists = tempList.exists(pred);
		System.out.println("Exist in data list : " + exists);

		long count = tempList.count(pred);
		System.out.println("Count the data : " + count);
		
		Function<Integer, Integer> fn = t -> (t * 2);
		
		List<Integer> map = tempList.map(fn);
		System.out.println("Mapped data : " + map);
		
		List<Integer> filter = arrlist.filter(pred);
		System.out.println("Even numbers are filtered : "+ filter);
		
		
		List<Integer> mapFilter = arrlist.mapFilter(fn, pred);
		System.out.println("List after filter & map are : "+ mapFilter);
		
	}

}
