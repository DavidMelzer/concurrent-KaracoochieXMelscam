package codes.davidmelzer.msi.concurrent.task5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FactorizerService {

    private final Map<Long, List<Long>> cache = new ConcurrentHashMap<>();
    private final Object lock = new Object();

    public List<Long> factorize(long number) {
        // Obtain an atomic snapshot of the cache
        Map<Long, List<Long>> snapshot = new ConcurrentHashMap<>(cache);

        // Check if the result is present in the snapshot
        if (snapshot.containsKey(number)) {
            return snapshot.get(number);
        }

        List<Long> factors = new ArrayList<>();

        // Factorize the number
        synchronized (lock) {
            // Recheck to avoid race condition
            if (!cache.containsKey(number)) {
                for (long i = 2; i <= number; i++) {
                    while (number % i == 0) {
                        factors.add(i);
                        number /= i;
                    }
                }
                cache.put(number, factors);
            }
        }

        return factors;
    }

    public static void main(String[] args) {
        FactorizerService factorizer = new FactorizerService();

        // Test concurrently
        Runnable task = () -> {
            long number = 1000000000000L; // Example number to factorize
            List<Long> factors = factorizer.factorize(number);
            System.out.println("Factors of " + number + ": " + factors);
        };

        // Create multiple threads to test concurrency
        for (int i = 0; i < 5; i++) {
            new Thread(task).start();
        }
    }
}

