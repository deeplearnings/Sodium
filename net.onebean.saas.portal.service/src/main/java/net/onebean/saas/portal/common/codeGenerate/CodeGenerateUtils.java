package net.onebean.saas.portal.common.codeGenerate;


import net.onebean.saas.portal.model.CodeDatabaseField;
import net.onebean.saas.portal.model.CodeDatabaseTable;
import freemarker.template.Template;
import net.onebean.saas.portal.enumModel.CodeDatabaseTableEnum;
import net.onebean.saas.portal.service.SysPermissionService;
import net.onebean.util.DateUtils;
import net.onebean.util.FreeMarkerTemplateUtils;
import net.onebean.util.PropUtil;
import net.onebean.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * 生成代码 工具类
 * @author 0neBean
 */
@Service
public class CodeGenerateUtils {

    @Autowired
    private SysPermissionService sysPermissionService;

    private final static String CHARSET_STR ="utf-8";
    private final static String projectPath = getProjectPath();
    private final static String mapperPath = PropUtil.getInstance().getConfig("apache.freemarker.model.mapper.path",PropUtil.PUBLIC_CONF_FREEMARKER);
    private final static String daoPath = PropUtil.getInstance().getConfig("apache.freemarker.dao.path",PropUtil.PUBLIC_CONF_FREEMARKER);
    private final static String voPath = PropUtil.getInstance().getConfig("apache.freemarker.vo.path",PropUtil.PUBLIC_CONF_FREEMARKER);
    private final static String servicePath = PropUtil.getInstance().getConfig("apache.freemarker.service.path",PropUtil.PUBLIC_CONF_FREEMARKER);
    private final static String serviceImplPath = PropUtil.getInstance().getConfig("apache.freemarker.service.impl.path",PropUtil.PUBLIC_CONF_FREEMARKER);
    private final static String actionPath = PropUtil.getInstance().getConfig("apache.freemarker.action.path",PropUtil.PUBLIC_CONF_FREEMARKER);
    private final static String modelPath = PropUtil.getInstance().getConfig("apache.freemarker.model.path",PropUtil.PUBLIC_CONF_FREEMARKER);
    private final static String pagePath = PropUtil.getInstance().getConfig("apache.freemarker.page.path",PropUtil.PUBLIC_CONF_FREEMARKER);
    private final static String daoPackageName = PropUtil.getInstance().getConfig("apache.freemarker.dao.packagename",PropUtil.PUBLIC_CONF_FREEMARKER);
    private final static String voPackageName = PropUtil.getInstance().getConfig("apache.freemarker.vo.packagename",PropUtil.PUBLIC_CONF_FREEMARKER);
    private final static String modelPackageName =PropUtil.getInstance().getConfig("apache.freemarker.model.packagename",PropUtil.PUBLIC_CONF_FREEMARKER);
    private final static String servicePackageName =PropUtil.getInstance().getConfig("apache.freemarker.service.packagename",PropUtil.PUBLIC_CONF_FREEMARKER);
    private final static String actionPackageName =PropUtil.getInstance().getConfig("apache.freemarker.action.packagename",PropUtil.PUBLIC_CONF_FREEMARKER);

    /**
     * 初始化方法
     * @param table 数据库模型
     */
    protected void init(CodeDatabaseTable table){
        table.setTableNameGenerate(StringUtils.replaceUnderLineAndUpperCase(table.getTableName()));
        table.setMapping(StringUtils.getMappingStr(table.getTableNameGenerate()));
        decorationTableField(table);
    }

    /**
     * 生成代码方法入口
     * @param table 数据库模型
     * @throws Exception 抛出所有异常前端通知用户
     */
    public void generate(CodeDatabaseTable table) throws Exception{
        init(table);
        String generateType = Optional.ofNullable(table).map(CodeDatabaseTable::getGenerateType).orElse("");
        if (generateType.equals(CodeDatabaseTableEnum.GENREATER_TYPE_CRUD.getValue())){
            generateCRUD(table);
        }else if (generateType.equals(CodeDatabaseTableEnum.GENREATER_TYPE_TREE.getValue())){
            generateTree(table);
        }else if (generateType.equals(CodeDatabaseTableEnum.GENREATER_TYPE_CHILD.getValue())){
            generateChild(table);
        }
    }


    /**
     * 生成树型代码
     * @param table 参数体
     */
    private void generateTree(CodeDatabaseTable table) throws Exception{
    }

