import java.applet.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class AppletClient extends Applet 
{
  public static final int PORT = 6789;
  Socket s;
  DataInputStream in;
  PrintStream out;
  TextField inputfield;
  TextArea outputarea;
  StreamListener listener;

  // Create a socket to communicate with a server on port 6789 of the
  // host that the applet's code is on.  Create streams to use with
  // the socket.  Then create a TextField for user input and a TextArea
  // for server output.  Finally, create a thread to wait for and
  // display the server output.
  public void init ()
  {
    try
    {
      s = new Socket ( this.getCodeBase ().getHost (), PORT );
      in = new DataInputStream ( s.getInputStream () );
      out = new PrintStream ( s.getOutputStream () );

      inputfield = new TextField ();
      outputarea = new TextArea ();
      outputarea.setEditable ( false );
      this.setLayout ( new BorderLayout () );
      this.add ( "North", inputfield );
      this.add ( "Center", outputarea );

      listener = new StreamListener ( in, outputarea );

      this.showStatus ( "Connected to "
          + s.getInetAddress ().getHostName ()
          + ":" + s.getPort () );
    }  /* try */
    catch ( IOException e )  this.showStatus ( e.toString () );
  }  /* method init */


  // When the user types a line, send it to the server.
  public boolean action ( Event e, Object what )
  {
    if ( e.target == inputfield )
    {
      out.println ( (String) e.arg );
      inputfield.setText ( "" );
      return true;
    }  /* if */
    return false;
  }  /* method action */
}  /* class AppletClient */


// Wait for output from the server on the specified stream, and display
// it in the specified TextArea.
class StreamListener extends Thread 
{
  DataInputStream in;
  TextArea output;

  public StreamListener ( DataInputStream in, TextArea output )
  {
    this.in = in;
    this.output = output;
    this.start ();
  }  /* method StreamListener */


  public void run ()
  {
    String line;

    try
    {
      for ( ; ; )
      {
        line = in.readLine ();
        if ( line == null )  break;
        output.setText ( line );
      }  /* for */
    }  /* try */
    catch ( IOException e )  output.setText ( e.toString () );

    finally output.setText ( "Connection closed by server." );
  }  /* method run */
}  /* class StreamListener */
