package com.alibaba.nacos.plugin.datasource.enums.dmsql;

import java.util.HashMap;
import java.util.Map;

public enum TrustedDmsqlFunctionEnum {

    /**
     * NOW().
     */
    NOW("NOW()", "SYSDATE");

    private static final Map<String, TrustedDmsqlFunctionEnum> LOOKUP_MAP = new HashMap<>();

    static {
        for (TrustedDmsqlFunctionEnum entry : TrustedDmsqlFunctionEnum.values()) {
            LOOKUP_MAP.put(entry.functionName, entry);
        }
    }

    private final String functionName;

    private final String function;

    TrustedDmsqlFunctionEnum(String functionName, String function) {
        this.functionName = functionName;
        this.function = function;
    }

    /**
     * Get the function name.
     *
     * @param functionName function name
     * @return function
     */
    public static String getFunctionByName(String functionName) {
        TrustedDmsqlFunctionEnum entry = LOOKUP_MAP.get(functionName);
        if (entry != null) {
            return entry.function;
        }
        throw new IllegalArgumentException(String.format("Invalid function name: %s", functionName));
    }
    
}
