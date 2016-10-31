
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

//***************************************************************************************************************************************************

public class CustomerRepresentative extends Employee
{
  //=================================================================================================================================================

  private int numberOfProductRequests ;

  //=================================================================================================================================================

  public CustomerRepresentative ( String name , JobShop jobShop )
  {
    super( name , jobShop ) ;

    title                   = "Representative" ;
    numberOfProductRequests = 0                ;

    talk( "%s %s : (Constructor finished)" , title , name ) ;
  }

  //=================================================================================================================================================

  private String identifyPartName ( Part part )
  {
      try {
          Class c = part.getClass();
          Field f = c.getDeclaredField("name");
          f.setAccessible(true);
          return (String) f.get(part);
      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
          //Logger.getLogger(CustomerRepresentative.class.getName()).log(Level.SEVERE, null, ex);
      }
      try {
          Class c = part.getClass();
          Field f = c.getDeclaredField("index");
          f.setAccessible(true);
          int i = f.getInt(part);
          String partname = "" + (char)('A'+i-1);
          return partname;
      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
          //Logger.getLogger(CustomerRepresentative.class.getName()).log(Level.SEVERE, null, ex);
      }
      try {
          Class c = part.getClass();
          Field f = c.getDeclaredField("method");
          f.setAccessible(true);
          String methodname = (String) f.get(part);
          if(methodname.equals("getIndex"))
          {
              Method m = c.getDeclaredMethod(methodname, new Class [] {double.class});
              m.setAccessible(true);
              int i = (int) m.invoke(part, new Object [] {20.0});
              String partname = "" + (char)('A'+i-1);
              return partname;
          }
          else if (methodname.equals("getName"))
          {
              Method m = c.getDeclaredMethod(methodname, new Class [] {});
              m.setAccessible(true);
              String s = (String) m.invoke(part, new Object [] {});
              return s;
          }
         
          
      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
          //Logger.getLogger(CustomerRepresentative.class.getName()).log(Level.SEVERE, null, ex);
      }
      return null;
  }

  //=================================================================================================================================================

  @Override
  public void run ()
  {
    while(jobShop.isOpen)
    {
        talk( "%s %s : Checking for a standing product request" , title , name ) ;
        Part [] req = jobShop.getNextProductRequest();
        if(req == null)
        {
            talk( "%s %s : There are no product requests, so I'm waiting" , title , name ) ;
            synchronized(jobShop.productRequests)
            {
                while(true){
                    try {
                        jobShop.productRequests.wait();
                        break;
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(CustomerRepresentative.class.getName()).log(Level.SEVERE, null, ex);
                    }                
                }
            }
        }
        
        else{              
            int orderid = jobShop.generateNewOrderID();
            String [] reqString = new String [req.length];
            for(int a = 0; a< req.length; a++)
            {
                if(req[a]==null)
                    continue;
                reqString[a] = identifyPartName(req[a]);
            }
            Order order = new Order(orderid,reqString);
            talk( "%s %s : I am adding a new order %s" , title , name, order.toString() ) ;
            jobShop.addWorkingOrder(order);
            numberOfProductRequests++;
            synchronized(jobShop.workingOrders)
            {
                jobShop.workingOrders.notify();
            }

            synchronized(order)
            {
                while(true){
                    try {
                        order.wait();
                        break;
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(CustomerRepresentative.class.getName()).log(Level.SEVERE, null, ex);
                    }                
                }
            }
            synchronized(req)
            {
                req.notify();
            }                    
        } 
    }
    Part [] m;
    while((m = jobShop.getNextProductRequest())!= null)
    {
        synchronized(m)
        {
            m.notify();
        }
        
    }
    talk( "%s %s : Processed a total of %d product requests" , title , name, numberOfProductRequests ) ;
  }

  //=================================================================================================================================================
}

//***************************************************************************************************************************************************

