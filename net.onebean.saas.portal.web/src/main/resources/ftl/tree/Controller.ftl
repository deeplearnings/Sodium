package ${action_package_name}.${mapping};

import com.eakay.core.PageResult;
import com.eakay.core.Pagination;
import com.eakay.core.extend.Sort;
import BaseController;
import ${model_package_name}.${model_name};
import ${service_package_name}.${model_name}Service;
import ${vo_package_name}.${model_name}Tree;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
* @author ${author}
* @description ${description} controller
* @date ${create_time}
*/
@Controller
@RequestMapping("${mapping}")
public class ${model_name}Controller extends BaseController<${model_name},${model_name}Service> {

    /**
     * 预览列表页面
     * @return view
     */
    @RequestMapping(value = "preview")
    @Description(value = "预览列表页面")
    @PreAuthorize("hasPermission('$everyone','${premName}_PREVIEW')")
    public String preview(Model model) {
        return getView("list");
    }


    /**
     * 新增页面
     * @param model modelAndView
     * @param entity 实体
     * @return view
     */
    @RequestMapping(value = "add")
    @Description(value = "新增页面")
    @PreAuthorize("hasPermission('$everyone','${premName}_ADD')")
    public String add(Model model, ${model_name} entity) {
        model.addAttribute("add", true);
        model.addAttribute("entity", entity);
        return getView("detail");
    }

    /**
     * 查看页面
     * @param model modelAndView
     * @param id 主键
     * @return view
     */
    @RequestMapping(value = "view/{id}")
    @Description(value = "查看页面")
    @PreAuthorize("hasPermission('$everyone','${premName}_VIEW')")
    public String view(Model model, @PathVariable("id") Object id) {
        model.addAttribute("entity", baseService.findById(id));
        model.addAttribute("view", true);
        return getView("detail");
    }

    /**
     * 编辑页面
     * @param model modelAndView
     * @param id 主键
     * @return view
     */
    @RequestMapping(value = "edit/{id}")
    @Description(value = "编辑页面")
    @PreAuthorize("hasPermission('$everyone','${premName}_EDIT')")
    public String edit(Model model, @PathVariable("id") Object id) {
        model.addAttribute("entity", baseService.findById(id));
        model.addAttribute("edit", true);
        return getView("detail");
    }

    /**
     * 保存
     * @param entity 实体
     * @param result 结果集
     * @return PageResult<${model_name}>
    */
    @RequestMapping(value = "save")
    @Description(value = "保存")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','${premName}_SAVE')")
    public PageResult<${model_name}> save(${model_name} entity, PageResult<${model_name}> result) {
        baseService.save(entity);
        result.getData().add(entity);
        return result;
    }

    /**
     * 根据ID删除
     * @param id 主键
     * @param result 结果集
     * @return PageResult<${model_name}>
    */
    @RequestMapping(value = "delete/{id}")
    @Description(value = "删除")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','${premName}_DELETE')")
    public PageResult<${model_name}> delete(@PathVariable("id") Object id, PageResult<${model_name}> result) {
        baseService.deleteById(id);
        result.setFlag(true);
        return result;
    }



    /**
     * 查出一级节点
     * @param page 分页参数
     * @param result 结果集
     * @param parentId 父id
     * @param selfId 自身id
     * @return PageResult<${model_name}>
    */
    @RequestMapping("aSyncTree")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_TREE')")
    public PageResult<${model_name}> aSyncTree(Pagination page,PageResult<${model_name}> result,@RequestParam(value = "parentId",required = false) Long parentId,@RequestParam(value = "selfId",required = false) Long selfId){
        result.setData(baseService.findChildAsync(parentId,selfId));
        result.setPagination(page);
        return result;
    }

    /**
    * 查出所有节点
    * @param parentId 父id
    * @param result 结果集
    * @return PageResult<${model_name}>
    */
    @RequestMapping("syncTree")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_ALL_TREE')")
    public PageResult<${model_name}Tree> syncTree(@RequestParam(value = "parentId",required = false) Long parentId,PageResult<${model_name}Tree> result){
        List<${model_name}> list = baseService.findChildSync(parentId);
        result.setData(baseService.model2Tree(list,null));
        return result;
    }




    /**
     * 列表数据
     * @param sort 排序参数
     * @param page 分页参数
     * @param result 结果集
     * @param cond 表达式
     * @return PageResult<${model_name}>
    */
    @RequestMapping("list")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','${premName}_LIST')")
    public PageResult<${model_name}> list(Sort sort, Pagination page, PageResult<${model_name}> result, @RequestParam(value = "conditionList", required = false) String cond) {
        initData(sort, page, cond);
        result.setData(dataList);
        result.setPagination(page);
        return result;
    }
}
