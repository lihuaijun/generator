/**
 *    Copyright 2006-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/**
 *
 */
package org.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SelectAllPlugin extends PluginAdapter {
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze,
                                   TopLevelClass topLevelClass,
                                   IntrospectedTable introspectedTable) {
        generateMapperSelectAll(interfaze, topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document,
                                           IntrospectedTable introspectedTable) {
        generateXmlSelectAll(document, introspectedTable);
        return true;
    }


    private void generateMapperSelectAll(Interface interfaze,
                                         TopLevelClass topLevelClass,
                                         IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType
                .getNewListInstance();
        FullyQualifiedJavaType listType;
        listType = new FullyQualifiedJavaType(
                introspectedTable.getBaseRecordType());

        importedTypes.add(listType);
        returnType.addTypeArgument(listType);
        importedTypes.add(returnType);
        method.setReturnType(returnType);
        method.setName(introspectedTable.getSelectAllStatementId());

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        if (context.getPlugins().clientSelectAllMethodGenerated(method,
                interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    private void generateXmlSelectAll(Document document,
                                      IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getSelectAllStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                introspectedTable.getBaseResultMapId()));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select "); //$NON-NLS-1$
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getBaseColumnListElement(introspectedTable));
        if (introspectedTable.hasBLOBColumns()) {
            answer.addElement(new TextElement(",")); //$NON-NLS-1$
            answer.addElement(getBlobColumnListElement(introspectedTable));
        }

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        String orderByClause = introspectedTable.getTableConfigurationProperty(PropertyRegistry.TABLE_SELECT_ALL_ORDER_BY_CLAUSE);
        boolean hasOrderBy = StringUtility.stringHasValue(orderByClause);
        if (hasOrderBy) {
            sb.setLength(0);
            sb.append("order by "); //$NON-NLS-1$
            sb.append(orderByClause);
            answer.addElement(new TextElement(sb.toString()));
        }

        if (context.getPlugins().sqlMapSelectAllElementGenerated(
                answer, introspectedTable)) {
            XmlElement parentElement = document.getRootElement();
            parentElement.addElement(answer);
        }
    }

    private XmlElement getBaseColumnListElement(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("include"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("refid", //$NON-NLS-1$
                introspectedTable.getBaseColumnListId()));
        return answer;
    }

    private XmlElement getBlobColumnListElement(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("include"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("refid", //$NON-NLS-1$
                introspectedTable.getBlobColumnListId()));
        return answer;
    }
}
