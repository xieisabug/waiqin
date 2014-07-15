package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hazelcast.core.IMap;

public class SpringHazelcastTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ApplicationContext context=new ClassPathXmlApplicationContext("spring-hazelcast.xml");
		
		IMap latestFieldWorkerInfoMap=(IMap)context.getBean("latestFieldWorkerInfoMap");
		System.out.println(latestFieldWorkerInfoMap.getClass());
		SomeBean someBean=(SomeBean) context.getBean("someBean");
		System.out.println(someBean.getFieldWorkerWriteLock());
		
		

	}

}
