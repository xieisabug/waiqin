package hn.join.fieldwork.service;

import hn.join.fieldwork.persistence.FieldWorkerMapper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


/**
 * 外勤员工信息缓存
 * @author aisino_lzw
 *
 */
@Component
public class FieldWorkerCache {

	private final Logger LOG = Logger.getLogger(getClass());

	@Autowired
	private FieldWorkerMapper fieldWorkerMapper;

	private LoadingCache<String, List<Long>> fieldWorkIdCache;

	/**
	 * 外勤员工信息缓存初始化
	 */
	@PostConstruct
	public void init() {
		final FieldWorkerMapper _fieldWorkerMapper = fieldWorkerMapper;
		fieldWorkIdCache = CacheBuilder.newBuilder()
				.expireAfterAccess(30, TimeUnit.SECONDS).maximumSize(50)
				.build(new CacheLoader<String, List<Long>>() {
					@Override
					public List<Long> load(String areaCode) throws Exception {
						if (StringUtils.isEmpty(areaCode)) {
							return Collections.emptyList();
						}
						return _fieldWorkerMapper
								.findFieldWorkerIdByAreaCode(areaCode);
					}

				});

	}

	
	/**
	 * 根据区域编号获取缓存中员工信息
	 * @param areaCode 区域编号
	 * @return 员工id列表
	 */
	public List<Long> get(String areaCode) {
		try {
			return fieldWorkIdCache.get(areaCode);
		} catch (ExecutionException e) {
			LOG.error("加载外勤人员ID失败,areaCode:" + areaCode, e);
			return fieldWorkerMapper.findFieldWorkerIdByAreaCode(areaCode);

		}
	}

}