    /**
     * 生成父子代码
     * @param table 参数体
     */
    private void generateChild(CodeDatabaseTable table){
    }

    /**
     * 生成CRUD代码
     * @param table 参数体
     */
    private void generateCRUD(CodeDatabaseTable table){
        try {
            generateModelFile(table,"/crud/Model.ftl");//生成model文件
            generateDaoFile(table,"/crud/Dao.ftl");//生成Dao文件
            generateMapperFile(table,"/crud/Mapper.ftl");//生成Mapper文件
            generateServiceFile(table,"/crud/Service.ftl");//生成Service文件
            generateServiceImplFile(table,"/crud/ServiceImpl.ftl");//生成ServiceImpl文件
            if(table.getGenerateScope().equals(CodeDatabaseTableEnum.GENREATER_SCOPE_CONTROLLER.getValue()) || table.getGenerateScope().equals(CodeDatabaseTableEnum.GENREATER_SCOPE_PAGE.getValue())){
                generateActionFile(table,"/crud/Controller.ftl");//生成action文件
            }
            if(table.getGenerateScope().equals(CodeDatabaseTableEnum.GENREATER_SCOPE_PAGE.getValue())){
                sysPermissionService.generatePermission(table);
                generateCrudDetailFile(table,"/crud/Detail.ftl");//生成Detail-html文件
                generateCrudListFile(table,"/crud/List.ftl");//生成List-html文件
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回项目路径
     * @return
     */
    private static String getProjectPath(){
        String proPath = System.getProperty("user.dir");
        proPath = proPath.replaceAll("\\\\", "\\\\\\\\");
//        proPath += PropUtil.getInstance().getConfig("apache.freemarker.project.name");
        return proPath;
    }

    private void generateMapperFile(CodeDatabaseTable table,String templateName) throws Exception{
        final String suffix = "Mapper.xml";
        final String path = projectPath+mapperPath+table.getTableNameGenerate()+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("model_package_name",modelPackageName);
        dataMap.put("dao_package_name",daoPackageName);
        dataMap.put("model_name",table.getTableNameGenerate());
        dataMap.put("author",table.getAuthor());
        dataMap.put("description",table.getDescription());
        dataMap.put("create_time",table.getCreateTimeStr());
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }


    private void generateDaoFile(CodeDatabaseTable table,String templateName) throws Exception{
        final String suffix = "Dao.java";
        final String path = projectPath+daoPath+table.getTableNameGenerate()+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("is_split_table",table.getIsSplitTable().equals("1"));
        dataMap.put("dao_package_name",daoPackageName);
        dataMap.put("model_package_name",modelPackageName);
        dataMap.put("model_name",table.getTableNameGenerate());
        dataMap.put("author",table.getAuthor());
        dataMap.put("description",table.getDescription());
        dataMap.put("create_time",table.getCreateTimeStr());
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }


    private void generateServiceFile(CodeDatabaseTable table,String templateName) throws Exception{
        final String suffix = "Service.java";
        final String path = projectPath+servicePath+table.getTableNameGenerate()+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("is_split_table",table.getIsSplitTable().equals("1"));
        dataMap.put("service_package_name",servicePackageName);
        dataMap.put("model_package_name",modelPackageName);
        dataMap.put("vo_package_name",voPackageName);
        dataMap.put("model_name",table.getTableNameGenerate());
        dataMap.put("author",table.getAuthor());
        dataMap.put("description",table.getDescription());
        dataMap.put("create_time",table.getCreateTimeStr());
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }

    private void generateTreeModelFile(CodeDatabaseTable table,String templateName) throws Exception{
        final String suffix = "Tree.java";
        final String path = projectPath+voPath+table.getTableNameGenerate()+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("vo_package_name",voPackageName);
        dataMap.put("model_package_name",modelPackageName);
        dataMap.put("model_name",table.getTableNameGenerate());
        dataMap.put("author",table.getAuthor());
        dataMap.put("description",table.getDescription());
        dataMap.put("create_time",table.getCreateTimeStr());
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }

    private void generateServiceImplFile(CodeDatabaseTable table,String templateName) throws Exception{
        final String suffix = "ServiceImpl.java";
        final String path = projectPath+serviceImplPath+table.getTableNameGenerate()+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("is_split_table",table.getIsSplitTable().equals("1"));
        dataMap.put("vo_package_name",voPackageName);
        dataMap.put("dao_package_name",daoPackageName);
        dataMap.put("service_package_name",servicePackageName);
        dataMap.put("model_package_name",modelPackageName);
        dataMap.put("model_name",table.getTableNameGenerate());
        dataMap.put("author",table.getAuthor());
        dataMap.put("description",table.getDescription());
        dataMap.put("create_time",table.getCreateTimeStr());
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }


    private void generateActionFile(CodeDatabaseTable table,String templateName) throws Exception{
        final String suffix = "Controller.java";
        final String path = initPagePath(projectPath+actionPath+table.getMapping())+table.getTableNameGenerate()+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("dao_package_name",daoPackageName);
        dataMap.put("service_package_name",servicePackageName);
        dataMap.put("model_package_name",modelPackageName);
        dataMap.put("action_package_name",actionPackageName);
        dataMap.put("model_name",table.getTableNameGenerate());
        dataMap.put("author",table.getAuthor());
        dataMap.put("description",table.getDescription());
        dataMap.put("create_time",table.getCreateTimeStr());
        dataMap.put("mapping",table.getMapping());
        dataMap.put("premName",table.getPremName());
        dataMap.put("generateScope",table.getGenerateScope());
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }

    private void generateModelFile(CodeDatabaseTable table,String templateName) throws Exception{
        final String suffix = ".java";
        final String path = projectPath+modelPath+table.getTableNameGenerate()+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("model_package_name",modelPackageName);
        dataMap.put("is_split_table",table.getIsSplitTable().equals("1"));
        dataMap.put("logically_delete",table.getLogicallyDelete().equals("1"));
        dataMap.put("table_prefix",table.getTablePrefix());
        dataMap.put("model_name",table.getTableNameGenerate());
        dataMap.put("model_name_original",table.getTableName());
        dataMap.put("field_arr",table.getChildList());
        dataMap.put("author",table.getAuthor());
        dataMap.put("description",table.getDescription());
        dataMap.put("create_time",table.getCreateTimeStr());
        dataMap.put("generateType",table.getGenerateType());
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }

    private void generateCrudDetailFile(CodeDatabaseTable table,String templateName) throws Exception{
        final String suffix = "detail.html";
        final String path = initPagePath(projectPath+pagePath+table.getMapping())+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("model_name",table.getTableNameGenerate());
        dataMap.put("field_arr",table.getChildList());
        dataMap.put("description",table.getDescription());
        dataMap.put("mapping",table.getMapping());
        dataMap.put("premName",table.getPremName());
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }


    private void generateCrudListFile(CodeDatabaseTable table,String templateName) throws Exception{
        final String suffix = "list.html";
        final String path = initPagePath(projectPath+pagePath+table.getMapping())+suffix;
        File mapperFile = new File(path);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("model_name",table.getTableNameGenerate());
        dataMap.put("field_arr",table.getChildList());
        dataMap.put("description",table.getDescription());
        dataMap.put("mapping",table.getMapping());
        dataMap.put("premName",table.getPremName());
        generateFileByTemplate(templateName,mapperFile,dataMap);
    }

    /**
     * 创建页面的文件夹
     * @param path 路径
     */
    protected static String initPagePath(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path+"\\";
    }

    /**
     * 包装表和字段
     * @param table 数据库模型
     * @return CodeDatabaseTable
     */
    CodeDatabaseTable decorationTableField(CodeDatabaseTable table){
        List<CodeDatabaseField> childList = table.getChildList();
        for (CodeDatabaseField c : childList) {
            c.setMethodName(StringUtils.toUpperCaseFirstOne(c.getColumnName()));
            if (null != c.getPageValidate()){
                c.setValidateArr(c.getPageValidate().split(","));
            }
            if (null != c.getQueryOperator()){
                c.setIsDicQueryParam((c.getQueryOperator().startsWith("dic"))?"1":"0");
            }
        }
        table.setCreateTimeStr(DateUtils.getDateStrByTimestamp(table.getCreateTime()));
        table.setChildList(childList);
        return table;
    }


    /**
     * 根据ftl生成代码方法
     * @param templateName ftl模板名
     * @param file 文件
     * @param dataMap 渲染数据
     * @throws Exception 向上抛出异常
     */
    private void generateFileByTemplate(final String templateName,File file,Map<String,Object> dataMap) throws Exception{
        Template template = FreeMarkerTemplateUtils.getTemplate(templateName);
        FileOutputStream fos = new FileOutputStream(file);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, CHARSET_STR),10240);
        template.process(dataMap,out);
        fos.close();
        out.close();
    }


}
