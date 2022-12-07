# myBatisGeneratorPlugins
本项目基于已存在的 mybatis-generator 做了扩展处理, 增加更多插件功能。

[![GitHub (pre-)release](https://img.shields.io/github/release/misterchangray/mybatis-generator-plugins/all.svg)](https://github.com/misterchangray/mybatis-generator-plugins) 
[![GitHub issues](https://img.shields.io/github/issues/misterchangray/mybatis-generator-plugins.svg)](https://github.com/misterchangray/mybatis-generator-plugins/issues) 
[![GitHub closed issues](https://img.shields.io/github/issues-closed/misterchangray/mybatis-generator-plugins.svg)](https://github.com/misterchangray/mybatis-generator-plugins/issues?q=is%3Aissue+is%3Aclosed) 
[![GitHub](https://img.shields.io/github/license/misterchangray/mybatis-generator-plugins.svg)](./LICENSE)


#### 已实现功能
- 自动添加swagger2注解到实体类
- 扩展set方法,返回this实例;方便链式调用
- 增加只添加java注释
- 兼容swagger3 且 可以生成全包名路径
- 增加`GenerateOpenApiDoc`支持Open API的注解
  - @ApiModel -> @Schema
  - @ApiModelProperty(value="") -> @Schema(value="")
  ```xml
      <plugin type="mybatis.generator.plugins.GenerateOpenApiDoc">
          <!-- 启用只生成 java 注释 -->
          <property name="javaDoc" value="false"/>
          <!-- 是否使用完整路径作为 Schema 的 description 值，默认为false，设置为true时为避免swagger $ref报错将路径名称中的.替换为了$-->
          <property name="useFullPathName" value="false"/>
          <!-- 仅适用于GenerateSwagger3Doc 是否使用数据表的注释(comment)作为 ApiModel 的 value 值，默认为false。如果设置为true,需要在jdbcConnection设置useInformationSchema为true 见2.1示例-->
          <property name="useTableComment" value="true"/>
          <!-- 仅适用于GenerateSwagger3Doc 是否在实体类字段上标注required，如@ApiModelProperty(required = true, value = "用户账号")，默认为false-->
          <property name="markFieldRequired" value="true"/>
      </plugin>
  ```

#### 1.快速使用
##### 1. maven 在buid节点中引入本插件, 如下示例
```xml
<!-- maven  -->
<build>
    <finalName>common-core</finalName>

    <!--  .其他的配置..  -->
    
    <plugins>
      <!--  .其他的插件配置..  -->

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
          <!--  use plugin  -->
          <!-- https://mvnrepository.com/artifact/com.github.misterchangray.mybatis.generator.plugins/myBatisGeneratorPlugins -->
          <dependency>
              <groupId>com.github.misterchangray.mybatis.generator.plugins</groupId>
              <artifactId>myBatisGeneratorPlugins</artifactId>
              <version>1.4</version>
          </dependency>
        </dependencies>
      </plugin>
    </plugins>
  </build>
```
##### 2. 进行必要的生成配置
```xml
<!-- generatorConfig.xml， 此配置文件参考项目resource下同名文件,或者百度搜索配置  -->

<generatorConfiguration>
    <!-- ... 其他配置 ...  -->
    <context id="testTables" targetRuntime="MyBatis3" >
        <!-- ... 其他配置 ...  -->
        
        <!-- ... 引入本插件 ...  -->

        <!-- swagger3 插件兼容支持swagger3, 且生成全路径包名
            <plugin type="mybatis.generator.plugins.GenerateSwagger3Doc">
        -->
        <plugin type="mybatis.generator.plugins.GeneratorSwagger2Doc">
            <!-- 启用生成 ApiModel  -->
            <property name="apiModelAnnotationPackage" value="io.swagger.annotations.ApiModel" />
            <!-- 启用生成 ApiModelProperty  -->
            <property name="apiModelPropertyAnnotationPackage" value="io.swagger.annotations.ApiModelProperty" />
            <!-- 启用只生成 java 注释 -->
            <property name="apiModelJavaDoc" value="false"/>
            <!-- 生成JAVA注释 默认 true -->
            <property name="generatorJavaDoc" value="false"/>
            <!-- 生成 swagger 注释 默认 true -->
            <property name="generatorSwaggerDoc" value="false"/>
            <!-- GenerateSwagger3Doc支持 是否使用完整路径作为apiModel 的value值，默认为false，设置为true时为避免swagger $ref报错将路径名称中的.替换为了$-->
            <property name="useFullPathName" value="false"/>
        </plugin>
        
        <!-- 启用自动生成get set 方法, 支持链式调用 --> -->
        <plugin type="mybatis.generator.plugins.ExtendEntitySetter" />
 
    </context>
</generatorConfiguration>

```
###### 2.1 使用数据表的注释(comment)作为 ApiModel 的 value 值时，配置如下
```xml
<!--数据库地址及登陆账号密码 改成你自己的配置-->
        <jdbcConnection
                driverClass="com.mysql.jdbc.Driver"
                connectionURL=""
                userId=""
                password="">
            <!-- 设置 useInformationSchema 属性为 true,GenerateSwagger3Doc才能获取到表注释 -->
          <property name="useInformationSchema" value="true"/>
        </jdbcConnection>
```
-------------------------------------------------


#### 3. 怎么引入到项目中
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
              <version>1.4</version>
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


##### 4. 感谢
1. @Windman
2. @pdxh

