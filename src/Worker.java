
import java.util.logging.Level;
import java.util.logging.Logger;

//***************************************************************************************************************************************************

public class Worker extends Employee
{
  //=================================================================================================================================================

  private int numberOfPartsAssembled ;

  //=================================================================================================================================================

  public Worker ( String name , JobShop jobShop )
  {
    super( name , jobShop ) ;

    title                  = "Worker        " ;
    numberOfPartsAssembled = 0                ;

    talk( "%s %s : (Constructor finished)" , title , name ) ;
  }

  //=================================================================================================================================================

  @Override
  public void run ()
  {
    while(jobShop.isOpen)
    {
        talk( "%s %s : Checking for a working order" , title , name ) ;
        Order order = jobShop.getNextWorkingOrder();
        if(order == null)
        {
            talk( "%s %s : There are no working orders, so I'm waiting" , title , name ) ;
            synchronized(jobShop.workingOrders)
            {
                while(true){
                    try {
                        jobShop.workingOrders.wait();
                        break;
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(CustomerRepresentative.class.getName()).log(Level.SEVERE, null, ex);
                    }                
                }
            }
        }
        
        else{
            
            while(!order.isCompleted()&& jobShop.isOpen)
            {   
                talk( "%s %s : Currently working on order %s" , title , name, order.toString() ) ;
                String p = order.nextRemainingPart();
                
                try {
                    
                    if(jobShop.db.decrementPartCount(p)){
                        order.completeNextRemainingPart();
                        numberOfPartsAssembled++;
                        talk( "%s %s : Assembled next part of order %s" , title , name, order.toString() ) ;
                    }
                    else{
                        jobShop.addMissingPart(p);
                        synchronized(jobShop.missingParts)
                        {
                            jobShop.missingParts.notify();
                        }
                        synchronized(p)
                        {
                            while(true){
                                try {
                                    p.wait();
                                    break;
                                } catch (InterruptedException ex) {
                                    //Logger.getLogger(CustomerRepresentative.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }

                    }
                } catch (Exception ex) {
                    //Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            jobShop.addCompletedOrder(order);
            synchronized(order)
            {
                order.notify();
            }
        }
    }
    Order m;
    while((m = jobShop.getNextWorkingOrder())!= null)
    {
        synchronized(m)
        {
            m.notify();
        }
        
    }
    
    talk( "%s %s : Assembled a total of %d parts" , title , name, numberOfPartsAssembled ) ;
  }

  //=================================================================================================================================================
}

//***************************************************************************************************************************************************

