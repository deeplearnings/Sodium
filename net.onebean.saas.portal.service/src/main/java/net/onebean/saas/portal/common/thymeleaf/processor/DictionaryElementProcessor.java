package net.onebean.saas.portal.common.thymeleaf.processor;

import net.onebean.saas.portal.common.dictionary.DictionaryUtils;
import net.onebean.saas.portal.model.DicDictionary;
import net.onebean.util.CollectionUtil;
import net.onebean.util.StringUtils;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.*;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.List;

/**
 * @author 0neBean
 * 自定义thymeleaf标签 字典code转义 功能实现
 */
@Component
public class DictionaryElementProcessor extends AbstractElementTagProcessor {


    private static final String PREFIX = "dic";
    private static final String TAG_NAME = "code";
    private static final int PRECEDENCE = 1000;

    /**
     * 无参构造器
     */
    public DictionaryElementProcessor() {
        super(TemplateMode.HTML,PREFIX,TAG_NAME,true,null,false,PRECEDENCE);
    }

    /**
     * 构造器
     * @param dialectPrefix 前缀
     */
    public DictionaryElementProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML,dialectPrefix,TAG_NAME,true,null,false,PRECEDENCE);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        /*元素工厂用于创建元素*/
        final IModelFactory modelFactory = context.getModelFactory();
        final ICloseElementTag selectEnd = modelFactory.createCloseElementTag("select");
        IModel select = initSelect(modelFactory,tag);
        select = initOption(modelFactory,tag,select);
        select.add(selectEnd);
        /*Instruct the engine to replace this entire element with the specified model.*/
        structureHandler.replaceWith(select, false);
    }

    /**
     * 初始化option
     * @param modelFactory 工厂
     * @param tag 标签
     * @param select 选择标签
     * @return IModel
     */
    private IModel initOption(IModelFactory modelFactory,IProcessableElementTag tag,IModel select){
        final String code = tag.getAttributeValue("code");
        final String value = tag.getAttributeValue("value");
        final String pattern = tag.getAttributeValue("param-pattern");
        final String disabledOption = tag.getAttributeValue("disabledOption");


        List<DicDictionary> list = DictionaryUtils.getDicGroup(code);
        if(CollectionUtil.isNotEmpty(list)){
            if (StringUtils.isNotEmpty(pattern)) {
                final IModel option = modelFactory.createModel();
                IOpenElementTag optionStart = modelFactory.createOpenElementTag("option");
                final ICloseElementTag optionEnd = modelFactory.createCloseElementTag("option");
                optionStart =modelFactory.setAttribute(optionStart,"value","  ");
                final IText text = modelFactory.createText("未选择");
                option.add(optionStart);
                option.add(text);
                option.add(optionEnd);
                select.insertModel(select.size(),option);
            }
            for (DicDictionary d : list) {
                final IModel option = modelFactory.createModel();
                IOpenElementTag optionStart = modelFactory.createOpenElementTag("option");
                final ICloseElementTag optionEnd = modelFactory.createCloseElementTag("option");
                final IText text = modelFactory.createText(d.getDic());
                if (StringUtils.isNotEmpty(value)) {
                    String [] valueArr = value.split(",");
                    for (String s : valueArr) {
                        if(s.equals(d.getVal())){
                            optionStart = modelFactory.setAttribute(optionStart,"selected","");
                        }
                    }
                }
                optionStart = modelFactory.setAttribute(optionStart,"value",d.getVal());
                if (StringUtils.isNotEmpty(disabledOption)){
                    String [] disabledOptions = disabledOption.split(",");
                    for (String s : disabledOptions) {
                        if (s.equals(d.getVal())){
                            optionStart = modelFactory.setAttribute(optionStart,"disabled","");
                        }
                    }
                }

                option.add(optionStart);
                option.add(text);
                option.add(optionEnd);
                select.insertModel(select.size(),option);
            }
        }
            return select;
    }

    /**
     * 初始化select
     * @param modelFactory 工厂
     * @param tag 标签
     * @return IModel
     */
    private IModel initSelect(IModelFactory modelFactory,IProcessableElementTag tag){
        final String name = tag.getAttributeValue("name");
        final String id = tag.getAttributeValue("id");

        final String disabled = tag.getAttributeValue("disabled");
        final String pattern = tag.getAttributeValue("param-pattern");
        final String multiple = tag.getAttributeValue("multiple");
        final String inChildList = tag.getAttributeValue("inChildList");

        final IModel select = modelFactory.createModel();
        IOpenElementTag selectStart = modelFactory.createOpenElementTag("select");

        selectStart = modelFactory.setAttribute(selectStart,"data-am-selected","{btnSize: 'sm'}");
        selectStart = modelFactory.setAttribute(selectStart,"name",name);
        selectStart = modelFactory.setAttribute(selectStart,"id",id);
        StringBuffer classStr = new StringBuffer();


        if(StringUtils.isNotEmpty(inChildList)){
            classStr.append(" onebean-child-list-item");
        }
        if (StringUtils.isNotEmpty(disabled)) {
            selectStart = modelFactory.setAttribute(selectStart,"disabled",disabled);
        }
        if (StringUtils.isNotEmpty(pattern)) {
            classStr.append(" paramInput onebean-param-select-box");
            selectStart = modelFactory.setAttribute(selectStart,"param-pattern",pattern);
        }
        if (StringUtils.isNotEmpty(multiple)){
            selectStart = modelFactory.setAttribute(selectStart,"multiple","");
        }

        selectStart = modelFactory.setAttribute(selectStart,"class",classStr.toString());
        select.add(selectStart);
        return select;
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
