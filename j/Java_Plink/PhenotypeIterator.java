
import java.util.Vector;

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

public class PhenotypeIterator extends InputFile
{


/******************************************************************************/

  MathTools math = new MathTools ();		// Math tools library

  float [] phenotypes = null;			// phenotypes 

  Vector phenotype_tokens = new Vector ();	// Current phenotypes 


/******************************************************************************/
public PhenotypeIterator ()
{
  initialize ();
}  // constructor PhenotypeIterator


/******************************************************************************/
public PhenotypeIterator ( String file_name )
{
  initialize ();

  setFileName ( file_name );
  openFile ();
}  // method PhenotypeIterator


/*******************************************************************************/
  public float [] getPhenotypes ()
  {
    return phenotypes;
  }  // method getPhenotypes


/*******************************************************************************/
  // This method classifies phenotypes into categories based on mean and SD
  public int classifyPhenotype ( float value )
  {
    // Check for a missing value.
    if ( value <= 0.0f )  return 0;

    double mean = math.getAverage ();
    double sd = math.getStandardDeviation ();

    if ( value >= mean + 2.0d * sd )  return 6;		// >= mean + 2 SD
    if ( value >= mean + sd )  return 5;		// >= mean + 1 SD && < 2 SD
    if ( value >= mean )  return 4;			// >= mean && < 1 SD
    if ( value > mean - sd )  return 3;			// >  mean -1 SD && < mean
    if ( value > mean - 2.0d * sd )  return 2;		// >  mean -2 SD && <= mean -1 SD
    return 1;						// <= mean -2 SD
  }  // method classifyPhenotype


/*******************************************************************************/
  public int [] classifyPhenotypes ()
  {
    // Assert: phenotypes
    if ( phenotypes == null )  return null;
    if ( phenotypes.length <= 0 )  return null;

    int [] pheno_class = new int [ phenotypes.length ];
    for ( int i = 0; i < phenotypes.length; i++ )
      pheno_class [ i ] = classifyPhenotype ( phenotypes [ i ] );

    // for ( int i = 0; i < phenotypes.length; i++ )
      // System.out.println ( pheno_class [ i ] + " " + phenotypes [ i ] );

    return pheno_class;
  }  // method classifyPhenotypes


/*******************************************************************************/
  public String next ( int column )
  {
    Tokens next_phenotype = new Tokens ();		// create a new object

    // Get the next phenotype file line.
    nextLine ();

    if ( line.length () <= 0 )  return null;

    // Parse the next file line.
    next_phenotype.parseLine ( line.toString () );

    return next_phenotype.getToken ( column - 1 );
  }  // method next


/******************************************************************************/
  public void readPhenotypes ( int column )
  {
    phenotype_tokens.removeAllElements ();

    while ( isEndOfFile () == false )
    {
      String token = next ( column );
      if ( token != null )
        phenotype_tokens.add ( token );
    }  // while

    if ( phenotype_tokens.size () <= 0 )  return;

    phenotypes = new float [ phenotype_tokens.size () ];
    for ( int i = 0; i < phenotype_tokens.size (); i++ )
      phenotypes [ i ] = LineTools.getFloat ( (String) phenotype_tokens.elementAt ( i ) );

    math.computeStandardDeviation ( phenotypes );
    // System.out.println ( "Ave+SD: " + math.toString () );

    // for ( int i = 0; i < phenotypes.length; i++ )
      // System.out.println ( classifyPhenotype ( phenotypes [ i ] ) + "\t" + phenotypes [ i ] );

    phenotype_tokens.removeAllElements ();
  }  // method readPhenotypes


/******************************************************************************/
public static void main ( String [] args )
{
  PhenotypeIterator app = new PhenotypeIterator ();
  Vector phenotypes = new Vector ();
  Tokens phenotype = null;

  if ( args.length >= 1 )  app.setFileName ( args [ 0 ] );
  else app.setFileName ( "phenotypes.txt" );

  app.openFile ();

  if ( args.length >= 2 )  
    app.readPhenotypes ( LineTools.getInteger ( args [ 1 ] ) );
  else 
    app.readPhenotypes ( 3 );

  // float [] phenos = null;
  // phenos = app.getPhenotypes ();
  int [] pheno_class = app.classifyPhenotypes ();

  app.closeFile ();

}  // method main


/******************************************************************************/

}  // class PhenotypeIterator

