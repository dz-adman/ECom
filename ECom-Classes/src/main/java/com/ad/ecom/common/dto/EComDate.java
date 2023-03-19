package com.ad.ecom.common.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EComDate {
    @NotNull
    private int year;
    @NotNull
    private int month;
    @NotNull
    private int day;
    private int hour;
    private int minute;
    private int second;

    public EComDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.hour = calendar.get(Calendar.HOUR);
        this.minute = calendar.get(Calendar.MINUTE);
        this.second = calendar.get(Calendar.SECOND);
    }

    public Instant toInstant() {
        ZonedDateTime zonedDateTime = LocalDateTime.of(year, month, day, hour, minute, second)
                .atZone(ZoneId.systemDefault());
        return zonedDateTime.toInstant();
    }

    public boolean eq(EComDate eComDate) {
        return this.toInstant().equals(eComDate.toInstant());
    }

    public boolean gt(EComDate eComDate) {
        return this.toInstant().isAfter(eComDate.toInstant());
    }

    public boolean gtEq(EComDate eComDate) {
        Instant thisInstant = this.toInstant(), otherInstant = eComDate.toInstant();
        return thisInstant.isAfter(otherInstant) || thisInstant.equals(otherInstant);
    }

    public boolean lt(EComDate eComDate) {
        return this.toInstant().isBefore(eComDate.toInstant());
    }

    public boolean ltEq(EComDate eComDate) {
        Instant thisInstant = this.toInstant(), otherInstant = eComDate.toInstant();
        return thisInstant.isBefore(otherInstant) || thisInstant.equals(otherInstant);
    }

}
