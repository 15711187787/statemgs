package cn.com.hangdun.Controller;

import org.springframework.scheduling.annotation.Async;

public class Test {
	@Async
	public static void main(String[] args) {
		System.out.println("start ...");
		bbb();
		
	}
	
	@Async
	public static void  bbb() {
		
		ccc();
		System.out.println("bbbb");
	}
	
	@Async
	public static void ccc() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("cccc");
	}
	
}
