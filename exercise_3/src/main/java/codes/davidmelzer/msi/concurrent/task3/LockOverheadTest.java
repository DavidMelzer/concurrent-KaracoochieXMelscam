package codes.davidmelzer.msi.concurrent.task3;

public class LockOverheadTest {

    private long counter = 0;

    // Synchronized increment method
    public synchronized void synchronizedIncrement() {
        counter++;
    }

    // Unsynchronized increment method
    public void unsynchronizedIncrement() {
        counter++;
    }

    public static void main(String[] args) {
        LockOverheadTest test = new LockOverheadTest();

        final int iterations = 1_000_000_000;

        // Measure unsynchronized
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            test.unsynchronizedIncrement();
        }
        long endTime = System.nanoTime();
        long unsynchronizedTime = endTime - startTime;

        // Reset counter
        test.counter = 0;

        // Measure synchronized
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            test.synchronizedIncrement();
        }
        endTime = System.nanoTime();
        long synchronizedTime = endTime - startTime;

        // Output results
        System.out.println("Unsynchronized time: " + unsynchronizedTime + " ns");
        System.out.println("Synchronized time: " + synchronizedTime + " ns");
        System.out.println("Overhead per operation: " + (synchronizedTime - unsynchronizedTime) / (double) iterations + " ns");
    }
}

