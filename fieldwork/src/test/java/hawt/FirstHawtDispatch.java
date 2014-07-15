package hawt;
import static org.fusesource.hawtdispatch.Dispatch.HIGH;
import static org.fusesource.hawtdispatch.Dispatch.createQueue;
import static org.fusesource.hawtdispatch.Dispatch.getGlobalQueue;

import org.fusesource.hawtdispatch.DispatchQueue;

public class FirstHawtDispatch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DispatchQueue queue = getGlobalQueue(HIGH);
		queue.execute(new Runnable() {
			public void run() {
				System.out.println(Thread.currentThread().getName()+","+Thread.currentThread().getId());
				System.out.println("Hi!");
			}
		});
		DispatchQueue serialQueue = createQueue("My queue");
		
		serialQueue.execute(new Runnable(){
			public void run(){
				System.out.println(Thread.currentThread().getName()+","+Thread.currentThread().getId());
				System.out.println("sleep 10 minutes");
//				try {
//					Thread.currentThread().sleep(10*60*1000L);
//				} catch (InterruptedException e) {
//					Thread.interrupted();
//				}
				System.out.println("wake up");
				
			}
			
		});
		
		serialQueue.execute(new Runnable(){
			public void run(){
				System.out.println(Thread.currentThread().getName());
				System.out.println("another task");
			}
		});
	}

}
