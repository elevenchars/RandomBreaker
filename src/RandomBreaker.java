import java.util.*;

public class RandomBreaker extends Random { // so we can reuse some code

    final long a = 0x5DEECE66DL; // value used for Random objects
    final long c = 0xBL; // https://en.wikipedia.org/wiki/Linear_congruential_generator#c_%E2%89%A0_0
    final long mod = (1L << 48) - 1; // 2^48-1 ; a % 2^b == a & 2^b-1

    /**
     * Find all of the possible states of a java.util.Random object given two inputs.
     * @param n First number
     * @param nBits number of bits in n
     * @param m Second number
     * @param mBits number of bits in m
     * @return Set of possible states.
     */
    public Set<Long> findStates(int n, int nBits, int m, int mBits) {
        /*
        when m is a power of 2 (in our case 2^48) and c != 0:
        the maximum period of our linear congruential generator is m iff m, c are relatively prime
        a-1 is divisible by all prime factors of m
        (a-1) % 4 == 0 iff m % 4 ==0
         */

        //next(n) returns the upper (most significant) n bits of the 48 bit result of (aX + c) % (mod+1)
        //n, m, are the outputs of next(nBits), next(mBits), respectively

        /*
        This gets the upper mBits of the mod mask.
        For example, if mBits = 10,
        upperMofMod = 0b111111111100000000000000000000000000000000000000
         */
        long upperMofMod = ((1L << mBits) - 1) << (48 - mBits);

        /*
        This converts n, m into 48 bit numbers by padding with 0s in the least significant positions
         */
        long incompleteNState = ((long)n << (48-nBits)) & mod;
        long incompleteMState = ((long)m << (48-mBits)) & mod;

        Set<Long> states = new HashSet<>(); // make a set of potential states

        //increment from incompleteNState to the maximum value of an nBits length number
        for(long oldSeed = incompleteNState; oldSeed <= (incompleteNState | ((1L << (48 - nBits)) - 1)); oldSeed++) {
            long newSeed = (oldSeed * a + c ) & mod;

            //if we've found a number with the same upper mBits bits as incompleteMState, this is a possible candidate
            if((newSeed & upperMofMod) == incompleteMState) {
                states.add(newSeed ^ a);
            }
        }

        return states;
    }
}