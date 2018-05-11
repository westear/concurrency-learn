package demo;

public class TestPrint {

	public static void main(String[] args){
		Num num = new Num();
		PrintOdd printOdd = new PrintOdd(num);
		PrintEven printEven = new PrintEven(num);
		
		Thread thread_1 = new Thread(printOdd);
		Thread thread_2 = new Thread(printEven);
		
		thread_1.start();
		thread_2.start();
	}
}
