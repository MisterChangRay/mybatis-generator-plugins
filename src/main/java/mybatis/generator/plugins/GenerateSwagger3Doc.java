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
           value值不能包含.，否则会造成swagger获取api-docs时，$ref变成"com.xxx"从而去读取当前服务器上的文件，进而请求并不存在的如"http://localhost:8080/v3/com.xxx"，
           相关规范可以参考：https://swagger.io/docs/specification/using-ref/
         */
        String classAnnotation;
        // 是否使用完整类路径作为ApiModel的value，默认为false
        boolean useFullPathName = Boolean.parseBoolean(properties.getProperty("useFullPathName", "false"));
        if (!useFullPathName) {
            // 未设置value时swagger默认使用类名作为value
            classAnnotation = "@ApiModel";
        } else {
            // 替换掉完整路径名称中的.为其他符号
            String topLevelClassName = String.valueOf(topLevelClass.getType());
            classAnnotation = "@ApiModel(value=\"" + topLevelClassName.replace(".", SPLIT_CHAR) + "\")";
        }

        String generatorJavaDoc = properties.getProperty("generatorJavaDoc", "TRUE");
        String generatorSwaggerDoc = properties.getProperty("generatorSwaggerDoc", "TRUE");

        if("TRUE".equals(generatorJavaDoc.toUpperCase())) {
            generatorJavaDoc(field, introspectedColumn);
        }
        if("TRUE".equals(generatorSwaggerDoc.toUpperCase())) {
           generatorSwaggerAnnotation(topLevelClass, field, classAnnotation, introspectedColumn);
        }

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }


    /**
     * 生成swagger3相关注解
     * @param topLevelClass
     * @param field
     * @param classAnnotation
     * @param introspectedColumn
     */
    private void generatorSwaggerAnnotation(TopLevelClass topLevelClass, Field field, String classAnnotation, IntrospectedColumn introspectedColumn) {
        String apiModelAnnotationPackage = properties.getProperty("apiModelAnnotationPackage");
        String apiModelPropertyAnnotationPackage = properties.getProperty("apiModelPropertyAnnotationPackage");
        if (null == apiModelAnnotationPackage) {
            apiModelAnnotationPackage = "io.swagger.annotations.ApiModel";
        }
        if (null == apiModelPropertyAnnotationPackage) {
            apiModelPropertyAnnotationPackage = "io.swagger.annotations.ApiModelProperty";
        }

        if (!topLevelClass.getAnnotations().contains(classAnnotation)) {
            topLevelClass.addAnnotation(classAnnotation);
        }
        topLevelClass.addImportedType(apiModelAnnotationPackage);
        topLevelClass.addImportedType(apiModelPropertyAnnotationPackage);
        // 去掉了实体类Java属性
        field.addAnnotation("@ApiModelProperty(value=\"" + introspectedColumn.getRemarks() + "\")");
    }

    /**
     * 生成java文档注释
     * @param field
     * @param introspectedColumn
     */
    private void generatorJavaDoc(Field field, IntrospectedColumn introspectedColumn) {
        field.addJavaDocLine("/**");
        field.addJavaDocLine(introspectedColumn.getRemarks());
        field.addJavaDocLine("*/");
    }
}
