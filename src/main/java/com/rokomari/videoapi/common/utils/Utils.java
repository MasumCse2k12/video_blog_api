package com.rokomari.videoapi.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Utils {
    public static final String OPERATION_SUCESS = "SUCCESS";
    public static final String OPERATION_FAILED = "FAILED";
    public static final Integer DEFAULT_LIMIT = 100;


    public static Integer getIntegerFromObject(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Integer) {
            return ((Integer) object).intValue();
        } else if (object instanceof Short) {
            return ((Short) object).intValue();
        } else if (object instanceof Long) {
            return ((Long) object).intValue();
        } else if (object instanceof BigDecimal) {
            return ((BigDecimal) object).intValue();
        } else if (object instanceof BigInteger) {
            return ((BigInteger) object).intValue();
        } else if (object instanceof Byte) {
            return new Integer((Byte) object);
        }else if (object instanceof Double) {
            return ((Double) object).intValue();
        } else if(object instanceof String) {
            try {
                return Integer.parseInt((String) object);
            } catch (Throwable t) {
                return 0;
            }
        } else {
            return 0;
        }
    }


    public static String getFormattedDate(String format, Timestamp t) {
        if (t == null) {
            return "";
        }

        if (isEmpty(format)) {
            format = "dd/MM/yyyy";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(t.getTime()));
    }

    public static Timestamp getStringToTimestamp(String stringDate) {
        Timestamp dateAsTimestamp = null;
        try {
            DateFormat formatter;
            Date date;
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            date = formatter.parse(stringDate);
            dateAsTimestamp = new Timestamp(date.getTime());
        } catch (Exception x) {
            x.printStackTrace();
        }
        return dateAsTimestamp;
    }

    public static String getDateAsString(Date givenDate) {

        String dateAsString = "";
        try {
            SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat("dd/MM/yyyy");
            StringBuilder nowMMDDYYYY = new StringBuilder(dateformatMMDDYYYY.format(givenDate));
            dateAsString = nowMMDDYYYY.toString();
        } catch (Exception x) {
            x.printStackTrace();
        }
        return dateAsString;
    }

    public static String getDateToString(Date date) {
        if (date == null) {
            return "00-00-0000";
        }

        return (new SimpleDateFormat(Defs.DATE_FORMAT)).format(date);
    }

    public static String getDateToFormatString(Date date, String pattern) {
        if (date == null || pattern == null) {
            return "00-00=-0000";
        }

        return (new SimpleDateFormat(pattern)).format(date);
    }

    public static String getTimestampToString(Timestamp ts) {
        if (ts == null) {
            return "00-00-0000";
        }

        return (new SimpleDateFormat(Defs.DATE_FORMAT)).format(new Date(ts.getTime()));
    }

    public static boolean isDateEqual(Date d1, Date d2) {
        if (d1 == null && d2 == null) {
            return true;
        }
        if (d1 == null || d2 == null) {
            return false;
        }

        return getDateToString(d1).equalsIgnoreCase(getDateToString(d2));
    }

    public static boolean isDateEqual(Date d, Timestamp t) {
        if (d == null && t == null) {
            return true;
        }
        if (d == null || t == null) {
            return false;
        }

        return getDateToString(d).equalsIgnoreCase(getTimestampToString(t));
    }

    public static boolean isDateEqual(Timestamp t, Date d) {
        return isDateEqual(d, t);
    }

    public static boolean isDateEqual(Timestamp t1, Timestamp t2) {
        return getTimestampToString(t1).equalsIgnoreCase(getTimestampToString(t2));
    }

    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isInList(String s, List<String> args, boolean ignoreCase) {
        if (s == null) {
            return false;
        }

        if (Utils.isEmptyStringList(args)) {
            return false;
        }

        for (String arg : args) {
            if (arg == null) {
                continue;
            }

            if (ignoreCase && arg.equalsIgnoreCase(s)) {
                return true;
            } else if (arg.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInList(String s, Object... args) {
        if (s == null) {
            return false;
        }

        if (args.length < 1) {
            return false;
        }

        for (int i = 0; i < args.length; i++) {

            System.out.println(args[i]);
            if (args[i] == null) {
                continue;
            }

            if (args[i].toString().equals(s)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInList(Integer l, Integer... args) {
        if (l == null || l == 0L) {
            return false;
        }

        if (args.length < 1) {
            return false;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i] == null || ((Integer) args[i] == 0L)) {
                continue;
            }

            if (((Integer) args[i]).equals(l)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInListIgnoreCase(String s, Object... args) {
        if (s == null) {
            return false;
        }
        if (args.length < 1) {
            return false;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                continue;
            }

            if (args[i].toString().equalsIgnoreCase(s)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInList(Long s, Long... args) {
        if (s == null) {
            return false;
        }
        if (args.length < 1) {
            return false;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                continue;
            }
            if (args[i].longValue() == s.longValue()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInList(Short s, Short... args) {
        if (s == null) {
            return false;
        }
        if (args.length < 1) {
            return false;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                continue;
            }
            if (args[i].shortValue() == s.shortValue()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmpty(String s) {
        return ((s == null) || s.isEmpty());
    }

    public static boolean isEmpty(List list) {
        return ((list == null) || list.isEmpty());
    }

    public static boolean isNull(Short value) {

        if (value == null) {
            return true;
        }

        if (value <= 0) {
            return true;
        }

        return false;
    }

    public static boolean isNull(Float value) {

        if (value == null) {
            return true;
        }

//        if (value <= 0) {
//            return true;
//        }
        return false;
    }

    public static boolean isNull(BigInteger value) {

        if (value == null) {
            return true;
        }

//        if (value.toString().equals("0")) {
//            return true;
//        }
        return false;
    }

    public static boolean isNull(Long value) {
        if (value == null) {
            return true;
        }

        return false;
    }

    public static boolean isNull(byte[] byteData) {
        if (byteData == null || byteData.length == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Integer integer) {
        if (integer == null) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNull(BigDecimal number) {
        if (number == null) {
            return true;
        }
        return false;
    }

    public static Long getLong(Object s) {
        if (s == null || s.toString().length() < 1) {
            return null;
        }

        try {
            Long l = Long.parseLong(s.toString());
            return l;
        } catch (Exception e) {
            return null;
        }
    }

    public static Object compareDates(Date date1, Date date2) {
        try {
            return dateWithoutTime(date1).compareTo(dateWithoutTime(date2));
        } catch (Exception e) {
            return null;
        }
    }

    public static Object compareWithCurrentDate(Date date) {
        try {
            return dateWithoutTime(date).compareTo(dateWithoutTime(new Date()));
        } catch (Exception e) {
            return null;
        }
    }

    public static Date dateWithoutTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    public static Timestamp dateToTimeStamp(Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }

    public static Date getDateParam(Date date, boolean maxTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (maxTime) {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
        return calendar.getTime();
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(date);
    }

    public static String formatDateAndTime(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh.mm.ss aa");
        return df.format(date);
    }

    public static Integer getKeyFromMap(String val, HashMap<Integer, String> HASH_MAP) {

        if (val == null || HASH_MAP == null || HASH_MAP.size() == 0) {
            return 0;
        }

        for (Map.Entry<Integer, String> e : HASH_MAP.entrySet()) {
            if (e == null) {
                continue;
            }

            if (e.getValue().equalsIgnoreCase(val.trim())) {

                return e.getKey();
            }
        }

        return 0;
    }


    public static boolean isEmptyObjectList(List<Object> objectList) {
        if (objectList == null || objectList.size() <= 0) {
            return true;
        }

        int sizeOfList = objectList.size();
        int countEmpty = 0;

        for (Object object : objectList) {
            if (object == null) {
                countEmpty++;
            }
        }

        if (countEmpty == sizeOfList) {
            return true;
        }

        return false;
    }

    public static boolean isEmptyStringList(List<String> stringList) {
        if (stringList == null || stringList.size() <= 0) {
            return true;
        }

        int sizeOfList = stringList.size();
        int countEmpty = 0;

        for (String str : stringList) {
            if (str == null) {
                countEmpty++;
            }
        }

        if (countEmpty == sizeOfList) {
            return true;
        }

        return false;
    }

    public static boolean isEmptyLongList(List<Long> intList) {
        if (intList == null || intList.size() <= 0) {
            return true;
        }

        int sizeOfList = intList.size();
        int countEmpty = 0;

        for (Long intVal : intList) {
            if (intVal == null) {
                countEmpty++;
            }
        }

        if (countEmpty == sizeOfList) {
            return true;
        }

        return false;
    }


    public static Long getLong(Integer value) {
        if (isNull(value)) {
            return 0L;
        }
        try {
            return new Long(value);
        } catch (Throwable t) {
            return 0L;
        }
    }

    public static List<Object> getIdListInt(List<Integer> values) {
        if (values == null || values.size() <= 0) {
            return null;
        }

        List<Object> valueList = new ArrayList<Object>();
        for (Integer id : values) {
            if (isNull(id)) {
                continue;
            }
            valueList.add(id);
        }
        return valueList;
    }

    public static List<Object> getIdList(List<Long> values) {
        if (values == null || values.size() <= 0) {
            return null;
        }

        List<Object> valueList = new ArrayList<Object>();
        for (Long id : values) {
            if (isNull(id)) {
                continue;
            }
            valueList.add(id);
        }
        return valueList;
    }

    public static List<Long> getIdList(List<BigDecimal> values, boolean isLong) {
        if (values == null || values.size() <= 0) {
            return null;
        }

        List<Long> valueList = new ArrayList<Long>();
        for (BigDecimal id : values) {
            if (isNull(id)) {
                continue;
            }
            valueList.add(id.longValue());
        }
        return valueList;
    }

    public static String toUpper(String value, boolean isTrim) {
        if (isEmpty(value)) {
            return null;
        }

        if (isTrim) {
            return value.trim().toUpperCase();
        }

        return value.toUpperCase();
    }


    /**
     * if anyone is null, then not equal.
     *
     * @param first
     * @param second
     * @return
     */
    public static boolean isEqual(String first, String second, boolean ignoreCase) {
        if (isEmpty(first) && isEmpty(second)) {
            return true;
        }
        if (isEmpty(first) || isEmpty(second)) {
            return false;
        }
        if (ignoreCase) {
            return first.equalsIgnoreCase(second);
        }
        return first.equals(second);
    }

    public static boolean isEqual(Integer first, Integer second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        return (first.intValue() == second.intValue());
    }

    public static boolean isEqual(byte[] first, byte[] second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        return isEqual(md5(first), md5(second), false);
    }

    public static String md5(byte[] arr) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(arr);
            String md5Str = new BigInteger(digest.digest()).toString(16);
            return md5Str;
        } catch (NoSuchAlgorithmException | NullPointerException e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static boolean isEqual(Long first, Long second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        return (first.intValue() == second.intValue());
    }

    public static boolean isEqual(Short first, Short second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        return (first.shortValue() == second.shortValue());
    }

    public static boolean isEqual(BigDecimal first, BigDecimal second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        return (first.compareTo(second) == 0 ? true : false);
    }

    public static boolean isEqual(BigInteger first, BigInteger second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }

        return (first.compareTo(second) == 0 ? true : false);
    }

    public static Integer getInteger(Long value) {
        if (value == null) {
            return null;
        }
        try {
            return value.intValue();
        } catch (Exception e) {

        }

        return 0;
    }


    public static Integer getInteger(Object obj){
        if(obj instanceof String){
            try{
                String str= (String) obj;
                Integer no=Integer.parseInt(str);
                return no;
            }catch (Exception e){
                return null;
            }
        }else if(obj instanceof Long){
            Long data= (Long) obj;
            return data.intValue();
        }
        else if(obj instanceof BigInteger){
            BigInteger data= (BigInteger) obj;
            return data.intValue();
        }
        return null;
    }




    public static boolean isOk(Integer value) {
        return !(value == null || value.intValue() <= 0);
    }
    public static boolean isOk(Date value) {
        return !(value == null);
    }

    public static boolean isOk(Short value) {
        return !(value == null || value <= 0);
    }

    public static boolean isOk(Long value) {
        return !(value == null || value.longValue() <= 0);
    }

    public static boolean isOk(BigInteger value) {
        return !(value == null || value.intValue() <= 0);
    }

    public static boolean isOk(String str) {
        return !(str == null || str.isEmpty());
    }

    public static Date getStringToDate(String date, String format) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(format);

        try {
            return df.parse(date.trim());
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }


    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(Defs.DATE_FORMAT);
        return df.format(date);
    }

    public static String getReportStringFromDate(Date date) {
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date getStringToDate(String date) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(Defs.DATE_FORMAT);

        try {
            return df.parse(date);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static final String[] getValueFromRequest(String paramName, HttpServletRequest req) {
        if (isOk(paramName) && req != null) {
            return req.getParameterValues(paramName.trim());
        }
        return null;
    }

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };

    public static String getClientIpAddress(HttpServletRequest request) {
        if (request == null) return "No IP Found";
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    public static String withLargeIntegers(double value) {
        NumberFormat df = NumberFormat.getInstance();
        df.setGroupingUsed(true);
        return df.format(value);
    }

    public static String dateStringFromTimeZone(Date date, String userTimeZone) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            dateFormat.setTimeZone(TimeZone.getTimeZone(userTimeZone));
            return dateFormat.format(date);
        } catch (Throwable t) {
            return "";
        }
    }

    public static boolean isInListList(List<Integer> ops,Integer ...values) {
        if(ops == null || ops.size() ==0) {
            return false;
        }
        if(values == null || values.length == 0) {
            return false;
        }

        for (Integer op : ops) {
            if(!isOk(op)) {
                continue;
            }
            for (Integer val : values) {
                if(!isOk(val)) {
                    continue;
                }
                if(val.equals(op)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isOk(Enum value) {
        return !(value == null);
    }

    public static boolean isOk(Boolean value) {
        return !(value == null || value.booleanValue() == false);
    }

    public static boolean isOk(LocalDate date) {
        return !(date == null);
    }

    public static boolean isOk(Instant date) {
        return !(date == null);
    }

    public static String getInstantAsStringFormat(Instant instant) {
        String str = "";
        if (instant != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.ES_INSTANT_FORMAT)
                        .withZone(ZoneId.systemDefault());
                str = formatter.format(instant);

            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        return str;
    }

    public static String getStringFromInstant(Instant instant) {
        if(instant == null) {
            return null;
        }
        try {
            Date myDate = Date.from(instant);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.format(myDate);
        } catch (Throwable t) {
            t.printStackTrace();
            return "";
        }
    }

    public static String getStringFromInstant(Instant instant, String format) {
        if(instant == null) {
            return null;
        }
        try {
            Date myDate = Date.from(instant);
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(myDate);
        } catch (Throwable t) {
            t.printStackTrace();
            return "";
        }
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        if (dateToConvert != null) {
            return dateToConvert.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
        return null;
    }

    public static LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDate getLocalDateFromStringFormat(String date) {
        if (!isOk(date)) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.ES_DATE_FORMAT);
            return LocalDate.parse(date, formatter);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    public static String getDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getDateOnly(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static Date datefromString(String date) throws ParseException {
        final String NEW_FORMAT = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.OLD_DATE_FORMAT);
        Date d = sdf.parse(date);
        sdf.applyPattern(NEW_FORMAT);
        sdf.format(d);
        return d;
    }

    public static final LocalDate getLocalDate(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof java.sql.Date) {
            return ((java.sql.Date) object).toLocalDate();
        }
        if (object instanceof Date) {
            return new java.sql.Date(((Date) object).getTime()).toLocalDate();
        }
        if (object instanceof String) {
            return getLocalDateFromStringFormat((String) object);
        }
        return null;
    }

    public static Instant getInstant(Object object) {
        if(object instanceof Instant) {
            return (Instant) object;
        }
        LocalDate date = getLocalDate(object);
        if(object != null) {
            return date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        return null;
    }

    public static final boolean getBooleanFromObject(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof Boolean) {
            return ((Boolean) object).booleanValue();
        }
        if (object instanceof Integer) {
            if (Utils.getIntegerFromObject(object) == 1) {
                return true;
            } else {
                return false;
            }
        }
        if (object instanceof Long) {
            if (((Long) object).intValue() == 1) {
                return true;
            } else {
                return false;
            }
        }
        if (object instanceof Short) {
            if (((Short) object).intValue() == 1) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private static boolean isValidDateFormat(String date) {

        /*
         * Regular Expression that matches String with format dd/MM/yyyy.
         * dd -> 01-31
         * MM -> 01-12
         * yyyy -> 4 digit number
         */
        String pattern = "(0?[1-9]|[12][0-9]|3[01])\\/(0?[1-9]|1[0-2])\\/([0-9]{4})";
        boolean result = false;
        if (date.matches(pattern)) {
            result = true;
        }
        return result;
    }


    public static Date dateFromLocalDate(LocalDate ldate) {
        return Date.from(ldate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static boolean different(String oldValue, String newValue) {
        if (isOk(newValue) && isOk(oldValue)) {
            return !newValue.equalsIgnoreCase(oldValue);
        } else if (isOk(newValue) || isOk(oldValue)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean different(LocalDate oldValue, LocalDate newValue) {
        if (isOk(newValue) && isOk(oldValue)) {
            return newValue.compareTo(oldValue) != 0;
        } else if (isOk(newValue) || isOk(oldValue)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean different(Instant oldValue, Instant newValue) {
        if (isOk(newValue) && isOk(oldValue)) {
            return newValue.compareTo(oldValue) != 0;
        } else if (isOk(newValue) || isOk(oldValue)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean different(Enum oldValue, Enum newValue) {
        if (isOk(newValue) && isOk(oldValue)) {
            return !newValue.name().equals(oldValue.name());
        } else if (isOk(newValue) || isOk(oldValue)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean different(Integer oldValue, Integer newValue) {
        if (isOk(newValue) && isOk(oldValue)) {
            return oldValue.intValue() != newValue.intValue();
        } else if (isOk(newValue) || isOk(oldValue)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean different(Object oldValue, Object currentValue) {
        if (isOk(oldValue) && isOk(currentValue)) {
            if (oldValue instanceof Instant) {
                return different((Instant) oldValue, (Instant) currentValue);
            } else if (oldValue instanceof String) {
                return different((String) oldValue, (String) currentValue);
            } else if (oldValue instanceof Integer) {
                return different((Integer) oldValue, (Integer) currentValue);
            } else if (oldValue instanceof LocalDate) {
                return different((LocalDate) oldValue, (LocalDate) currentValue);
            } else if (oldValue instanceof Enum) {
                return different((Enum) oldValue, (Enum) currentValue);
            } else {
                return false;
            }
        } else if (isOk(oldValue) || isOk(currentValue)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isOk(Object value) {
        return !(value == null);
    }

    public static boolean isOk(byte[] value) {
        return !(value == null);
    }

    public static String exception(Throwable t) {
        if (t != null) {
            t.printStackTrace();
            return t.getMessage();
        } else {
            return null;
        }
    }

    public static String javaObjectToJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }


    public static String getLocalDateAsString(LocalDate date) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.ES_DATE_FORMAT);
        return date.format(formatter);
    }

    public static boolean valueExists(Object[] objecta, int index) {
        if (objecta == null) {
            return false;
        }
        return objecta.length >= (index + 1) && objecta[index] != null;
    }

    public static final String listToString(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        ids.stream().filter(id -> isOk(id)).forEach(id -> {
            sb.append(id);
            sb.append(',');
        });
        if(sb.length() >1) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String stringListToString(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        values.stream().filter(id -> isOk(id)).forEach(id -> {
            sb.append(id);
            sb.append(',');
        });
        if(sb.length() >1) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public static final String listToString(Integer... ids) {
        if (ids == null || ids.length <=0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Integer id : ids) {
            if(!isOk(id)) {
                continue;
            }
            sb.append(id);
            sb.append(',');
        }
        if(sb.length() >1) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String datetoReportString(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return "";
        }
    }

    public static Instant getInstantFromDate(Date date) {
        if (date == null)
            return null;
        return date.toInstant();
    }

    public static String getYearMonth() {
        DateFormat df = new SimpleDateFormat("yyMM");
        return df.format(new Date());
    }



    public static boolean validNameEn(String nameEn) {
        return (isOk(nameEn) && nameEn.matches("^([a-zA-Z]+[-.]?[ ]?)+$"));
    }


    public static boolean isDigitOnly(String digit) {
        return (isOk(digit) && digit.matches("[0-9]+"));
    }

}