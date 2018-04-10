package cn.zr.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Created by rui.zhang on 2018/4/9.
 * @version ver1.0
 * @email misterchangray@hotmail.com
 * @description 根据数据库注释对实体类增加swagger2文档注解
 */
public class GeneratorBatchUpdate  extends PluginAdapter {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorBatchInsert.class);
    private static final String BATCH_INSERT = "batchUpdate";

    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if(null != interfaze) {
            interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        }

        logger.debug("(批量插入插件):" + introspectedTable.getMyBatis3XmlMapperFileName() + "增加batchInsert实现方法。");

        String entityClassName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        Method mBatchInsert = new Method();
        mBatchInsert.setName(BATCH_INSERT);
        mBatchInsert.setReturnType(new FullyQualifiedJavaType("List<" + entityClassName + ">"));
        mBatchInsert.addParameter(new Parameter(new FullyQualifiedJavaType("List<" + entityClassName + ">"), "entitys"));
        interfaze.addMethod(mBatchInsert);
        return true;
    }


    /**
     * SQL Map Methods 生成
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     * @param document
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        // 1. batchInsert
        XmlElement batchInsertEle = new XmlElement("insert");
        batchInsertEle.addAttribute(new Attribute("id", BATCH_INSERT));
        // 参数类型
        batchInsertEle.addAttribute(new Attribute("parameterType", "map"));

        batchInsertEle.addElement(new TextElement("insert into " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " ("));
        String cols = "";
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            cols += column.getActualColumnName() + ", ";
        }
        Element element = new TextElement(cols.substring(0, cols.length() - 2));
        batchInsertEle.addElement(element);

        batchInsertEle.addElement( new TextElement(" ) values "));
        // 添加foreach节点
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("separator", ","));
        foreachElement.addAttribute(new Attribute("open", "("));
        foreachElement.addAttribute(new Attribute("close", ")"));

        String props = "";
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            props += "#{" + column.getJavaProperty() + "}, ";
        }
        foreachElement.addElement(new TextElement(props.substring(0, props.length() - 2)));
        batchInsertEle.addElement(foreachElement);

        document.getRootElement().addElement(batchInsertEle);
        logger.debug("(批量插入插件):" + introspectedTable.getMyBatis3XmlMapperFileName() + "增加batchInsert实现方法。");

        return true;
    }



}
