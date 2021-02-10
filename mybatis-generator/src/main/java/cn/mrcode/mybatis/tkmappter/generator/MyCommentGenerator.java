package cn.mrcode.mybatis.tkmappter.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.internal.util.StringUtility;
import tk.mybatis.mapper.generator.MapperCommentGenerator;

/**
 * 注释自定义类: 解决在 getter/setter 默认生成重复注释的 bug *
 * @author 99299
 */

public class MyCommentGenerator extends MapperCommentGenerator {
    /**
     * getter方法注释
     */
    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        method.addJavaDocLine("/**");
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            sb.append(" * ");
            sb.append(introspectedColumn.getRemarks());
            method.addJavaDocLine(sb.toString());
            method.addJavaDocLine(" *");
        }
        sb.setLength(0);
        sb.append(" * @return ");
        sb.append(introspectedColumn.getActualColumnName());
        //		if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
        // 			sb.append(" - ");
        // 			sb.append(introspectedColumn.getRemarks());//
        // 			}
        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" */");
    }

    /**
     * setter方法注释
     */
    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        method.addJavaDocLine("/**");
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            sb.append(" * ");
            sb.append(introspectedColumn.getRemarks());
            method.addJavaDocLine(sb.toString());
            method.addJavaDocLine(" *");
        }
        Parameter parm = method.getParameters().get(0);
        sb.setLength(0);
        sb.append(" * @param ");
        sb.append(parm.getName());
        //		if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
        // 			sb.append(" ");
        // 			sb.append(introspectedColumn.getRemarks());
        // 		}
        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" */");
    }
}
