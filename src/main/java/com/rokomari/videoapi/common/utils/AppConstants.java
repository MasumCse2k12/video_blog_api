package com.rokomari.videoapi.common.utils;


public interface AppConstants {
    String DEFAULT_PAGE_NUMBER = "0";
    String DEFAULT_PAGE_SIZE = "30";
    Integer WAITING =1 ;
    int active = 1;
    int inactive = 0;
    Integer ACCOUNT_STATUS_ACTIVE =1 ;
    String SECURITY_HEADER = "Authorization";

    int MAX_PAGE_SIZE = 50;

    String SUCCESS_MESSAGE = "Successfully updated";
    String SUCCESS = "Success";
    String FAILED = "Failed";

    String OLD_DATE_FORMAT = "MM/dd/yyyy";
    String ES_DATE_FORMAT = "yyyy-MM-dd";
    String ES_INSTANT_FORMAT = "yyyy-MM-dd HH:mm:ss";//"yyyy-MM-dd'T'HH:mm:ssXXX";//"yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";
}
