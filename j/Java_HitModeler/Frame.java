
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

public class Frame extends Object
{


/******************************************************************************/

  private   String  bases_3 = "";		// 3' end incomplete codon bases

  private   String  bases_5 = "";		// 5' end incomplete codon bases

  private   int  frame = 0;			// frame number {0, 1, 2}

  private   int  frame_minimum = 0;		// minimum frame limit

  private   int  frame_maximum = 0;		// maximum frame limit

  private   int  hit_begin = 0;			// start of similarity

  private   int  hit_end = 0;			// end of similarity

  private   boolean  open = false;		// open reading frame flag

  private   String  translation = "";		// genomic frame translation


/******************************************************************************/
  // Constructor Frame
  public Frame ()
  {
    initialize ();
  }  // constructor Frame


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    bases_3 = "";
    bases_5 = "";
    frame = 0;
    frame_minimum = 0;
    frame_maximum = 0;
    hit_begin = 0;
    hit_end = 0;
    open = false;
    translation = "";
  }  // method initialize 


/******************************************************************************/
  public void close ()
  {
    initialize ();
  }  // method close


/******************************************************************************/
  public String getBases3 ()
  {
    return bases_3;
  }  // method getBases3


/******************************************************************************/
  public String getBases5 ()
  {
    return bases_5;
  }  // method getBases5


/******************************************************************************/
  public int getFrame ()
  {
    return frame;
  }  // method getFrame


/******************************************************************************/
  public int getFrameMinimum ()
  {
    return frame_minimum;
  }  // method getFrameMinimum


/******************************************************************************/
  public int getFrameMaximum ()
  {
    return frame_maximum;
  }  // method getFrameMaximum


/******************************************************************************/
  public int getHitBegin ()
  {
    return hit_begin;
  }  // method getHitBegin


/******************************************************************************/
  public int getHitEnd ()
  {
    return hit_end;
  }  // method getHitEnd


/******************************************************************************/
  public boolean getOpen ()
  {
    return open;
  }  // method getOpen


/******************************************************************************/
  public String getTranslation ()
  {
    return translation;
  }  // method getTranslation


/******************************************************************************/
  public boolean isOpen ()
  {
    return open;
  }  // method isOpen


/******************************************************************************/
  public void setBases3 ( String value )
  {
    bases_3 = value;

/*
    System.out.println ( "Frame " + frame + ", 3' bases = " + bases_3 + 
        " " + translation.substring ( hit_end - 6, hit_end + 1 ) );
*/
  }  // method setBases3


/******************************************************************************/
  public void setBases5 ( String value )
  {
    bases_5 = value;

/*
    System.out.println ( "Frame " + frame + ", 5' bases = " + bases_5 +
        " " + translation.substring ( hit_begin, hit_begin + 7 ) );
*/
  }  // method setBases5


/******************************************************************************/
  public void setFrame ( int value )
  {
    frame = value;
  }  // method setFrame


/******************************************************************************/
  public void setFrameMinimum ( int value )
  {
    frame_minimum = value;
  }  // method setFrameMinimum


/******************************************************************************/
  public void setFrameMaximum ( int value )
  {
    frame_maximum = value;

    if ( frame_maximum >= hit_end )
      open = true;
    else
      open = false;
  }  // method setFrameMaximum


/******************************************************************************/
  public void setHitBegin ( int value )
  {
    hit_begin = value;
  }  // method setHitBegin


/******************************************************************************/
  public void setHitEnd ( int value )
  {
    hit_end = value;
  }  // method setHitEnd


/******************************************************************************/
  public void setOpen ( boolean value )
  {
    open = value;
  }  // method setOpen


/******************************************************************************/
  public void setTranslation ( String value )
  {
    translation = value;
  }  // method setTranslation


/******************************************************************************/

}  // class Frame
