package net.onebean.saas.portal.service;
import net.onebean.saas.portal.model.CodeDatabaseField;
import com.eakay.core.IBaseBiz;

import java.util.List;

public interface CodeDatabaseFieldService extends IBaseBiz<CodeDatabaseField> {

    /**
     * 根据表名和数据库名查出所有字段
     * @param tablename
     * @return
     */
    List<CodeDatabaseField> findAllTableFieldbyTableName(String tablename);
}