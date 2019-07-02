package net.onebean.saas.portal.common.thymeleaf.processor;

import net.onebean.saas.portal.common.thymeleaf.base.OneBeanBaseAbstractElementTagProcessor;
import net.onebean.component.SpringUtil;
import net.onebean.saas.portal.service.SysPermissionService;
import net.onebean.saas.portal.service.impl.SysPermissionServiceImpl;
import net.onebean.util.StringUtils;
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
public class MenuElementProcessor extends OneBeanBaseAbstractElementTagProcessor {



    private static final String PREFIX = "tree";
    private static final String TAG_NAME = "menu";
    private static final int PRECEDENCE = 1000;


    /**
     * 默认构造器
     */
    public MenuElementProcessor() {
        super(TemplateMode.HTML,PREFIX,TAG_NAME,true,null,false,PRECEDENCE);
    }

    /**
     * 构造器
     * @param dialectPrefix 前缀
     */
    public MenuElementProcessor(final String dialectPrefix) {
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
        final SysPermissionService sysPermissionService = SpringUtil.getBean(SysPermissionServiceImpl.class);

        /*表单input 点击后模态窗口*/
        final IModel input = modelFactory.createModel();
        IOpenElementTag inputStart = modelFactory.createOpenElementTag("input");
        final ICloseElementTag inputEnd = modelFactory.createCloseElementTag("input");
        inputStart = modelFactory.setAttribute(inputStart,"id","menuTreeSelectorInput");
        inputStart = modelFactory.setAttribute(inputStart,"type","text");
        inputStart = modelFactory.setAttribute(inputStart,"class","tpl-form-input");
        inputStart = modelFactory.setAttribute(inputStart,"placeholder","请选择上级菜单");
        inputStart = modelFactory.setAttribute(inputStart,"onclick","modalmenuTree("+selfId+","+businessInPutId+")");
        inputStart = modelFactory.setAttribute(inputStart,"name","menuTree");

        if (pid == null || pid.equals("0")) {
            inputStart = modelFactory.setAttribute(inputStart,"value","未选择菜单");
        } else {
            inputStart = modelFactory.setAttribute(inputStart,"value",sysPermissionService.findById(pid).getDescritpion());
        }
        if (StringUtils.isNotEmpty(disabled) && disabled.equals("disabled")) {
            inputStart = modelFactory.setAttribute(inputStart,"disabled",disabled);
        }

        input.add(inputStart);
        input.add(inputEnd);

        /*模态弹窗模板*/
        Object templFragmentObj = computeFragment(context, "~{public/menuTree :: menuTreeTips}");
        final IModel treeTempl = modelFactory.parse(context.getTemplateData(),templFragmentObj.toString());


        /*机构树js内容*/
        Object jsFragmentObj = computeFragment(context, "~{public/menuTree :: menuTreeTipsJs}");
        final IModel js = modelFactory.parse(context.getTemplateData(),jsFragmentObj.toString());

        final IModel nodes = modelFactory.createModel();
        nodes.insertModel(nodes.size(),input);
        nodes.insertModel(nodes.size(),treeTempl);
        nodes.insertModel(nodes.size(),js);
        /*Instruct the engine to replace this entire element with the specified model.*/
        structureHandler.replaceWith(nodes, false);
    }
}
