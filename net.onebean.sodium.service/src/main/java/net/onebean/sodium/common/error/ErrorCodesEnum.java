package net.onebean.sodium.common.error;


/**
 * 错误码定义
 * @author 0neBean
 * 1-4位 1000 代表 租售车辆门户
 * 异常类型：5～6位，标识数据库、接口、缓存、权限等
 *	01-参数异常，参数有效性校验错误，在接口的response 的msg中请将校验有问题的参数返回给调用方；
 *	02-数据库异常，操作数据库异常，log的message中要有sql、参数信息，sql如果能在堆栈中体现，可以不放在message中；
 *	03-缓存异常，如对redis、JVM缓存操作失败；
 *	04-权限异常，功能权限、数据权限类异常；
 *	05-接口异常，接口调用异常，log 的message中要包含接口地址、接口参数信息，如果能在堆栈中体现，可以不放在message中；
 *	06-业务异常, 业务执行异常， 比如”用户名已存在”，此种类型的异常通常前端要关注；
 *	07-其它运行异常，如JAVA程序运行时异常；
 *	08-消息队列异常，如对rabbitMq 的操作失败；
 *	...
 *	99-除以上外的异常
 *	异常序列号：6～10位，每个模块自定义，要求模块范围内唯一性
 */
public enum ErrorCodesEnum {
    SUSSESS("0",""),
    //01
    REQUEST_PARAM_ERROR("1000010001","请求参数校验异常"),
    //02
    NONE_QUERY_DATA("1000020001","没有对应的数据"),
    GET_DATE_ERR("1000020002","数据库查询数据异常"),
    TENANT_INFO_EXIST("1000020003","租户信息已经存在"),
    INIT_TENANT_INFO_ERROR("1000020004","初始化租户数据库信息失败"),
    //05
    CLOUD_API_RESP_ERR("1000050001","远程调用接口失败"),
    //07
    JSON_CAST_ERROR("1000070001","序列化JSON异常"),
    REF_ERROR("1000070002","反射对象属性异常"),
    VELOCITY_ERROR("1000070003","模板生成异常"),
    //08
    RABBIT_MQ_BUSSINES_ERR("1000080001","mq业务处理异常"),
    //99
    OTHER("1000999999","操作失败")
    ;

    private String code;

    private String msg;

    private ErrorCodesEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String code(){
        return this.code;
    }

    public String msg(){
        return this.msg;
    }
}
