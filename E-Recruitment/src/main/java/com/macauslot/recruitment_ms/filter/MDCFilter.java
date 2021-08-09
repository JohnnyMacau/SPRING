package com.macauslot.recruitment_ms.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

@WebFilter(filterName = "MDCFilter", urlPatterns = {"/*"})
@Order(3)
public class MDCFilter implements javax.servlet.Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

	    try {
	        HttpServletRequest httpReq = (HttpServletRequest)request;
	        MDC.put("requestIp", httpReq.getRemoteAddr());

	        chain.doFilter(request, response);
	    } finally {
	        MDC.clear();
	    }
	}

	 @Override
     public void destroy() {
     }
}
