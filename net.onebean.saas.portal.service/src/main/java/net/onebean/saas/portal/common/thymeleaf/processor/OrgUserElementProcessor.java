package net.onebean.saas.portal.common.thymeleaf.processor;

import net.onebean.saas.portal.common.thymeleaf.base.OneBeanBaseAbstractElementTagProcessor;
import net.onebean.saas.portal.model.SysUser;
import com.eakay.component.SpringUtil;
import net.onebean.saas.portal.service.SysUserService;
import net.onebean.saas.portal.service.impl.SysUserServiceImpl;
import com.eakay.util.StringUtils;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.*;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author 0neBean
 * 自定义标签 转义org树的选择结果
 */
@Component
public class OrgUserElementProcessor extends OneBeanBaseAbstractElementTagProcessor {


    private static final String PREFIX = "tree";
    private static final String TAG_NAME = "org-user";
    private static final int PRECEDENCE = 1000;


    /**
     * 默认构造器
     */
    public OrgUserElementProcessor() {
        super(TemplateMode.HTML,PREFIX,TAG_NAME,true,null,false,PRECEDENCE);
    }

    /**
     * 构造器
     * @param dialectPrefix 前缀
     */
    public OrgUserElementProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML,dialectPrefix,TAG_NAME,true,null,false,PRECEDENCE);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        /*元素工厂用于创建元素*/
        final IModelFactory modelFactory = context.getModelFactory();
        SysUserService sysUserService = SpringUtil.getBean(SysUserServiceImpl.class);
        final String selfId = tag.getAttributeValue("selfId");
        final String disabled = tag.getAttributeValue("disabled");
        final String value = tag.getAttributeValue("value");
        final String businessInPutId = tag.getAttributeValue("businessInPutId");

        /*表单input 点击后模态窗口*/
        final IModel input = modelFactory.createModel();
        IOpenElementTag inputStart = modelFactory.createOpenElementTag("input");
        final ICloseElementTag inputEnd = modelFactory.createCloseElementTag("input");
        inputStart = modelFactory.setAttribute(inputStart,"id","orgTreeSelectorInput");

        inputStart = modelFactory.setAttribute(inputStart,"id","orgLinkageUserTreeSelector");
        inputStart = modelFactory.setAttribute(inputStart,"type","text");
        inputStart = modelFactory.setAttribute(inputStart,"class","tpl-form-input");
        inputStart = modelFactory.setAttribute(inputStart,"placeholder","请选择用户");
        inputStart = modelFactory.setAttribute(inputStart,"onclick","modalOrgUserTree("+selfId+","+businessInPutId+")");
        inputStart = modelFactory.setAttribute(inputStart,"name","orgUserTree");
        if (StringUtils.isNotEmpty(value)) {
            SysUser sysUser = sysUserService.findById(value);
            inputStart = modelFactory.setAttribute(inputStart,"value",sysUser.getRealName());
        }else {
            inputStart = modelFactory.setAttribute(inputStart,"value","未选择用户");
        }
        if (StringUtils.isNotEmpty(disabled)) {
            inputStart = modelFactory.setAttribute(inputStart,"disabled",disabled);
        }
        input.add(inputStart);
        input.add(inputEnd);


        /*模态弹窗模板*/
        Object templFragmentObj = computeFragment(context, "~{public/orgUserTree :: orgTreeLinkageUserTips}");
        final IModel treeTempl = modelFactory.parse(context.getTemplateData(),templFragmentObj.toString());

//        final IModel treeTempl = modelFactory.createModel();
//        IOpenElementTag treeTemplStart = modelFactory.createOpenElementTag("script");
//        final ICloseElementTag treeTemplEnd = modelFactory.createCloseElementTag("script");
//        treeTemplStart = modelFactory.setAttribute(treeTemplStart,"id","orgTreeLinkageUserTempl");
//        treeTemplStart = modelFactory.setAttribute(treeTemplStart,"type","text/html");
//        treeTemplStart = modelFactory.setAttribute(treeTemplStart,"th:replace","~{public/orgUserTree :: orgTreeLinkageUserTips}");
//        treeTempl.add(treeTemplStart);
//        treeTempl.add(treeTemplEnd);


        /*用户选项option模板*/
        Object optionTemplFragmentObj = computeFragment(context, "~{public/orgUserTree :: orgTreeLinkageUserOptionTips}");
        final IModel optionTempl = modelFactory.parse(context.getTemplateData(),optionTemplFragmentObj.toString());
//
//        final IModel optionTempl = modelFactory.createModel();
//        IOpenElementTag optionTemplStart = modelFactory.createOpenElementTag("script");
//        final ICloseElementTag optionTemplEnd = modelFactory.createCloseElementTag("script");
//        optionTemplStart = modelFactory.setAttribute(optionTemplStart,"id","userOptinTempl");
//        optionTemplStart = modelFactory.setAttribute(optionTemplStart,"type","text/html");
//        optionTemplStart = modelFactory.setAttribute(optionTemplStart,"th:replace","");
//        optionTempl.add(optionTemplStart);
//        optionTempl.add(optionTemplEnd);

        /*用户机构树js内容*/
        Object jsFragmentObj = computeFragment(context, "~{public/orgUserTree :: orgTreeLinkageUserTreeTipsJs}");
        final IModel js = modelFactory.parse(context.getTemplateData(),jsFragmentObj.toString());
//        final IModel js = modelFactory.createModel();
//        IOpenElementTag jsStart = modelFactory.createOpenElementTag("script");
//        final ICloseElementTag jslEnd = modelFactory.createCloseElementTag("script");
//        jsStart = modelFactory.setAttribute(jsStart,"type","text/javascript");
//        jsStart = modelFactory.setAttribute(jsStart,"th:replace","~{public/orgUserTree :: orgTreeLinkageUserTreeTipsJs}");
//        js.add(jsStart);
//        js.add(jslEnd);

        final IModel nodes = modelFactory.createModel();
        nodes.insertModel(nodes.size(),input);
        nodes.insertModel(nodes.size(),treeTempl);
        nodes.insertModel(nodes.size(),optionTempl);
        nodes.insertModel(nodes.size(),js);
        /*Instruct the engine to replace this entire element with the specified model.*/
        structureHandler.replaceWith(nodes, false);
    }
}
