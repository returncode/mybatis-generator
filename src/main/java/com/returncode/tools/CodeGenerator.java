package com.returncode.tools;

import cn.hutool.setting.dialect.Props;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/**
 * 数据库代码生成工具
 * https://baomidou.com/pages/981406/
 */
public class CodeGenerator {
    /** 项目路径 */
    private static final String PROJECT = System.getProperty("user.dir");
    /** 数据库连接 */
    private static String url;
    /** 数据库用户 */
    private static String username;
    /** 数据库密码 */
    private static String password;
    /** 作者 */
    private static String author;
    /** 输出目录 */
    private static String outputDir;
    /** 包命名 */
    private static String packagePath;
    /** 模块目录 */
    private static String module;
    /** 实体目录 */
    private static String entity;
    /** XML目录 */
    private static String mapperXml;
    /** 转换表名 */
    private static String[] tables;
    /** 去除表前缀 */
    private static String tablePrefix;

    public static void main(String[] args) {
        initCodeGeneratorConfig();
        FastAutoGenerator.create(new DataSourceConfig.Builder(url, username, password))
                // 全局配置
                .globalConfig(builder -> { builder
                    // 设置作者
                    .author(author)
                    //禁止打开输出目录
                    .disableOpenDir()
                    // 指定输出目录
                    .outputDir(outputDir);
                })
                // 包配置
                .packageConfig(builder -> { builder
                    // 设置父包名
                    .parent(packagePath)
                    // 设置父包模块名
                    .moduleName(module)
                    //设置entity包名
                    .entity(entity)
                    // 设置mapperXml生成路径
                    .pathInfo(Collections.singletonMap(OutputFile.xml, mapperXml));
                })
                // 注入配置
                .injectionConfig(builder -> {builder
                    // 注入配置
                    .beforeOutputFile((tableInfo, objectMap) -> {
                        tableInfo.getImportPackages().remove(TableName.class.getCanonicalName());
                        tableInfo.setConvert(false);
                    });
                })
                // 策略配置
                .strategyConfig(builder -> {
                    builder.enableSchema()
                            .addInclude(tables)
                            .addTablePrefix(tablePrefix);
                    builder.entityBuilder()
                            .formatFileName("%sPO")
                            .enableFileOverride();
                    builder.controllerBuilder()
                            .enableRestStyle()
                            .enableFileOverride();
                    builder.serviceBuilder()
                            .superServiceClass("")
                            .superServiceImplClass("")
                            .enableFileOverride();
                    builder.mapperBuilder()
                            .superClass("")
                            .enableFileOverride()
                            .enableBaseResultMap()
                            .enableBaseColumnList();
                }).execute();
    }

    /** 初始化代码生成设置 */
    private static void initCodeGeneratorConfig() {
        Props props = new Props("code.properties");
        // 数据库连接
        url = props.getProperty("url");
        // 数据库用户
        username = props.getProperty("username");
        // 数据库密码
        password = props.getProperty("password");
        // 作者
        author = props.getProperty("author");
        // 输出目录
        outputDir = PROJECT + props.getProperty("outputDir");
        // 包命名
        packagePath = props.getProperty("packagePath");
        // 模块目录
        module = props.getProperty("module");
        // 实体目录
        entity = props.getProperty("entity");
        // XML目录
        mapperXml = PROJECT + props.getProperty("mapperXml");
        // 转换表名
        tables = props.getProperty("tables").split(",");
        // 去除表前缀
        tablePrefix = props.getProperty("tablePrefix");
    }
}
