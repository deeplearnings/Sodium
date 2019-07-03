package net.onebean.sodium.service.impl;

import net.onebean.sodium.dao.CodeDatabaseTableDao;
import net.onebean.sodium.model.CodeDatabaseField;
import net.onebean.sodium.model.CodeDatabaseTable;
import net.onebean.sodium.service.CodeDatabaseFieldService;
import net.onebean.sodium.service.CodeDatabaseTableService;
import net.onebean.core.BaseBiz;
import net.onebean.core.Condition;
import net.onebean.util.CollectionUtil;
import net.onebean.util.PropUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodeDatabaseTableServiceImpl extends BaseBiz<CodeDatabaseTable, CodeDatabaseTableDao> implements CodeDatabaseTableService {

    @Autowired
    private CodeDatabaseFieldService codeDatabaseFieldService;

    @Override
    public List<String> findDatabaseTableList() {
        return baseDao.findDatabaseTableList(PropUtil.getInstance().getConfig("spring.datasource.databaseName",PropUtil.PUBLIC_CONF_JDBC));
    }

    @Override
    public void deleteChildList(Object id) {
        Condition condition = Condition.parseModelCondition("tableId@int@eq$");
        condition.setValue(id);
        List<CodeDatabaseField> childList = codeDatabaseFieldService.find(null,condition);
        if(CollectionUtil.isNotEmpty(childList)){
            List<Long> ids = new ArrayList<>();
            for (CodeDatabaseField codeDatabaseField : childList) {
                ids.add(codeDatabaseField.getId());
            }
            codeDatabaseFieldService.deleteByIds(ids);
        }
    }
}