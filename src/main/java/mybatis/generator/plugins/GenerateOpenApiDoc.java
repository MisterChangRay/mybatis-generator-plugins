package mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * @author tanshin
 * date 2022/02/28
 * description 根据数据库注释对实体类生成注解，扩展支持Open API，允许开启或者关闭注解生成
 *
 */
public class GenerateOpenApiDoc extends PluginAdapter {
    /**
     * 替换Schema(description=xxx) value包含的.
     */
    public static final String SPLIT_CHAR = "$";

    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        /*
           Schema的description值默认为实体类的简称；
         */
        String classAnnotation;
        boolean useFullPathName = Boolean.parseBoolean(properties.getProperty("useFullPathName", "false"));
        System.out.println(useFullPathName);
        if (!useFullPathName) {
            classAnnotation = "@Schema";
        } else {
            // 替换掉完整路径名称中的.为其他符号
            String topLevelClassName = String.valueOf(topLevelClass.getType());
            classAnnotation = "@Schema(description=\"" + topLevelClassName.replace(".", SPLIT_CHAR) + "\")";
        }

        if (!topLevelClass.getAnnotations().contains(classAnnotation)) {
            topLevelClass.addAnnotation(classAnnotation);
        }

        String schemaAnnotationPackage = "io.swagger.v3.oas.annotations.media.Schema";

        topLevelClass.addImportedType(schemaAnnotationPackage);

        String apiJavaDoc = properties.getProperty("javaDoc", "false");
        if("TRUE".equals(apiJavaDoc.toUpperCase())) {
            field.addJavaDocLine("/**");
            field.addJavaDocLine(introspectedColumn.getRemarks());
            field.addJavaDocLine("*/");
        }

        // 去掉了实体类Java属性
        field.addAnnotation("@Schema(description=\"" + introspectedColumn.getRemarks() + "\")");
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
