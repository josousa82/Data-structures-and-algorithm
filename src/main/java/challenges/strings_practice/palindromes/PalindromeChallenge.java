package challenges.strings_practice.palindromes;

import java.util.Objects;

/**
 * Created by sousaJ on 18/10/2020
 * in package - challenges.strings_practice.palindromes
 **/
public class PalindromeChallenge {

    public static boolean isStringPalindrome(String str){
        if(Objects.isNull(str) || str.isEmpty()){
            return true;
        }
        return str.equals(new StringBuffer(str).reverse().toString());
    }
}
