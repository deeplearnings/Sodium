package net.onebean.sodium.service.impl;

import net.onebean.core.base.BaseBiz;
import net.onebean.core.error.BusinessException;
import net.onebean.core.query.Condition;
import net.onebean.sodium.common.codeGenerate.CodeGenerateUtils;
import net.onebean.sodium.common.error.ErrorCodesEnum;
import net.onebean.sodium.dao.CodeDatabaseTableDao;
import net.onebean.sodium.model.CodeDatabaseField;
import net.onebean.sodium.model.CodeDatabaseTable;
import net.onebean.sodium.service.CodeDatabaseFieldService;
import net.onebean.sodium.service.CodeDatabaseTableService;
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
    @Autowired
    private CodeGenerateUtils codeGenerateUtils;

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


    @Override
    public Boolean saveCodeDatabaseTable(CodeDatabaseTable entity) {
        List<CodeDatabaseField> childList = entity.getChildList();
        if (entity.getId() != null) {
            this.update(entity);
        } else {
            this.save(entity);
        }
        for (CodeDatabaseField codeDatabaseField : childList) {
            codeDatabaseField.setTableId(entity.getId());
        }
        this.deleteChildList(entity.getId());
        codeDatabaseFieldService.saveBatch(childList);
        return true;
    }

    @Override
    public String generate(Object id) {
        String res = "";
        CodeDatabaseTable entity = this.findById(id);
        Condition param = Condition.parseModelCondition("tableId@int@eq$");
        param.setValue(id);
        codeDatabaseFieldService.find(null, param);
        entity.setChildList(codeDatabaseFieldService.find(null, param));
        try {
            codeGenerateUtils.generate(entity);
        } catch (Exception e) {
            throw new BusinessException(ErrorCodesEnum.GENERATE_FILE_FAILURE.code(),ErrorCodesEnum.GENERATE_FILE_FAILURE.msg());
        }
        switch (entity.getGenerateScope()){
            case "page":
                res = "代码已生成完毕,并重启程序预览!";
            case "controller":
                res = "代码已生成完毕,重启程序后生效";
            case "service":
                res = "代码已生成完毕,重启程序后生效";
        }
        return res;
    }


    @Override
    public Boolean isRepeatTable(String tableName) {
        Condition param = Condition.parseModelCondition("tableName@string@eq$");
        param.setValue(tableName);
        return CollectionUtil.isEmpty(this.find(null, param));
    }

    @Override
    public Boolean deleteCodeDatabaseTable(Object id) {
        this.deleteById(id);
        this.deleteChildList(id);
        return true;
    }
}