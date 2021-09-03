# myBatisGeneratorPlugins
一些mybatis-generator扩展插件集合

#### 已实现功能
- 自动添加swagger2注解到实体类
- 扩展set方法,返回this实例;方便链式调用



#### maven引用
```xml
<!-- https://mvnrepository.com/artifact/com.github.misterchangray.mybatis.generator.plugins/myBatisGeneratorPlugins -->
<dependency>
    <groupId>com.github.misterchangray.mybatis.generator.plugins</groupId>
    <artifactId>myBatisGeneratorPlugins</artifactId>
    <version>1.2</version>
</dependency>
```


-----------------------------------------------
### 详细介绍

#### 1. 自动添加swagger2注解到实体类
自动为`entity`类生成`swagger2`文档注解，注解内容为数据库`comment`内容
``` xml
        <!-- 自动为entity生成swagger2文档-->
        <plugin type="mybatis.generator.plugins.GeneratorSwagger2Doc">
          <property name="apiModelAnnotationPackage" value="io.swagger.annotations.ApiModel" />
          <property name="apiModelPropertyAnnotationPackage" value="io.swagger.annotations.ApiModelProperty" />
        </plugin>
```
#### 2.扩展entity的`set`方法
扩展entity的`set`方法；返回当前`this`实例，方便链式调用
``` xml
        <!-- 扩展entity的set方法-->
        <plugin type="mybatis.generator.plugins.ExtendEntitySetter" />
```
#### 3.针对第1点中的配置存在的坑，新增了新的方式
ApiModel的value值默认为实体类的简称，不同包名下面如果有同名类将会冲突；
value值不能包含.，否则会造成swagger获取api-docs时，$ref变成"com.xxx"从而去读取当前服务器上的文件，进而请求并不存在的资源：localhost:8080/v3/com.xxx，相关规范可以参考：[https://swagger.io/docs/specification/using-ref/](https://swagger.io/docs/specification/using-ref/)

新增了一个属性useFullPathName，用于控制ApiModel value值，以下是新的配置（使用时需要注释掉旧的配置）：
``` xml
        <!-- 注意plugin名称，需要注释掉GeneratorSwagger2Doc -->
        <plugin type="mybatis.generator.plugins.GenerateSwagger3Doc">
            <!--是否使用完整路径作为apiModel 的value值，默认为false，设置为true时为避免swagger $ref报错将路径名称中的.替换为了$-->
            <property name="useFullPathName" value="false"/>
            <property name="apiModelAnnotationPackage" value="io.swagger.annotations.ApiModel" />
            <property name="apiModelPropertyAnnotationPackage" value="io.swagger.annotations.ApiModelProperty" />
        </plugin>
```



-------------------------------------------------


#### 怎么引入到项目中
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
          <!--  use plugin  -->
          <!-- https://mvnrepository.com/artifact/com.github.misterchangray.mybatis.generator.plugins/myBatisGeneratorPlugins -->
          <dependency>
              <groupId>com.github.misterchangray.mybatis.generator.plugins</groupId>
              <artifactId>myBatisGeneratorPlugins</artifactId>
              <version>1.2</version>
          </dependency>
  
        </dependencies>
      </plugin>
    </plugins>
  </build>
```

-------------------------------------


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
