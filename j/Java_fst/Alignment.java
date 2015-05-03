
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

public class Alignment extends Object
{

  public static final int MIN_AA_IDENTITIES = 24;	// Minimum number of AA identities

  public static final int MIN_AA_PERCENT = 40;		// Minimum AA percent identity


  public static final int MIN_DNA_IDENTITIES = 100;	// Minimum number of DNA identities

  public static final int MIN_DNA_PERCENT = 70;		// Minimum DNA percent identity


// Pfam limits.

  public static final int MIN_PFAM_AA_IDENTITIES = 20;	// Minimum Pfam number of AA identities

  public static final int MIN_PFAM_AA_PERCENT = 15;	// Minimum Pfam AA percent identity

  public static final int MIN_PFAM_REGION_PERCENT = 89;	// Minimum Pfam domain region percentage

}  // class Alignment
