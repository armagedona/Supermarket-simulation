import simpleIO.*;
import java.util.Random;
import java.text.*;
/**
 * Write a description of class Items here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Items
{
    private String [][] list = new String [50][2];
    private String reader;
    private double bill = 0;
    int itemsSoFar = 0;
    DecimalFormat formatVar = new DecimalFormat("#0.00");

    public Items(){
        TextReader reader = new TextReader("Products.txt");
        String line;
        int row = 0;
        int column = 0;
        while (row < 50){
            line = reader.readLine();
            //System.out.println(line);
            list[row] = line.split("\t");  
            //System.out.println(row);
            row ++; 
            //System.out.println(row);
        }
        reader.close();
    }

    private String TextReader(String Products){
        TextReader reader = new TextReader("Products.txt");
        String line;
        String stock;
        line = reader.readLine();
        stock = line  + "\n";
        do{
            line = reader.readLine();
            if(line != null)
                stock += line + "\n";
        }while(line != null);
        reader.close();
        return stock;
    }

    public void showItems(String filename)
    {
        System.out.println(TextReader(filename));

    }  

    public void selectItems (int numberOfItems, int custID){
        Random generator = new Random();
        int itemIndex;
        String ID = Integer.toString(custID);
        String fileName = "customer " + ID + ".txt";
        TextWriter writer = new TextWriter(fileName);
        int i;
        writer.writeLine("customer " + ID + "\n");
        while (itemsSoFar < numberOfItems){          
            itemIndex = generator.nextInt(50);
            i = 0;
            while (i < 2){
                writer.writeString(list[itemIndex][i] + "\t");
                i++;
            }                 	
            bill += Double.parseDouble(list[itemIndex][1]);       
            writer.writeLine("");
            itemsSoFar ++;
        }
        String finalBill = formatVar.format(bill);
        writer.writeLine("\n" + "Total money spent\t" + finalBill);
        writer.close();
    }

}
