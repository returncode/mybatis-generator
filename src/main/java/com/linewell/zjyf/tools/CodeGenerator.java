package com.linewell.zjyf.tools;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 数据库代码生成工具
 */
public class CodeGenerator {
    // 项目路径
    private static String projectPath = System.getProperty("user.dir");
    // 基本包名
    private static String basePackage = "com.linewell.zjyf.model";
    // 数据源
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://127.0.0.1:3306/archives_center_conform?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf8&useSSL=false&allowMultiQueries=true";
    private static String username = "admin";
    private static String password = "admin@123";


    public static void main(String[] args) {

        AutoGenerator mpg = new AutoGenerator();

        /**
         * 全局配置
         */
        mpg.setGlobalConfig(new GlobalConfig()
                        //生成文件的输出目录
                        .setOutputDir(projectPath + "/src/main/java")
                        //是否覆盖已有文件
                        .setFileOverride(false)
                        //开发人员
                        .setAuthor(scanner("开发人员名称"))
                        //是否打开输出目录
                        .setOpen(false)
                        //开启 swagger2 模式
                        .setSwagger2(false)
                        //开启 BaseResultMap
                        .setBaseResultMap(true)
                        //开启 baseColumnList
                        .setBaseColumnList(true)
                //指定生成的主键的ID类型
                //.setIdType(IdType.UUID)
        );

        /**
         * 数据源配置
         */
        mpg.setDataSource(new DataSourceConfig()
                .setDbType(DbType.MYSQL)
                .setDriverName(driverName)
                .setUrl(url)
                .setUsername(username)
                .setPassword(password)
                .setTypeConvert(new MySqlTypeConvert() {
                    // 自定义数据库表字段类型转换【可选】
                    @Override
                    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                        if (fieldType.toLowerCase().contains("datetime")) {
                            return DbColumnType.DATE;
                        }
                        return super.processTypeConvert(globalConfig, fieldType);
                    }
                }));

        /**
         * 包配置
         */
        final String moduleName = scanner("模块名");
        mpg.setPackageInfo(new PackageConfig()
                .setModuleName(moduleName)
                .setParent(basePackage)
        );

        /**
         * 自定义配置
         */
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/" + moduleName + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        focList.add(new FileOutConfig("/templates/entity.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/java/com/linewell/zjyf/model/" + moduleName + "/entity/pojo/" + tableInfo.getEntityName()+"DO" + StringPool.DOT_JAVA;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        /**
         * 模板配置
         */
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.setTemplate(new TemplateConfig()
                .setController("templates/controller.java")
                .setMapper("templates/mapper.java")
                .setService("templates/service.java")
                .setServiceImpl("templates/serviceImpl.java")
                .setEntity(null)
                .setXml(null));

        /**
         * 策略配置
         */
        mpg.setStrategy(new StrategyConfig()
                        .setNaming(NamingStrategy.underline_to_camel)
                        .setColumnNaming(NamingStrategy.underline_to_camel)
                        //生成 @RestController 控制器
                        .setRestControllerStyle(true)
                        .setInclude(scanner("表名，多个英文逗号分割").split(","))
                        .setControllerMappingHyphenStyle(true)
                        .setTablePrefix(scanner("要过滤的表前缀") + "_")
                //逻辑删除属性名称
                //.setLogicDeleteFieldName("delete_flag")
        );

        /**
         * 执行生成
         */
        mpg.execute();
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
