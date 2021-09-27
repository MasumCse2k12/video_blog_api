package com.rokomari.videoapi.common.enums;



import com.rokomari.videoapi.common.utils.AbstractEnumConverter;
import com.rokomari.videoapi.common.utils.EnumUtils;
import com.rokomari.videoapi.common.utils.PersistableEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum AccountStatus implements PersistableEnum<Integer> {
    active(1, "Active"),
    inactive(2,"Inactive");

    private static final Map<Integer, AccountStatus> ACCOUNT_STATUS_MAP = new HashMap<>();

    static {
        for (AccountStatus e : values()) {
            ACCOUNT_STATUS_MAP.put(e.value, e);
        }
    }

    private final int value;
    private final String  accountStatus;

    AccountStatus(int value, String status) {
        this.value = value;
        this.accountStatus = status;
    }

    private final static Function<String, AccountStatus> func =
            EnumUtils.lookupMap(AccountStatus.class, e -> e.name());

    public static AccountStatus get(String str)
    {
        return func.apply(str);
    }

    public static String name(AccountStatus accountStatus) {
        return accountStatus == null ? null : accountStatus.name();
    }

    public static AccountStatus getAccountStatus(int id) {
        return ACCOUNT_STATUS_MAP.get(id);
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public String getAccountStatus() {
        return accountStatus;
    }
    

    public static class Converter extends AbstractEnumConverter<AccountStatus, Integer> {
        public Converter() {
            super(AccountStatus.class);
        }
    }
}
