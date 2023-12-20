package com.returncode.tools;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;
import java.util.Scanner;

/**
 * 数据库代码生成工具
 */
public class CodeGenerator {
    // 项目路径
    private static String projectPath = System.getProperty("user.dir");
    // 基本包名
    private static String basePackage = "com.github.returncode";
    // 数据源
    private static String url = "jdbc:mysql://127.0.0.1:3306/sample?useSSL=false";
    private static String username = "root";
    private static String password = "root";
    // 数据源配置
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig.Builder(url, username, password);

    public static void main(String[] args) {
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {
                    builder.author(scanner("开发人员名称"))
                            .fileOverride()
                            .dateType(DateType.ONLY_DATE)
                            .outputDir(projectPath + "/src/main/java");
                })
                .packageConfig(builder -> {
                    final String moduleName = scanner("模块名");
                    builder.parent(basePackage)
                            .moduleName(moduleName)
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, projectPath + "/src/main/resources/mapper/" + moduleName));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(scanner("表名，多个英文逗号分割").split(","))
                            .addTablePrefix(scanner("要过滤的表前缀") + "_")
                            .controllerBuilder()
                                .enableRestStyle()
                            .entityBuilder()
                                .formatFileName("%sPO")
                            .mapperBuilder()
                                .enableBaseResultMap()
                                .enableBaseColumnList()
                                .enableMapperAnnotation();

                })
                .templateEngine(new VelocityTemplateEngine())
                .templateConfig(builder -> {
                    builder.controller("templates/controller.java")
                            .service("templates/service.java")
                            .serviceImpl("templates/serviceImpl.java")
                            .entity("templates/entity.java")
                            .mapper("templates/mapper.java")
                            .mapperXml("/templates/mapper.xml");
                })
                .injectionConfig(builder -> {
                    builder.beforeOutputFile((tableInfo, stringObjectMap) -> {
                        tableInfo.setConvert(false).getImportPackages().remove(TableName.class.getCanonicalName());
                    });
                })
                .execute();
    }


    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

}
