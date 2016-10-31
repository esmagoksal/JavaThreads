//***************************************************************************************************************************************************

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random ;
import java.util.logging.Level;
import java.util.logging.Logger;

//***************************************************************************************************************************************************




//***************************************************************************************************************************************************

public class Customer extends Thread implements Person
{
  //=================================================================================================================================================

  public        String  title                   ;
  public        String  name                    ;
  public        JobShop jobShop                 ;

  //-------------------------------------------------------------------------------------------------------------------------------------------------

  private       int     numberOfProductRequests ;
  private final Random  random                  ;

  //=================================================================================================================================================

  public Customer ( String name , JobShop jobShop )
  {
    this.title                   = "Customer      " ;
    this.name                    = name             ;
    this.jobShop                 = jobShop          ;
    this.numberOfProductRequests = 0                ;
    this.random                  = new Random()     ;

    talk( "%s %s : (Constructor finished)" , title , name ) ;
  }

  //=================================================================================================================================================

  @Override
  public void talk ( String format , Object ... args )  // This is a synchronized wrapper for printf method
  {
    synchronized ( System.out )  { System.out.printf( format + "%n" , args ) ;  System.out.flush() ; }
  }

  //=================================================================================================================================================

  @Override
  public void spendTime ( int minMilliseconds , int maxMilliseconds )  // This is a wrapper for Thread.sleep
  {
    int duration = minMilliseconds + (int) ( Math.random() * ( maxMilliseconds - minMilliseconds ) ) ;

    try { Thread.sleep( duration ) ; } catch ( InterruptedException ex ) { /* Do nothing */ }
  }

  //=================================================================================================================================================

  private Part generateRandomPart ()
  {
      List<Pair <String, Integer>> mylist = null;
      try {
          mylist = jobShop.db.getInventory();
      } catch (Exception ex) {
          Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
      }
      if(mylist == null)
      {
          return null;
      }
      else{
          int count = random.nextInt(mylist.size());
          String partname = mylist.get(count).first;
          try {
              Class c = Class.forName( "Part" + partname );
              Constructor cons = c.getConstructor();
              return (Part) cons.newInstance();
          } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
              Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
          }
          return null;
      }
     
  }

  //=================================================================================================================================================

  private Part [] generateRandomProductRequest ()
  {
    int     numberOfParts = 2 + random.nextInt( 4 )   ;
    Part [] parts         = new Part[ numberOfParts ] ;

    for ( int i = 0 ; i < numberOfParts ; i++ )  { parts[ i ] = generateRandomPart() ; }

    return parts ;
  }

  //=================================================================================================================================================

  @Override
  public void run ()
  {
    while(jobShop.isOpen)
    {
        Part [] request = generateRandomProductRequest();
        talk( "%s %s : Submitting a product request of %d parts" , title , name,request.length ) ;
        jobShop.addProductRequest(request);
        numberOfProductRequests ++;
        synchronized(jobShop.productRequests){
            jobShop.productRequests.notify();
        }
        synchronized(request){
            while(true)
            {
                try {
                    request.wait();
                    break;

                } catch (InterruptedException ex) {                   
                }
            }
        }
        
        try { 
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            //Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    talk( "%s %s : Submitted a total of %d product requests to the jobshop" , title , name, numberOfProductRequests ) ;
  }

  //=================================================================================================================================================
}

//***************************************************************************************************************************************************

