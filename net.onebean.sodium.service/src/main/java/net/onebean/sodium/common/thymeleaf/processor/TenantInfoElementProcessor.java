package net.onebean.sodium.common.thymeleaf.processor;

import net.onebean.sodium.model.TenantInfo;
import net.onebean.component.SpringUtil;
import net.onebean.sodium.service.TenantInfoService;
import net.onebean.sodium.service.impl.TenantInfoServiceImpl;
import net.onebean.util.CollectionUtil;
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
public class TenantInfoElementProcessor extends AbstractElementTagProcessor {


    private static final String PREFIX = "dic";
    private static final String TAG_NAME = "tenant-info";
    private static final int PRECEDENCE = 1000;

    /**
     * 无参构造器
     */
    public TenantInfoElementProcessor() {
        super(TemplateMode.HTML,PREFIX,TAG_NAME,true,null,false,PRECEDENCE);
    }
    /**
     * 构造器
     * @param dialectPrefix 前缀
     */
    public TenantInfoElementProcessor(final String dialectPrefix) {
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

        TenantInfoService tenantInfoService = SpringUtil.getBean(TenantInfoServiceImpl.class);
        List<TenantInfo> list = tenantInfoService.findAll();
        if(CollectionUtil.isNotEmpty(list)){

            final IModel option = modelFactory.createModel();
            IOpenElementTag optionStart = modelFactory.createOpenElementTag("option");
            final ICloseElementTag optionEnd = modelFactory.createCloseElementTag("option");
            optionStart =modelFactory.setAttribute(optionStart,"value","");
            option.add(optionStart);
            option.add(optionEnd);
            select.insertModel(select.size(),option);


            for (TenantInfo t : list) {
                final IModel optionT = modelFactory.createModel();
                IOpenElementTag optionStartT = modelFactory.createOpenElementTag("option");
                final ICloseElementTag optionEndT = modelFactory.createCloseElementTag("option");
                final IText textT = modelFactory.createText(t.getTenantName());
                optionStartT = modelFactory.setAttribute(optionStartT,"value",t.getTenantId().toString());
                optionT.add(optionStartT);
                optionT.add(textT);
                optionT.add(optionEndT);
                select.insertModel(select.size(),optionT);
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

        final IModel select = modelFactory.createModel();
        IOpenElementTag selectStart = modelFactory.createOpenElementTag("select");

        selectStart = modelFactory.setAttribute(selectStart,"data-am-selected","{btnSize: 'sm',btnWidth: '100%',btnStyle: 'secondary'}");
        selectStart = modelFactory.setAttribute(selectStart,"name",name);
        selectStart = modelFactory.setAttribute(selectStart,"id",id);
        selectStart = modelFactory.setAttribute(selectStart,"required","");
        selectStart = modelFactory.setAttribute(selectStart,"placeholder","请选择子公司");
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
