package net.onebean.sodium.service;

import net.onebean.core.base.IBaseBiz;
import net.onebean.sodium.model.DicDictionary;

public interface DicDictionaryService extends IBaseBiz<DicDictionary> {
    /**
     * 根据code  查询组下一个排序值
     * @param code
     * @return
     */
    Integer findGroupOrderNextNum(String code);
}