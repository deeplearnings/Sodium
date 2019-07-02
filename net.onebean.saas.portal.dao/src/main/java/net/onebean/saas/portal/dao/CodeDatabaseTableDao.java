package net.onebean.saas.portal.dao;
import com.eakay.core.BaseDao;
import net.onebean.saas.portal.model.CodeDatabaseTable;
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