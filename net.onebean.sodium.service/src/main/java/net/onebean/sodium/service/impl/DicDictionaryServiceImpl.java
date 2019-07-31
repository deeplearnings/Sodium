package net.onebean.sodium.service.impl;
import net.onebean.sodium.dao.DicDictionaryDao;
import net.onebean.sodium.model.DicDictionary;
import net.onebean.sodium.service.DicDictionaryService;
import org.springframework.stereotype.Service;
import net.onebean.core.base.BaseBiz;

@Service
public class DicDictionaryServiceImpl extends BaseBiz<DicDictionary, DicDictionaryDao> implements DicDictionaryService {

    @Override
    public Integer findGroupOrderNextNum(String code) {
        return baseDao.findGroupOrderNextNum(code);
    }
}