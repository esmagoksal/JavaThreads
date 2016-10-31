
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

//***************************************************************************************************************************************************

// import ...

//***************************************************************************************************************************************************




//***************************************************************************************************************************************************

public class DBServices
{
  //=================================================================================================================================================

  public  String     driver     ;
  public  String     url        ;
  public  String     database   ;
  public  String     username   ;
  public  String     password   ;

  //-------------------------------------------------------------------------------------------------------------------------------------------------

  private Connection connection ;
  private Statement  statement  ;

  //=================================================================================================================================================

  public DBServices () throws Exception
  {
    driver   = "com.mysql.jdbc.Driver"        ;
    url      = "jdbc:mysql://localhost:3306/" ;
    database = "CENG443"                      ;
    username = "root"                         ;
    password = "root"                         ;

    Class.forName( driver ) ;

    connection = DriverManager.getConnection( url + database , username , password ) ;
    statement  = (Statement) connection.createStatement()                                        ;
  }

  //=================================================================================================================================================

  public synchronized  List< Pair< String , Integer > > getInventory () throws Exception
  {
    List<Pair <String, Integer>> mylist = new LinkedList<>();
    String data = "select * from "+ database + ".inventory";
    
    ResultSet resultset = statement.executeQuery(data);
    
    while(resultset.next())
    {
        String partName = resultset.getString("Part");
        Integer count = resultset.getInt("Count");
        Pair <String,Integer> tempPair  = new Pair<>(partName, count);
        mylist.add(tempPair);
    }
    
      return mylist ; 
  }

  //=================================================================================================================================================

  public synchronized void setPartCount ( String partName , int partCount ) throws Exception
  {
    String data = "update ceng443.inventory set Count = "+ partCount+ " where Part = '" + partName+ "' ";
    statement.executeUpdate(data);
  }

  //=================================================================================================================================================

  public synchronized void incrementPartCount ( String partName ) throws Exception
  {
    String data = "update ceng443.inventory set Count = Count + 5 where Part = '" + partName+ "' ";
    statement.executeUpdate(data);
  }

  //=================================================================================================================================================

  public synchronized boolean decrementPartCount ( String partName ) throws Exception
  {
    String check = "select * from "+ database + ".inventory where Part = '" + partName+ "' " ; 
    ResultSet resultset = statement.executeQuery(check);
    if(resultset.next())
    {
        if(resultset.getInt("Count") == 0)
          return false;  
    }
    String data = "update ceng443.inventory set Count = Count - 1 where Part = '" + partName+ "' ";
    statement.executeUpdate(data);
    
    return true;
  }

  //=================================================================================================================================================

  public void close () throws Exception
  {
    if ( statement  != null )  { statement.close() ; }
    if ( connection != null )  { connection.close() ; }
  }

  //=================================================================================================================================================
}

//***************************************************************************************************************************************************

