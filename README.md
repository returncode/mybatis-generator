# mybatis-generator
mybatis-generator Mysql代码生成工具

1、修改CodeGenerator下基本参数
```java
    // 基本包名
    private static String basePackage = "com.linewell.archives";
    // 数据源
    private static String url = "jdbc:mysql://127.0.0.1:3306/data?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf8&useSSL=false";
    private static String username = "root";
    private static String password = "123456@mysql";
```
2、执行CodeGenerator中的main方法，按控制台提示输入，即可生成对应代码。  

3、如果数据库表没有前缀，随便输入一个字符代替即可。