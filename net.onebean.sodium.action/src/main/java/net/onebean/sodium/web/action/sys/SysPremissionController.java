package net.onebean.sodium.web.action.sys;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.onebean.core.base.BasePaginationRequest;
import net.onebean.core.base.BasePaginationResponse;
import net.onebean.core.base.BaseResponse;
import net.onebean.core.error.BusinessException;
import net.onebean.core.extend.Sort;
import net.onebean.core.query.Pagination;
import net.onebean.sodium.common.error.ErrorCodesEnum;
import net.onebean.sodium.core.BaseSplitController;
import net.onebean.sodium.model.CodeDatabaseTable;
import net.onebean.sodium.model.SysPermission;
import net.onebean.sodium.model.SysUser;
import net.onebean.sodium.security.SpringSecurityUtil;
import net.onebean.sodium.service.SysPermissionService;
import net.onebean.sodium.vo.InitTreeReq;
import net.onebean.sodium.vo.MenuTree;
import net.onebean.sodium.vo.PremissionRoleReq;
import net.onebean.util.DateUtils;
import org.springframework.context.annotation.Description;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 权限管理
 *
 * @author 0neBean
 */
@Controller
@RequestMapping("/syspremission")
public class SysPremissionController extends BaseSplitController<SysPermission, SysPermissionService> {



