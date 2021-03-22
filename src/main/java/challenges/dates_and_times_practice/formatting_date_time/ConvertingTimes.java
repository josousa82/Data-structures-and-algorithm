package challenges.dates_and_times_practice.formatting_date_time;

import java.time.*;

/**
 * Created by sousaJ on 11/03/2021
 * in package - challenges.dates_and_times_practice.formatting_date_time
 *
 * Converting between Instant and LocalDateTime, ZonedDateTime, and OffsetDateTime
 **/

public class ConvertingTimes {
    public static void main(String[] args) {

        // Convert between Instant and LocalDateTime—since LocalDateTime has no idea of time zone, use a zero offset UTC+0:
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        System.out.println(ldt);
        //2021-03-13T11:48:19.410861

        Instant instantLDT = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        System.out.println(instantLDT);
        //2021-03-13T11:48:19.441143Z

        // Convert between Instant and ZonedDateTime—convert an Instant UTC+0 to a Paris ZonedDateTime UTC+1:
        ZonedDateTime zdt = Instant.now().atZone(ZoneId.of("Europe/Paris"));
        System.out.println("ZonedDateTime: " + zdt);
        //ZonedDateTime: 2021-03-13T12:48:19.449454+01:00[Europe/Paris]

        // Convert between Instant and OffsetDateTime—specify an offset of 2 hours:
        OffsetDateTime odt = Instant.now().atOffset(ZoneOffset.of("+02:00"));
        System.out.println("odt = " + odt);
        // odt = 2021-03-13T13:48:19.465953+02:00 NOTE: +2 hours

        Instant instantODT = LocalDateTime.now().atOffset(ZoneOffset.of("+02:00")).toInstant();

        System.out.println("instantODT = " + instantODT);
        // instantODT = 2021-03-13T09:48:19.466668Z NOTE: that hour is -2 instead of +2


    }
}