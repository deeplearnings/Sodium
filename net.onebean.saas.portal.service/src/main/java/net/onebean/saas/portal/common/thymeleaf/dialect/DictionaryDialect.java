package net.onebean.saas.portal.common.thymeleaf.dialect;

import net.onebean.saas.portal.common.thymeleaf.processor.DictionaryElementProcessor;
import net.onebean.saas.portal.common.thymeleaf.processor.TenantInfoElementProcessor;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 0neBean
 * 自定义thymeleaf方言 字典
 */
@Component
public class DictionaryDialect implements IProcessorDialect {


    @Override
    public String getPrefix() {
        return "dic";
    }

    @Override
    public int getDialectProcessorPrecedence() {
        return 1000;
    }

    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new DictionaryElementProcessor(dialectPrefix));
        processors.add(new TenantInfoElementProcessor (dialectPrefix));
        return processors;
    }

    @Override
    public String getName() {
        return "dic";
    }
}