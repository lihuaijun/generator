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
package org.mybatis.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.GameleyShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 用来从数据库表结构生成Mybatis相关的Domain/Dao/Mapper XML等相关文件.
 * 数据库连接、生成文件所在目录等配置信息，请在generator.xml文件中进行配置。
 */
public class MyBatisGeneratorMain {
    public static void main(String[] args) {
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        boolean merge = false;
        File configurationFile = new File(MyBatisGeneratorMain.class.getResource("/config.xml").getPath());
        ConfigurationParser cp = new ConfigurationParser(warnings);
        try {
            Configuration config = cp.parseConfiguration(configurationFile);
            ShellCallback shellCallback = new GameleyShellCallback(overwrite, merge);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
    //        myBatisGenerator.generate(new NullProgressCallback());
            myBatisGenerator.generate(null);
            System.out.println("成功");

        } catch (Exception e) {
            System.out.println("失败");
            e.printStackTrace();
        }
    }

}
