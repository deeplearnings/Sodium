package net.onebean.sodium.web.action.code;


import net.onebean.sodium.core.BaseController;
import net.onebean.sodium.model.CodeDatabaseField;
import net.onebean.sodium.service.CodeDatabaseFieldService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 数据库模型字段管理
 * @author 0neBean
 */
@Controller
@RequestMapping("databasefield")
public class CodeDatabaseFieldController extends BaseController<CodeDatabaseField,CodeDatabaseFieldService> {
}