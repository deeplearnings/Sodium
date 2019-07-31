package net.onebean.sodium.web.action.sys;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.onebean.core.base.BasePaginationRequest;
import net.onebean.core.base.BasePaginationResponse;
import net.onebean.core.base.BaseResponse;
import net.onebean.core.error.BusinessException;
import net.onebean.core.extend.Sort;
import net.onebean.core.query.Pagination;
import net.onebean.sodium.common.dataPerm.DataPermUtils;
import net.onebean.sodium.common.error.ErrorCodesEnum;
import net.onebean.sodium.core.BaseSplitController;
import net.onebean.sodium.model.SysRole;
import net.onebean.sodium.model.SysUser;
import net.onebean.sodium.security.SpringSecurityUtil;
import net.onebean.sodium.service.SysRoleService;
import net.onebean.sodium.vo.AddRoleUserReq;
import net.onebean.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * 角色管理
 *
 * @author 0neBean
 */
@Controller
@RequestMapping("/sysrole")
public class SysRoleController extends BaseSplitController<SysRole, SysRoleService> {


    @Autowired
    private DataPermUtils dataPermUtils;


    /**
     * 预览列表页面
     *
     * @return view
     */
    @RequestMapping("preview")
    @Description(value = "预览列表页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ROLE_PREVIEW')")
    public String preview() {
        return getView("list");
    }

    /**
     * 新增页面
     *
     * @param model  modelAndView
     * @param entity 实体
     * @return view
     */
    @RequestMapping("add")
    @Description(value = "新增页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ROLE_ADD')")
    public String add(Model model, SysRole entity) {
        model.addAttribute("add", true);
        model.addAttribute("entity", entity);
        return getView("detail");
    }

    /**
     * 查看页面
     *
     * @param model modelAndView
     * @param id    主键
     * @return view
     */
    @RequestMapping("view/{id}")
    @Description(value = "查看页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ROLE_VIEW')")
    public String view(Model model, @PathVariable("id") Object id) {
        model.addAttribute("entity", baseService.findById(id));
        model.addAttribute("view", true);
        return getView("detail");
    }

    /**
     * 编辑页面
     *
     * @param model modelAndView
     * @param id    主键
     * @return view
     */
    @RequestMapping("edit/{id}")
    @Description(value = "编辑页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ROLE_EDIT')")
    public String edit(Model model, @PathVariable("id") Object id) {
        model.addAttribute("entity", baseService.findById(id));
        model.addAttribute("edit", true);
        return getView("detail");
    }

    /**
     * 保存
     * @param entity 实体
     * @return BaseResponse<SysRole>
     */
    @RequestMapping("save")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_ROLE_SAVE')")
    public BaseResponse<SysRole> add(@RequestBody SysRole entity) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<SysRole> response = new BaseResponse<>();
        try {
            logger.debug("method add entity = " + JSON.toJSONString(entity, SerializerFeature.WriteMapNullValue));
            entity = loadOperatorData(entity);
            baseService.save(entity);
            response = BaseResponse.ok(entity);
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method add BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method add catch Exception e = ", e);
        }
        return response;
    }



    /**
     * 删除数据库模型及其关联字段
     * @param id 主键
     * @return BaseResponse<Boolean>
     */
    @RequestMapping(value = "delete/{id}")
    @Description(value = "删除")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_ROLE_DELETE')")
    public BaseResponse<Boolean> delete(@PathVariable("id") Object id) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<Boolean> response = new BaseResponse<>();
        try {
            logger.debug("method delete id = " + JSON.toJSONString(id, SerializerFeature.WriteMapNullValue));
            response = BaseResponse.ok(baseService.deleteRole(id));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method delete BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method delete catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 列表数据
     *
     * @param request 参数体
     * @return BasePaginationResponse<CodeDatabaseTable>
     */
    @RequestMapping("list")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_ROLE_LIST')")
    public BasePaginationResponse<SysRole> list(@RequestBody BasePaginationRequest<String> request) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<SysRole> response = new BasePaginationResponse<>();
        try {
            logger.debug("method list request = " + JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            String cond = Optional.ofNullable(request).map(BasePaginationRequest::getData).orElse("");
            Pagination page = Optional.ofNullable(request).map(BasePaginationRequest::getPage).orElse(new Pagination());
            Sort sort = Optional.ofNullable(request).map(BasePaginationRequest::getSort).orElse(new Sort(Sort.DESC, "id"));
            SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
            String tenantId = baseService.getTenantId();
            String join = MessageFormat.format("LEFT JOIN sys_user_{0} u on u.id = t.operator_id LEFT JOIN sys_organization_{1} o ON o.`id` = u.org_id",tenantId,tenantId);
            initData(sort,page,cond,dataPermUtils.dataPermFilter(currentUser,"o","t",baseService.getTenantId(),join));
            dicCoverList(null,"dic@SF$isLock","dic@SJQX$dataPermissionLevel","date@createTime$");
            response = BasePaginationResponse.ok(dataList, page);
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method list BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method list catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 查找用户的所有角色
     */
    @RequestMapping("findbyuid/{userId}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_ROLE_FIND_BY_USERID')")
    public BasePaginationResponse<SysRole> findRolesByUserId(@PathVariable("userId") Long userId) {
        logger.info("method findRolesByUserId access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<SysRole> response = new BasePaginationResponse<>();
        try {
            logger.debug("method findRolesByUserId userId = " + JSON.toJSONString(userId, SerializerFeature.WriteMapNullValue));
            response = BasePaginationResponse.ok(baseService.findRolesByUserId(userId));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method findRolesByUserId BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method findRolesByUserId catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 根据角色名查找角色
     */
    @RequestMapping("findByRoleName/{roleName}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_ROLE_FIND_BY_NAME')")
    public BasePaginationResponse<SysRole> findByRoleName(@PathVariable("roleName") String roleName) {
        logger.info("method findByRoleName access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<SysRole> response = new BasePaginationResponse<>();
        try {
            logger.debug("method findByRoleName name = " + JSON.toJSONString(roleName, SerializerFeature.WriteMapNullValue));
            response = BasePaginationResponse.ok(baseService.findByName(roleName));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method findByRoleName BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method findByRoleName catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 添加用户角色关联信息
     */
    @RequestMapping("addroleuser")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_ROLE_ADD_ROLE_USER')")
    public BaseResponse<Boolean> addRoleUser(@RequestBody BasePaginationRequest<AddRoleUserReq> request) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<Boolean> response = new BaseResponse<>();
        try {
            logger.debug("method addRoleUser request = " + JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            Long userId = Optional.ofNullable(request).map(BasePaginationRequest::getData).map(AddRoleUserReq::getUserId).orElse(0L);
            String roleIds = Optional.ofNullable(request).map(BasePaginationRequest::getData).map(AddRoleUserReq::getRoleIds).orElse("");
            response = BaseResponse.ok(baseService.addRoleUser(userId,roleIds));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method addRoleUser BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method addRoleUser catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 删除用户角色关联信息
     */
    @RequestMapping("removeroleuser")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_ROLE_REMOVE_ROLE_USER')")
    public BaseResponse<SysRole> removeRoleUser(@RequestBody BasePaginationRequest<String> request) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<SysRole> response = new BaseResponse<>();
        try {
            logger.debug("method removeRoleUser request = " + JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            String urIds = Optional.ofNullable(request).map(BasePaginationRequest::getData).orElse("");
            response = BaseResponse.ok(baseService.removeRoleUser(urIds));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method removeRoleUser BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method removeRoleUser catch Exception e = ", e);
        }
        return response;
    }


}
