
import java.util.Vector;

// import Alleles;
// import OutputFile;
// import Tokens;
// import TokensIterator;

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

public class TpedIterator extends InputFile
{


/******************************************************************************/

  final static int MAX_SNPS = 8;		// Maximum SNPs per haploblock


/******************************************************************************/

  Alleles alleles = new Alleles ();		// Alleles genotype

  Vector genotypes = new Vector ();		// Current haplotype block genotypes 

  Tokens next_genotype = null;			// Next genotype


/******************************************************************************/
public TpedIterator ()
{
  initialize ();
}  // constructor TpedIterator


/******************************************************************************/
public TpedIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method TpedIterator


/*******************************************************************************/
  public Vector getGenotypes ()
  {
    return genotypes;
  }  // method getTokens


/*******************************************************************************/
  public Vector next ()
  {
    int count = 1;			// SNPs in current haploblock

    // Clear last haplotype block.
    genotypes.removeAllElements ();

    // Add the first genotype of this haplotype block.
    if ( next_genotype == null ) next_genotype = nextGenotype ();
    genotypes.add ( next_genotype );

    String chromosome = next_genotype.getToken ( 0 );
    int last_position = LineTools.getInteger ( next_genotype.getToken ( 3 ) );

    next_genotype = nextGenotype ();
    if ( next_genotype == null )  return genotypes;

    int next_position = LineTools.getInteger ( next_genotype.getToken ( 3 ) );

    while ( ( isEndOfFile () == false ) && 
            ( next_position - last_position < 30000 ) &&
            ( next_genotype != null ) &&
            ( chromosome.equals ( next_genotype.getToken ( 0 ) ) ) )
    {
      // Skip non-informative SNPs
      if ( alleles.isMultiple () == true )
      {
        // Add this SNP to the haploblock
        genotypes.add ( next_genotype );
        count++;
        if ( count >= MAX_SNPS)  return genotypes;

        last_position = next_position;		// advance to next SNP
      }  // if

      next_genotype = nextGenotype ();
      if ( next_genotype != null )
        next_position = LineTools.getInteger ( next_genotype.getToken ( 3 ) );
    }  // while

    return genotypes;
  }  // method next


/*******************************************************************************/
  public Tokens nextGenotype ()
  {
    next_genotype = new Tokens ();		// create a new object

    // Get the next .tped file line.
    nextLine ();

    if ( line.length () <= 0 )  return null;

    // Parse the next file line.
    next_genotype.parseLine ( line.toString () );

    alleles.initialize ();
    alleles.genotype ( next_genotype );
    // System.out.println ( alleles.toString () );

    return next_genotype;
  }  // method nextGenotype


/******************************************************************************/
  public void listSnps ( Vector haploblock )
  {
    int count = haploblock.size ();
    if ( count <= 0 )  return;

    Tokens tokens = null;
    for ( int i = 0; i < count; i++ )
    {
      tokens = (Tokens) haploblock.elementAt ( i );
 
      System.out.print ( tokens.getToken ( 1 ) );
      System.out.print ( "(" + tokens.getToken ( 3 ) + ")" );

      if ( i < count + 1 )  System.out.print ( ", " );
    }  // for
    System.out.println ();
  }  // method listSnps


/******************************************************************************/
public static void main ( String [] args )
{
  TpedIterator app = new TpedIterator ();
  Vector genotypes = new Vector ();

  String filename = "test.tped";
  if ( args.length == 1 )  filename = args [ 0 ];
  app.setFileName ( filename );
  app.openFile ();

  while ( app.isEndOfFile () == false )
  {
    genotypes = app.next ();
    // app.listSnps ( genotypes ); 
  }  // while
  app.closeFile ();

}  // method main


/******************************************************************************/

}  // class TpedIterator