    /**
     * 预览列表页面
     *
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
     *
     * @param model  modelAndView
     * @param entity 实体
     * @return view
     */
    @RequestMapping("add")
    @Description(value = "新增页面")
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_ADD')")
    public String add(Model model, SysPermission entity) {
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
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_VIEW')")
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
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_EDIT')")
    public String edit(Model model, @PathVariable("id") Object id) {
        model.addAttribute("entity", baseService.findById(id));
        model.addAttribute("edit", true);
        return getView("detail");
    }

    /**
     * 保存
     *
     * @param entity 实体
     * @return BaseResponse<SysPermission>
     */
    @RequestMapping("save")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_SAVE')")
    public BaseResponse<SysPermission> add(@RequestBody SysPermission entity) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<SysPermission> response = new BaseResponse<>();
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
     * 列表数据
     *
     * @param request 参数体
     * @return BasePaginationResponse<CodeDatabaseTable>
     */
    @RequestMapping("list")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_LIST')")
    public BasePaginationResponse<CodeDatabaseTable> list(@RequestBody BasePaginationRequest<String> request) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<CodeDatabaseTable> response = new BasePaginationResponse<>();
        try {
            logger.debug("method list request = " + JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            String cond = Optional.ofNullable(request).map(BasePaginationRequest::getData).orElse("");
            Pagination page = Optional.ofNullable(request).map(BasePaginationRequest::getPage).orElse(new Pagination());
            Sort sort = Optional.ofNullable(request).map(BasePaginationRequest::getSort).orElse(new Sort(Sort.DESC, "id"));
            initData(sort, page, cond);
            dicCoverList(null, "date@createTime$");
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
     * 删除数据库模型及其关联字段
     *
     * @param id 主键
     * @return PageResult<Boolean>
     */
    @RequestMapping(value = "delete/{id}")
    @Description(value = "删除")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_DELETE')")
    public BaseResponse<Boolean> delete(@PathVariable("id") Object id) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<Boolean> response = new BaseResponse<>();
        try {
            logger.debug("method delete id = " + JSON.toJSONString(id, SerializerFeature.WriteMapNullValue));
            response = BaseResponse.ok(baseService.delPerm(id));
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
     * 查出一级节点
     */
    @RequestMapping("menutree")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_MENU_TREE')")
    public BasePaginationResponse<MenuTree> MenuTree(@RequestBody BasePaginationRequest<InitTreeReq> request) {
        logger.info("method MenuTree access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<MenuTree> response = new BasePaginationResponse<>();
        try {
            logger.debug("method MenuTree request = " + JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            Long parentId = Optional.ofNullable(request).map(BasePaginationRequest::getData).map(InitTreeReq::getParentId).orElse(null);
            Long selfId = Optional.ofNullable(request).map(BasePaginationRequest::getData).map(InitTreeReq::getSelfId).orElse(null);
            response = BasePaginationResponse.ok(baseService.findChildAsync(parentId,selfId));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method MenuTree BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method MenuTree catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 查出所有节点
     */
    @RequestMapping("allmenutree")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_ALL_MENU_TREE')")
    public BasePaginationResponse<MenuTree> allMenuTree() {
        logger.info("method allmenutree access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<MenuTree> response = new BasePaginationResponse<>();
        try {
            SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
            List<SysPermission> list = baseService.findChildSync(currentUser);//查出url和menu的数据
            list = dicCoverTree("childList", list, "dic@CDLX$menuType");
            response = BasePaginationResponse.ok(baseService.permissionToMenuTree(list, null));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method allmenutree BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method allmenutree catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 登录用户的所有有权限的菜单
     */
    @RequestMapping("allpermissiontree")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("isAuthenticated()")
    public BasePaginationResponse<SysPermission> allPermissionTree() {
        logger.info("method allPermissionTree access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<SysPermission> response = new BasePaginationResponse<>();
        try {
            List<SysPermission> list = baseService.findChildSyncForMenu();//只查出menu的数据
            list = list.get(0).getChildList();
            list = dicCoverTree("childList", list, "dic@CDLX$menuType");
            list = baseService.getCurrentLoginUserHasPermission(list, SpringSecurityUtil.getCurrentPermissions());
            response = BasePaginationResponse.ok(list);
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method allPermissionTree BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method allPermissionTree catch Exception e =  ", e);
        }
        return response;
    }

    /**
     * 获取对应角色的所有菜单的树
     */
    @RequestMapping("getrolepremission")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_GET_ROLE_PREM')")
    public BasePaginationResponse<MenuTree> getRolePremission(@RequestBody BasePaginationRequest<Long> request) {
        logger.info("method getRolePremission access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<MenuTree> response = new BasePaginationResponse<>();
        try {
            SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
            Long roleId = Optional.ofNullable(request).map(BasePaginationRequest::getData).orElse(0L);
            List<SysPermission> list = baseService.findChildSync(currentUser);//查出url和menu的数据
            response = BasePaginationResponse.ok(baseService.permissionToMenuTreeForRole(list, null, roleId));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method getRolePremission BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method getRolePremission catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 保存角色拥有的菜单权限
     */
    @RequestMapping("savepremissionrole")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_SAVE_ROLE_PREM')")
    public BaseResponse<Boolean> savePremissionRole(@RequestBody BasePaginationRequest<PremissionRoleReq> req) {
        logger.info("method savePremissionRole access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<Boolean> response = new BaseResponse<>();
        try {
            String premIds = Optional.ofNullable(req).map(BasePaginationRequest::getData).map(PremissionRoleReq::getPremIds).orElse("");
            String roleId = Optional.ofNullable(req).map(BasePaginationRequest::getData).map(PremissionRoleReq::getRoleId).orElse("");
            response = BaseResponse.ok(baseService.savePremissionRole(premIds,roleId));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method savePremissionRole BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method savePremissionRole catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 防止菜单url和name重复
     */
    @RequestMapping("isPermissionRepeat")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_IS_REPEAT')")
    public BaseResponse<Boolean> isPermissionRepeat(@RequestParam String reg,@RequestParam Long id) {

        logger.info("method urlRepeat access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<Boolean> response = new BaseResponse<>();
        try {
            response = BaseResponse.ok(baseService.urlRepeat(reg,id));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method urlRepeat BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method urlRepeat catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 添加子项
     *
     * @param model    modelAndView
     * @param parentId 父id
     * @param entity   实体
     * @return view
     */
    @RequestMapping(value = "addchild")
    @PreAuthorize("hasPermission('$everyone','PERM_PREMISSION_ADD_CHILD')")
    public String addChild(Model model, @RequestParam("parentId") Long parentId, SysPermission entity) {
        entity.setSort(baseService.findChildOrderNextNum(parentId));
        entity.setName(baseService.findById(parentId.toString()).getName() + "_");
        model.addAttribute("entity", entity);
        model.addAttribute("add", true);
        return getView("detail");
    }

}
