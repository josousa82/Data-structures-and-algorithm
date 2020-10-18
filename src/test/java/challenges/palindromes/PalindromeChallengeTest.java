package challenges.palindromes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PalindromeChallengeTest {

    @Test
    void isStringPalindromeTrue() {
        Assertions.assertTrue(PalindromeChallenge.isStringPalindrome("madam"));
        Assertions.assertTrue(PalindromeChallenge.isStringPalindrome(""));
        Assertions.assertTrue(PalindromeChallenge.isStringPalindrome(null));
    }
    @Test
    void isStringPalindromeFalse() {
        Assertions.assertFalse(PalindromeChallenge.isStringPalindrome("aabb"));
        Assertions.assertFalse(PalindromeChallenge.isStringPalindrome("race car"));
    }


}