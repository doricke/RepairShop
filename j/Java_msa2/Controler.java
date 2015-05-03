
import Align;
import Sequence;
import SeqTools;

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

public class Controler extends Object
{

/******************************************************************************/

String file_name1;				// First input file

String file_name2;				// Second input file

String file_name3;				// Third input file

Sequence seq1 = new Sequence ();		// First sequence

Sequence seq2 = new Sequence ();		// Second sequence

Sequence seq3 = new Sequence ();		// Third sequence


/******************************************************************************/
public Controler ()
{
  initialize ();
}  // constructor Controler


/******************************************************************************/
public void initialize ()
{
  file_name1 = null;
  file_name2 = null;
  file_name3 = null;
}  // method initialize


/******************************************************************************/
public void setFileName1 ( String filename1 )
{
  file_name1 = filename1;
}  // method setFileName1


/******************************************************************************/
public void setFileName2 ( String filename2 )
{
  file_name2 = filename2;
}  // method setFileName2


/******************************************************************************/
public void setFileName3 ( String filename3 )
{
  file_name3 = filename3;
}  // method setFileName3


/******************************************************************************/


/******************************************************************************/


/******************************************************************************/
public void process ()
{
  seq1.setFileName ( file_name1 );
  seq1.openSequenceFile ();
  seq1.nextSequence ();
  // System.out.println ( "seq1 = " + seq1.getSequence () );

  seq2.setFileName ( file_name2 );
  seq2.openSequenceFile ();
  seq2.nextSequence ();
  // System.out.println ( "seq2 = " + seq2.getSequence () );

  Align align = new Align ();
  align.align ( seq1.getSequence (), seq2.getSequence () );

  // align.printCoordinates ();

  seq3.setFileName ( file_name3 );
  seq3.openSequenceFile ();
  seq3.setSequenceType ( Sequence.AA );
  seq3.nextSequence ();
  // System.out.println ( "seq3 = " + seq3.getSequence () );

  // System.out.println ( SeqTools.convert1to3 ( seq3.getSequence () ) );

  align.alignPep ( seq1.getSequence (), SeqTools.convert1to3 ( seq3.getSequence () ) );

  align.printCoordinates ();
  align.printAlignment ();
}  // method process


/******************************************************************************/
public static void usage ()
{
  System.out.println ( "This is the Controler program." );
  System.out.println ();
  System.out.println ( "This program aligns two sequences." );
  System.out.println ();
  System.out.println ( "To run type:" );
  System.out.println ();
  System.out.println ( "java Controler <name.1> <name.2> <name.3>" );
  System.out.println ();
  System.out.println ( "Where <name.1> is genomic sequence" );
  System.out.println ( "  and <name.2> is cDNA sequence" );
  System.out.println ( "  and <name.3> is peptide sequence" );
  System.out.println ();
}  // method usage


/******************************************************************************/
public static void main ( String [] args )
{
  if ( args.length <= 1 )
    usage ();
  else
  {
    Controler application = new Controler ();

    application.setFileName1 ( args [ 0 ] );

    application.setFileName2 ( args [ 1 ] );

    application.setFileName3 ( args [ 2 ] );

    application.process ();
  }  // else
}  // method main


/******************************************************************************/

}  // class Controler
