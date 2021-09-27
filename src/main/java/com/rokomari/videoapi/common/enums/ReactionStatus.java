package com.rokomari.videoapi.common.enums;

import com.rokomari.videoapi.common.utils.AbstractEnumConverter;
import com.rokomari.videoapi.common.utils.EnumUtils;
import com.rokomari.videoapi.common.utils.PersistableEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum ReactionStatus implements PersistableEnum<Integer> {
    like(1, "Like"),
    dislike(2,"Dislike");

    private static final Map<Integer, ReactionStatus> REACTION_STATUS_MAP = new HashMap<>();

    static {
        for (ReactionStatus e : values()) {
            REACTION_STATUS_MAP.put(e.value, e);
        }
    }

    private final int value;
    private final String  reactionStatus;

    ReactionStatus(int value, String status) {
        this.value = value;
        this.reactionStatus = status;
    }

    private final static Function<String, ReactionStatus> func =
            EnumUtils.lookupMap(ReactionStatus.class, e -> e.name());

    public static ReactionStatus get(String str)
    {
        return func.apply(str);
    }

    public static String name(ReactionStatus ReactionStatus) {
        return ReactionStatus == null ? null : ReactionStatus.name();
    }

    public static ReactionStatus getReactionStatus(int id) {
        return REACTION_STATUS_MAP.get(id);
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public String getReactionStatus() {
        return reactionStatus;
    }


    public static class Converter extends AbstractEnumConverter<ReactionStatus, Integer> {
        public Converter() {
            super(ReactionStatus.class);
        }
    }
}
