package com.alibaba.nacos.plugin.datasource.impl.pgsql;

import com.alibaba.nacos.plugin.datasource.constants.DataSourceConstant;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.plugin.datasource.enums.pgsql.TrustedPgsqlFunctionEnum;

/**
 * The abstract pgsql mapper contains CRUD methods.
 *
 * @author bulain
 **/
public abstract class AbstractMapperByPgSql extends AbstractMapper {

    @Override
    public String getFunction(String functionName) {
        return TrustedPgsqlFunctionEnum.getFunctionByName(functionName);
    }

    @Override
    public String getDataSource() {
        return DataSourceConstant.PGSQL;
    }

}
