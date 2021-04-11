import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MyArrayList<E> implements MyList<E>{
    private ArrayList<E> data = new ArrayList<>();

    // adds element to the ArrayList
    @Override
    synchronized public void add(E x) {
        data.add(x);
    }

    // adds all element from the given list to the ArrayList
    @Override
    synchronized public void add(List<E> lst) {
        data.addAll(lst);
    }

    // returns true if  all elements of ArrayList match to the given Predicate
    @Override
    synchronized public boolean forAll(Predicate<E> pr) {
        return data.parallelStream().allMatch(pr);
    }

    // returns true if any element of ArrayList matches to the given Predicate
    @Override
    synchronized public boolean exists(Predicate<E> pr) {
        return data.parallelStream().anyMatch(pr);
    }

    // counts the elements that match to the given Predicate
    @Override
    synchronized public long count(Predicate<E> pr) {
        return data.parallelStream().filter(pr).count();
    }

    // returns List consisting of the results of applying the given
    // Function to the elements of ArrayList
    @Override
    synchronized public List<E> map(Function<E, E> fn) {
        return data.parallelStream().map(fn).collect(Collectors.toCollection(ArrayList::new));
    }

    // returns List of elements that match to the given Predicate
    @Override
    synchronized public List<E> filter(Predicate<E> pr) {
        return data.parallelStream().filter(pr).collect(Collectors.toCollection(ArrayList::new));
    }

    // returns List of elements that match to the given Predicate and consisting of the
    // results of applying the given Function to the filtered elements
    @Override
    synchronized public List<E> mapFilter(Function<E, E> fn, Predicate<E> pr) {
        return data.parallelStream().filter(pr).map(fn).collect(Collectors.toCollection(ArrayList::new));
    }
}