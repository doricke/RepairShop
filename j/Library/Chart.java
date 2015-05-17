import java.awt.*;
import java.applet.*;
import java.io.*;

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

/******************************************************************************/
public class Chart extends Applet
{
  double values [];
  String names [];
  String title;


/******************************************************************************/
public void init ()
{
  int n = getInteger ( getParameter ( "values" ) );
  values = new double [ n ];
  names = new String [ n ];
  title = getParameter ( "title" );

  int i;
  for ( i = 0; i < n; i++ )
  {
    values [ i ] = getInteger ( getParameter ( "value_" + (i + 1) ) );

    names [ i ] = getParameter ( "name_" + (i + 1) );
  }  /* for */
}  /* init */


/******************************************************************************/
public void paint ( Graphics g )
{
  int i;
  int n = getInteger ( getParameter ( "values" ) );
  double minValue = 0;
  double maxValue = 0;

  // Note that minValue is set to zero - unlikely > values [ i ]!!!
  for ( i = 0; i < values.length; i++ )
  {
    if ( minValue > values [ i ] )  minValue = values [ i ];
    if ( maxValue < values [ i ] )  maxValue = values [ i ];
  }  /* for */

  Dimension d = size ();
  Insets in = insets ();
  int clientWidth = d.width - in.right - in.left;
  int clientHeight = d.height - in.bottom - in.top;
  int barWidth = clientWidth / n;

  Font titleFont = new Font ( "helvetica", Font.BOLD, 20 );
  FontMetrics titleFontMetrics = g.getFontMetrics ( titleFont );
  Font labelFont = new Font ( "Helvetica", Font.PLAIN, 10 );
  FontMetrics labelFontMetrics = g.getFontMetrics ( labelFont );

  int titleWidth = titleFontMetrics.stringWidth ( title );
  int y = titleFontMetrics.getAscent ();
  int x = (clientWidth - titleWidth) / 2;
  g.setFont ( titleFont );
  g.drawString ( title, x, y );

  int top = titleFontMetrics.getHeight ();
  int bottom = labelFontMetrics.getHeight ();
  if ( maxValue == minValue )  return;

  double scale = (clientHeight - top - bottom ) / (maxValue - minValue);
  y = clientHeight - labelFontMetrics.getDescent ();
  g.setFont (labelFont);

  for ( i = 0; i < n; i++ )
  {
    int x1 = i * barWidth + 1;
    int y1 = top;
    int height = (int) (values [ i ] * scale);

    if ( values [ i ] > 0 )
      y1 += (int) ( (maxValue - values [ i ]) * scale);
    else
    {
      y1 += (int) (maxValue * scale);
      height = -height;
    }  /* else */

    g.setColor ( Color.red );
    g.fillRect ( x1, y1, barWidth - 2, height );
    g.setColor ( Color.black );
    g.drawRect ( x1, y1, barWidth - 2, height );
    int labelWidth = labelFontMetrics.stringWidth ( names [ i ] );
    x = i * barWidth + (barWidth - labelWidth) / 2;
    g.drawString ( names [ i ], x, y );
  }  /* for */

}  /* method paint */


/******************************************************************************/
private int getInteger ( String line )
{
  int i = 0;
  int index = 0;
  int sign = 1;					// Default sign = +

  // Skip leading white space.
  while ( ( line.charAt ( index ) == ' ' ) ||
          ( line.charAt ( index ) == '\t' ) )  index++;

  // Check for a sign.
  if ( line.charAt ( index ) == '+' )
    index++;
  else
    if ( line.charAt ( index ) == '-' )
    {
      sign = -1;
      index++;
    }  /* if */

  // Traverse the integer.
  while ( index < line.length () )
  {
    if ( ( line.charAt ( index ) >= '0' ) && ( line.charAt ( index ) <= '9' ) )

      i = i * 10 + (int) line.charAt ( index ) - (int) '0';

    else  index = line.length ();		// Terminate loop

    index++;
  }  /* while */

  // Set the sign.
  i *= sign;

  return ( i );					// Return the integer
}  /* method getInteger */


}  /* class Chart */
