package org.frc4931.utils;

/**
 * Utility class for useful mathematical operations.
 * 
 * @author Zach Anderson
 *
 */
public class Operations {
    private static final double DOUBLE_COMPARE_TOLERANCE = 1e-6;

    /**
     * Compares two floating point numbers with a tolerance.
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
        double difference = Math.abs(a - b);
        double max = Math.max(Math.abs(a), Math.abs(b));

        if (a == 0.0 || b == 0.0)
            if (a == b || difference <= DOUBLE_COMPARE_TOLERANCE)
                return 0;

        if (a == b || difference / max <= DOUBLE_COMPARE_TOLERANCE)
            return 0;
        else if (a > b)
            return 1;
        else if (a < b)
            return -1;
        else
            throw new IllegalStateException();
    }
}
