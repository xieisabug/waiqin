package test;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;

@Component
public class SomeBean {
	
	
    @Autowired
    IMap latestFieldWorkerInfoMap; // instead of IMap<K, V> map
    
    @Autowired
    ILock fieldWorkerWriteLock;
    
    @Autowired
    MultiMap areaCode2FieldWorkerMap;

	public IMap getLatestFieldWorkerInfoMap() {
		return latestFieldWorkerInfoMap;
	}

	public ILock getFieldWorkerWriteLock() {
		return fieldWorkerWriteLock;
	}

	public MultiMap getAreaCode2FieldWorkerMap() {
		return areaCode2FieldWorkerMap;
	}
    
	public void setLatestFieldWorkerInfoMap(IMap latestFieldWorkerInfoMap) {
		this.latestFieldWorkerInfoMap = latestFieldWorkerInfoMap;
	}

	public void setFieldWorkerWriteLock(ILock fieldWorkerWriteLock) {
		this.fieldWorkerWriteLock = fieldWorkerWriteLock;
	}

	public void setAreaCode2FieldWorkerMap(MultiMap areaCode2FieldWorkerMap) {
		this.areaCode2FieldWorkerMap = areaCode2FieldWorkerMap;
	}

	@PostConstruct
	public void init(){
		System.out.println(fieldWorkerWriteLock);
	}
	
    


}