package com.ad.ecom.util;

import com.ad.ecom.common.dto.EComDate;

import java.sql.Date;
import java.util.Calendar;
import java.util.Optional;

public final class DateConverter {

    public static EComDate convertToECcmDate(Date sqlDate) {
        if(sqlDate == null) return null;
        EComDate ecomDate = new EComDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(sqlDate);
        ecomDate.setYear(cal.get(Calendar.YEAR));
        ecomDate.setMonth(cal.get(Calendar.MONTH));
        ecomDate.setDay(cal.get(Calendar.DAY_OF_MONTH));
        ecomDate.setHour(cal.get(Calendar.HOUR_OF_DAY));
        ecomDate.setMinute(cal.get(Calendar.MINUTE));
        ecomDate.setSecond(cal.get(Calendar.SECOND));
        return ecomDate;
    }

    public static Date convertToDate(EComDate ecomDate) {
        if(ecomDate == null) return null;
        Calendar cal = Calendar.getInstance();
        if(Optional.ofNullable(ecomDate.getYear()).isPresent())     cal.set(Calendar.YEAR, ecomDate.getYear());
        if(Optional.ofNullable(ecomDate.getMonth()).isPresent())    cal.set(Calendar.MONTH, ecomDate.getMonth());
        if(Optional.ofNullable(ecomDate.getDay()).isPresent())      cal.set(Calendar.DAY_OF_MONTH, ecomDate.getDay());
        if(Optional.ofNullable(ecomDate.getHour()).isPresent())     cal.set(Calendar.HOUR_OF_DAY, ecomDate.getHour());
        if(Optional.ofNullable(ecomDate.getMinute()).isPresent())   cal.set(Calendar.MINUTE, ecomDate.getMinute());
        if(Optional.ofNullable(ecomDate.getSecond()).isPresent())   cal.set(Calendar.SECOND, ecomDate.getSecond());
        Date sqlDate = new Date(cal.getTimeInMillis());
        return sqlDate;
    }
}
