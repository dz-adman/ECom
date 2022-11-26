package com.ad.ecom.common.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
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
    public boolean eq(EComDate eComDate) {
        return this.getYear() == eComDate.getYear() && this.getMonth() == eComDate.getMonth() && this.getDay() == eComDate.getDay()
                && this.getHour() == eComDate.getHour() && this.getMinute() == eComDate.getMinute() && this.getSecond() == eComDate.getSecond();
    }

    public boolean gt(EComDate eComDate) {
        if (this.getYear() > eComDate.getYear()) return true;
        else if (this.getYear() == eComDate.getYear()) {
            if (this.getMonth() > eComDate.getMonth()) return true;
            else if (this.getMonth() == eComDate.getMonth()) {
                if (this.getDay() > eComDate.getDay()) return true;
                else if (this.getDay() == eComDate.getDay()) {
                    if (this.getHour() > eComDate.getHour()) return true;
                    else if (this.getHour() == eComDate.getHour()) {
                        if (this.getMinute() > eComDate.getMinute()) return true;
                        else if (this.getMinute() == eComDate.getMinute()) {
                            return this.getSecond() > eComDate.getSecond();
                        } else return false;
                    } else return false;
                } else return false;
            } else return false;
        } else return false;
    }

    public boolean gtEq(EComDate eComDate) {
        if (this.getYear() > eComDate.getYear()) return true;
        else if (this.getYear() == eComDate.getYear()) {
            if (this.getMonth() > eComDate.getMonth()) return true;
            else if (this.getMonth() == eComDate.getMonth()) {
                if (this.getDay() > eComDate.getDay()) return true;
                else if (this.getDay() == eComDate.getDay()) {
                    if (this.getHour() > eComDate.getHour()) return true;
                    else if (this.getHour() == eComDate.getHour()) {
                        if (this.getMinute() > eComDate.getMinute()) return true;
                        else if (this.getMinute() == eComDate.getMinute()) {
                            return this.getSecond() >= eComDate.getSecond();
                        } else return false;
                    } else return false;
                } else return false;
            } else return false;
        } else return false;
    }

    public boolean lt(EComDate eComDate) {
        if (this.getYear() < eComDate.getYear()) return true;
        else if (this.getYear() == eComDate.getYear()) {
            if (this.getMonth() < eComDate.getMonth()) return true;
            else if (this.getMonth() == eComDate.getMonth()) {
                if (this.getDay() < eComDate.getDay()) return true;
                else if (this.getDay() == eComDate.getDay()) {
                    if (this.getHour() < eComDate.getHour()) return true;
                    else if (this.getHour() == eComDate.getHour()) {
                        if (this.getMinute() < eComDate.getMinute()) return true;
                        else if (this.getMinute() == eComDate.getMinute()) {
                            return this.getSecond() < eComDate.getSecond();
                        } else return false;
                    } else return false;
                } else return false;
            } else return false;
        } else return false;
    }

    public boolean ltEq(EComDate eComDate) {
        if (this.getYear() < eComDate.getYear()) return true;
        else if (this.getYear() == eComDate.getYear()) {
            if (this.getMonth() < eComDate.getMonth()) return true;
            else if (this.getMonth() == eComDate.getMonth()) {
                if (this.getDay() < eComDate.getDay()) return true;
                else if (this.getDay() == eComDate.getDay()) {
                    if (this.getHour() < eComDate.getHour()) return true;
                    else if (this.getHour() == eComDate.getHour()) {
                        if (this.getMinute() < eComDate.getMinute()) return true;
                        else if (this.getMinute() == eComDate.getMinute()) {
                            return this.getSecond() <= eComDate.getSecond();
                        } else return false;
                    } else return false;
                } else return false;
            } else return false;
        } else return false;
    }

}
