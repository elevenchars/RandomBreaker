import java.util.*;

public class RandomTester {
    public static void main(String[] args) {

        Random r = new Random();

        /*
        we only need 2 inputs to generate possible states of the PRNG.
        however, there is a chance that we will get multiple possible states.
        we generate more than 2 so we can check the consecutive outputs
         */
        int[] consecutiveRandoms = new int[12];
        for(int i = 0; i < consecutiveRandoms.length; i++) {
            consecutiveRandoms[i] = r.nextInt();
        }

        System.out.println(consecutiveRandoms.length + " consecutive integers from Random.nextInt():");
        System.out.println(Arrays.toString(consecutiveRandoms));

        RandomBreaker rb = new RandomBreaker();

        Set<Long> states = rb.findStates(consecutiveRandoms[0], 32, consecutiveRandoms[1], 32);
        System.out.println(states.size() + " possible state" + (states.size() == 1 ? "" : "s") + " found\n");
        long seed = 0; // final seed matching r and rb

        /*
        We can eliminate collisions (several states resulting in the same most significant upper 32 bits)
        by checking a few extra outputs
         */
        for(long state: states) {
            System.out.println("Trying State " + (state)); // see implementation in java.util.Random
            rb.setSeed(state);
            boolean success = true;
            System.out.println("\nTesting " + (consecutiveRandoms.length - 2) + " consecutive outputs");
            for(int i = 2; i < consecutiveRandoms.length; i++) {
                int predictedRandom = rb.nextInt();
                System.out.println("Expected output: " + consecutiveRandoms[i]);
                System.out.println("Predicted output: " + predictedRandom);

                if(predictedRandom != consecutiveRandoms[i]) {
                    success = false;
                    break;
                }
            }
            if(success) {
                seed = state;
                break;
            }
        }

        System.out.println("\nFound seed: " + seed);

    }
}