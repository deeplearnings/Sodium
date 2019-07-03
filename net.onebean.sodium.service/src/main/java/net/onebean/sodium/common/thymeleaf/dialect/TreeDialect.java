package net.onebean.sodium.common.thymeleaf.dialect;

import net.onebean.sodium.common.thymeleaf.processor.MenuElementProcessor;
import net.onebean.sodium.common.thymeleaf.processor.OrgTreeProcessor;
import net.onebean.sodium.common.thymeleaf.processor.OrgUserElementProcessor;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 0neBean
 * 自定义thymeleaf方言 树
 */
@Component
public class TreeDialect implements IProcessorDialect {

    @Override
    public String getPrefix() {
        return "tree";
    }

    @Override
    public int getDialectProcessorPrecedence() {
        return 1000;
    }

    @Override
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new OrgTreeProcessor(dialectPrefix));
        processors.add(new MenuElementProcessor(dialectPrefix));
        processors.add(new OrgUserElementProcessor(dialectPrefix));
        return processors;
    }

    @Override
    public String getName() {
        return "tree";
    }
}