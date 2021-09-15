package batch3

/**
 * Problem 23: Non-Abundant Sums
 *
 * https://projecteuler.net/problem=23
 *
 * Goal: Return whether or not N can be expressed as the sum of 2 abundant numbers.
 *
 * Constraints: 0 <= N <= 1e5
 *
 * Perfect Number: the sum of its proper divisors equals itself.
 * e.g. proper_D(6) = {1,2,3}.sum() = 6
 *
 * Deficient Number: the sum of its proper divisors is less than itself.
 * e.g. proper_D(4) = {1, 2}.sum() = 3
 *
 * Abundant Number: the sum of its proper divisors exceeds itself.
 * e.g. proper_D(12) = {1,2,3,4,6}.sum() = 16
 *
 * By mathematical analysis, all integers > 28123 can be expressed as the sum
 * of 2 abundant numbers. This upper limit cannot, however, be reduced further
 * even though it is known that the largest number that cannot be expressed as
 * the sum of 2 abundant numbers is less than this upper limit.
 *
 * e.g.: N = 24
 *       smallest abundant number = 12,
 *       so smallest integer that can be expressed = 12 + 12 = 24
 *       result = True
 */

class NonAbundantSums {

}