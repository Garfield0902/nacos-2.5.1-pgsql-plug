package com.alibaba.nacos.plugin.datasource.impl.pgsql;

import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoGrayMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

import java.util.Collections;

/**
 * @author zhangwc
 * @date 2025/3/19 11:43
 */
public class ConfigInfoGrayMapperByPgSql  extends AbstractMapperByPgSql implements ConfigInfoGrayMapper {

    @Override
    public MapperResult findAllConfigInfoGrayForDumpAllFetchRows(MapperContext context) {
        String sql = "SELECT id,data_id,group_id,tenant_id,gray_name,gray_rule,app_name,content,md5,gmt_modified "
                + "FROM config_info_gray ORDER BY id LIMIT " + context.getPageSize() + " OFFSET "
                + context.getStartRow();
        return new MapperResult(sql, Collections.emptyList());
    }
}
