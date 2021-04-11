import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// class provides some tests cases. JVM option -ea must be provided.
public class MyArrayListTest {
    public static void main(String[] args) {
        // single-threaded tests
        // creates the List of integers
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        // and adds all elements to the MyArrayList
        myArrayList.add(list);
        // tests for existing of 5 in the MyArrayList
        assert myArrayList.exists(i -> i == 5);
        // tests for not existing of 0 in the MyArrayList
        assert !myArrayList.exists(i -> i == 0);
        // adds 0 to the MyArrayList
        myArrayList.add(0);
        // tests for existing of 0 in the MyArrayList
        assert myArrayList.exists(i -> i == 0);
        // counts the elements greater than 5
        assert myArrayList.count(i -> i > 5) == 4;
        // counts the elements equals to 8
        assert myArrayList.count(i -> i == 8) == 1;
        // adds one more 8 to the MyArrayList
        myArrayList.add(8);
        // counts the elements equals to 8
        assert myArrayList.count(i -> i == 8) == 2;
        // tests if all elements greater or equal to 0
        assert myArrayList.forAll(i -> i >= 0);
        // tests if all elements greater than 0
        assert !myArrayList.forAll(i -> i > 0);
        // tests if all elements less than 5 are [0,1,2,3,4]
        assert myArrayList.filter(i -> i < 5).containsAll(Arrays.asList(0, 1, 2, 3, 4));
        // tests if all elements greater than 7 are [8,8,9]
        assert myArrayList.filter(i -> i > 7).containsAll(Arrays.asList(8, 8, 9));
        // tests if all elements increased by 1 are [2,3,4,5,6,7,8,9,10,1,9]
        assert myArrayList.map(i -> i + 1).containsAll(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 9));
        // tests if all elements increased by 1 and less than 5 are [1,2,3,4]
        assert myArrayList.mapFilter(i -> i + 1, i -> i < 5).containsAll(Arrays.asList(1, 2, 3, 4));

        // multi-threaded tests
        ExecutorService service = Executors.newFixedThreadPool(100);
        // MyArrayList of Strings to test
        MyArrayList<String> strings = new MyArrayList<>();
        // executes 100 threads in parallel
        for (int i = 0; i < 100; i++) {
            final int index = i;
            // submits the Runnable to the executor service
            service.execute(() -> {
                // adds unique String to the MyArrayList
                strings.add("String from runnable " + index + ".");
                // tests if the MyArrayList contains only one added above string using method count()
                assert strings.count(s -> s.contains(" " + String.valueOf(index) + ".")) == 1;
                // tests if the MyArrayList contains only one added above string using method filter()
                assert strings.filter(s -> s.contains(" " + String.valueOf(index)))
                        .containsAll(Arrays.asList("String from runnable " + index + "."));
                // tests if all elements of the MyArrayList contain substring 'runnable'
                assert strings.forAll(s -> s.contains("runnable"));
                // tests if the MyArrayList contains only one added above string using method exists()
                assert strings.exists(s -> s.contains(" " + String.valueOf(index) + "."));
                // maps added above unique string to another unique string and tests if the
                // result list contains only one mapped string
                assert strings.mapFilter(s -> "Mapped string " + index,
                        s -> s.contains(" " + String.valueOf(index) + "."))
                        .containsAll(Arrays.asList("Mapped string " + index));
                // creates temporary MyArrayList
                MyArrayList<String> mappedList = new MyArrayList<>();
                // and adds to temporary MyArrayList all elements of the MyArrayList
                // mapped with the specified rule
                mappedList.add(strings.map(s -> "Mapped string " + index));
                // tests if all elements of the temporary MyArrayList match to the specified rule
                assert mappedList.forAll(s -> s.contains("Mapped"));
            });
        }
        // terminates the executor service
        service.shutdown();
    }
}
