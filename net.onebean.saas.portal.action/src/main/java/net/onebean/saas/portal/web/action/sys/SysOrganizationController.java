package net.onebean.saas.portal.web.action.sys;


import com.eakay.core.PageResult;
import com.eakay.core.Pagination;
import com.eakay.core.extend.Sort;
import com.eakay.core.form.Parse;
import net.onebean.saas.portal.VO.OrgTree;
import net.onebean.saas.portal.core.BaseSplitController;
import net.onebean.saas.portal.model.SysOrganization;
import net.onebean.saas.portal.model.SysUser;
import net.onebean.saas.portal.security.SpringSecurityUtil;
import net.onebean.saas.portal.service.SysOrganizationService;
import net.onebean.saas.portal.service.SysUserService;
import com.eakay.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 机构管理
 * @author 0neBean
 */
@Controller
@RequestMapping("/sysorg")
public class SysOrganizationController extends BaseSplitController <SysOrganization,SysOrganizationService> {

    @Autowired
    private SysUserService sysUserService;
    /**
     * 预览列表页面
     * @return view
     */
    @RequestMapping("preview")
    @Description(value = "预览列表页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_PREVIEW')")
    public String preview() {
        return getView("list");
    }

    /**
     * 新增页面
     * @param model modelAndView
     * @param entity 实体
     * @return view
     */
    @RequestMapping("add")
    @Description(value = "新增页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_ADD')")
    public String add(Model model,SysOrganization entity) {
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
    @RequestMapping("view/{id}")
    @Description(value = "查看页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_VIEW')")
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
    @RequestMapping("edit/{id}")
    @Description(value = "编辑页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_EDIT')")
    public String edit(Model model,@PathVariable("id")Object id) {
        model.addAttribute("entity",baseService.findById(id));
        model.addAttribute("edit",true);
        return getView("detail");
    }

    /**
     * 保存
     * @param entity 实体
     * @param result 结果集
     * @return PageResult<SysOrganization>
     */
    @RequestMapping("save")
    @Description(value = "保存")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_SAVE')")
    public PageResult<SysOrganization> save(SysOrganization entity, PageResult<SysOrganization> result) {
        entity = loadOperatorData(entity);
        baseService.save(entity);
        result.getData().add(entity);
        return result;
    }
    /**
     * 列表数据
     * @param sort 排序参数
     * @param page 分页参数
     * @param result 结果集
     * @param cond 表达式
     * @return PageResult<SysOrganization>
     */
    @RequestMapping("list")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_LIST')")
    public PageResult<SysOrganization> list (Sort sort, Pagination page, PageResult<SysOrganization> result,
                                             @RequestParam(value = "conditionList",required = false) String cond){
        initData(sort,page,cond);
        result.setData(dataList);
        result.setPagination(page);
        return result;
    }

    /**
     * 根据ID删除
     * @param id 主键
     * @param result 结果集
     * @return PageResult<SysOrganization>
     */
    @RequestMapping("delete/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_DELETE')")
    public PageResult<SysOrganization> delete(@PathVariable("id")Object id, PageResult<SysOrganization> result) {
        result.setFlag(true);
        if (CollectionUtil.isNotEmpty(sysUserService.findUserByOrgID(id))){
            result.setFlag(false);
            result.setMsg("该机构关联了用户不能删除!");
        }
        if(!baseService.deleteSelfAndChildById(Parse.toLong(id))){
            result.setFlag(false);
            result.setMsg("该机构下级机构关联了用户不能删除!");
        }
        return result;
    }

    /**
     * 查出一级节点
     * @param page 分页参数
     * @param result 结果集
     * @param parentId 父id
     * @param selfId 自身id
     * @return PageResult<OrgTree>
     */
    @RequestMapping("orgtree")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_TREE')")
    public PageResult<OrgTree> orgTree(Pagination page,PageResult<OrgTree> result,
                                       @RequestParam(value = "parentId",required = false) Long parentId,
                                       @RequestParam(value = "selfId",required = false) Long selfId){
        SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
        result.setData(baseService.findChildAsync(parentId,selfId,currentUser));
        result.setPagination(page);
        return result;
    }

    /**
     * 查出所有节点
     * @param result 结果集
     * @return PageResult<OrgTree>
     */
    @RequestMapping("allorgtree")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_ALL_TREE')")
    public PageResult<OrgTree> allorgTree(PageResult<OrgTree> result){
        SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
        List<SysOrganization> list = baseService.findChildSync(currentUser);
        result.setData(baseService.organizationToOrgTree(list,null));
        return result;
    }

    /**
     * 添加子项
     * @param model modelAndView
     * @param parentId 父id
     * @param entity 实体
     * @return view
     */
    @RequestMapping("addchild")
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_ADD_CHILD')")
    public String addChild(Model model, @RequestParam("parentId")Long parentId,SysOrganization entity) {
        /*获取当前最大排序值*/
        entity.setSort(baseService.findChildOrderNextNum(parentId));
        model.addAttribute("entity",entity);
        model.addAttribute("add",true);
        return getView("detail");
    }
}
