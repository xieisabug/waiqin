package test;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BarObject {

	public static void main(String[] args) throws UnsupportedEncodingException,
			InterruptedException, ExecutionException {

		System.out.println(System.getProperty("file.encoding"));
		String unicode = "好";
		String gbk = new String(unicode.getBytes(), "GBK");
		System.out.println(gbk);
		String utf8 = new String(unicode.getBytes(), "utf-8");

		System.out.println(utf8);
		testScheduler();
//		 testExecutor();
	}

	public static void testScheduler() throws InterruptedException, ExecutionException {
		ScheduledExecutorService schedulerExecutorService = Executors
				.newScheduledThreadPool(1);
		Future<Void> f1= schedulerExecutorService.schedule(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				System.out.println("do work");
				return null;
			}

		}, 100, TimeUnit.SECONDS);
		System.out.println("isDone:"+f1.isDone());
		System.out.println("isCancelled:"+f1.isCancelled());
		try {
			f1.get(5, TimeUnit.SECONDS);
			System.out.println("isDone:"+f1.isDone());
			System.out.println("isCancelled:"+f1.isCancelled());
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		schedulerExecutorService.shutdownNow();
		System.out.println("here");

//		schedulerExecutorService.awaitTermination(2, TimeUnit.MINUTES);
	}

	public static void testExecutor() {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					try {
						System.out.println("running");
						Thread.sleep(3000L);
					} catch (InterruptedException ex) {
						System.out.println("interrupt");
						System.out.println(Thread.currentThread().isInterrupted());
						Thread.currentThread().interrupt();
					}
				}

			}

		});
		List<Runnable> tasks = executorService.shutdownNow();
		System.out.println(tasks);
	}

}
