import java.util.ArrayDeque;
/**
 * Write a description of class Checkout here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Checkout
{
    private ArrayDeque<Customer> queue;

    /**
     * Constructor for objects of class Checkout
     */
    public Checkout()
    {
        queue = new ArrayDeque<Customer>(1);
    }

    public int getSize(){
        return queue.size();   
    }

    public void addCustomer(Customer cust){
        queue.add(cust);
    }

    public Customer getFirstCustomer(){
        if(queue.peek() != null){
            return queue.getFirst();   
        }
        return null;
    }
    
    public void removeCustomer(){
    	queue.removeFirst();
    }
}
