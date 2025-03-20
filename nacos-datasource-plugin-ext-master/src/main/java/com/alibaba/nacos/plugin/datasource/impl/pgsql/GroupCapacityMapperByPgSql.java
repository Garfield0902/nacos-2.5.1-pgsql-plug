/*
 * Copyright 1999-2024 Bulain.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * OFFSETations under the License.
 */

package com.alibaba.nacos.plugin.datasource.impl.pgsql;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.NamespaceUtil;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.GroupCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The derby implementation of {@link GroupCapacityMapper}.
 *
 * @author bulain
 */
public class GroupCapacityMapperByPgSql extends AbstractMapperByPgSql implements GroupCapacityMapper {




    @Override
    public MapperResult select(MapperContext context) {
        // 替换 MySQL 反引号为 PostgreSQL 双引号
        String sql = "SELECT id, quota, \"usage\", max_size, max_aggr_count, max_aggr_size, group_id FROM group_capacity "
                + "WHERE group_id = ?";
        return new MapperResult(sql, Collections.singletonList(context.getWhereParameter(FieldConstant.GROUP_ID)));
    }

    @Override
    public MapperResult insertIntoSelect(MapperContext context) {
        List<Object> paramList = new ArrayList<>();
        paramList.add(context.getUpdateParameter(FieldConstant.GROUP_ID));
        paramList.add(context.getUpdateParameter(FieldConstant.QUOTA));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_SIZE));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_AGGR_COUNT));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_AGGR_SIZE));
        paramList.add(context.getUpdateParameter(FieldConstant.GMT_CREATE));
        paramList.add(context.getUpdateParameter(FieldConstant.GMT_MODIFIED));

        // 替换 usage 的反引号为双引号
        String sql =
                "INSERT INTO group_capacity (group_id, quota, \"usage\", max_size, max_aggr_count, max_aggr_size, gmt_create,"
                        + " gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info";
        return new MapperResult(sql, paramList);
    }

    @Override
    public MapperResult insertIntoSelectByWhere(MapperContext context) {
        // 替换 usage 的反引号并检查 WHERE 条件
        final String sql =
                "INSERT INTO group_capacity (group_id, quota, \"usage\", max_size, max_aggr_count, max_aggr_size, gmt_create,"
                        + " gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info WHERE group_id=? AND tenant_id = '"
                        + NamespaceUtil.getNamespaceDefaultId() + "'";
        List<Object> paramList = new ArrayList<>();
        paramList.add(context.getUpdateParameter(FieldConstant.GROUP_ID));
        paramList.add(context.getUpdateParameter(FieldConstant.QUOTA));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_SIZE));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_AGGR_COUNT));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_AGGR_SIZE));
        paramList.add(context.getUpdateParameter(FieldConstant.GMT_CREATE));
        paramList.add(context.getUpdateParameter(FieldConstant.GMT_MODIFIED));

        paramList.add(context.getWhereParameter(FieldConstant.GROUP_ID));

        return new MapperResult(sql, paramList);
    }

    @Override
    public MapperResult incrementUsageByWhereQuotaEqualZero(MapperContext context) {
        // 替换双引号并检查条件逻辑
        return new MapperResult(
                "UPDATE group_capacity SET \"usage\" = \"usage\" + 1, gmt_modified = ? WHERE group_id = ? AND \"usage\" < ? AND quota = 0",
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.GROUP_ID),
                        context.getWhereParameter(FieldConstant.USAGE)));
    }

    @Override
    public MapperResult incrementUsageByWhereQuotaNotEqualZero(MapperContext context) {
        return new MapperResult(
                "UPDATE group_capacity SET \"usage\" = \"usage\" + 1, gmt_modified = ? WHERE group_id = ? AND \"usage\" < quota AND quota != 0",
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.GROUP_ID)));
    }

    @Override
    public MapperResult incrementUsageByWhere(MapperContext context) {
        return new MapperResult(
                "UPDATE group_capacity SET \"usage\" = \"usage\" + 1, gmt_modified = ? WHERE group_id = ?",
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.GROUP_ID)));
    }

    @Override
    public MapperResult decrementUsageByWhere(MapperContext context) {
        return new MapperResult(
                "UPDATE group_capacity SET \"usage\" = \"usage\" - 1, gmt_modified = ? WHERE group_id = ? AND \"usage\" > 0",
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.GROUP_ID)));
    }

    @Override
    public MapperResult updateUsage(MapperContext context) {
        return new MapperResult(
                "UPDATE group_capacity SET \"usage\" = (SELECT count(*) FROM config_info), gmt_modified = ? WHERE group_id = ?",
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.GROUP_ID)));
    }

    @Override
    public MapperResult updateUsageByWhere(MapperContext context) {
        return new MapperResult(
                "UPDATE group_capacity SET \"usage\" = (SELECT count(*) FROM config_info WHERE group_id=? AND tenant_id = '"
                        + NamespaceUtil.getNamespaceDefaultId() + "')," + " gmt_modified = ? WHERE group_id= ?",
                CollectionUtils.list(context.getWhereParameter(FieldConstant.GROUP_ID),
                        context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.GROUP_ID)));
    }
    @Override
    public MapperResult selectGroupInfoBySize(MapperContext context) {
        String sql = "SELECT id, group_id FROM group_capacity WHERE id > ? OFFSET 0 LIMIT ?";
        return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter(FieldConstant.ID), context.getPageSize()));
    }
}

