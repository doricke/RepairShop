import java.net.*;
import java.io.*;

class ReadURLFiles 
{

  public static void main ( String args[] ) 
  {
    ReadURLFiles reading = new ReadFugu();
  }  /* procedure main */
	
  public ReadURLFiles() 
  {
    String strRead;

    try 
    {
      URL url = new URL ( "http://transcriptome.affymetrix.com/publication/tfbs" );
      FileOutputStream fileout = new FileOutputStream ( "fugu" );
      DataOutputStream fout = new DataOutputStream ( fileout );
      DataInputStream stream = new DataInputStream ( url.openStream() );

      /* Copy the file. */
      while ( ( strRead = stream.readLine() )!=null )

        fout.writeBytes ( strRead + "\n" );
		
    }  /* try */
    catch ( Exception e ) 
    {
      System.out.println ( e );
    }  /* catch*/
  }  /* method ReadURLFiles */
}  /* class ReadURLFiles */
