package challenges.palindromes;

import java.util.Objects;

/**
 * Created by sousaJ on 18/10/2020
 * in package - challenges.palindromes
 **/
public class PalindromeChallenge {
    public static void main(String[] args) {

    }

    public static boolean isStringPalindrome(String str){
        if(Objects.isNull(str) || str.isEmpty()){
            return true;
        }
        return str.equals(new StringBuffer(str).reverse().toString());
    }
}
