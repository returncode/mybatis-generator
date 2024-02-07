package com.returncode.tools;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
    /** 数据库连接 */
    private static String url;
    /** 数据库用户 */
    private static String username;
    /** 数据库密码 */
    private static String password;
    /** 作者 */
    private static String author;
    /** 输出目录 */
    private static String codePath;
    /** 包命名 */
    private static String packagePath;
    /** 模块目录 */
    private static String module;
    /** 实体目录 */
    private static String entity;
    /** XML目录 */
    private static String xmlPath;
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
                .outputDir(codePath);
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
                .pathInfo(Collections.singletonMap(OutputFile.xml, xmlPath));
            })
            // 注入配置
            .injectionConfig(builder -> { builder
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
        String propsJson = ResourceUtil.readUtf8Str("CodeGeneratorConfig.json");
        JSONObject props = JSONUtil.parseObj(propsJson);
        // 数据库配置
        JSONObject dataSource = props.getJSONObject("dataSource");
        // 数据库连接
        url = dataSource.getStr("url");
        // 数据库用户
        username = dataSource.getStr("username");
        // 数据库密码
        password = dataSource.getStr("password");

        // 全局配置
        JSONObject globalConfig = props.getJSONObject("globalConfig");
        // 作者
        author = globalConfig.getStr("author");
        // 项目地址
        String projectPath = globalConfig.getStr("projectPath");
        // 输出目录
        codePath = projectPath + globalConfig.getStr("codePath");

        // 包配置
        JSONObject packageConfig = props.getJSONObject("packageConfig");
        // 包命名codePath
        packagePath = packageConfig.getStr("packagePath");
        // 模块目录
        module = packageConfig.getStr("module");
        // 实体目录
        entity = packageConfig.getStr("entity");
        // XML目录
        xmlPath = projectPath + packageConfig.getStr("xmlPath");

        // 策略配置
        JSONObject strategyConfig = props.getJSONObject("strategyConfig");
        // 转换表名
        tables = strategyConfig.getStr("tables").split(",");
        // 去除表前缀
        tablePrefix = strategyConfig.getStr("tablePrefix");
    }
}
