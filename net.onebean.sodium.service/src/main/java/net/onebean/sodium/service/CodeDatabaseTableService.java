package net.onebean.sodium.service;

import net.onebean.sodium.model.CodeDatabaseTable;
import net.onebean.core.base.IBaseBiz;

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

    /**
     * 保存数据库实体信息
     * @param entity 实体
     * @return bool
     */
    Boolean saveCodeDatabaseTable(CodeDatabaseTable entity);

    /**
     * 生成diamante
     * @param id 数据库实体 主键
     * @return bool
     */
    String generate(Object id);

    /**
     * 是否是重复的表
     * @param tableName 表名
     * @return bool
     */
    Boolean isRepeatTable(String tableName);

    /**
     * 删除数据库模型
     * @param id 主键
     * @return bool
     */
    Boolean deleteCodeDatabaseTable(Object id);
}