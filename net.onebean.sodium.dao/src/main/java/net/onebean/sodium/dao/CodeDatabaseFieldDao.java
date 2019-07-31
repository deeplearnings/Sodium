package net.onebean.sodium.dao;
import net.onebean.core.base.BaseDao;
import net.onebean.sodium.model.CodeDatabaseField;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CodeDatabaseFieldDao extends BaseDao<CodeDatabaseField> {
    /**
     * 根据表名和数据库名查出所有字段
     * @param databaseName
     * @param tableName
     * @return
     */
    List<CodeDatabaseField> findAllTableFieldbyTableName(@Param("databaseName")String databaseName,@Param("tableName")String tableName);
}