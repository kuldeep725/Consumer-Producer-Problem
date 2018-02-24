import java.util.*;
/**
 * @aim  		:  Consumer-Producer Problem solving using synchronization in java 
 * @author 	:  Kuldeep Singh Bhandari
 * @date 		:  24 February, 2018
 */
public class Market {
	/**
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
				
		/**************
		 * Set maximum item size of user's wish
		 */
		Item.MAXIMUM_ITEM_SIZE = 2;
		/**************
		 * initialize an object of Item with itemName "apple"
		 */
		Item item = new Item("apple ");		
		/**************
		 * initiate the market process
		 */
		InitiateMarket(item);
		
	}
	
	private static void InitiateMarket(Item item) throws InterruptedException {
		
		/**********
		 * Initialize two consumers and two producers who will
		 * participate in the market
		 */
		Consumer c1;
		Consumer c2;
		Producer p1;
		Producer p2;
		c1= new Consumer(item, "1");
		p1= new Producer(item,	"1");		
		c2 = new Consumer(item, "2");
		p2 = new Producer(item, "2");
		/**********
		 * Begin the threads
		 */
		p1.start();
		c1.start();
		p2.start();
		c2.start();
		/*********
		 * Join the threads
		 */
		p1.join();		
		c1.join();
		p2.join();
		c2.join();
		
	}

	/**********
	 * Item class which has informations about the item in the market
	 *
	 */
	public static class Item {
		
		private static int MAXIMUM_ITEM_SIZE;
		private LinkedList<Integer> list;
		private String itemName;
		private static int value;
		
		public Item (String itemName) {
			list = new LinkedList<>();
			this.itemName = itemName;
			value = 1;
		}
		
		/**********
		 * 
		 * @return first item in the list by deleting it from the list
		 */
		public int getItem() {
			
			if(list.isEmpty())		return Integer.MIN_VALUE;
			int firstItem = list.removeFirst();
			return firstItem;
			
		}
		/**********
		 * 
		 * @param product - add this to the end of the list
		 */
		public void putItem(int product) {
			
			list.addLast(product);
			
		}
		/**********
		 * 
		 * @return size of the list
		 */
		public int getSize() {
			return list.size();
		}
		
		/**********
		 * 
		 * @return get the name of the list
		 */
		public String getItemName() {
			return itemName;
		}

		/**********
		 * function runs an infinite loop and in the loop, it checks
		 * whether the size of the list is empty or not
		 * if "empty", then it waits for the producer to insert items in the list
		 * else then     it consumes the item until the list is empty
		 * @param consumerId - id of the consumer
		 */
		public void consumerTurn(String consumerId) {
			int ele = 0;
			while(true) {
				synchronized(this) {
					try {
						/***********
						 * checks whether size of the list has become 0 or not
						 */
						if(this.getSize() == 0) {
							/************
							*    Causes the current thread to wait until another thread
							*   invokes the java.lang.Object.notify()  method or 
							*   the java.lang.Object.notifyAll() method for this object
							*   In this case, it waits for the producer to produce and notify 
							*   after producing
							*/
							wait();
						}
						/**
						 * remove an element from the list
						 */
						ele = this.getItem();
						/**
						 * display the item consumed by the consumer
						 */
						System.out.println("Consumer "+ consumerId+" bought " +this.getItemName() 
								+ " (id = " + ele  + ")");
						/**
						 * Wakes up all threads that are waiting on this object's monitor
						 */
						notifyAll();
						/**
						 * sleeps for 1s to control the speed of output in the screen
						 */
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}				
			}
			
		}
		
		/**
		 * function runs an infinite loop and in the loop, it checks
		 * whether the list has reached to its maximum limit
		 * if "reached maximum limit", then wait for the consumer to consume 
		 * those items
		 * else then, it adds items in the list until list has reached its maximum limit
		 * @param producerId - id of the producer
		 */
		public void producerTurn(String producerId) {
			
			while(true) {
				synchronized(this) {
					try {
						/**
						 * check whether the list has reached its maximum limit or not
						 */
						if(this.getSize() == Item.MAXIMUM_ITEM_SIZE) {
							/**
							 *  Causes the current thread to wait until another thread
							 *   invokes the java.lang.Object.notify()  method or 
							 *   the java.lang.Object.notifyAll() method for this object
							 *   In this case, it waits for the consumer to consume and notify 
							 *   after consuming
							 */
							wait();
						}
						/**
						 * insert item in the list
						 */
						this.putItem(value++);
						/**
						 * print the item produced by the producer
						 */
						System.out.println("Producer "+producerId+" produce "+this.getItemName() 
								+"(id = "+ (value-1)+") ");
						/**
						 * Wakes up all threads that are waiting on this object's monitor
						 */
						notifyAll();
						/**
						 * sleeps for 1s to control the speed of output in the screen
						 */
						Thread.sleep(1000);
						
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			
			}
	
		}
		
	}
	
	/**
	 * Consumer class which has information about the consumer
	 *
	 */
	public static class Consumer extends Thread {
		
		private Item item;
		private String consumerId;
		
		public Consumer(Item item, String consumerId) {
			this.item = item;
			this.consumerId = consumerId;
		}

		@Override
		public void run() {
			
			super.run();
			item.consumerTurn(consumerId);
			
		}
				
	}
	
	/**
	 * Producer class which has information about the producer
	 *
	 */
	public static class Producer extends Thread {
		
		private Item item;
		private String producerId;
		
		public Producer (Item item, String producerId) {
			this.item = item;
			this.producerId = producerId;
		}
		
		@Override
		public void run() {
			
			super.run();
			item.producerTurn(producerId);
			
		}
				
	}
	
}
