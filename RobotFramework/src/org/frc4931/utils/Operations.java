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
     * @param a the first value
     * @param b the second value
     * @return {@code 0} if both values are within a tolerance of each other; {@code 1} if {@code a} is greater than {@code b};
     *         {@code -1} if {@code b} is greater than {@code a}
     */
    public static int fuzzyCompare(double a, double b) {
        return fuzzyCompare(a, b, DEFAULT_NUMBER_OF_BITS);
    }

    /**
     * Compares two floating point numbers with a tolerance.
     * 
     * @param a the first value
     * @param b the second value
     * @param bits the number of bits of precision
     * @return {@code 0} if both values are within {@code tolerance} of each other; {@code 1} if {@code a} is greater than
     *         {@code b}; {@code -1} if {@code b} is greater than {@code a}
     */
    public static int fuzzyCompare(double a, double b, int bits) {
        return fuzzyCompare(a, b, calcTolerance(bits));
    }

    private static double calcTolerance(int bits) {
        return 1.0 / (1 << bits);
    }

    /**
     * Compares two floating point numbers with a tolerance.
     * 
     * @param a the first value
     * @param b the second value
     * @param tolerance the smallest delta that is still considered equal
     * @return {@code 0} if both values are within {@code tolerance} of each other; {@code 1} if {@code a} is greater than
     *         {@code b}; {@code -1} if {@code b} is greater than {@code a}
     */
    public static int fuzzyCompare(double a, double b, double tolerance) {
        double difference = Math.abs(a - b);

        if (difference <= tolerance) return 0;
        if (a > b) return 1;
        assert a < b;
        return -1;
    }

    /**
     * Limit values to between 0.0 and 1.0, treating any input value less than the minimum as 0.
     * 
     * @param maximum the maximum allowed value; must be positive or equal to zero
     * @param num the input value
     * @param minimum the minimum value below which 0.0 is used; must be positive or equal to zero
     * @return the positive limited output value
     */
    public static double positiveLimit(double maximum, double num, double minimum) {
        assert maximum >= 0.0;
        num = Math.abs(num);
        if (num > maximum) {
            return 1.0;
        }
        return Math.abs(num) > minimum ? num : 0.0;
    }

    /**
     * Limit values to between -1.0 and +1.0, inclusive.
     * 
     * @param maximum the maximum allowed value; must be positive or equal to zero
     * @param num the input value; may be any value
     * @param minimum the minimum value below which 0.0 is used; must be positive or equal to zero, but less than or equal to maximum
     * @return the limited output value
     */
    public static double limit(double maximum, double num, double minimum) {
        assert maximum >= 0.0;
        assert minimum >= 0.0;
        assert maximum >= minimum;
        if (num > maximum) {
            return 1.0;
        }
        double positiveNum = Math.abs(num);
        if (positiveNum > maximum) {
            return -1.0;
        }
        return positiveNum > minimum ? num : 0.0;
    }
}
