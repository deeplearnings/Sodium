package net.onebean.sodium.web.action.sys;


import net.onebean.sodium.common.dataPerm.DataPermUtils;
import net.onebean.sodium.core.BaseSplitController;
import net.onebean.core.Condition;
import net.onebean.core.PageResult;
import net.onebean.core.Pagination;
import net.onebean.core.extend.Sort;
import net.onebean.core.form.Parse;
import net.onebean.sodium.model.SysUser;
import net.onebean.sodium.security.OneBeanPasswordEncoder;
import net.onebean.sodium.security.SpringSecurityUtil;
import net.onebean.sodium.service.SysRoleUserService;
import net.onebean.sodium.service.SysUserService;
import net.onebean.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * 用户管理
 * @author 0neBean
 */
@Controller
@RequestMapping("/sysuser")
public class SysUserController extends BaseSplitController<SysUser,SysUserService> {

    @Autowired
    private OneBeanPasswordEncoder oneBeanPasswordEncoder;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private DataPermUtils dataPermUtils;

    private static final String CLOUD_ADMIN_USER = "root";
    private static final String DISABLE_USER_TYPE = "root,admin";

    /**
     * 编辑页面
     * @param model modelAndView
     * @param id 主键
     * @return view
     */
    @RequestMapping("edit/{id}")
    @Description(value = "编辑页面")
    @PreAuthorize("hasPermission('$everyone','PERM_USER_EDIT')")
    public String edit(Model model,@PathVariable("id")Object id) {
        SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
        String cUserType = Optional.ofNullable(currentUser).map(SysUser::getUserType).orElse(null);
        if (StringUtils.isNotEmpty(cUserType) && !cUserType.equals(CLOUD_ADMIN_USER)){
            model.addAttribute("rootAdminEdit",DISABLE_USER_TYPE);
        }
        model.addAttribute("entity",baseService.findById(id));
        model.addAttribute("edit",true);
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
    @PreAuthorize("hasPermission('$everyone','PERM_USER_VIEW')")
    public String view(Model model,@PathVariable("id")Object id){
        model.addAttribute("entity",baseService.findById(id));
        model.addAttribute("view",true);
        return getView("detail");
    }

    /**
     * 新增页面
     * @param model modelAndView
     * @param entity 实体
     * @return view
     */
    @RequestMapping("add")
    @Description(value = "新增页面")
    @PreAuthorize("hasPermission('$everyone','PERM_USER_ADD')")
    public String add(Model model,SysUser entity) {
        SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
        String cUserType = Optional.ofNullable(currentUser).map(SysUser::getUserType).orElse(null);
        if (StringUtils.isNotEmpty(cUserType) && !cUserType.equals(CLOUD_ADMIN_USER)){
            model.addAttribute("rootAdminEdit",DISABLE_USER_TYPE);
        }
        model.addAttribute("add",true);
        model.addAttribute("entity",entity);
        return getView("detail");
    }

    /**
     * 保存
     * @param entity 实体
     * @param result 结果集
     * @return PageResult<SysUser>
     */
    @RequestMapping(value = "save")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_USER_SAVE')")
    public PageResult<SysUser> save(SysUser entity, PageResult<SysUser> result) {
        entity = loadOperatorData(entity);
        if (null != entity.getPassword() && entity.getPassword().length() != 80){
            //页面限制用户密码长度为30,加密过得密码长度为80 未加密就加个密吧!
            entity.setPassword(oneBeanPasswordEncoder.encode(entity.getPassword()));
        }
        baseService.save(entity);
        result.getData().add(entity);
        return result;
    }

    /**
     * 预览列表页面
     * @return view
     */
    @RequestMapping("preview")
    @Description(value = "预览列表页面")
    @PreAuthorize("hasPermission('$everyone','PERM_USER_PREVIEW')")
    public String preview() {
        return getView("list");
    }


    /**
     * 列表数据
     * @param sort 排序参数
     * @param page 分页参数
     * @param result 结果集
     * @param cond 表达式
     * @return PageResult<SysUser>
     */
    @RequestMapping("list")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_USER_LIST')")
    public PageResult<SysUser> list (Sort sort, Pagination page, PageResult<SysUser> result,@RequestParam(value = "conditionList",required = false) String cond){
        SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
        String join =MessageFormat.format("LEFT JOIN sys_organization_{0} o ON o.`id` = t.org_id",baseService.getTenantId());
        initData(sort,page,cond,dataPermUtils.dataPermFilter(currentUser,"o","t",baseService.getTenantId(),join));
        dicCoverList(null,"dic@SF$isLock","dic@YHLX$userType","date@createTime$");
        result.setData(dataList);
        result.setPagination(page);
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
    @PreAuthorize("hasPermission('$everyone','PERM_USER_DELETE')")
    public PageResult<SysUser> delete(@PathVariable("id")Object id, PageResult<SysUser> result) {
        baseService.deleteById(id);
        sysRoleUserService.deleteByUserId(Parse.toLong(id));
        result.setFlag(true);
        return result;
    }

    /**
     * 重置密码
     * @param id 主键
     * @param result 结果集
     * @return PageResult<SysUser>
     */
    @RequestMapping(value = "resetpassword/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_USER_RESET_PASSWORD')")
    public PageResult<SysUser> resetpassword(@PathVariable("id")Object id, PageResult<SysUser> result) {
        SysUser entity =  baseService.findById(id);
        entity.setPassword(oneBeanPasswordEncoder.encode("123456"));
        baseService.update(entity);
        result.setFlag(true);
        return result;
    }

    /**
     * 账户设置
     * @param model modelAndView
     * @return view
     */
    @RequestMapping(value = "setting")
    @PreAuthorize("isAuthenticated()")
    public String setting(Model model) {
        SysUser entity = SpringSecurityUtil.getCurrentLoginUser();
        model.addAttribute("entity",entity);
        model.addAttribute("edit",true);
        return getView("setting");
    }

    /**
     * 设置密码
     * @param model modelAndView
     * @return view
     */
    @RequestMapping(value = "setpassword")
    @PreAuthorize("isAuthenticated()")
    public String setpassword(Model model) {
        model.addAttribute("entity",SpringSecurityUtil.getCurrentLoginUser());
        return getView("setpassword");
    }

    /**
     * 根据用户'orgid'查找用户
     * @param orgId 机构ID
     * @param result 结果集
     * @return view
     */
    @RequestMapping(value = "finduserbyorgid")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_USER_FIND_BY_ORGID')")
    public PageResult<SysUser> findUserByOrgId(@RequestParam("orgId")String orgId,PageResult<SysUser> result){
        Condition param =  Condition.parseModelCondition("orgId@string@eq");
        param.setValue(orgId);
        result.setData(baseService.find(null,param));
        return  result;
    }

}
