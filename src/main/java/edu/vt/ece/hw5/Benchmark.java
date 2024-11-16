package edu.vt.ece.hw5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import edu.vt.ece.hw5.sets.*;

public class Benchmark {
    private static final int UPPER_BOUND = 100;
    private static final int ITERATIONS = 10000;
    private static final int BYTE_PADDING = 64;

    private static Set<Integer> mySet;
    private static volatile boolean[] containsResults;

    private static float ADD_LIMIT;
    private static float REMOVE_LIMIT;

    public static void main(String[] args) throws Exception {
        String setType = args[0];
        int threadCount = Integer.parseInt(args[1]);
        float containsPercentage = Float.parseFloat(args[2]);

        ADD_LIMIT = (1 - containsPercentage) / 2;
        REMOVE_LIMIT = ADD_LIMIT + (1 - containsPercentage) / 2;

        mySet = getSet(setType);
        containsResults = new boolean[threadCount * BYTE_PADDING];

        List<Callable<Long>> calls = getCallables(threadCount);
        ExecutorService excs = Executors.newFixedThreadPool(threadCount);

        long startTime = System.nanoTime();
        List<Future<Long>> futures = excs.invokeAll(calls);
        long endTime = System.nanoTime();

        long totalTime = 0;
        for (Future<Long> future : futures) {
            totalTime += future.get();
        }

        double throughput = (double) (threadCount * ITERATIONS) / ((endTime - startTime) / 1_000_000_000.0);

        System.out.println("Throughput: " + throughput + " ops/sec");
        excs.shutdown();
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
            default:
                throw new IllegalArgumentException("Unknown set type: " + setType);
        }
    }

    private static List<Callable<Long>> getCallables(int threadCount) {
        List<Callable<Long>> calls = new ArrayList<>(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            calls.add(() -> doStuff(index));
        }
        return calls;
    }

    private static long doStuff(int index) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            float operation = random.nextFloat();
            int value = random.nextInt(UPPER_BOUND);

            if (operation < ADD_LIMIT) {
                mySet.add(value);
            } else if (operation < REMOVE_LIMIT) {
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