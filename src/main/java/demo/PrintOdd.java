package demo;

public class PrintOdd implements Runnable{

	Num num;
	
	public PrintOdd(Num num){
		this.num = num;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(num.i <= 100){
			synchronized (num) {
				if(num.flag){
					try{
						num.wait();
					}catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}else{
					System.out.println("奇数----" + num.i);
					num.i++;
					num.flag = true;
					num.notify();
				}
			}
		}
	}

}
