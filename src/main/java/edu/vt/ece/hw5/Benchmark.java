package edu.vt.ece.hw5;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import edu.vt.ece.hw5.sets.CoarseSet;
import edu.vt.ece.hw5.sets.FineSet;
import edu.vt.ece.hw5.sets.LazySet;
import edu.vt.ece.hw5.sets.LockFreeSet;
import edu.vt.ece.hw5.sets.OptimisticSet;
import edu.vt.ece.hw5.sets.Set;

public class Benchmark {
    private static final int UPPER_BOUND = 100;
    private static final int ITERATIONS = 10000;
    private static final int BYTE_PADDING = 64;      // To avoid false sharing.
    private static final float ADD_LIMIT = 0.1f;     // Modify this per your requirement.
    private static final float REMOVE_LIMIT = 0.2f;  // Modify this per your requirement.

    private static Set<Integer> mySet;
    private static boolean[] containsResults;

    public static void main(String[] args) throws Throwable {
        mySet = getSet(args[0]); // SetType
        int threadCount = Integer.parseInt(args[1]); // ThreadCount

        containsResults = new boolean[threadCount * BYTE_PADDING];
        List<Callable<Long>> calls = getCallables(threadCount);
        ExecutorService excs = Executors.newFixedThreadPool(threadCount);
        
        long nanos = 0;
        for (Future<Long> f : excs.invokeAll(calls)) {
            try {
                nanos += f.get();
            } catch (ExecutionException e) {
                throw e.getCause(); 
            }
        }

        System.out.println(Arrays.toString(containsResults));
        System.out.println(nanos);
    }

    private static Set<Integer> getSet(String setType) {
        switch(SetType.valueOf(setType)) {
            case CoarseSet:
                return new CoarseSet<>();
            case FineSet:
                return new FineSet<>();
            case LazySet:
                return new LazySet<>();
            case LockFreeSet:
                return new LockFreeSet<>();
            case OptimisticSet:
                return new OptimisticSet<>();
        }

        return null;
    }

    private static List<Callable<Long>> getCallables(int threadCount) {
        List<Callable<Long>> calls = new ArrayList<>(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int x = i;
            calls.add(() -> doStuff(x));
        }

        return calls;
    }

    /* Invoke operations of set: add, remove, contains based on configurable
    ADD_LIMIT and REMOVE_LIMIT.

    Hint: Use System.nanoTime and ThreadLocalRandom.current for execution time
    and Random number generation (for randomizing set operations). */
    private static long doStuff(int index) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            float operation = random.nextFloat();
            int value = random.nextInt(UPPER_BOUND);

            if (operation < ADD_LIMIT) {
                mySet.add(value);
            } else if (operation < ADD_LIMIT + REMOVE_LIMIT) {
                mySet.remove(value);
            } else {
                boolean result = mySet.contains(value);
                containsResults[index * BYTE_PADDING] = result;
            }
        }

        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}
