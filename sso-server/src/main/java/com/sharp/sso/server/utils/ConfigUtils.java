package com.sharp.sso.server.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * 应用配置工具类
 *
 * @author QinYupeng
 * @since 2018-09-27 10:03:44
 */
public class ConfigUtils extends PropertyPlaceholderConfigurer {
    private static Properties properties;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        properties = props;
    }

    public static String getProperty(String name) {
        return properties.getProperty(name);
    }

}
