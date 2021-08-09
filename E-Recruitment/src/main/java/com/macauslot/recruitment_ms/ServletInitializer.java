package com.macauslot.recruitment_ms;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Component;

/**
 * @author jim.deng
 */
@Component
public class ServletInitializer extends SpringBootServletInitializer {



    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RecruitmenthkApplication.class);
    }


    //配置session的cookie
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.getSessionCookieConfig().setName("mslot-ID");
        servletContext.getSessionCookieConfig().setMaxAge(60 * 60 / 2  );//半小时
    }

}
