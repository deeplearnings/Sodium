package net.onebean.sodium.web.action.dic;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.onebean.sodium.common.error.ErrorCodesEnum;
import net.onebean.sodium.core.BaseController;
import net.onebean.sodium.model.DicDictionary;
import net.onebean.sodium.service.DicDictionaryService;
import net.onebean.core.base.BasePaginationRequest;
import net.onebean.core.base.BasePaginationResponse;
import net.onebean.core.base.BaseResponse;
import net.onebean.core.error.BusinessException;
import net.onebean.core.extend.Sort;
import net.onebean.core.query.Pagination;
import net.onebean.util.DateUtils;
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
 * 字典管理
 *
 * @author 0neBean
 */
@Controller
@RequestMapping("/dic")
public class DicDictionaryController extends BaseController<DicDictionary, DicDictionaryService> {

    /**
     * 预览列表页面
     *
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
     *
     * @param model  modelAndView
     * @param entity 实体
     * @return view
     */
    @RequestMapping(value = "add")
    @Description(value = "新增页面")
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_ADD')")
    public String add(Model model, DicDictionary entity) {
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
    @RequestMapping(value = "view/{id}")
    @Description(value = "查看页面")
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_VIEW')")
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
    @RequestMapping(value = "edit/{id}")
    @Description(value = "编辑页面")
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_EDIT')")
    public String edit(Model model, @PathVariable("id") Object id) {
        model.addAttribute("entity", baseService.findById(id));
        model.addAttribute("edit", true);
        return getView("detail");
    }


    /**
     * 保存
     * @return BaseResponse<DicDictionary>
     */
    @RequestMapping("save")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_SAVE')")
    public BaseResponse<DicDictionary> add(@RequestBody DicDictionary entity) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<DicDictionary> response = new BaseResponse<>();
        try {
            logger.debug("method add entity = " + JSON.toJSONString(entity, SerializerFeature.WriteMapNullValue));
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
     */
    @RequestMapping(value = "delete/{id}")
    @Description(value = "删除")
    @ResponseBody
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_DELETE')")
    public BaseResponse<Integer> delete(@PathVariable("id")Object id) {
        logger.info("access"+ DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<Integer> response = new BaseResponse<>();
        try {
            logger.debug("method delete id = "+ JSON.toJSONString(id, SerializerFeature.WriteMapNullValue));
            response = BaseResponse.ok(baseService.deleteById(id));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method delete BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method delete catch Exception e = ",e);
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
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_LIST')")
    public BasePaginationResponse<DicDictionary> list(@RequestBody BasePaginationRequest<String> request) {
        logger.info("method list access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<DicDictionary> response = new BasePaginationResponse<>();
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
     * 添加子项
     *
     * @param model modelAndView
     * @param id    主键
     * @return view
     */
    @RequestMapping(value = "group/{id}")
    @PreAuthorize("hasPermission('$everyone','PERM_DIC_GROUP')")
    public String group(Model model, @PathVariable("id") Object id) {
        DicDictionary entity = baseService.findById(id);
        entity.setSort(baseService.findGroupOrderNextNum(entity.getCode()));
        model.addAttribute("entity", entity);
        model.addAttribute("group", true);
        return getView("group");
    }

}
