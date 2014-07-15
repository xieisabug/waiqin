package hn.join.fieldwork.cache;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.shiro.ShiroException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.ClientConfigBuilder;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.ConfigLoader;
import com.hazelcast.core.HazelcastInstance;
/**
 * 负责所有缓存的主要管理组件，它返回Cache 实例
 * @author chenjinlong
 *
 */
public class HazelcastAdapterShiroCacheManager implements CacheManager,
		Initializable, Destroyable, ServletContextAware {

	private static final Logger logger = LoggerFactory
			.getLogger(HazelcastAdapterShiroCacheManager.class);

	private HazelcastInstance hazelcastInstance;

	private String clientConfigLocation;

	private boolean cacheManagerImplicitlyCreated = false;

	private ServletContext servletContext;
	
	
	

	public void setClientConfigLocation(String clientConfigLocation) {
		this.clientConfigLocation = clientConfigLocation;
	}

	@Override
	public void destroy() throws Exception {
		cacheManagerImplicitlyCreated = false;
	}

	@Override
	public void init() throws ShiroException {
		try {
			hazelcastInstance = createInstance();
			cacheManagerImplicitlyCreated = true;
		} catch (ServletException e) {
			throw new ShiroException(e);
		}

	}

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		if(!cacheManagerImplicitlyCreated){
			throw new CacheException("HazelcastInstance can't create.");
		}
		try {
			Map<K, V> shiroSessionMap = hazelcastInstance.getMap(name);
			return new HazelcastAdapterShiroCache<K,V>(shiroSessionMap);
		} catch (Exception ex) {
			throw new CacheException(ex);
		}
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;

	}
	/**
	 * 读取配置文件，生成一个Hazelcast实例
	 * @return
	 * @throws ServletException
	 */
	private HazelcastInstance createInstance() throws ServletException {
		if (logger.isWarnEnabled()) {
			logger.warn("Creating HazelcastClient, make sure this node has access to an already running cluster...");
		}

		URL configUrl = getConfigURL(clientConfigLocation);
		ClientConfig clientConfig;
		if (configUrl == null) {
			clientConfig = new ClientConfig();
			clientConfig.setUpdateAutomatic(true);
			clientConfig.setInitialConnectionAttemptLimit(3);
			clientConfig.setReconnectionAttemptLimit(5);
		} else {
			try {
				clientConfig = new ClientConfigBuilder(configUrl).build();
			} catch (IOException e) {
				throw new ServletException(e);
			}
		}
		return HazelcastClient.newHazelcastClient(clientConfig);

	}
	/**
	 * 获得配置文件中的URL
	 * @param configLocation
	 * @return
	 * @throws ServletException
	 */
	private URL getConfigURL(final String configLocation)
			throws ServletException {
		URL configUrl = null;
		try {
			configUrl = servletContext.getResource(configLocation);
		} catch (MalformedURLException e) {
		}
		if (configUrl == null) {
			configUrl = ConfigLoader.locateConfig(configLocation);
		}

		if (configUrl == null && logger.isWarnEnabled()) {
			logger.warn("Can't find configuration file,use default configuration.");
			
		}
		return configUrl;
	}


}
