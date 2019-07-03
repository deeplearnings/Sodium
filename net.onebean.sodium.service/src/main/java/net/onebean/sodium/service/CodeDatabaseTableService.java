package net.onebean.sodium.service;

import net.onebean.sodium.model.CodeDatabaseTable;
import net.onebean.core.IBaseBiz;

import java.util.List;

public interface CodeDatabaseTableService extends IBaseBiz<CodeDatabaseTable> {
    /**
     * 查询数据库所有表名
     * @return
     */
    List<String> findDatabaseTableList();

    /**
     * 根据ID删除子列表
     * @param id
     */
    void deleteChildList(Object id);
}