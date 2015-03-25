import java.util.ArrayDeque;
/**
 * Write a description of class ExpressCheckout here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ExpressCheckout
{
    private ArrayDeque<Customer> queue;

    /**
     * Constructor for objects of class ExpressCheckout
     */
    public ExpressCheckout()
    {
        queue = new ArrayDeque<Customer>(1);
    }

    public int getSizeExpress(){
        return queue.size();   
    }

    public void addCustomerExpress(Customer cust){
        queue.add(cust);
    }

    public Customer getFirstCustomerExpress(){
        if(queue.peek() != null){
            return queue.getFirst();   
        }
        return null;
    }

    public void removeCustomerExpress(){
        queue.removeFirst();
    }
}
