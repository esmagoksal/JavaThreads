//***************************************************************************************************************************************************

import java.util.Queue      ;
import java.util.Arrays     ;
import java.util.LinkedList ;

//***************************************************************************************************************************************************




//***************************************************************************************************************************************************

public class Order
{
  //=================================================================================================================================================

  private final int             id             ;
  private final Queue< String > completedParts ;
  private final Queue< String > remainingParts ;

  //=================================================================================================================================================

  public Order ( int id , String ... parts )
  {
    this.id             = id                 ;
    this.completedParts = new LinkedList<>() ;
    this.remainingParts = new LinkedList<>() ;

    remainingParts.addAll( Arrays.asList( parts ) ) ;
  }

  //=================================================================================================================================================

  public synchronized boolean isCompleted               ()  { return              remainingParts.isEmpty()   ; }
  public synchronized String  nextRemainingPart         ()  { return              remainingParts.peek   ()   ; }
  public synchronized void    completeNextRemainingPart ()  { completedParts.add( remainingParts.poll   () ) ; }

  //=================================================================================================================================================

  @Override
  public String toString ()
  {
    String result = String.format( "[%03d : " , id ) ;

    for ( String partName : completedParts )  { result += partName ; }    result += "|" ;
    for ( String partName : remainingParts )  { result += partName ; }    result += "]" ;

    return result ;
  }

  //=================================================================================================================================================
}

//***************************************************************************************************************************************************

