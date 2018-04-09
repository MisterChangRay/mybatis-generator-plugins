package cn.zr.mybatis.generator.plugins;

import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

/**
 * @author Created by rui.zhang on 2018/4/9.
 * @version ver1.0
 * @email misterchangray@hotmail.com
 * @description 根据数据库注释对实体类增加swagger2文档注解
 */
public class GeneratorBatchDelete  extends PluginAdapter {


    public boolean validate(List<String> list) {
        return false;
    }
}
