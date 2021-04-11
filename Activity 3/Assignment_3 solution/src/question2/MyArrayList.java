package question2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MyArrayList<E> implements MyList<E>{

	private List<E> data = new ArrayList<>(); 


	@Override
	public synchronized  void add(E x) {
		data.add(x);

	}

	@Override
	public synchronized  void add(List<E> lst) {
		for (E e : lst) {
			data.add(e);
		}
	}

	@Override
	public synchronized boolean forAll(Predicate<E> pr) {
		return data.parallelStream().allMatch(pr);
	}

	@Override
	public synchronized boolean exists(Predicate<E> pr) {
		return data.parallelStream().anyMatch(pr);
	}

	@Override
	public synchronized  long count(Predicate<E> pr) {
		return data.parallelStream().filter(pr).count();
	}

	@Override
	public synchronized List<E> map(Function<E, E> fn) {
		List<E> result = new ArrayList<>();
		for (E e : data) {
			result.add(fn.apply(e));
		}
		return result;
	}

	@Override
	public List<E> filter(Predicate<E> pr) {
		return data.parallelStream().filter(pr).collect(Collectors.toList());
	}

	@Override
	public List<E> mapFilter(Function<E, E> fn, Predicate<E> pr) {
		return data.parallelStream().filter(pr).map(fn).collect(Collectors.toList());
	}

}
