import java.io.*;
import java.net.*;
import ParseXml;


/******************************************************************************/
public class ComputeServer extends Thread 
{
  public final static int DEFAULT_PORT = 6789;
  protected int port;
  protected ServerSocket listen_socket;

/******************************************************************************/
  // Exit with an error message, when an exception occurs.
  public static void fail ( Exception e, String msg )
  {
    System.err.println ( msg + ": " + e );
    System.exit ( 1 );
  }  // method fail 

/******************************************************************************/
  // Create a ServerSocket to listen for connection on; start the thread.
  public ComputeServer ( int port )
  {
    if ( port == 0 )  port = DEFAULT_PORT;
    this.port = port;
    try
    {
      listen_socket = new ServerSocket ( port );
    }  // try 
    catch ( IOException e )
    {
      fail ( e, "Exception creating server socket" );
    }  // catch

    System.out.println ( "ComputeServer: listening on port " + port );
    this.start ();
  }  // method ComputeServer 

/******************************************************************************/
  // The body of the server thread.  Loop forever, listening for and
  // accepting connections from clients.  For each connection,
  // create a Connection object to handle communication through the
  // new Socket.
  public void run ()
  {
    try
    {
      while ( true )
      {
        Socket client_socket = listen_socket.accept ();
        Connection c = new Connection ( client_socket );
      }  // while 
    }  // try 
    catch ( IOException e )
    {
      fail ( e, "Exception while listening for connections" );
    }  // catch
  }  // method run 


/******************************************************************************/
  // Start the server up, listening on an optionally specified port
  public static void main ( String [] args )
  {
    int port = 0;

    if ( args.length == 1 )
    {
      try
      {
        port = Integer.parseInt ( args [ 0 ] );
      }  // try
      catch ( NumberFormatException e )
      {
        port = 0;
      }  // catch
    }  // if 
    new ComputeServer ( port );
  }  // method main 
}  // class ComputeServer 


/******************************************************************************/
// This class is the thread that handles all communication with a client.
class Connection extends Thread
{
  protected Socket client;
  protected DataInputStream in;
  protected PrintStream out;
  protected ParseXml xml_parser = new ParseXml ();

  // Initialize the streams and start the thread.
  public Connection ( Socket client_socket )
  {
    client = client_socket;
    try
    {
      in = new DataInputStream ( client.getInputStream () );
      out = new PrintStream ( client.getOutputStream () );
    }  // try 
    catch ( IOException e )
    {
      try
      {
        client.close ();  
      }  // try
      catch ( IOException e2 )
      {
      }  // catch
      System.err.println ( "Exception while getting socket streams: " + e );
      return;
    }  // catch 
    this.start ();
  }  // method Connection 


/******************************************************************************/
  // Provide the service.
  // Read a line, reverse it, send it back.
  public void run ()
  {
    System.out.println ( "class Connection method run" );

    xml_parser.parseStream ( in );

    // xml_parser.parseDocument ();

    try
    { 
      client.close ();
    }
    catch ( IOException e )
    {
    }  // catch
  }  // method run 
}  // class Connection 

