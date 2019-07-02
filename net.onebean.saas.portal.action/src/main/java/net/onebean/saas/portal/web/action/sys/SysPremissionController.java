package net.onebean.saas.portal.web.action.sys;


import com.eakay.core.Condition;
import com.eakay.core.PageResult;
import com.eakay.core.Pagination;
import com.eakay.core.extend.Sort;
import com.eakay.core.form.Parse;
import net.onebean.saas.portal.VO.MenuTree;
import net.onebean.saas.portal.core.BaseSplitController;
import net.onebean.saas.portal.model.SysPermission;
import net.onebean.saas.portal.model.SysPermissionRole;
import net.onebean.saas.portal.model.SysUser;
import net.onebean.saas.portal.security.SpringSecurityUtil;
import net.onebean.saas.portal.service.SysPermissionRoleService;
import net.onebean.saas.portal.service.SysPermissionService;
import com.eakay.util.CollectionUtil;
import com.eakay.util.StringUtils;
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
 * 权限管理
 * @author 0neBean
 */
@Controller
@RequestMapping("/syspremission")
public class SysPremissionController extends BaseSplitController <SysPermission,SysPermissionService> {

    @Autowired
    private SysPermissionRoleService sysPermissionRoleService;


    /**
     * 预览列表页面
     * @return view
     */
    @RequestMapping("preview")
    @Description(value = "预览列表页面")
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_PREVIEW')")
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
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_ADD')")
    public String add(Model model,SysPermission entity) {
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
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_VIEW')")
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
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_EDIT')")
    public String edit(Model model,@PathVariable("id")Object id) {
        model.addAttribute("entity",baseService.findById(id));
        model.addAttribute("edit",true);
        return getView("detail");
    }

    /**
     * 保存
     * @param entity 实体
     * @param result 结果集
     * @return PageResult<SysPermission>
     */
    @RequestMapping("save")
    @Description(value = "保存")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_SAVE')")
    public PageResult<SysPermission> save(SysPermission entity, PageResult<SysPermission> result) {
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
     * @return PageResult<SysPermission>
     */
    @RequestMapping("list")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_LIST')")
    public PageResult<SysPermission> list (Sort sort, Pagination page, PageResult<SysPermission> result,
                                           @RequestParam(value = "conditionList",required = false) String cond){
        initData(sort,page,cond);
        dicCoverList(null,"date@createTime$");
        result.setData(dataList);
        result.setPagination(page);
        return result;
    }

    /**
     * 根据ID删除
     * @param id 主键
     * @param result 结果集
     * @return PageResult<SysPermission>
     */
    @RequestMapping(value = "delete/{id}")
    @Description(value = "删除")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_DELETE')")
    public PageResult<SysPermission> delete(@PathVariable("id")Object id, PageResult<SysPermission> result) {
        baseService.deleteSelfAndChildById(Parse.toLong(id));
        sysPermissionRoleService.deteleByPermissionId(Parse.toLong(id));
        result.setFlag(true);
        return result;
    }


    /**
     * 查出一级节点
     * @param page 分页参数
     * @param result 结果集
     * @param parentId 父id
     * @param selfId 自身id
     * @return PageResult<MenuTree>
     */
    @RequestMapping("menutree")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_MENU_TREE')")
    public PageResult<MenuTree> MenuTree(Pagination page,PageResult<MenuTree> result,@RequestParam(value = "parentId",required = false) Long parentId,@RequestParam(value = "selfId",required = false) Long selfId){
        result.setData(baseService.findChildAsync(parentId,selfId));
        result.setPagination(page);
        return result;
    }

    /**
     * 查出所有节点
     * @param result 结果集
     * @return PageResult<OrgTree>
     */
    @RequestMapping("allmenutree")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_ALL_MENU_TREE')")
    public PageResult<MenuTree> allMenuTree(PageResult<MenuTree> result){
        SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
        List<SysPermission> list = baseService.findChildSync(currentUser);//查出url和menu的数据
        list = dicCoverTree("childList",list,"dic@CDLX$menuType");
        result.setData(baseService.permissionToMenuTree(list,null));
        return result;
    }

    /**
     * 登录用户的所有有权限的菜单
     * @param result 结果集
     * @return PageResult<SysPermission>
     */
    @RequestMapping("allpermissiontree")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public PageResult<SysPermission> allPermissionTree(PageResult<SysPermission> result){
        List<SysPermission> list = baseService.findChildSyncForMenu();//只查出menu的数据
        list = list.get(0).getChildList();
        list = dicCoverTree("childList",list,"dic@CDLX$menuType");
        list = baseService.getCurrentLoginUserHasPermission(list,SpringSecurityUtil.getCurrentPermissions());
        result.setData(list);
        return result;
    }

    /**
     * 获取对应角色的所有菜单的树
     * @param roleId 角色id
     * @param result 结果集
     * @return PageResult<MenuTree>
     */
    @RequestMapping("getrolepremission")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_GET_ROLE_PREM')")
    public PageResult<MenuTree> getRolePremission(@RequestParam(value = "roleId") Long roleId,PageResult<MenuTree> result){
        SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
        List<SysPermission> list = baseService. findChildSync(currentUser);//查出url和menu的数据
        result.setData(baseService.permissionToMenuTreeForRole(list,null,roleId));
        return result;
    }

    /**
     * 保存角色拥有的菜单权限
     * @param premIds 权限IDs 用','分割的字符串
     * @param result 结果集
     * @param roleId 角色ID
     * @return PageResult<SysPermissionRole>
     */
    @RequestMapping("savepremissionrole")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_SAVE_ROLE_PREM')")
    public PageResult<SysPermissionRole> savePremissionRole(@RequestParam("premIds")String premIds,PageResult<SysPermissionRole> result,@RequestParam("roleId")String roleId){
        sysPermissionRoleService.deteleByRoleId(Parse.toLong(roleId));
        if (StringUtils.isNotEmpty(premIds)){
            sysPermissionRoleService.insertBatch(premIds,roleId);
        }
        result.setFlag(true);
        return result;
    }

    /**
     * 防止菜单url和name重复
     * @param reg 权限的url或name
     * @param id 权限ID
     * @param result 结果集
     * @return PageResult<SysPermission>
     */
    @RequestMapping(value = "isrepeat")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_IS_REPEAT')")
    public PageResult<SysPermission> urlRepeat(@RequestParam("reg")String reg,@RequestParam("id")Long id,PageResult<SysPermission> result){
        Condition param;
        if(reg.startsWith("PERM_")){
            param = Condition.parseModelCondition("name@string@eq");
        }else{
            param= Condition.parseModelCondition("url@string@eq");
        }
        param.setValue(reg);
        List <SysPermission> list = baseService.find(null,param);
        if(CollectionUtil.isEmpty(list)){
            result.setFlag(true);
        }else{
            if(id == null){
                result.setFlag(false);
            }else{
                result.setFlag((list.get(0).getId().equals(id))?true:false);
            }
        }
        return result;
    }

    /**
     * 添加子项
     * @param model modelAndView
     * @param parentId 父id
     * @param entity 实体
     * @return view
     */
    @RequestMapping(value = "addchild")
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_ADD_CHILD')")
    public String addChild(Model model, @RequestParam("parentId")Long parentId,SysPermission entity) {
        entity.setSort(baseService.findChildOrderNextNum(parentId));
        entity.setName(baseService.findById(parentId.toString()).getName()+"_");
        model.addAttribute("entity",entity);
        model.addAttribute("add",true);
        return getView("detail");
    }

}
