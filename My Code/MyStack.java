/**
 * 
 */

/**
 * @author Wafiq Syed Rahmathulla
 * @author Student No. 250868594
 *This program computes a path that a drone can follow from the UWO store
 *to a customer's house. 
 *This class implements a stack using an array.
 */

public class MyStack<T> implements MyStackADT<T> {
	private T[] arrayStack;		//This array will store data items of stack
	private int numItems;		//Stores the number of data items in stack
	private int maximumCapacity; //Indicates the maximum size of the array
	private final int DEFAULT_CAPACITY = 10;
	
	/**
	 * Creates an empty stack of an initial capacity of 10.
	 */
	public MyStack() {
		numItems = 0;
		maximumCapacity = 1000;
		arrayStack = (T[]) (new Object[DEFAULT_CAPACITY]);
	}
	
	/*
	 * Creates an empty stack using the specified capacity. 
	 * @param initialCapcity is the specified capacity.
	 * @param maxCap is the maximum size for the array.
	 */
	public MyStack(int initialCapacity, int maxCap) {
		numItems = 0;
		maximumCapacity = maxCap;
		arrayStack = (T[])(new Object[initialCapacity]);
	}
	
	/*
	 * Adds data item to the top of the stack. 
	 * If the array is full, the capacity will be increased as follows:
	 * If array is smaller than 60, array capacity will be increased by factor of 3
	 * Otherwise, capacity will increase by 100.
	 * If array is increased to more than maximumCapacity, an OverflowExcpetion will be 
	 * thrown.
	 * @param dataItem is the data item that will be added to top of stack
	 */
	
	public void push (T dataItem) throws OverflowException{
		if(size() == maximumCapacity - 1)
			throw new OverflowException("The size of the stack has exceeded maximum capacity of 1000.");
		if(arrayStack.length < 60) {tripleCapacity();}
		if(size() == arrayStack.length) {expandCapacity();}
		
		arrayStack[numItems] = dataItem;
		numItems ++;
	
	}
	
	/*
	 * Increases the array capacity by a factor of 3
	 */
	private void tripleCapacity() {
		
		T[] tripled = (T[])(new Object[arrayStack.length*3]);
	    for (int index=0; index < arrayStack.length; index++)
	      tripled[index] = arrayStack[index];
	    
	    arrayStack = tripled;
	}
	
	/*
	 * increases array capacity by 100
	 */
	private void expandCapacity() {
		T[] larger = (T[])(new Object[arrayStack.length + 100]);
	    for (int index=0; index < arrayStack.length; index++)
	      larger[index] = arrayStack[index];
	    
	    arrayStack = larger;
	}
	/*Removes and returns the data items at the top of the stack.
	 * If stack is empty, EmptyStackException is thrown. 
	 * (non-Javadoc)
	 * @see MyStackADT#pop()
	 */
	public T pop() throws EmptyStackException{
		if (isEmpty()) 
			throw new EmptyStackException("The stack is empty.There is no available path to the customer's house.");
			
		    numItems--;
		    T result = arrayStack[numItems];
		    arrayStack[numItems] = null; 

		    return result;
	}
	
	/*
	 * Returns data item at top of stack. 
	 * If stack is empty, EmptyStackException is thrown. 
	 * (non-Javadoc)
	 * @see MyStackADT#peek()
	 */
	public T peek() throws EmptyStackException{
		if (isEmpty())
			throw new EmptyStackException("The stack is empty. There is no available path to the customer's house.");

		    return arrayStack[numItems-1];
	}
	
	/*
	 * Returns true of stack is empty, and returns false if otherwise. 
	 */
	public boolean isEmpty() {
		return (numItems == 0);
	}
	
	/*
	 * Returns number of data items in stack
	 */
	public int size() {
		return numItems;
	}
	
	/*
	 * Returns a String representation of the stack. 
	 */
	public String toString() {
		String result = "";
		
		for (int scan = numItems-1; scan >= 0; scan--)
		      result = result + arrayStack[scan].toString() + "\n";

		return result;
	}
}

