package net.onebean.saas.portal.common.thymeleaf.processor;

import net.onebean.saas.portal.common.thymeleaf.base.OneBeanBaseAbstractElementTagProcessor;
import com.eakay.util.StringUtils;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.*;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author 0neBean
 * 自定义thymeleaf标签 图标选择器 功能实现
 */
@Component
public class IconElementProcessor extends OneBeanBaseAbstractElementTagProcessor {

    private static final String PREFIX = "picker";
    private static final String TAG_NAME = "icon";
    private static final int PRECEDENCE = 1000;

    /**
     * 无参构造器
     */
    public IconElementProcessor() {
        super(TemplateMode.HTML,PREFIX,TAG_NAME,true,null,false,PRECEDENCE);
    }

    /**
     * 构造器
     * @param dialectPrefix 前缀
     */
    public IconElementProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML,dialectPrefix,TAG_NAME,true,null,false,PRECEDENCE);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        /*元素工厂用于创建元素*/
        final IModelFactory modelFactory = context.getModelFactory();
        final String name = tag.getAttributeValue("name");
        final String id = tag.getAttributeValue("id");
        final String disabled = tag.getAttributeValue("disabled");
        final String value = tag.getAttributeValue("value");

        /*表单input 点击后模态窗口*/
        final IModel input = modelFactory.createModel();
        IOpenElementTag inputStart = modelFactory.createOpenElementTag("input");
        final ICloseElementTag inputEnd = modelFactory.createCloseElementTag("input");
        inputStart = modelFactory.setAttribute(inputStart,"name",name);
        inputStart = modelFactory.setAttribute(inputStart,"id",id);
        inputStart = modelFactory.setAttribute(inputStart,"type","text");
        inputStart = modelFactory.setAttribute(inputStart,"class","oneBean-icon-picker");
        if (StringUtils.isNotEmpty(disabled) && disabled.equals("disabled")) {
            inputStart = modelFactory.setAttribute(inputStart,"disabled",disabled);
        }
        if (StringUtils.isNotEmpty(value)) {
            inputStart = modelFactory.setAttribute(inputStart,"value",value);
        }
        input.add(inputStart);
        input.add(inputEnd);

        /*机构树js内容*/
        Object jsFragmentObj = computeFragment(context, "~{public/iconPicker :: iconPickerJs}");
        final IModel js = modelFactory.parse(context.getTemplateData(),jsFragmentObj.toString());

        final IModel nodes = modelFactory.createModel();
        nodes.insertModel(nodes.size(),input);
        nodes.insertModel(nodes.size(),js);
        /*Instruct the engine to replace this entire element with the specified model.*/
        structureHandler.replaceWith(nodes, false);
    }
}