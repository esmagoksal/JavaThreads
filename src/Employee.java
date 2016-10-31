//***************************************************************************************************************************************************

public abstract class Employee extends Thread implements Person
{
  //=================================================================================================================================================

  public String  title   ;
  public String  name    ;
  public JobShop jobShop ;

  //=================================================================================================================================================

  public Employee ( String name , JobShop jobShop )
  {
    this.name    = name    ;
    this.jobShop = jobShop ;
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
}

//***************************************************************************************************************************************************

