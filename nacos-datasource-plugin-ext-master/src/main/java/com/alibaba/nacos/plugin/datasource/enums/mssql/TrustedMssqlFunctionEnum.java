package com.alibaba.nacos.plugin.datasource.enums.mssql;

import java.util.HashMap;
import java.util.Map;

public enum TrustedMssqlFunctionEnum {

    /**
     * NOW().
     */
    NOW("NOW()", "GETDATE()");

    private static final Map<String, TrustedMssqlFunctionEnum> LOOKUP_MAP = new HashMap<>();

    static {
        for (TrustedMssqlFunctionEnum entry : TrustedMssqlFunctionEnum.values()) {
            LOOKUP_MAP.put(entry.functionName, entry);
        }
    }

    private final String functionName;

    private final String function;

    TrustedMssqlFunctionEnum(String functionName, String function) {
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
        TrustedMssqlFunctionEnum entry = LOOKUP_MAP.get(functionName);
        if (entry != null) {
            return entry.function;
        }
        throw new IllegalArgumentException(String.format("Invalid function name: %s", functionName));
    }
    
}
