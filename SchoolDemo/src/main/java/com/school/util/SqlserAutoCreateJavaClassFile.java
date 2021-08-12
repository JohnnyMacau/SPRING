package com.school.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.*;

public class SqlserAutoCreateJavaClassFile {
    //数据库配置四要素
    private static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String url = "jdbc:sqlserver://localhost:1433;DatabaseName=数据名";
    private static String username = "数据库用户";
    private static String password = "数据库密码";
    //项目路径
    private static String canonicalPath = new File(Class.class.getClass().getResource("/").getPath()).getParentFile().getParent()+"/src/main/java";
    //保存工作路径下的文件夹
    private static String packge="当前项目生产Java文件保存路径";
    //查询字段sql
    private static String sql="SELECT A.TABLE_NAME, \n" +
            "C.NAME AS COLUME_NAME,\n" +
            "CONVERT(NVARCHAR(100),ISNULL(E.VALUE,''))AS'REMARKS',\n" +
            "D.NAME AS CLOUMN_TYPE,\n" +
            "C.COLUMN_ID AS CLOUME_INDEX\n" +
            "FROM INFORMATION_SCHEMA.TABLES A\n" +
            "JOIN SYS.TABLES B ON A.TABLE_NAME=B.NAME\n" +
            "JOIN SYS.COLUMNS C ON B.OBJECT_ID=C.OBJECT_ID\n" +
            "JOIN SYS.TYPES D ON C.SYSTEM_TYPE_ID=D.SYSTEM_TYPE_ID\n" +
            "LEFT JOIN SYS.EXTENDED_PROPERTIES E ON (C.OBJECT_ID=E.MAJOR_ID AND C.COLUMN_ID=E.MINOR_ID)\n" +
            "WHERE TABLE_NAME=?\n" +
            "ORDER BY C.COLUMN_ID ASC;";

    public static void main(String[] args) {
        Connection ct=null;
        PreparedStatement sm=null;
        ResultSet rs=null;
        ResultSet rsTable=null;
        try {
            //1、加载驱动(把需要的驱动程序加入内存)
            Class.forName(driverName);
            //2、得到连接(指定连接到哪个数据源、数据库的用户名和密码)
            //如果配置数据源的时候选择的是windows NT验证方式，则不需要数据库的用户名和密码
            ct= DriverManager.getConnection(url,username,password);
            if(!ct.isClosed()){
                System.out.println("连接成功");
            }else {
                System.out.println("连接异常");
            }
            DatabaseMetaData metaData = ct.getMetaData();
            //只查询表
            rs = metaData.getTables(null, null, null,
                    new String[] { "TABLE" });
            while (rs.next()) {
                List<List<String>> tableMsg=new ArrayList<>();
                String tableName = rs.getString("TABLE_NAME");
                sm=ct.prepareStatement(sql);
                sm.setString(1,tableName);
                ResultSet resultSet = sm.executeQuery();
                while (resultSet.next()){
                    List<String> fieldMsg=new ArrayList<>();
                    String columeName=resultSet.getString(2);
                    String columeRemakes=resultSet.getString(3);
                    String columeType=resultSet.getString(4);
                    fieldMsg.add(columeName);
                    if(columeRemakes==null){
                        fieldMsg.add("");
                    }else {
                        fieldMsg.add(columeRemakes);
                    }
                    fieldMsg.add(columeType);
                    tableMsg.add(fieldMsg);
                }
                createClasss(tableName,tableMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //关闭资源，关闭顺序:先创建后关闭，后创建先关闭
            try {
                //为了程序健壮
                if(rs!=null){
                    rs.close();
                }
                if(sm!=null){
                    sm.close();
                }
                if(ct!=null){
                    ct.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String changeDbType(String fieldType) {
        fieldType = fieldType.toUpperCase();
        if(fieldType.length()>=3&&fieldType.substring(fieldType.length()-3).equals("INT")){
            return "Integer";
        }else if(fieldType.equals("FLOAT")){
            return "double";
        }else if(fieldType.equals("DOUBLE")){
            return "double";
        }else {
            return "String";
        }
    }

    private static String changeClassFieldName(String dbTableName){
        StringBuffer classFieldName=new StringBuffer();
        for(int i=0;i<=dbTableName.length()-1;i++){
            char c = dbTableName.charAt(i);
            if('_'==c){
                String s = (dbTableName.charAt(i + 1) + "").toUpperCase();
                classFieldName.append(s);
                ++i;
            }else {
                classFieldName.append(c);
            }
        }
        return classFieldName.toString();
    }

    private static String changeTableName(String tableName){
        String s = changeClassFieldName(tableName);
        String className=(s.charAt(0)+"").toUpperCase()+s.substring(1);
        return className;
    }

    private static void createClasss(String tableName, List<List<String>> tableField){
        StringBuffer cl = new StringBuffer();
        cl.append("package "+packge+";\n");
        cl.append("import com.baomidou.mybatisplus.annotation.*;\n" +
                "import lombok.*;\n");
        cl.append("@Setter\n" +
                "@Getter\n" +
                "@AllArgsConstructor\n" +
                "@NoArgsConstructor\n"+
                "@TableName(\""+tableName+"\")\n");
        String tableClassName = changeTableName(tableName);
        cl.append("public class "+tableClassName+"{");
        for(List<String> row:tableField){
            String columeName=row.get(0);
            String columeRemakes=row.get(1);
            String columeType=row.get(2);
            cl.append("\n\t@TableField(\""+columeName+"\")");
            String classFieldName=changeClassFieldName(columeName);
            String dbType = changeDbType(columeType);
            cl.append("\n\tprivate "+dbType+" "+classFieldName+";");
            if(!"".equals(columeRemakes)){
                cl.append("//"+columeRemakes);
            }
        }
        cl.append("\n}");
        putClassFile(canonicalPath+"/"+packge.replaceAll("[.]","/")+"/"+tableClassName+".java",cl.toString());
    }

    private static void putClassFile(String path,String classMsg){
        File classFile = new File(path);
        if(!classFile.getParentFile().exists()){
            classFile.getParentFile().mkdirs();
        }
        OutputStream outputStream=null;
        try {
            if(!classFile.exists()){
                classFile.createNewFile();
            }
            outputStream=new FileOutputStream(classFile);
            outputStream.write(classMsg.getBytes());
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(outputStream!=null){
                    outputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
