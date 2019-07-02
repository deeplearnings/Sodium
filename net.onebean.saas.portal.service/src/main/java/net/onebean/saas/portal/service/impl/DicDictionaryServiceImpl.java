package net.onebean.saas.portal.service.impl;
import net.onebean.saas.portal.dao.DicDictionaryDao;
import net.onebean.saas.portal.model.DicDictionary;
import net.onebean.saas.portal.service.DicDictionaryService;
import org.springframework.stereotype.Service;
import com.eakay.core.BaseBiz;

@Service
public class DicDictionaryServiceImpl extends BaseBiz<DicDictionary, DicDictionaryDao> implements DicDictionaryService {

    @Override
    public Integer findGroupOrderNextNum(String code) {
        return baseDao.findGroupOrderNextNum(code);
    }
}