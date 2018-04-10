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
public class GeneratorBatchDelete  extends PluginAdapter {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorBatchInsert.class);
    private static final String BATCH_DELETE = "batchDelete";


    public boolean validate(List<String> list) {
        return true;
    }


    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if(null != interfaze) {
            interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        }

        logger.debug("(批量删除插件):" + introspectedTable.getMyBatis3XmlMapperFileName() + "增加batchDelete实现方法。");

        String entityClassName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        Method mBatchInsert = new Method();
        mBatchInsert.setName(BATCH_DELETE);
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
        String logicalDelete = this.getContext().getProperty("logicalDeleteBy");

        String where = "";
        if(null == logicalDelete) {
            //根据主键删除
            String key1 = null;
            List<IntrospectedColumn> keyColms = introspectedTable.getPrimaryKeyColumns();
            if(null != keyColms) key1 = keyColms.get(0).getActualColumnName();
            if(null == key1) {
                logger.debug("(批量删除插件):" + introspectedTable.getMyBatis3XmlMapperFileName() + "增加batchDelete实现方法失败，table:" + introspectedTable.getFullyQualifiedTable() + "没有主键");
                return  false;
            }

            // 1. batchInsert
            XmlElement batchInsertEle = new XmlElement("delete");
            batchInsertEle.addAttribute(new Attribute("id", BATCH_DELETE));
            // 参数类型
            batchInsertEle.addAttribute(new Attribute("parameterType", "map"));

            batchInsertEle.addElement(new TextElement("delete from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));

            batchInsertEle.addElement(new TextElement(" where "));


            // 添加foreach节点
            XmlElement foreachElement = new XmlElement("foreach");
            foreachElement.addAttribute(new Attribute("collection", "list"));
            foreachElement.addAttribute(new Attribute("item", "item"));
            foreachElement.addAttribute(new Attribute("separator", ","));
            foreachElement.addAttribute(new Attribute("open", "("));
            foreachElement.addAttribute(new Attribute("close", ")"));


            where = where.substring(0, where.length() - 4);
            batchInsertEle.addElement(new TextElement(where));
            document.getRootElement().addElement(batchInsertEle);

        } else {
            //逻辑删除


        }

        logger.debug("(批量删除插件):" + introspectedTable.getMyBatis3XmlMapperFileName() + "增加batchDelete实现方法。");
        return true;
    }

}
