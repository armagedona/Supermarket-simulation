import java.util.ArrayDeque;
import java.util.Random;
import java.text.*;
import java.io.File;

/**
 * Write a description of class Supermarket here.
 * 
 * @author Nikola Hristov 
 * @version 2.0
 */
public class Supermarket
{
    private static final int TIME_INC = 1;
    private static final double ARRIVAL_PROB = 0.5;
    private static final int MAX_QUEUE = 5;

    private ArrayDeque<Checkout> checkout;
    private ArrayDeque<ExpressCheckout> expressCheckout;

    private double totalSimulationTime;
    private int numberOfCustomers;
    private int numberOfExprCustomers;
    private double totalLeaveTime;
    private double totalExprLeaveTime;

    private Random newArrival = new Random();

    private double averageQueueSize;
    private double averageExprQueueSize;
    private double queueSize;    
    //private double lateQueueSize;
    private double time;
    private int ID;

    DecimalFormat formatVar = new DecimalFormat("#0.00");

    /**
     * Constructor for objects of class Supermarket
     */
    public Supermarket(double totalSimulationTime)
    {
        cleanUp();
        checkout  = new ArrayDeque<Checkout>(1);
        expressCheckout = new ArrayDeque<ExpressCheckout>(1);
        this.totalSimulationTime = totalSimulationTime;
        simulate();
    }

    public void simulate(){
        time = 1;

        numberOfCustomers = 0;
        numberOfExprCustomers = 0;
        totalLeaveTime = 0;
        totalExprLeaveTime = 0;
        averageQueueSize = 0;
        averageExprQueueSize = 0;
        queueSize = 0;
        ID = 1;
        //lateQueueSize = 0;

        checkout.clear();

        while(time <= totalSimulationTime){
            System.out.println("time " + time);
            if(newCustomer()){

                Customer cust = new Customer(ID, time);                
                ID ++;                
                if(cust.numberOfItems() >= 10){                    
                    if(checkout.isEmpty()){
                        checkout.add(new Checkout());
                    }
                    Checkout check = getShortestCheck();                
                    check.addCustomer(cust);

                    numberOfCustomers++;

                    System.out.println("Customer added");
                    System.out.println("customer " + cust);
                    if(check.getSize() > 0){
                        cust.setLeaveTime(time);
                        System.out.println("leave time " + cust.getLeaveTime());
                    }
                }
                else{                    
                    if(expressCheckout.isEmpty()){
                        expressCheckout.add(new ExpressCheckout());
                    }       
                    ExpressCheckout expressCheck = getShortestExprCheck();
                    expressCheck.addCustomerExpress(cust);

                    numberOfExprCustomers++;

                    System.out.println("Expr Customer added");
                    System.out.println("customer " + cust);
                    if(expressCheck.getSizeExpress() > 0){
                        cust.setLeaveTime(time);
                        System.out.println("leave time " + cust.getLeaveTime());
                    }
                }

            }

            for(Checkout check : checkout){
                Customer cust = check.getFirstCustomer();
                if((cust != null) && (time >= cust.getLeaveTime())){                   
                    totalLeaveTime += cust.getDelay();
                    System.out.println("first cust " + cust);
                    check.removeCustomer();
                    System.out.println("Customer removed");
                    //check.push(cust);
                    cust = check.getFirstCustomer();
                    System.out.println("new first cust " + cust);
                    if(cust != null){
                        cust.setLeaveTime(time);
                        System.out.println("new first cust new leave " + cust.getLeaveTime());
                    }

                }
                /**
                if(cust == null){
                    checkout.remove(check);
                    System.out.println("checkout closed");
                }*/
            }    

            for(ExpressCheckout check : expressCheckout){
                Customer cust = check.getFirstCustomerExpress();
                if((cust != null) && (time >= cust.getLeaveTime())){                   
                    totalExprLeaveTime += cust.getDelay();
                    System.out.println("first cust " + cust);
                    check.removeCustomerExpress();
                    System.out.println("Customer removed");
                    //check.push(cust);
                    cust = check.getFirstCustomerExpress();
                    System.out.println("new first cust " + cust);
                    if(cust != null){
                        cust.setLeaveTime(time);
                        System.out.println("new first cust new leave " + cust.getLeaveTime());
                    }
                }                              
            }    

            averageQueueSize(); 
            averageExprQueueSize();
            boolean newCheckoutNeeded = false;            

            for(Checkout check : checkout){
                newCheckoutNeeded = false;
                System.out.println("ASDASD");
                if(check.getSize() < MAX_QUEUE){
                    break;   
                }
                newCheckoutNeeded = true;
            }

            boolean newExprCheckoutNeeded = false;

            for(ExpressCheckout check : expressCheckout){
                newExprCheckoutNeeded = false;
                System.out.println("ASDASDEXPR");
                if(check.getSizeExpress() < MAX_QUEUE){
                    break;   
                }
                newCheckoutNeeded = true;
            }

            if(newCheckoutNeeded){
                checkout.add(new Checkout());
                System.out.println("New checkout openned at : " + time);
            }

            if(newExprCheckoutNeeded){
                expressCheckout.add(new ExpressCheckout());
                System.out.println("New expr checkout openned at : " + time);
            }

            time += TIME_INC;

            if(time > totalSimulationTime){
                System.out.println("closing time");
                //queueSizeWhenClosing();
                closingTime();                
            }            
        }
        String decFormat = formatVar.format(totalSimulationTime);        
        System.out.println("Total simulation time " + decFormat);

        System.out.println("Number of checkouts open " + checkout.size());
        System.out.println("Number of express checkouts open " + expressCheckout.size());

        decFormat = formatVar.format(totalLeaveTime);        
        System.out.println("Total leave time " + decFormat);
        decFormat = formatVar.format(totalExprLeaveTime);
        System.out.println("Total express leave time " + decFormat);

        double avWait = totalLeaveTime/numberOfCustomers;
        decFormat = formatVar.format(avWait);
        System.out.println(numberOfCustomers + " customers with an average wait time of " + decFormat);

        avWait = totalExprLeaveTime/numberOfExprCustomers;
        decFormat = formatVar.format(avWait);
        System.out.println(numberOfExprCustomers + " express customers with an average time of " + decFormat);
        averageQueueSize /= totalSimulationTime;        
        if(averageQueueSize >= 1){
            int avQueueSize = (int) averageQueueSize;
            System.out.println(avQueueSize + " average queue lengtht for the whole day.");   
        }
        else{
            System.out.println("The average queue length for the whole day is less than 1 person per queue.");
        }
        averageExprQueueSize /= totalSimulationTime;
        if(averageExprQueueSize >= 1){
            int avQueueSize = (int) averageExprQueueSize;
            System.out.println(avQueueSize + " average queue lengtht for the whole day at express lanes.");   
        }
        else{
            System.out.println("The average queue length at express lanes for the whole day is less than 1 person per queue.");
        }
        //System.out.println("Queue size at closing time : " + lateQueueSize);

    }

