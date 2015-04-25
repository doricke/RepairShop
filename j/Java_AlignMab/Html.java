
/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2000 Darrell O. Ricke, Ph.D., Paragon Software
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

/******************************************************************************/
public class Html extends Object
{


/******************************************************************************/

public static String address = "ADDRESS";	// Author's address

public static String big = "BIG";		// Large font 

public static String body = "BODY";		// Bulk of web page

public static String bold = "B";		// Bold text

public static String br = "BR";		// Forced text break

public static String center = "CENTER";	// Center text

public static String code = "CODE";		// Source code listing

public static String color = "COLOR=";		// Text color

public static String div = "DIV";		// Division

public static String font = "FONT";		// Font

public static String h1 = "H1";		// Heading 1

public static String h2 = "H2";		// Heading 2

public static String h3 = "H3";		// Heading 3

public static String h4 = "H4";		// Heading 4

public static String h5 = "H5";		// Heading 5

public static String h6 = "H6";		// Heading 6

public static String head = "HEAD";		// Header

public static String html = "HTML";		// Document type

public static String italic = "I";		// Italic text

public static String line = "HR";		// Horizontal rule line

public static String paragraph = "P";		// Paragraph

public static String pre = "PRE";		// Preformated text

public static String sample = "SAMP";		// Sample output

public static String small = "SMALL";		// Small font

public static String title = "TITLE";		// Page title - must be in header

public static String tt = "TT";		// Typewriter text

public static String underline = "U";		// Underline text

public static String x = "";		// 


/******************************************************************************/
/* Symbols */

public static String ampersand = "&amp";	// &

public static String copyright = "&copy";	// Copyright symbol

public static String gt = "&gt";		// >

public static String lt = "&lt";		// <

public static String quote = "&quot";		// "

public static String registered = "&reg";	// Registered Trademark

public static String y = "";		// 


/******************************************************************************/
/* Colors */

public static String black = "#000000";		// HTML black

public static String red = "#FF0000";		// HTML red

public static String yellow = "#FFFF00";	// HTML yellow

public static String blue = "#00FFFF";		// HTML blue

public static String green = "#00FF00";		// HTML green

public static String white = "#FFFFFF";		// HTML white


/******************************************************************************/
  public Html ()
  {
  }  // constructor Html


/******************************************************************************/
  public static String fontColor ( String color )
  {
    return "<FONT COLOR=\"" + color + "\">";
  }  // method fontColor


/******************************************************************************/
  public static String htmlEndTag ( String tag )
  {
    return "</" + tag + ">";
  }  // method html_end_tag


/******************************************************************************/
  public static String htmlTag ( String tag )
  {
    return "<" + tag + ">";
  }  // method html_tag


/******************************************************************************/
  public static void main ( String [] args )
  {
    System.out.println ( htmlTag ( Html.html ) );
    System.out.println ( htmlTag ( Html.head ) );
    System.out.println ( htmlEndTag ( Html.head ) );
    System.out.println ( htmlTag ( Html.body ) );
    System.out.println ( fontColor ( Html.red ) );
    System.out.println ( "Hello" );
    System.out.println ( htmlEndTag ( Html.font ) );
    System.out.println ( htmlEndTag ( Html.body ) );
    System.out.println ( htmlEndTag ( Html.html ) );
  }  // method main


/******************************************************************************/

}  // class Html

