package net.onebean.saas.portal.common.thymeleaf.base;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.*;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * thymeleaf ElementTag 处理器基类
 * @author 0neBean
 */
public abstract class OneBeanBaseAbstractElementTagProcessor extends AbstractElementTagProcessor {

    public OneBeanBaseAbstractElementTagProcessor(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence) {
        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence);
    }

    @Override
    protected void doProcess(ITemplateContext iTemplateContext, IProcessableElementTag iProcessableElementTag, IElementTagStructureHandler iElementTagStructureHandler) {

    }

    /**
     * 表达式 包装 Fragment 异常
     * @param input 表达式
     * @return boolean
     */
    static boolean shouldBeWrappedAsFragmentExpression(String input) {
        int inputLen = input.length();
        if (inputLen > 2 && input.charAt(0) == '~' && input.charAt(1) == '{') {
            return false;
        } else {
            int bracketLevel = 0;
            int paramLevel = 0;
            boolean inLiteral = false;
            int n = inputLen;
            int i = 0;

            while(true) {
                while(n-- != 0) {
                    char c = input.charAt(i);
                    if (c >= 'a' && c <= 'z' || c == ' ') {
                        ++i;
                    } else {
                        if (c == '\'') {
                            inLiteral = !inLiteral;
                        } else if (!inLiteral) {
                            if (c == '{') {
                                ++bracketLevel;
                            } else if (c == '}') {
                                --bracketLevel;
                            } else if (bracketLevel == 0) {
                                if (c == '(') {
                                    ++paramLevel;
                                } else if (c == ')') {
                                    --paramLevel;
                                } else {
                                    if (c == '=' && paramLevel == 1) {
                                        return true;
                                    }

                                    if (c == '~' && n != 0 && input.charAt(i + 1) == '{') {
                                        return false;
                                    }

                                    if (c == ':' && n != 0 && input.charAt(i + 1) == ':') {
                                        return true;
                                    }
                                }
                            }
                        }

                        ++i;
                    }
                }

                return true;
            }
        }
    }

    /**
     * 从模板引擎上下文获取 Fragment
     * @param context 模板引擎上下文
     * @param input 表达式
     * @return Object
     */
    protected static Object computeFragment(ITemplateContext context, String input) {
        IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(context.getConfiguration());
        String trimmedInput = input.trim();
        if (shouldBeWrappedAsFragmentExpression(trimmedInput)) {
            FragmentExpression fragmentExpression = (FragmentExpression)expressionParser.parseExpression(context, "~{" + trimmedInput + "}");
            FragmentExpression.ExecutedFragmentExpression executedFragmentExpression = FragmentExpression.createExecutedFragmentExpression(context, fragmentExpression);
            if (executedFragmentExpression.getFragmentSelectorExpressionResult() == null && executedFragmentExpression.getFragmentParameters() == null) {
                Object templateNameExpressionResult = executedFragmentExpression.getTemplateNameExpressionResult();
                if (templateNameExpressionResult != null) {
                    if (templateNameExpressionResult instanceof Fragment) {
                        return templateNameExpressionResult;
                    }

                    if (templateNameExpressionResult == NoOpToken.VALUE) {
                        return NoOpToken.VALUE;
                    }
                }
            }

            return FragmentExpression.resolveExecutedFragmentExpression(context, executedFragmentExpression, true);
        } else {
            IStandardExpression fragmentExpression = expressionParser.parseExpression(context, trimmedInput);
            Object fragmentExpressionResult;
            if (fragmentExpression != null && fragmentExpression instanceof FragmentExpression) {
                FragmentExpression.ExecutedFragmentExpression executedFragmentExpression = FragmentExpression.createExecutedFragmentExpression(context, (FragmentExpression)fragmentExpression);
                fragmentExpressionResult = FragmentExpression.resolveExecutedFragmentExpression(context, executedFragmentExpression, true);
            } else {
                fragmentExpressionResult = fragmentExpression.execute(context);
            }

            if (fragmentExpressionResult != null && fragmentExpressionResult != NoOpToken.VALUE) {
                if (!(fragmentExpressionResult instanceof Fragment)) {
                    throw new TemplateProcessingException("Invalid fragment specification: \"" + input + "\": expression does not return a Fragment object");
                } else {
                    return fragmentExpressionResult;
                }
            } else {
                return fragmentExpressionResult;
            }
        }
    }
}
