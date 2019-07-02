package net.onebean.saas.portal.enumModel;

/**
 * CodeDatabaseTable枚举类
 * @author 0neBean
 */
public enum  CodeDatabaseTableEnum {
    GENREATER_TYPE_CRUD("crud"),
    GENREATER_TYPE_TREE("tree"),
    GENREATER_TYPE_CHILD("child"),
    GENREATER_SCOPE_PAGE("page"),
    GENREATER_SCOPE_CONTROLLER("controller"),
    GENREATER_SCOPE_SERVICE("service")
    ;

    private String value;

    CodeDatabaseTableEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
