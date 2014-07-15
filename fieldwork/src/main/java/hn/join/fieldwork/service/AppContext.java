package hn.join.fieldwork.service;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
/**
 * 系统Servlet上下文，在web.xml中引用
 * @author chenjinlong
 *
 */
public class AppContext implements ServletContextListener {

	private static ServletContext servletContext;
	/**
	 * 获取servletContext
	 * @return servletContext
	 */
	public static ServletContext getServletContext() {
		return servletContext;
	}
	/**
	 * 初始化servletContext
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		servletContext = sce.getServletContext();

	}
	/**
	 * 销毁servletContext
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		servletContext = null;

	}

}
