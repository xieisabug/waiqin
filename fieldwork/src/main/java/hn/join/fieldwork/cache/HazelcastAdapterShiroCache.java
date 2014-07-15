package hn.join.fieldwork.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
/**
 * 实现SHIRO CACHE接口，维护Hazelcast缓存key/value 对
 * @author chenjinlong
 *
 *
 * @param <K>
 * @param <V>
 */
public class HazelcastAdapterShiroCache<K, V> implements Cache<K, V> {

	private Map<K, V> shiroSessionMap;
	
	

	public HazelcastAdapterShiroCache(Map<K, V> shiroSessionMap) {
		super();
		this.shiroSessionMap = shiroSessionMap;
	}

	public void setShiroSessionMap(Map<K, V> shiroSessionMap) {
		this.shiroSessionMap = shiroSessionMap;
	}

	@Override
	public void clear() throws CacheException {
		shiroSessionMap.clear();
	}

	@Override
	public V get(K key) throws CacheException {
		return shiroSessionMap.get(key);
	}

	@Override
	public Set<K> keys() {
		return shiroSessionMap.keySet();
	}

	@Override
	public V put(K key, V value) throws CacheException {
		return shiroSessionMap.put(key, value);
	}

	@Override
	public V remove(K key) throws CacheException {
		return shiroSessionMap.remove(key);
	}

	@Override
	public int size() {
		return shiroSessionMap.size();
	}

	@Override
	public Collection<V> values() {
		return shiroSessionMap.values();
	}

}
