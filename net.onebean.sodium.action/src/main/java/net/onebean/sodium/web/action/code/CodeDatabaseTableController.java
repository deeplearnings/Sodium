package net.onebean.sodium.web.action.code;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.onebean.sodium.common.error.ErrorCodesEnum;
import net.onebean.sodium.core.BaseController;
import net.onebean.sodium.model.CodeDatabaseTable;
import net.onebean.sodium.model.SysPermission;
import net.onebean.sodium.service.CodeDatabaseFieldService;
import net.onebean.sodium.service.CodeDatabaseTableService;
import net.onebean.core.base.BasePaginationRequest;
import net.onebean.core.base.BasePaginationResponse;
import net.onebean.core.base.BaseResponse;
import net.onebean.core.error.BusinessException;
import net.onebean.core.extend.Sort;
import net.onebean.core.query.Condition;
import net.onebean.core.query.Pagination;
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

import java.util.Optional;

/**
 * @author 0neBean
 * 生成代码模型管理
 */
@Controller
@RequestMapping("/databasetable")
public class CodeDatabaseTableController extends BaseController<CodeDatabaseTable, CodeDatabaseTableService> {

    @Autowired
    private CodeDatabaseFieldService codeDatabaseFieldService;



    /**
     * 预览列表页面
     *
     * @return view
     */
    @RequestMapping("preview")
    @Description(value = "预览列表页面")
    @PreAuthorize("hasPermission('$everyone','PERM_CODE_DATABASE_MODEL_PREVIEW')")
    public String preview() {
        return getView("list");
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
    @PreAuthorize("hasPermission('$everyone','PERM_CODE_DATABASE_MODEL_LIST')")
    public BasePaginationResponse<CodeDatabaseTable> list(@RequestBody BasePaginationRequest<String> request) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<CodeDatabaseTable> response = new BasePaginationResponse<>();
        try {
            logger.debug("method list request = " + JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            String cond = Optional.ofNullable(request).map(BasePaginationRequest::getData).orElse("");
            Pagination page = Optional.ofNullable(request).map(BasePaginationRequest::getPage).orElse(new Pagination());
            Sort sort = Optional.ofNullable(request).map(BasePaginationRequest::getSort).orElse(new Sort(Sort.DESC, "id"));
            initData(sort, page, cond);
            dicCoverList(null, "dic@SCDMFG$generateType", "dic@SCDMFW$generateScope", "date@createTime$");
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
     * 新增按钮选择表页面
     *
     * @param model modelAndView
     * @return view
     */
    @RequestMapping("select")
    @PreAuthorize("hasPermission('$everyone','PERM_CODE_DATABASE_MODEL_SELECT')")
    public String select(Model model) {
        model.addAttribute("databaseList", baseService.findDatabaseTableList());
        return getView("select");
    }


    /**
     * 删除数据库模型及其关联字段
     *
     * @param id 主键
     * @return PageResult<CodeDatabaseTable>
     */
    @RequestMapping(value = "delete/{id}")
    @Description(value = "删除")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_CODE_DATABASE_MODEL_DELETE')")
    public BaseResponse<CodeDatabaseTable> delete(@PathVariable("id") Object id) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<CodeDatabaseTable> response = new BaseResponse<>();
        try {
            logger.debug("method delete id = " + JSON.toJSONString(id, SerializerFeature.WriteMapNullValue));
            response = BaseResponse.ok(baseService.deleteCodeDatabaseTable(id));
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
     * 编辑页面
     *
     * @param model modelAndView
     * @param id    主键
     * @return view
     */
    @RequestMapping(value = "edit/{id}")
    @Description(value = "编辑页面")
    @PreAuthorize("hasPermission('$everyone','PERM_CODE_DATABASE_MODEL_EDIT')")
    public String edit(Model model, @PathVariable("id") Object id) {
        model.addAttribute("entity", baseService.findById(id));
        Condition condition = Condition.parseModelCondition("tableId@int@eq$");
        condition.setValue(id);
        model.addAttribute("fieldList", codeDatabaseFieldService.find(null, condition));
        model.addAttribute("edit", true);
        model.addAttribute("sysPermission", new SysPermission());
        return getView("detail");
    }

    /**
     * 查看页面
     *
     * @param model modelAndView
     * @param id    主键
     * @return view
     */
    @RequestMapping(value = "view/{id}")
    @Description(value = "查看页面")
    @PreAuthorize("hasPermission('$everyone','PERM_CODE_DATABASE_MODEL_VIEW')")
    public String view(Model model, @PathVariable("id") Object id) {
        model.addAttribute("entity", baseService.findById(id));
        Condition condition = Condition.parseModelCondition("tableId@int@eq$");
        condition.setValue(id);
        model.addAttribute("fieldList", codeDatabaseFieldService.find(null, condition));
        model.addAttribute("view", true);
        return getView("detail");
    }

    /**
     * 添加页面
     *
     * @param tablename 表名
     * @param model     modelAndView
     * @param entity    实体
     * @return view
     */
    @RequestMapping("add/{tablename}")
    @PreAuthorize("hasPermission('$everyone','PERM_CODE_DATABASE_MODEL_ADD')")
    public String add(@PathVariable("tablename") String tablename, Model model, CodeDatabaseTable entity) {
        model.addAttribute("fieldList", codeDatabaseFieldService.findAllTableFieldbyTableName(tablename));
        model.addAttribute("add", true);
        entity.setTableName(tablename);
        model.addAttribute("entity", entity);
        return getView("detail");
    }

    /**
     * 保存详情主体及子列表
     * @return BaseResponse<Boolean>
     */
    @RequestMapping("save")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_CODE_DATABASE_MODEL_SAVE')")
    public BaseResponse<CodeDatabaseTable> add(@RequestBody CodeDatabaseTable entity) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<CodeDatabaseTable> response = BaseResponse.ok(true);
        try {
            logger.debug("method add entity = " + JSON.toJSONString(entity, SerializerFeature.WriteMapNullValue));
            response = BaseResponse.ok(baseService.saveCodeDatabaseTable(entity));
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
     * 生成代码
     * @param id 主键
     * @return BaseResponse<CodeDatabaseTable>
     */
    @RequestMapping("generate/{id}")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_CODE_DATABASE_MODEL_GENERATE')")
    public BaseResponse<String> generate(@PathVariable Object id) {
        logger.info("method generate access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<String> response = new BaseResponse<>();
        try {
            logger.debug("method list id = " + JSON.toJSONString(id, SerializerFeature.WriteMapNullValue));
            response = BaseResponse.ok(baseService.generate(id));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method generate BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method generate catch Exception e = ", e);
        }
        return response;
    }


    /**
     * 数据去重接口
     * @param request 参数体
     * @return PageResult<CodeDatabaseTable>
     */
    @RequestMapping(value = "isRepeatTable")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_CODE_DATABASE_MODEL_IS_REPEAT')")
    public BaseResponse<Boolean> isRepeatTable(@RequestBody BasePaginationRequest<CodeDatabaseTable> request) {
        logger.info("method isRepeatTable access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<Boolean> response = new BaseResponse<>();
        try {
            logger.debug("method isRepeatTable request = " + JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            String tableName = Optional.of(request).map(BasePaginationRequest::getData).map(CodeDatabaseTable::getTableName).orElse("");
            response = BaseResponse.ok(baseService.isRepeatTable(tableName));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method isRepeatTable BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method isRepeatTable catch Exception e = ", e);
        }
        return response;
    }


}
