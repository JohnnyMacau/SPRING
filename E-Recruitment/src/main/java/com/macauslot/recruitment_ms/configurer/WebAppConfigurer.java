package com.macauslot.recruitment_ms.configurer;

import com.macauslot.recruitment_ms.filter.XSSEscapeFilter;
import com.macauslot.recruitment_ms.interceptor.AuthenticationInterceptor;
import com.macauslot.recruitment_ms.interceptor.HrTopPageInterceptor;
import com.macauslot.recruitment_ms.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.CacheControl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * springboot拦截器配置类
 *
 * @author jim.deng
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }
    @Bean
    public HrTopPageInterceptor hrTopPageInterceptor() {
        return new HrTopPageInterceptor();
    }
    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    /**
     * xss过滤拦截器
     */
    @Bean
    public FilterRegistrationBean xssFilterRegistrationBean() {
        FilterRegistrationBean initXssFilterBean = new FilterRegistrationBean();
        initXssFilterBean.setFilter(new XSSEscapeFilter());
        initXssFilterBean.setOrder(2);
        initXssFilterBean.setEnabled(true);
        initXssFilterBean.addUrlPatterns("/*");
        initXssFilterBean.setDispatcherTypes(DispatcherType.REQUEST);
        return initXssFilterBean;
    }

    @Bean
    public ViewResolver configureViewResolver() {
        InternalResourceViewResolver vr = new InternalResourceViewResolver();
        vr.setRedirectHttp10Compatible(false);
        return vr;
    }

    /**
     * tomcat 配置
     */
    @Value("${http.port}")
    Integer httpPort;


//https://www.cnblogs.com/sufferingStriver/p/9026902.html
//    @Bean
//    public TomcatServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint constraint = new SecurityConstraint();
//                constraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                constraint.addCollection(collection);
//                context.addConstraint(constraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
//        return tomcat;
//    }
//
//    @Bean
//    public Connector createHTTPConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//    //同时启用http（8088）、https（8443）两个端口
//        connector.setScheme("http");
//        //Connector监听的http的端口号
//        System.err.println("httpPort--->"+httpPort);
//        connector.setPort(httpPort);
//        connector.setSecure(false);
//        //监听到http的端口号后转向到的https的端口号
////        connector.setRedirectPort(httpsPort);
//        return connector;
//    }


    @Bean
    public ConfigurableServletWebServerFactory configurableServletWebServerFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.setPort(httpPort);
        return tomcat;
    }


//    @Bean
//    public WebServerFactoryCustomizer webServerFactoryCustomizer() {
//        return (WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>) factory -> {
//            factory.setPort(8090);
//            factory.set
//        };
//    }


    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            servletContext.getSessionCookieConfig().setName("mslot-ID");
            servletContext.getSessionCookieConfig().setMaxAge(60 * 60 / 2);//半小时
        };

    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //黑名单
        List<String> loginForbidList = new ArrayList<>();
        loginForbidList.add("/applicant/**");
        loginForbidList.add("/hr_top2.html");
//        loginForbidList.add("/hr_top3.html");
//        loginForbidList.add("/hr_top4.html");
//        loginForbidList.add("/hr_top5.html");
        loginForbidList.add("/hr_top6.html");
        loginForbidList.add("/hr_top7.html");


        //白名单
        List<String> loginAllowList = new ArrayList<>();
        //注册
        loginAllowList.add("/applicant/top0");
        loginAllowList.add("/applicant/checkSessionTimeout");
        loginAllowList.add("/applicant/checkLogin");

        loginAllowList.add("/applicant/top1");

        loginAllowList.add("/hr_top3.html");
        loginAllowList.add("/hr_top4.html");
        loginAllowList.add("/hr_top5.html");

        loginAllowList.add("/applicant/top3");
        loginAllowList.add("/applicant/top4");
        loginAllowList.add("/applicant/top5");

        loginAllowList.add("/applicant/t1/data1");
        loginAllowList.add("/applicant/t3/data1");
        loginAllowList.add("/applicant/t3/data2");
        loginAllowList.add("/applicant/t4/data");
        loginAllowList.add("/applicant/t5/data1");
        loginAllowList.add("/applicant/t5/data2");

        //登录
        loginAllowList.add("/applicant/session");
        //注销
        loginAllowList.add("/applicant/delete/session");
        //忘记密码
        loginAllowList.add("/applicant/forgetPassword");

        loginAllowList.add("/applicant/pageNo");

      /*  List<String> hrTopPageforbidlist = new ArrayList<>();
        hrTopPageforbidlist.add("/hr_top3.html");
        hrTopPageforbidlist.add("/hr_top4.html");
        hrTopPageforbidlist.add("/hr_top5.html");*/

        registry.addInterceptor(loginInterceptor())
                .addPathPatterns(loginForbidList)
                .excludePathPatterns(loginAllowList);
    /*    registry.addInterceptor(hrTopPageInterceptor())
                .addPathPatterns(hrTopPageforbidlist);*/
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**");    // 权限拦截器拦截所有请求

        /**
         * 需求:
         *      所有開戶人士過期警告為:填表超时,未完成注册,资料会流失 
         *      所有重登錄人士過期警告為:會話過期
         *
         * 概括:loginInterceptor目前没什么作用;
         * 开户人士点击分页tab会全部禁止, 因此HrTopPageInterceptor的notFinish弹框警告专为重登录人士而设置;
         * 当上述两种人士点击"下一步"按钮时,AuthenticationInterceptor会拦截 根据session中的"reLogin"的flag来弹出不同警告框
         * 对于在hr_top1.html中的操作: 对开户人士无任何要求,session过期也会重新生成来将top1资料放入;(开户人士点击分页tab会全部禁止)
         *                  对重登陆人士,则会在点"下一步"按钮时,通过对参数验证(当作注册)来查出参数不足,弹出警告
         *                    重登陆人士  在hr_top1.html点击分页tab 会走 HrTopPageInterceptor的notFinish
         *
         *
         *
         *
         */


    }

    /**
     * 注入路径匹配规则
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //设置忽略请求URL的大小写
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);
        configurer.setPathMatcher(matcher);
        //设置匹配规则
        /*
         setUseSuffixPatternMatch : 设置是否是后缀模式匹配，如“/user”是否匹配/user.*，默认值为true。
             true:  /user.html，/user.aa，/user.*都能是正常访问/user的。
             false:  只能访问/user或者/user/( 这个前提是setUseTrailingSlashMatch 设置为true了)。

         setUseTrailingSlashMatch : 设置是否自动后缀路径模式匹配，如“/user”是否匹配“/user/”，默认true即匹配
            true:  /user，/user/都能正常访问。
            false:  只能访问/user了。
         */
        configurer.setUseSuffixPatternMatch(false).
                setUseTrailingSlashMatch(true);
    }

    @Bean
    public TaskExecutor getTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setQueueCapacity(20);
        taskExecutor.setThreadNamePrefix("TaskExecutorPool-");

        return taskExecutor;
    }

    /*
     * 精确设定缓存资源。使用配置文件可以粗略设置缓存时间。
     * 指定png、jpg、jpeg、gif结尾的文件，缓存时间为4小时。
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**/*.png","/**/*.jpg","/**/*.jpeg","/**/*.gif")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(4, TimeUnit.HOURS).cachePublic());
    }



}
