package net.onebean.sodium.dao;
import net.onebean.core.BaseDao;
import net.onebean.sodium.model.CodeDatabaseTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CodeDatabaseTableDao extends BaseDao<CodeDatabaseTable> {
    /**
     * 查询数据库所有表名
     * @param databaseName
     * @return
     */
    List<String> findDatabaseTableList(@Param("databaseName")String databaseName);

}