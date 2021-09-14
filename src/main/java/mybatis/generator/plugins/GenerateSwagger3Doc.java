package mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * @author Windman1320
 * date 2021/09/03
 * description 根据数据库注释对实体类生成注解，扩展支持swagger3.0，允许开启或者关闭注解生成
 *
 */
public class GenerateSwagger3Doc extends PluginAdapter {
    /**
     * 替换ApiModel(value=xxx) value包含的.
     */
    public static final String SPLIT_CHAR = "$";

    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        /*
           ApiModel的value值默认为实体类的简称，不同包名下面如果有同名类将会冲突；
           value值不能包含.，否则会造成swagger获取api-docs时，$ref变成"com.xxx"从而去读取当前服务器上的文件，进而请求并不存在的如"http://localhost:8080/v3/com.xxx"，相关规范可以参考：https://swagger.io/docs/specification/using-ref/
         */
        String classAnnotation;
        // 是否使用完整类路径作为ApiModel的value，默认为false
        boolean useFullPathName = Boolean.parseBoolean(properties.getProperty("useFullPathName", "false"));
        System.out.println(useFullPathName);
        if (!useFullPathName) {
            // 未设置value时swagger默认使用类名作为value
            classAnnotation = "@ApiModel";
        } else {
            // 替换掉完整路径名称中的.为其他符号
            String topLevelClassName = String.valueOf(topLevelClass.getType());
            classAnnotation = "@ApiModel(value=\"" + topLevelClassName.replace(".", SPLIT_CHAR) + "\")";
        }

        if (!topLevelClass.getAnnotations().contains(classAnnotation)) {
            topLevelClass.addAnnotation(classAnnotation);
        }

        String apiModelAnnotationPackage = properties.getProperty("apiModelAnnotationPackage");
        String apiModelPropertyAnnotationPackage = properties.getProperty("apiModelPropertyAnnotationPackage");
        if (null == apiModelAnnotationPackage) {
            apiModelAnnotationPackage = "io.swagger.annotations.ApiModel";
        }
        if (null == apiModelPropertyAnnotationPackage) {
            apiModelPropertyAnnotationPackage = "io.swagger.annotations.ApiModelProperty";
        }

        topLevelClass.addImportedType(apiModelAnnotationPackage);
        topLevelClass.addImportedType(apiModelPropertyAnnotationPackage);

        String apiJavaDoc = properties.getProperty("apiModelJavaDoc", "false");
        if("TRUE".equals(apiJavaDoc.toUpperCase())) {
            field.addJavaDocLine("/**");
            field.addJavaDocLine(introspectedColumn.getRemarks());
            field.addJavaDocLine("*/");
        }

        // 去掉了实体类Java属性
        field.addAnnotation("@ApiModelProperty(value=\"" + introspectedColumn.getRemarks() + "\")");
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
