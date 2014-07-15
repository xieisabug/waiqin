import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.AtomicNumber;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ISet;
import com.hazelcast.core.MultiMap;

public class HazelcastMultiMapTest {

	private static IMap<String, String> fooMap;

	private static MultiMap<String, String> barMap;

	private static ILock lock;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setUpdateAutomatic(true);
		clientConfig.setInitialConnectionAttemptLimit(3);
		clientConfig.setReconnectionAttemptLimit(5);
		clientConfig.addAddress("127.0.0.1:5701");
	

		HazelcastInstance hazelcastInstance = HazelcastClient
				.newHazelcastClient(clientConfig);
//		ConcurrentMap<String,AtomicNumber> numberMap=hazelcastInstance.getMap("numberMap");
		AtomicNumber number1 = hazelcastInstance.getAtomicNumber("atomicNum-1");
		ISet<String> nameSet=hazelcastInstance.getSet("nameSet");
//		numberMap.putIfAbsent("number1", number1);
//		System.out.println(number1.equals(numberMap.get("number1")));
		
		
		System.out.println(number1);
		if(number1.get()==0L){
			
			boolean succ=number1.compareAndSet(0L, 100L);
			System.out.println(succ);
		}
		System.out.println(number1.get());
	

	}

}
