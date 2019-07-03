package net.onebean.sodium.common.dictionary;

import net.onebean.sodium.model.DicDictionary;
import net.onebean.sodium.service.DicDictionaryService;
import net.onebean.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @Auther 0neBean
 * 字典工具类
 */
@Service
public class DictionaryUtils {


    @Autowired
    private DicDictionaryService dicDictionaryService;


    /**
     * 静态字典
     */
    private static Map<String,List<DicDictionary>> dictionary = new HashMap<>();

    public void init(){
        List<DicDictionary> list = dicDictionaryService.findAll();
        List<DicDictionary> templist;
        if(CollectionUtil.isNotEmpty(list)){
            for(DicDictionary d:list){
                if (dictionary.get(d.getCode()) != null) {
                    templist = dictionary.get(d.getCode());
                } else {
                    templist = new ArrayList<>();
                }
                templist.add(d);
                dictionary.put(d.getCode(),templist);
            }
        }
    }

    /**
     * 获取字典词组
     * @param code
     * @return
     */
    public static List<DicDictionary> getDicGroup(String code){
        List<DicDictionary> list = dictionary.get(code);
        Collections.sort(list, new Comparator<DicDictionary>(){
            /*
             * int compare(Person p1, Person p2) 返回一个基本类型的整型，
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            @Override
            public int compare(DicDictionary p1, DicDictionary p2) {
                //按照DicDictionary的排序字段进行升序排列
                if(p1.getSort() > p2.getSort()){
                    return 1;
                }
                if(p1.getSort() == p2.getSort()){
                    return 0;
                }
                return -1;
            }
        });
        return list;
    }

    /**
     * 获取字典数据
     * @param code
     * @return
     */
    public static String dic(String code,String val){
        String result = "nullDic";
        List<DicDictionary> list = dictionary.get(code);
        for (DicDictionary dicDictionary : list) {
            if (dicDictionary.getVal().equals(val)) {
                result =  dicDictionary.getDic();
            }
        }
        return result;
    }

}
