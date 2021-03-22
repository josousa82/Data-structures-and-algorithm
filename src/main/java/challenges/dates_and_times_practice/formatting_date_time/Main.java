package challenges.dates_and_times_practice.formatting_date_time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by sousaJ on 11/03/2021
 * in package - challenges.dates_and_times_practice.formatting_date_time
 **/
public class Main {
    public static void main(String[] args) {
        LocalDate now = LocalDate.now();
        System.out.println(now.toString());

        // LocalDateTime also has a constructor where you can pass the parameters values directly
        LocalDateTime ldt1 = LocalDateTime.of(2012, 02,16,12,54, 25);
        System.out.println("Constructor without passing LocalXXX Object : " + ldt1);
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2021, 02, 21), LocalTime.of(14, 20,59));

        System.out.println("Constructor passing LocalXXX Object : " + ldt);

        // Instant class, useful for timestamps

        Instant thisInstant = Instant.now();
        System.out.println("Instant : " + thisInstant);

        // converting string to Instant
        Instant timeStampFromString = Instant.parse("2020-05-22T14:53:23.123456789Z");
        System.out.println("Timestamp, Instant parsed from String: " + timeStampFromString);


        // Adding or subtracting time from instant
        System.out.println("In 2 hours the time is : " +Instant.now().plus(2, ChronoUnit.HOURS));
        System.out.println("In 2 Days later : " + Instant.now().plus(200000, ChronoUnit.DAYS));
        System.out.println("At 2 days the time was : " +Instant.now().minus(2, ChronoUnit.DAYS));

        // Comparing Instant Objects

        Instant timestamp1 = Instant.now();

              // timestamp1.plus(10, ChronoUnit.SECONDS)

        Instant timestamp2 = timestamp1.plusSeconds(10);

        System.out.println("isAfter : " + timestamp1.isAfter(timestamp2));
        System.out.println("isBefore : " + timestamp1.isBefore(timestamp2));

        // distance between two instants

        System.out.println("Distance in seconds from timestamp 1 to 2 = " + timestamp1.until(timestamp2, ChronoUnit.SECONDS));

    }
}
