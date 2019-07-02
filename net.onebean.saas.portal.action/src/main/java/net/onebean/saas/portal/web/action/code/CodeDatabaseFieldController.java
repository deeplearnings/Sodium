package net.onebean.saas.portal.web.action.code;


import net.onebean.saas.portal.core.BaseController;
import net.onebean.saas.portal.model.CodeDatabaseField;
import net.onebean.saas.portal.service.CodeDatabaseFieldService;
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