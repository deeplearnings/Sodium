package net.onebean.saas.portal.web.action.dic;


import net.onebean.saas.portal.core.BaseController;
import net.onebean.core.PageResult;
import net.onebean.core.Pagination;
import net.onebean.core.extend.Sort;
import net.onebean.saas.portal.model.DicDictionary;
import net.onebean.saas.portal.service.DicDictionaryService;
import org.springframework.context.annotation.Description;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 字典管理
 * @author 0neBean
 */
@Controller
@RequestMapping("/dic")
public class DicDictionaryController extends BaseController<DicDictionary,DicDictionaryService> {

    /**
     * 预览列表页面
     * @return view
     */
    @RequestMapping(value = "preview")
    @Description(value = "预览列表页面")
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_PREVIEW')")
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
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_ADD')")
    public String add(Model model,DicDictionary entity) {
        model.addAttribute("add",true);
        model.addAttribute("entity",entity);
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
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_VIEW')")
    public String view(Model model,@PathVariable("id")Object id){
        model.addAttribute("entity",baseService.findById(id));
        model.addAttribute("view",true);
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
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_EDIT')")
    public String edit(Model model,@PathVariable("id")Object id) {
        model.addAttribute("entity",baseService.findById(id));
        model.addAttribute("edit",true);
        return getView("detail");
    }

    /**
     * 保存
     * @param entity 实体
     * @param result 结果集
     * @return PageResult<SysUser>
     */
    @RequestMapping(value = "save")
    @Description(value = "保存")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_SAVE')")
    public PageResult<DicDictionary> save(Model model, DicDictionary entity, PageResult<DicDictionary> result) {
        baseService.save(entity);
        result.getData().add(entity);
        return result;
    }

    /**
     * 根据ID删除
     * @param id 主键
     * @param result 结果集
     * @return PageResult<SysUser>
     */
    @RequestMapping(value = "delete/{id}")
    @Description(value = "删除")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_DELETE')")
    public PageResult<DicDictionary> delete(Model model, @PathVariable("id")Object id, PageResult<DicDictionary> result) {
        baseService.deleteById(id);
        result.setFlag(true);
        return result;
    }

    /**
     * 列表数据
     * @param sort 排序参数
     * @param page 分页参数
     * @param result 结果集
     * @param conditionStr 表达式
     * @return PageResult<SysUser>
     */
   @RequestMapping("list")
   @ResponseBody
   @PreAuthorize("hasPermission('$everyone','PERM_DIC_LIST')")
   public PageResult<DicDictionary> list (Sort sort, Pagination page, PageResult<DicDictionary> result, @RequestParam(value = "conditionList",required = false) String conditionStr){
        initData(sort,page,conditionStr);
        result.setData(dataList);
        result.setPagination(page);
        return result;
    }

    /**
     * 添加子项
     * @param model modelAndView
     * @param id 主键
     * @return view
     */
    @RequestMapping(value = "group/{id}")
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_GROUP')")
    public String group(Model model, @PathVariable("id")Object id) {
        DicDictionary entity = baseService.findById(id);
        entity.setSort(baseService.findGroupOrderNextNum(entity.getCode()));
        model.addAttribute("entity",entity);
        model.addAttribute("group",true);
        return getView("group");
    }

}
