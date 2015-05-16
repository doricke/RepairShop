

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

public class Ssr extends Object
{


/******************************************************************************/

  private  static  int  MAX_LENGTHS = 1002;	// Maximum SSR length


/******************************************************************************/

  private  String name;					// SSR name

  private  int counts [] = new int [ MAX_LENGTHS ];	// SSR length counts

  private  int max_length = 0;				// Maximum length value


/******************************************************************************/
public Ssr ( int index )
{
  initialize ();
}  // constructor Ssr


/******************************************************************************/
public void initialize ()
{
  name = "";

  // Allocate mono-nucleotide SSR objects.
  for ( int i = 0; i < MAX_LENGTHS; i++ )

    counts [ i ] = 0;

  max_length = 0;
}  // method initialize


/******************************************************************************/
public void add ( int count, String pattern, int units )
{
  name = pattern;

  if ( units > max_length )  max_length = units;

  if ( units < MAX_LENGTHS - 1 )

    counts [ units ] += count;

  else

    counts [ MAX_LENGTHS - 1 ] += count;
}  // method add


/******************************************************************************/
public int getMaxLength ()
{
  return max_length;
}  // method getMaxLength


/******************************************************************************/
public int[] getCounts ()
{
  return counts;
}  // method getCounts


/******************************************************************************/
public int getCount ( int index )
{
  if ( index < MAX_LENGTHS - 1 )
    return  counts [ index ];

  return  counts [ MAX_LENGTHS - 1 ];
}  // method getCount


/******************************************************************************/
public String getName ()
{
  return name;
}  // method getName


/******************************************************************************/

}  // class Ssr

