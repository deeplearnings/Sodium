package net.onebean.saas.portal.common.thymeleaf.processor;

import net.onebean.saas.portal.common.thymeleaf.base.OneBeanBaseAbstractElementTagProcessor;
import net.onebean.component.SpringUtil;
import net.onebean.saas.portal.service.SysOrganizationService;
import net.onebean.saas.portal.service.impl.SysOrganizationServiceImpl;
import net.onebean.util.StringUtils;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.*;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author 0neBean
 * 自定义方言 机构树选择器
 */
@Component
public class OrgTreeProcessor extends OneBeanBaseAbstractElementTagProcessor {

    private static final String PREFIX = "tree";
    private static final String TAG_NAME = "org";
    private static final int PRECEDENCE = 1000;


    /**
     * 默认构造器
     */
    public OrgTreeProcessor() {
        super(TemplateMode.HTML,PREFIX,TAG_NAME,true,null,false,PRECEDENCE);
    }

    /**
     * 构造器
     * @param dialectPrefix 前缀
     */
    public OrgTreeProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML,dialectPrefix,TAG_NAME,true,null,false,PRECEDENCE);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {

        /*元素工厂用于创建元素*/
        final IModelFactory modelFactory = context.getModelFactory();
        final String pid = tag.getAttributeValue("pid");
        final String selfId = tag.getAttributeValue("selfId");
        final String disabled = tag.getAttributeValue("disabled");
        final String businessInPutId = tag.getAttributeValue("businessInPutId");

        final SysOrganizationService sysOrganizationService = SpringUtil.getBean(SysOrganizationServiceImpl.class);


        /*表单input 点击后模态窗口*/
        final IModel input = modelFactory.createModel();
        IOpenElementTag inputStart = modelFactory.createOpenElementTag("input");
        final ICloseElementTag inputEnd = modelFactory.createCloseElementTag("input");
        inputStart = modelFactory.setAttribute(inputStart,"id","orgTreeSelectorInput");
        inputStart = modelFactory.setAttribute(inputStart,"type","text");
        inputStart = modelFactory.setAttribute(inputStart,"class","tpl-form-input");
        inputStart = modelFactory.setAttribute(inputStart,"placeholder","请选择上级机构");
        inputStart = modelFactory.setAttribute(inputStart,"onclick","modalOrgTree("+selfId+","+businessInPutId+")");
        inputStart = modelFactory.setAttribute(inputStart,"name","orgTree");



        if (pid == null || pid.equals("0")) {
            inputStart = modelFactory.setAttribute(inputStart,"value","未选择机构");
        } else {
            inputStart = modelFactory.setAttribute(inputStart,"value",sysOrganizationService.findById(pid).getOrgName());
        }
        if (StringUtils.isNotEmpty(disabled)) {
            inputStart = modelFactory.setAttribute(inputStart,"disabled",disabled);
        }

        input.add(inputStart);
        input.add(inputEnd);

        /*模态弹窗模板*/
        Object templFragmentObj = computeFragment(context, "~{public/orgTree :: orgTreeTips}");
        final IModel treeTempl = modelFactory.parse(context.getTemplateData(),templFragmentObj.toString());


        /*机构树js内容*/
        Object jsFragmentObj = computeFragment(context, "~{public/orgTree :: OrgTreeTipsJs}");
        final IModel js = modelFactory.parse(context.getTemplateData(),jsFragmentObj.toString());


        final IModel nodes = modelFactory.createModel();
        nodes.insertModel(nodes.size(),input);
        nodes.insertModel(nodes.size(),treeTempl);
        nodes.insertModel(nodes.size(),js);
        /*Instruct the engine to replace this entire element with the specified model.*/
        structureHandler.replaceWith(nodes, false);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
