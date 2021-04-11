package question2;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface MyList<E> {
	public void add(E x);  
	public void add(List<E> lst);  
	public boolean forAll(Predicate<E> pr);  
	public boolean exists(Predicate<E> pr);  
	public long count(Predicate<E> pr); 
	public List<E> map(Function<E,E> fn);  
	public List<E> filter(Predicate<E> pr);  
	public List<E> mapFilter(Function<E,E> fn, Predicate<E> pr); 

}
