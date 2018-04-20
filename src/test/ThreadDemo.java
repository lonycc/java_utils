class Thread1 extends Thread {
	private String name;
	
	public Thread1(String name){
		this.name = name;
	}
	
	public void run(){
		for(int i=0;i<10;i++){
			System.out.println(name + " running:" + i);
		}
		try {
			sleep((int) Math.random()*10);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}

class Thread2 implements Runnable {
	private String name;
	
	public Thread2(String name){
		this.name = name;
	}
	
	@Override
	public void run(){
		for(int i=0;i<10;i++){
			System.out.println(name + " running:" + i);
		}
		try {
			Thread.sleep((int) Math.random()*10);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}

public class ThreadDemo {
	public static void main(String[] args){
		Thread1 mth1 = new Thread1("A");
		Thread1 mth2 = new Thread1("B");
		mth1.start();
		mth2.start();
		new Thread(new Thread2("C")).start();
		new Thread(new Thread2("D")).start();
	}
}