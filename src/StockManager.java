
import java.util.logging.Level;
import java.util.logging.Logger;

//***************************************************************************************************************************************************

public class StockManager extends Employee
{
  //=================================================================================================================================================

  private int numberOfPartsSupplied ;

  //=================================================================================================================================================

  public StockManager ( String name , JobShop jobShop )
  {
    super( name , jobShop ) ;

    title                 = "Stock Manager " ;
    numberOfPartsSupplied = 0                ;

    talk( "%s %s : (Constructor finished)" , title , name ) ;
  }

  //=================================================================================================================================================

  @Override
  public void run ()
  {
    while(jobShop.isOpen)
    {
        talk( "%s %s : Checking for a reported missing part" , title , name ) ;
        String missingpart = jobShop.getNextMissingPart();
        if(missingpart == null){
            
            talk( "%s %s : There are no reported missing parts, so I'm waiting" , title , name ) ;
            synchronized(jobShop.missingParts)
            {
                while(true)
                {
                    try {
                        jobShop.missingParts.wait();
                        break;
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(StockManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        else{
            try {
                talk( "%s %s : Ordering part %s" , title , name, missingpart ) ;
                jobShop.db.incrementPartCount(missingpart);
                numberOfPartsSupplied++;
                synchronized(missingpart)
                {
                    missingpart.notify();
                }
            } catch (Exception ex) {
                //Logger.getLogger(StockManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    String m;
    while((m = jobShop.getNextMissingPart())!= null)
    {
        synchronized(m)
        {
            m.notify();
        }
        
    }
    talk( "%s %s : Restocked a total of %d parts" , title , name, numberOfPartsSupplied ) ;
  }

  //=================================================================================================================================================
}

//***************************************************************************************************************************************************

