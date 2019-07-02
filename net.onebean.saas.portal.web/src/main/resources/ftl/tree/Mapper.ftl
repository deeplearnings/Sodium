<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--author ${author}-->
<!--description ${description} mapper-->
<!--date ${create_time}-->
<mapper namespace="${dao_package_name}.${model_name}Dao">

    <!-- 一对多查询关联  -->
    <resultMap type="${model_package_name}.${model_name}" id="defaultResultMap">
        <id property="id" column="id"/>
       <#if field_arr?exists>
           <#list field_arr as item>
                   <result property="${item.columnName}" column="${item.columnName}"/>
           </#list>
       </#if>
    </resultMap>

    <!--查找所有子节点-->
    <resultMap id="treeResultMapSync" type="${model_package_name}.${model_name}" extends="defaultResultMap">
        <collection property="childList" javaType="java.util.ArrayList" column="id" ofType="${model_package_name}.${model_name}" select="findChildSync"></collection>
    </resultMap>

    <!--异步查找子节点,每次查找一级-->
    <select id="findChildAsync" resultType="${model_package_name}.${model_name}" parameterType="long">
        SELECT o.id,o.name as title,IF(EXISTS(SELECT o1.id FROM ${model_name} o1  WHERE o1.parentId = o.id) = 1 ,"folder","item") AS TYPE FROM ${model_name} o
        <where>
            <choose>
                <when test="parentId != null">
                    o.parentId = ${r"${parentId}"}
                </when>
                <otherwise>
                    o.isRoot = 1
                </otherwise>
            </choose>
        </where>
    </select>

    <!--查找所有子节点-->
    <select id="findChildSync" resultMap="treeResultMapSync" parameterType="long">
        SELECT o.* FROM ${model_name} o
        <where>
            o.parentId = ${r"${parentId}"}
        </where>
        ORDER by o.sort asc
    </select>

    
    <select id="getParentIds" parameterType="long" resultType="string">
        SELECT getParentIds(${r"${childId}"},${model_name});
    </select>


    <delete id="deleteSelfAndChildById" parameterType="long">
        DELETE FROM ${model_name}  WHERE FIND_IN_SET(${r"#{id}"},parentIds) OR id = ${r"#{id}"}
    </delete>

    <select id="findChildOrderNextNum" parameterType="long" resultType="int">
        SELECT p.sort + 1 FROM `${model_name}` p
        <where>
            p.parentId = ${r"${parentId}"} ORDER BY p.sort DESC LIMIT 1;
        </where>
    </select>
</mapper>