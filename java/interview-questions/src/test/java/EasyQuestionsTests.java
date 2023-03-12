import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testclasses.BadHashDistribution;

import java.util.*;

public class EasyQuestionsTests {

    @Test
    public void primitivesByValue_Demo() {
        int first = 1;
        int second = 1;

        // ==
        Assertions.assertSame(first, second);
        // equals
        Assertions.assertEquals(first, second);
    }

    @SuppressWarnings("removal")
    @Test
    public void objectsByValue_Demo() {
        // jvm hack for caching small values
        // https://wiki.owasp.org/index.php/Java_gotchas#Immutable_Objects_.2F_Wrapper_Class_Caching
        Integer first = 1;
        Integer second = 1;

        Assertions.assertSame(first, second);
        Assertions.assertEquals(first, second);

        // extended example
        first = 1337;
        second = 1337;
        Assertions.assertNotSame(first, second);
        Assertions.assertEquals(first, second);

        // typical question
        Assertions.assertNotSame(new Boolean(false), Boolean.FALSE);
        Assertions.assertEquals(false, Boolean.FALSE);
        Assertions.assertSame(false, Boolean.FALSE);
    }

    @Test
    public void hashMapEqualsHashCode_Demo() {
        // regular map
        Map<Long, Long> map = new HashMap<>();
        map.put(1L, 1L);
        Long val = map.get(1L);
        Assertions.assertEquals(1L, val);

        // map with key is mutable object
        Map<Map<Long, Long>, Long> multiMapMap = new HashMap<>();
        multiMapMap.put(map, 2L);

        // build new object to get value => works
        Map<Long, Long> mapKey = new HashMap<>();
        mapKey.put(1L, 1L);
        Long objectValue = multiMapMap.get(mapKey);
        Assertions.assertEquals(2L, objectValue);

        // mutate initial map and try to get => not working
        map.put(2L, 1L);
        Long mutable = multiMapMap.get(map);
        Assertions.assertNotEquals(2L, mutable);

        // try again with same object key => not working
        Long tryAgain = multiMapMap.get(mapKey);
        Assertions.assertNotEquals(2L, tryAgain);

        // try again with modified object key => not working
        mapKey.put(2L, 1L);
        Long tryAgainMutated = multiMapMap.get(mapKey);
        Assertions.assertNotEquals(2L, tryAgainMutated);
    }

    @Test
    public void hashMapBadDistribution_Demo() {
        var map = new HashMap<BadHashDistribution, Integer>();
        for (int i = 1; i < 8; i++) {
            map.put(new BadHashDistribution(String.valueOf(i)), i);
        }
        Integer val = map.get(new BadHashDistribution(String.valueOf(7)));

        Assertions.assertEquals(7, val);
    }

    /**
     * Read more: https://www.baeldung.com/java-collections
     */
    @Test
    public void collectionFramework_Demo() {
        // iterable -> collection -> list, queue, set
        // map not in collections

        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        List<Integer> vector = new Vector<>();
        List<Integer> stack = new Stack<>();

        Queue<Integer> priorityQueue = new PriorityQueue<>();
        Queue<Integer> linkedListQueue = new LinkedList<>();
        Queue<Integer> arrayListQueue = new ArrayDeque<>();

        Set<Integer> hashSet = new HashSet<>();
        Set<Integer> linkedHashSet = new LinkedHashSet<>();
        Set<Integer> treeSet = new TreeSet<>();

        // map is not in collections
        Map<Integer, Integer> hashMap = new HashMap<>();
        Map<Integer, Integer> linkedHashMap = new LinkedHashMap<>();
        Map<Integer, Integer> treeMap = new TreeMap<>();
        // specific map for enum values
        //Map<Integer, Integer> enumMap = new EnumMap<>();
    }

    @Test()
    public void checkedException_Demo() throws InterruptedException {
        throw new InterruptedException();
    }

    @Test
    public void unCheckedException_Demo() {
        throw new RuntimeException();
    }

    @Test
    public void optionalOf_Demo() {
        Assertions.assertThrows(NullPointerException.class, () -> Optional.of(null));

        String name = null;
        Assertions.assertEquals(Optional.empty(), Optional.of(new BadHashDistribution(name)).map(v -> v.getName()));
        Assertions.assertThrows(NullPointerException.class,() -> Optional.of(name).orElseGet(()->"default"));
    }

    @Test
    public void optionalOfNullable_Demo() {
        Assertions.assertEquals(Optional.empty(), Optional.ofNullable(null));

        String name = null;
        Assertions.assertDoesNotThrow(() -> Optional.ofNullable(name).orElseGet(()->"default"));
    }

    /**
     * More info https://www.baeldung.com/java-threadlocal
     */
    @Test
    public void threadLocal_Demo() throws InterruptedException {
        Runnable shared = new Runnable() {
            private final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

            @Override
            public void run() {
                System.out.println("before: " + threadLocal.get());

                threadLocal.set((int) (Math.random() * 100D));

                try {
                    // emulate workload
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }

                System.out.println("after: " + threadLocal.get());
            }
        };

        // concurrent access to data, datarace issues
        Thread first = new Thread(shared, "first");
        Thread second = new Thread(shared, "second");

        // happens before
        first.start();
        second.start();

        first.join();
        second.join();
    }

    /**
     * More info https://jenkov.com/tutorials/java-concurrency/threadlocal.html
     */
    @Test
    public void inheritableThreadLocal_Demo() throws InterruptedException {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        InheritableThreadLocal<String> inheritableThreadLocal =
                new InheritableThreadLocal<>();

        Thread thread1 = new Thread(() -> {
            System.out.println("===== Thread 1 =====");
            threadLocal.set("Thread 1 - ThreadLocal");
            inheritableThreadLocal.set("Thread 1 - InheritableThreadLocal");

            System.out.println(threadLocal.get());
            System.out.println(inheritableThreadLocal.get());

            Thread childThread = new Thread(() -> {
                System.out.println("===== ChildThread =====");
                System.out.println(threadLocal.get());
                System.out.println(inheritableThreadLocal.get());
            });
            childThread.start();
            try {
                childThread.join();
            } catch (InterruptedException ignored) {
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("===== Thread2 =====");
            System.out.println(threadLocal.get());
            System.out.println(inheritableThreadLocal.get());
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    @Test
    public void streams_Demo() {

    }

    @Test
    public void primitiveStreams_Demo() {

    }

    @Test
    public void tryCatchFinally_Demo() {

    }

    @Test
    public void tryWithResource_Demo() {

    }
}