    private boolean newCustomer(){
        double newCust = newArrival.nextDouble();
        return (newCust > ARRIVAL_PROB);
    }

    private Checkout getShortestCheck(){
        Checkout smallestCheck = checkout.getFirst();
        System.out.println("short " + smallestCheck);
        for(Checkout check : checkout){
            if(smallestCheck.getSize() > check.getSize()){
                smallestCheck = check;
            }   
        }
        return smallestCheck;
    }

    private ExpressCheckout getShortestExprCheck(){
        ExpressCheckout smallestCheck = expressCheckout.getFirst();
        System.out.println("expr short " + smallestCheck);
        for(ExpressCheckout check : expressCheckout){
            if(smallestCheck.getSizeExpress() > check.getSizeExpress()){
                smallestCheck = check;
            }   
        }
        return smallestCheck;
    }

    private void closingTime(){
        Customer cust;
        for(Checkout check : checkout){
            cust = check.getFirstCustomer();            
            while(cust != null){
                totalLeaveTime += cust.getDelay();
                check.removeCustomer();
                System.out.println("Customer removed");
                cust = check.getFirstCustomer();
                if(cust != null){
                    cust.setLeaveTime(time);
                }
                time ++;
            }            
        }
        time = totalSimulationTime + 1;
        for(ExpressCheckout check : expressCheckout){
            cust = check.getFirstCustomerExpress();
            while(cust != null){
                totalExprLeaveTime += cust.getDelay();
                check.removeCustomerExpress();
                System.out.println("exp Customer removed");
                cust = check.getFirstCustomerExpress();
                if(cust != null){
                    cust.setLeaveTime(time);  
                }
                time ++;
            }
        }
    }

    private void averageQueueSize(){
        queueSize = 0;
        if(numberOfCustomers > 0){
            System.out.println("vlizam");
            for(Checkout check : checkout){
                if(check.getFirstCustomer() != null){
                    System.out.println("DSADSA");
                    System.out.println("size = " + check.getSize());
                    queueSize += check.getSize();
                }  
            }   

            System.out.println("queuesize = " + queueSize);
            queueSize /= checkout.size();
            System.out.println("queuesize = " + queueSize);
            averageQueueSize += queueSize;
            System.out.println("av q s " + averageQueueSize);
        }
    }

    private void averageExprQueueSize(){
        queueSize = 0;
        if(numberOfExprCustomers > 0){
            System.out.println("expr vlizam");
            for(ExpressCheckout check : expressCheckout){
                if(check.getFirstCustomerExpress() != null){
                    System.out.println("DSADSA");
                    System.out.println("size = " + check.getSizeExpress());
                    queueSize += check.getSizeExpress();
                }  
            }   

            System.out.println("queuesize = " + queueSize);
            queueSize /= expressCheckout.size();
            System.out.println("queuesize = " + queueSize);
            averageExprQueueSize += queueSize;
            System.out.println("av expr q s " + averageExprQueueSize);
        }

    }

    private void cleanUp(){
        if(numberOfCustomers == 0){
            int i = 1;
            boolean success = true;
            String fileName;
            File f;
            while(success) {
                fileName = "customer " + i + ".txt";
                f = new File(fileName);
                if(f.exists()){
                    f.delete();
                    i++;
                }
                else{
                    success = false;   
                }
            }
        }
    }
    /**
    private void queueSizeWhenClosing(){
    lateQueueSize = 0;
    if(numberOfCustomers >0){
    for(Checkout check : checkout){
    if(check.getFirstCustomer() != null){
    lateQueueSize += check.getSize(); 
    }  
    }   
    }
    lateQueueSize /= checkout.size(); 		
    }
     */
}