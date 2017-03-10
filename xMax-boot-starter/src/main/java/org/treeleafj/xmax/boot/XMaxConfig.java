package org.treeleafj.xmax.boot;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.treeleafj.xmax.boot.exception.GlobalExceptionHandler;
import org.treeleafj.xmax.date.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author leaf
 * @date 2017-03-10 16:00
 */
@ConfigurationProperties("xMax")
public class XMaxConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        converters.add(fastConverter);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(Date.class, new DateFormatter() {

            @Override
            public Date parse(String text, Locale locale) throws ParseException {
                if (text.length() <= DateUtils.DATE_PATTERN.length()) {
                    return DateUtils.parseDate(text);
                } else {
                    return DateUtils.parseDateTime(text);
                }
            }
        });
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }


}
