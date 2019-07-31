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
import net.onebean.sodium.model.SysOrganization;
import net.onebean.sodium.model.SysUser;
import net.onebean.sodium.security.SpringSecurityUtil;
import net.onebean.sodium.service.SysOrganizationService;
import net.onebean.sodium.vo.InitTreeReq;
import net.onebean.sodium.vo.OrgTree;
import net.onebean.util.DateUtils;
import org.springframework.context.annotation.Description;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 机构管理
 *
 * @author 0neBean
 */
@Controller
@RequestMapping("/sysorg")
public class SysOrganizationController extends BaseSplitController<SysOrganization, SysOrganizationService> {

    /**
     * 预览列表页面
     *
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
     *
     * @param model  modelAndView
     * @param entity 实体
     * @return view
     */
    @RequestMapping("add")
    @Description(value = "新增页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_ADD')")
    public String add(Model model, SysOrganization entity) {
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
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_VIEW')")
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
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_EDIT')")
    public String edit(Model model, @PathVariable("id") Object id) {
        model.addAttribute("entity", baseService.findById(id));
        model.addAttribute("edit", true);
        return getView("detail");
    }

    /**
     * 保存
     */
    @RequestMapping("save")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_SAVE')")
    public BaseResponse<SysOrganization> add(@RequestBody SysOrganization entity) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<SysOrganization> response = new BaseResponse<>();
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
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_LIST')")
    public BasePaginationResponse<SysOrganization> list(@RequestBody BasePaginationRequest<String> request) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<SysOrganization> response = new BasePaginationResponse<>();
        try {
            logger.debug("method list request = " + JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            String cond = Optional.ofNullable(request).map(BasePaginationRequest::getData).orElse("");
            Pagination page = Optional.ofNullable(request).map(BasePaginationRequest::getPage).orElse(new Pagination());
            Sort sort = Optional.ofNullable(request).map(BasePaginationRequest::getSort).orElse(new Sort(Sort.DESC, "id"));
            initData(sort, page, cond);
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
     * @param id 主键
     * @return PageResult<Boolean>
     */
    @RequestMapping(value = "delete/{id}")
    @Description(value = "删除")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_DELETE')")
    public BaseResponse<Boolean> delete(@PathVariable("id") Object id) {
        logger.info("method delete access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<Boolean> response = new BaseResponse<>();
        try {
            logger.debug("method delete id = " + JSON.toJSONString(id, SerializerFeature.WriteMapNullValue));
            response = BaseResponse.ok(baseService.deleteOrg(id));
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
     * @return BasePaginationResponse<OrgTree>
     */
    @RequestMapping("orgtree")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_TREE')")
    @SuppressWarnings("unchecked")
    public BasePaginationResponse<OrgTree> orgTree(@RequestBody BasePaginationRequest<InitTreeReq> request) {
        logger.info("method orgTree access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<OrgTree> response = new BasePaginationResponse<>();
        try {
            logger.debug("method orgTree request = " + JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
            Long parentId = Optional.ofNullable(request).map(BasePaginationRequest::getData).map(InitTreeReq::getParentId).orElse(null);
            Long selfId = Optional.ofNullable(request).map(BasePaginationRequest::getData).map(InitTreeReq::getSelfId).orElse(null);
            response = BasePaginationResponse.ok(baseService.findChildAsync(parentId,selfId,currentUser));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method orgTree BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method orgTree catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 查出所有节点
     * @return BasePaginationResponse<OrgTree>
     */
    @RequestMapping("allorgtree")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_ALL_TREE')")
    public BasePaginationResponse<OrgTree> allorgTree() {
        logger.info("method orgTree access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<OrgTree> response = new BasePaginationResponse<>();
        try {
            SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
            response = BasePaginationResponse.ok(baseService.organizationToOrgTree(baseService.findChildSync(currentUser), null));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method orgTree BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method orgTree catch Exception e = ", e);
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
    @RequestMapping("addchild")
    @PreAuthorize("hasPermission('$everyone','PERM_ORG_ADD_CHILD')")
    public String addChild(Model model, @RequestParam("parentId") Long parentId, SysOrganization entity) {
        /*获取当前最大排序值*/
        entity.setSort(baseService.findChildOrderNextNum(parentId));
        model.addAttribute("entity", entity);
        model.addAttribute("add", true);
        return getView("detail");
    }
}
