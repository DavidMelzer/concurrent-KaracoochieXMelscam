package org.example.problem_1;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis(); //starting time of the proof of work
        String block = "new block"; //the block that needs to be hashed
        BigInteger target = new BigInteger("100000000000000000000000000000000000000000000000000000000000",
                16); //the target threshold for the hashing

        long nonce = 0; // Determine the winner nonce using the proof-of-work algorithm
        try {
            nonce = proveWork(block,target); // the winner nonce
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis(); //end time of the execution

        System.out.println("Winner Nonce: " + nonce);
        System.out.println("Number of checkHash computations: " + nonce);
        System.out.println("Execution time: " + (endTime - startTime) + " milliseconds");
    }

    public static long proveWork(String block, BigInteger target) throws NoSuchAlgorithmException {
        long nonce = 0; // Initialize nonce to start searching for a valid nonce

        while(!checkHash(block,target,nonce)){ // Keep incrementing nonce until a valid nonce is found
            nonce++; // Increment nonce for each iteration
        }

        return nonce; // Return the winning nonce
    }

    public static boolean checkHash(String block, BigInteger target, long nonce) throws NoSuchAlgorithmException {

        byte[] input = (block + nonce).getBytes();  // Convert the concatenation of the block and nonce to bytes

        byte[] hash = computeSHA256(computeSHA256(input)); // Compute the SHA-256 has of the Input data twice

        BigInteger hashInt = new BigInteger(1,hash); // Convert the resulting hash value back to a BigInteger

        return hashInt.compareTo(target) < 0; // Check if the hash value is less than the target threshold
    }

    private static byte[] computeSHA256(byte[] input) throws NoSuchAlgorithmException{

        MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Get an instance of SHA-256 MessageDigest

        return digest.digest(input); // Compute the SHA-256 hash of the input byte array
    }
}