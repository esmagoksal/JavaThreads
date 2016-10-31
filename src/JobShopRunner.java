//***************************************************************************************************************************************************

import java.util.List      ;
import java.util.ArrayList ;

//***************************************************************************************************************************************************




//***************************************************************************************************************************************************

public class JobShopRunner
{
  //=================================================================================================================================================

  public static void main ( String [] args ) throws Exception
  {
    //-----------------------------------------------------------------------------------------------------------------------------------------------

    if ( args.length == 0 )
    {
      args = ( "1 R1 "             /* Number and Names of Customer Representatives */ +
               "1 W1 "             /* Number and Names of Workers                  */ +
               "1 S1 "             /* Number and Names of Stock Managers           */ +
               "1 C1 "             /* Number and Names of Customers                */ ).split( "\\s+" ) ;

    //args = ( "3 R1 R2 R3 "       /* Number and Names of Customer Representatives */ +
      //       "4 W1 W2 W3 W4 "    /* Number and Names of Workers                  */ +
       //      "2 S1 S2 "          /* Number and Names of Stock Managers           */ +
         //    "5 C1 C2 C3 C4 C5"  /* Number and Names of Customers                */ ).split( "\\s+" ) ;

    //args = ( "2 R1 R2 "          /* Number and Names of Customer Representatives */ +
    //         "3 W1 W2 W3 "       /* Number and Names of Workers                  */ +
    //         "1 S1 "             /* Number and Names of Stock Managers           */ +
    //         "4 C1 C2 C3 C4 "    /* Number and Names of Customers                */ ).split( "\\s+" ) ;
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    List< String> customerRepresentativeNames = new ArrayList<>() ;    List< String > rn = customerRepresentativeNames ;  // Short named reference
    List< String> workerNames                 = new ArrayList<>() ;    List< String > wn = workerNames                 ;  // Short named reference
    List< String> stockManagerNames           = new ArrayList<>() ;    List< String > sn = stockManagerNames           ;  // Short named reference
    List< String> customerNames               = new ArrayList<>() ;    List< String > cn = customerNames               ;  // Short named reference

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    int argIndex = 0 ;

    int numR = Integer.parseInt( args[ argIndex ++ ].trim() ) ;    for ( int i = 0 ; i < numR ; i++ )  { rn.add( args[ argIndex ++ ] ) ; }
    int numW = Integer.parseInt( args[ argIndex ++ ].trim() ) ;    for ( int i = 0 ; i < numW ; i++ )  { wn.add( args[ argIndex ++ ] ) ; }
    int numS = Integer.parseInt( args[ argIndex ++ ].trim() ) ;    for ( int i = 0 ; i < numS ; i++ )  { sn.add( args[ argIndex ++ ] ) ; }
    int numC = Integer.parseInt( args[ argIndex ++ ].trim() ) ;    for ( int i = 0 ; i < numC ; i++ )  { cn.add( args[ argIndex ++ ] ) ; }

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    JobShop jobShop = new JobShop( customerRepresentativeNames , workerNames , stockManagerNames ) ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    for ( String name : customerNames )  { new Customer( name , jobShop ).start() ; }

    //-----------------------------------------------------------------------------------------------------------------------------------------------
  }

  //=================================================================================================================================================
}

//***************************************************************************************************************************************************

