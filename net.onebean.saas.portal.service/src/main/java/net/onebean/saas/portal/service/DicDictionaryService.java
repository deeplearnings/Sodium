package net.onebean.saas.portal.service;
import net.onebean.saas.portal.model.DicDictionary;
import net.onebean.core.IBaseBiz;

public interface DicDictionaryService extends IBaseBiz<DicDictionary> {
    /**
     * 根据code  查询组下一个排序值
     * @param code
     * @return
     */
    Integer findGroupOrderNextNum(String code);
}