package net.onebean.sodium.common.thymeleaf.dialect;

import net.onebean.sodium.common.thymeleaf.processor.IconElementProcessor;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 0neBean
 * 自定义thymeleaf标签 dic字典标签
 */
@Component
public class PickerDialect implements IProcessorDialect {


    @Override
    public String getPrefix() {
        return "picker";
    }

    @Override
    public int getDialectProcessorPrecedence() {
        return 1000;
    }

    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new IconElementProcessor(dialectPrefix));
        return processors;
    }

    @Override
    public String getName() {
        return "picker";
    }
}