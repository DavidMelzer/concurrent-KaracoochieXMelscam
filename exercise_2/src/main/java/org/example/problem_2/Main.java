package org.example.problem_2;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    private static final AtomicBoolean found = new AtomicBoolean(false);

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis(); //starting time of the proof of work
        String block = "new block"; //the block that needs to be hashed
        BigInteger target = new BigInteger("100000000000000000000000000000000000000000000000000000000000",
                16); //the target threshold for the hashing

        int numberOfThreads = Runtime.getRuntime().availableProcessors(); // Get the number of available processors

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads); // Create a fixed-size thread pool with the number of available processors

        AtomicLong nonce = new AtomicLong(0); // Initialize an AtomicLong to hold the nonce value

        // Submit tasks to the thread pool
        for(int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() ->{
                try {
                    proveWork(block,target, nonce); // Perform proof-of-work computation for each thread
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.shutdown();

        while (! executorService.isTerminated() && !found.get()){
            //Waits for all the tasks to finish
        }
        executorService.shutdownNow();

        long endTime = System.currentTimeMillis(); // end time of the execution

        System.out.println("Available Processor: " + numberOfThreads);

        System.out.println("Winner Nonce: " + nonce);
        System.out.println("Number of checkHash computations: " + nonce);
        System.out.println("Execution time: " + (endTime - startTime) + " milliseconds");
    }

    public static void proveWork(String block, BigInteger target, AtomicLong nonce) throws NoSuchAlgorithmException {
        // Loop until a valid nonce is found by any thread
        while(!found.get()){
            long currentNonce = nonce.getAndIncrement();
            // Get the current nonce and increment it atomically
            // Check if the current nonce satisfies the proof-of-work condition
            if(!checkHash(block,target,currentNonce)){
                found.set(true); // If the condition is not satisfied, set the found flag to true to stop the loop
            }
        }
    }

    private static boolean checkHash(String block, BigInteger target, long nonce) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] input = (block + nonce).getBytes(); // Convert the concatenation of the block and nonce to bytes

        byte[] hash = digest.digest(digest.digest(input));// Compute the SHA-256 has of the Input data twice

        BigInteger hashInt = new BigInteger(1,hash); // Convert the resulting hash value back to a BigInteger

        return hashInt.compareTo(target) < 0; // Check if the hash value is less than the target threshold
    }
}
