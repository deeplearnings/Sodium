package net.onebean.sodium.service;
import net.onebean.sodium.model.CodeDatabaseField;
import net.onebean.core.IBaseBiz;

import java.util.List;

public interface CodeDatabaseFieldService extends IBaseBiz<CodeDatabaseField> {

    /**
     * 根据表名和数据库名查出所有字段
     * @param tablename
     * @return
     */
    List<CodeDatabaseField> findAllTableFieldbyTableName(String tablename);
}