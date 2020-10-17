package challenges.find_a_number_that_appears_once;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeAll
    void setUp() {
    }

    @Test
    void main() {
        System.setOut(new PrintStream(outputStream));
        Main.main(new String[]{" ", " "});
        assertEquals("test\n", outputStream.toString());
    }


    @Test
    void findUniqueNumberInArray() {
        int[] numbers = {1,2,3,4,1,2,4,3,5};
        int[] numbers1 = {1,2,3,4,4,2,4,3,5,5};
        int[] numbers2 = {1,2,3,4,1,1,4,3};
        int[] numbers3 = {1,2,3,4,1,2,4,5,5};

        assertEquals(5, Main.findUniqueNumberInArray(numbers));
        assertEquals(1, Main.findUniqueNumberInArray(numbers1));
        assertEquals(2, Main.findUniqueNumberInArray(numbers2));
        assertEquals(3, Main.findUniqueNumberInArray(numbers3));



    }

    @AfterAll
    public void restoreStreams(){
        System.setOut(originalOut);
    }



}