package com.ad.ecom.common.stub;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
