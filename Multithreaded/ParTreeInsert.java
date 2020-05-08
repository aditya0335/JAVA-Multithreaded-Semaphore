import java.util.Random;

public class ParTreeInsert {
	public static void main(String[] args) {
		Tree tr = new ParTree(5000);   
		//replace by 'new ParTree(5000)' for A4 Part 1
		int N = 5;
		InsertNums[] threads = 
			  { new InsertNums(tr), new InsertNums(tr), 
			    new InsertNums(tr), new InsertNums(tr),
				new InsertNums(tr) };
		try {
			System.out.println("Start Parallel Insert ...");
			for (int i = 0; i < N; i++) {
				threads[i].start();
			}
			for (int i = 0; i < N; i++) {
				threads[i].join();
			}
			System.out.println("Done Parallel Insert ...\nResults:");
			tr.print();
		} catch (Exception e) {
		}
	}
}

class InsertNums extends Thread {
	Tree tr;
//	ParTree tr;

	public InsertNums(Tree tr) {
		this.tr = tr;
	}

	public void run() {
		Random r = new Random();
		for (int i = 0; i < 5; i++)
			tr.insert(r.nextInt(10000));
		tr = null;
	}
}

class Tree {

	public Tree(int n) {
		value = n;
		left = null;
		right = null;
	}

	public void insert(int n) {
		if (value == n) {
			return;
		}
		if (value < n)
			if (right == null)
				right = new Tree(n);
			else
				right.insert(n);
		else if (left == null)
			left = new Tree(n);
		else
			left.insert(n);
	}

	void print() {
		if (left != null)
			left.print();
		System.out.print(value + " ");
		if (right != null)
			right.print();
	}
	
	protected int value;
	protected Tree left, right;
	
}

class ParTree extends Tree {
	
	private boolean empty = true;
	private boolean lock;
	public ParTree(int n) {
		
		super(n);
		lock=false;
	}
	
	public void insert(int n) {
		lock();
		if (value == n) {
			unlock();
			return;
		}
		if (value < n)
			if (right == null) {
				right = new ParTree(n);
				unlock();
			}
			else
			{
				//lock();
				unlock();
				right.insert(n);
			}
				
		else if (left == null) {
			left = new ParTree(n);
			unlock();
		}
		else
		{
			//lock();
			unlock();
			left.insert(n);
		}
	}

	synchronized void lock() {
		// fill in code
		try {
			while(lock)
	        	wait();
	        lock = true;
	        	
	        notify();
	      } 
	   catch (InterruptedException e)
	 	  {}

	}

	synchronized void unlock() {
		// fill in code
		      	  lock = false;
		      	  notify();
		    
	}

	
	// add any fields for ParTree
}

