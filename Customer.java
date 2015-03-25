import java.util.Random;
import java.lang.Math;
/**
 * Write a description of class Customer here.
 * 
 * @author Nikola Hristov
 * @version 2.1
 */
public class Customer
{
	Random generator = new Random();
	private static final int MINIMUM_ITEMS = 1;
	private static final int MAXIMUM_ITEMS = 50;
	private static final double AT_CHECKOUT = 0.05;//3 sec time spent processing each item at checkout
	private static final double DECIDE = 0.08;//5 sec time spent on thinking before taking the product
		
	private double rand;
	private double timeForItems;
	private int items;
	private Items bill = new Items();
	
	private int personalID;
    private double arrivalTime;
    private double leaveTime;
    private double delayTime;
    private double processingTime;

    /**
     * Constructor for objects of class Customer
     */
    public Customer(int personalID, double arrivalTime)
    {
        this.personalID = personalID; 
        double calculateItems;
    	this.arrivalTime = arrivalTime;
    	generateGaussian();
    	System.out.println("rand= " + rand);
    	calculateItems = rand*(MAXIMUM_ITEMS - MINIMUM_ITEMS) + MINIMUM_ITEMS;
    	items = (int) calculateItems;
    	System.out.println("has " + items + " items");
    	timeForItems = items*DECIDE;    	
    	processingTime = timeForItems + items*AT_CHECKOUT;
    	bill.selectItems(items, personalID);
    }   
    
    public void setLeaveTime(double time){
    	leaveTime = time + processingTime;
    }
    
    public double getLeaveTime(){
    	return leaveTime;
    }
    
    public double getDelay(){
    	delayTime = leaveTime - arrivalTime;
    	return delayTime;
    }   
    
    private void generateGaussian(){
    	rand = Math.abs(generator.nextGaussian());
    	if(rand > 1){
    		generateGaussian();   
    	}
    }
    
    public int numberOfItems(){
    	return items;
    }
}
