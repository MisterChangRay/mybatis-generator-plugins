# myBatisGeneratorPlugins
一些mybatis-generator扩展插件集合

#### 已实现功能
- 自动添加swagger2注解到实体类



#### maven引用
```xml
 <dependency>
    <groupId>com.github.misterchangray.mybatis.generator.plugins</groupId>
    <artifactId>myBatisGeneratorPlugins</artifactId>
    <version>1.0</version>
  </dependency>
```


#### 怎么使用 how to use
增加依赖到你的pom.xml文件 mybatis 节点下,如下：<br>
add dependency to your pom.xml on mybatis node. like:
``` xml
<!-- maven  -->
<build>
    <finalName>common-core</finalName>
    <!--  ...  -->
    <plugins>
      <!--mybatis 逆向工程插件-->
      <plugin>
        <groupId>org.mybatis.generator</groupId>
        <artifactId>mybatis-generator-maven-plugin</artifactId>
        <version>1.3.5</version>
        <configuration>
          <verbose>true</verbose>
          <overwrite>true</overwrite>
        </configuration>
        <dependencies>
          <!--  us plugin  -->
          <dependency>
            <groupId>com.github.misterchangray.mybatis.generator.plugins</groupId>
            <artifactId>myBatisGeneratorPlugins</artifactId>
            <version>1.0</version>
          </dependency>
  
        </dependencies>
      </plugin>
    </plugins>
  </build>
```
-----------------------------------------------

#### 1. 自动添加swagger2注解到实体类
generatorConfig.xml中增加插件配置
``` xml
        <!-- 自动为entity生成swagger2文档-->
        <plugin type="mybatis.generator.plugins.GeneratorSwagger2Doc">
          <property name="apiModelAnnotationPackage" value="io.swagger.annotations.ApiModel" />
          <property name="apiModelPropertyAnnotationPackage" value="io.swagger.annotations.ApiModelProperty" />
        </plugin>
```



####  runtime environment
- OS Microsoft Windows 10 Pro
- Java 8
- springMVC 4.3
- Mybitis 3.4
- Mysql 5.5.50
- Restful interface
- Maven 3.5.3
- Git 2.14.1
- Swagger 2.6.1
