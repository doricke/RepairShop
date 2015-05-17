import java.io.*;
import java.lang.*;
import java.net.*;
import java.util.*;

/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2002 Darrell O. Ricke, Ph.D., Paragon Software
  License::   	GNU GPL license  (http://www.gnu.org/licenses/gpl.html)
  Contact::   	Paragon Software, 1314 Viking Blvd., Cedar, MN 55011
 
              	This program is free software; you can redistribute it and/or modify
              	it under the terms of the GNU General Public License as published by
              	the Free Software Foundation; either version 2 of the License, or
              	(at your option) any later version.
          
              	This program is distributed in the hope that it will be useful,
              	but WITHOUT ANY WARRANTY; without even the implied warranty of
              	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
              	GNU General Public License for more details.
 
                You should have received a copy of the GNU General Public License
                along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

public class SysExec extends Object
{
  public static void main ( String [] args )
  {
    Runtime r = Runtime.getRuntime ();

    System.out.println ( "Free memory = " + r.freeMemory () );
    System.out.println ( "Total memory = " + r.totalMemory () );

    try
    {
      r.exec ( "cp SysExec.java itWorks.java" );
    }
    catch ( IOException e )
    {
      System.out.println ( "IOException " + e );
    }  // catch

    try
    {
      InetAddress i = InetAddress.getLocalHost ();
      System.out.println ();
      String hostName = i.getHostName ();
      System.out.println ( "Host name = " + hostName );
      System.out.println ( "Host i.toString = " + i.toString () );
      System.out.println ( "Host getHostAddress = " + i.getHostAddress () );
//    System.out.println ( "Host address = " + i.toString () );
    }
    catch ( UnknownHostException e )
    {
      System.out.println ( "UnknownHostException " + e );
    }  // catch

    Properties prop = System.getProperties ();
    System.out.println ( "user name = " + prop.getProperty ( "user.name" ) );
  }  // method main
}  // class SysExec

