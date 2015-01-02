package org.frc4931.utils;

/**
 * Utility class for useful mathematical operations.
 * 
 * @author Zach Anderson
 *
 */
public final class Operations {
	public static final int DEFAULT_NUMBER_OF_BITS = 12;
    
    /**
     * Compares two floating point numbers with the {@link #DEFAULT_NUMBER_OF_BITS}.
     * 
     * @param a
     *            the first value
     * @param b
     *            the second value
     * @return {@code 0} if both values are within a tolerance of each other;
     *         {@code 1} if {@code a} is greater than {@code b}; {@code -1} if
     *         {@code b} is greater than {@code a}
     */
    public static int fuzzyCompare(double a, double b) {
        return fuzzyCompare(a, b, DEFAULT_NUMBER_OF_BITS);
    }
    
    /**
     * Compares two floating point numbers with a tolerance.
     * 
     * @param a
     *            the first value
     * @param b
     *            the second value
     * @param bits
     *            the number of bits of precision
     * @return {@code 0} if both values are within {@code tolerance} of each other;
     *         {@code 1} if {@code a} is greater than {@code b}; {@code -1} if
     *         {@code b} is greater than {@code a}
     */
    public static int fuzzyCompare(double a, double b, int bits) {
 	  return fuzzyCompare(a, b, calcTolerance(bits));
    }
    
    private static double calcTolerance(int bits){
		   return 1.0/(1<<bits);
	}

	/**
     * Compares two floating point numbers with a tolerance.
     * 
     * @param a
     *            the first value
     * @param b
     *            the second value
     * @param tolerance
     *            the smallest delta that is still considered equal
     * @return {@code 0} if both values are within {@code tolerance} of each other;
     *         {@code 1} if {@code a} is greater than {@code b}; {@code -1} if
     *         {@code b} is greater than {@code a}
     */
    public static int fuzzyCompare(double a, double b, double tolerance) {
        double difference = Math.abs(a - b);

        if (difference<=tolerance) return 0;
        if (a > b) return 1;
        assert a<b;
        return -1;
    }
}
