
// import FastaSequence;
// import Format;
// import LineTools;
// import Zipper;

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

public class Friends extends Object
{


/******************************************************************************/

  // Maximum number of protein sequences. 
  private final static int MAX_PROTEINS = 60000;


/******************************************************************************/

  // Summary of best matches between proteins
  private   Best best [] = new Best [ MAX_PROTEINS ];

  private   int  families = 0;				// number of familes

  private   int  groups = 0;				// number of family groups

  private   boolean identity = false;			// identity alignment flag

  private   int  super_families = 0;			// number of super protein familes

  private   int  total = 0;				// number of protein families


/******************************************************************************/
  // Constructor Friends
  public Friends ()
  {
    initialize ();
  }  // constructor Friends


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    families = 0;
    groups = 0;
    super_families = 0;
    total = 0;
  }  // method initialize 


/******************************************************************************/
  public void addNext ( Best next )
  {
    if ( total >= MAX_PROTEINS )
    {
      System.out.println ( ".addBest: *Warning* too many proteins." );
      return;
    }  // if

    best [ total ] = next;
    total++;
  }  // method addNext


/******************************************************************************/
  public int getTotal ()
  {
    return total;
  }  // method getTotal


/******************************************************************************/
  public int getFriends ()
  {
    return families;
  }  // method getFriends


/******************************************************************************/
  public int getGroups ()
  {
    return groups;
  }  // method getGroups


/******************************************************************************/
  public int getNewFamily ()
  {
    families++;
    return families;
  }  // method getNewFamily


/******************************************************************************/
  public int getNewSuperFamily ()
  {
    super_families++;
    return super_families;
  }  // method getNewSuperFamily


/******************************************************************************/
  public int getSuperFriends ()
  {
    return super_families;
  }  // method getSuperFriends


/******************************************************************************/
  public boolean isDuplicate ( FastaSequence fasta1 )
  {
    if ( total <= 0 )  return false;

    FastaSequence fasta2 = null;
    for ( int i = 0; i < total; i++ )
    {
      fasta2 = best [ i ].getFastaSequence ();

      // Check for duplicate sequence names.
      if ( fasta2.getShortName ().equals ( fasta1.getShortName () ) == true )

        return true;

      // Check for duplicate sequences for the same species.
      if ( fasta2.getSpecies ().equals ( fasta1.getSpecies () ) == true )

        if ( fasta2.getSequence ().toLowerCase ().equals 
           ( fasta1.getSequence ().toLowerCase () ) == true )

          return true;
    }  // for

    return false;
  }  // method isDuplicate


/******************************************************************************/
  public void setIdentity ( boolean value )
  {
    identity = value;
  }  // method setIdentity


/******************************************************************************/
  public void setTotal ( int value )
  {
    total = value;
  }  // method setTotal


/******************************************************************************/
  public void addFastaSequence ( FastaSequence fasta_sequence )
  {
    Best best = new Best ();
    best.setFastaSequence ( fasta_sequence );

    // Find the best match.
    // findBest ( best );

    // Add this protein to the list.
    addNext ( best );
  }  // method addFastaSequence


/******************************************************************************/
  public void sortHits ()
  {
    // Check for no proteins.
    if ( total <= 1 )  return;

    // Compare this protein to all other proteins.
    AminoWords words = best [ 0 ].getWords ();
    int matches = 0;
    // int words_total = 0;
    for ( int i = 1; i < total; i++ )
    {
      // Count the number of words common to both sequences.
      matches = words.countCommonWords ( best [ i ].getWords () );
      if ( words.getTotal () > 0 )
        best [ i ].setPercentFirst ( (byte) ( (matches * 100) / words.getTotal () ) ); 
    }  // for

    // Sort the best hits.
    if ( total == 2 )  return;
    Best temp = null;
    for ( int i = 1; i < total - 1; i++ )
    {
      for ( int j = i + 1; j < total; j++ )
      {
        // Compare by percent words.
        if ( best [ j ].getPercentFirst () > best [ i ].getPercentFirst () )
        {
          temp = best [ i ];
          best [ i ] = best [ j ];
          best [ j ] = temp;
        }  // if
        else
          // Check for the same percent identity.
          if ( best [ j ].getPercentFirst () == best [ i ].getPercentFirst () )
          {
            if ( best [ j ].getFastaSequence ().getLength () >
                 best [ i ].getFastaSequence ().getLength () )
            {
              temp = best [ i ];
              best [ i ] = best [ j ];
              best [ j ] = temp;
            }  // if
          }  // if
      }  // for
    }  // for

    // Find the best matches.
    for ( int i = 0; i < total; i++ )

      findBest ( i );
  }  // method sortHits


/******************************************************************************/
  public int checkGene ( String gene )
  {
    FastaSequence sequence = null;

    // Check for a protein for this gene with group_id set.
    for ( int i = 0; i < total; i++ )

      // Check for a known group id.
      if ( best [ i ].getGroup () > 0 )
      {
        sequence = best [ i ].getFastaSequence ();

        // Check for matching gene names.
        if ( gene.toLowerCase ().equals ( sequence.getGene ().toLowerCase () ) == true )

          return best [ i ].getGroup ();
      }  // if 

    return 0;
  }  // method checkGene


/******************************************************************************/
  public boolean isGeneMatches ( String gene, int index )
  {
    FastaSequence sequence = null;

    // Check for a protein for this gene with group_id set.
    for ( int i = index + 1; i < total; i++ )
    {
      sequence = best [ i ].getFastaSequence ();

      // Check for matching gene names.
      if ( gene.toLowerCase ().equals ( sequence.getGene ().toLowerCase () ) == true )

        return true;
    }  // if 

    return false;
  }  // method isGeneMatches


/******************************************************************************/
  public int checkDef ( String def )
  {
    FastaSequence sequence = null;
    String def_lower = def.toLowerCase ();

    // Check for a protein for this def with group_id set.
    for ( int i = 0; i < total; i++ )

      // Check for a known group id.
      if ( best [ i ].getGroup () > 0 )
      {
        sequence = best [ i ].getFastaSequence ();

        // Check for matching def strings.
        if ( def_lower.equals ( sequence.getDef ().toLowerCase () ) == true )

          return best [ i ].getGroup ();
      }  // if 

    return 0;
  }  // method checkDef


/******************************************************************************/
  public boolean isDefMatches ( String def, int index )
  {
    // Check for the end of the list.
    if ( index + 1 >= total )  return false;

    FastaSequence sequence = null;
    String def_lower = def.toLowerCase ();

    // Check for a protein for this def with group_id set.
    for ( int i = index + 1; i < total; i++ )
    {
      sequence = best [ i ].getFastaSequence ();

      // Check for matching def strings.
      if ( def_lower.equals ( sequence.getDef ().toLowerCase () ) == true )

        return true;
    }  // for

    return false;
  }  // method isDefMatches


/******************************************************************************/
  public void groupGenes ()
  {
    FastaSequence sequence = null;

    // Group the sequences by SwissProt IDs.
    String groups_id = "";
    String seq_id = "";
    for ( int i = 0; i < total; i ++ )

      if ( best [ i ].getGroup () == 0 )
      {
        sequence = best [ i ].getFastaSequence ();
        groups_id = sequence.getId ();

        if ( groups_id.length () > 0 )
        {
          groups++;
          best [ i ].setGroup ( groups );
          best [ i ].setGroupId ( groups_id );

          // Assign matching IDs to this group.
          if ( i + 1 < total )
            for ( int j = i + 1; j < total; j++ )
            {
              sequence = best [ j ].getFastaSequence ();
              seq_id = sequence.getId ();

              // Check that the family id matches.         
              // if ( best [ i ].getFamily () == best [ j ].getFamily () )
              { 
                if ( seq_id.equals ( groups_id ) == true )
                {
                  best [ j ].setGroup ( groups );
                  best [ j ].setGroupId ( seq_id );
                }  // if
              }  // if
            }  // for
        }  // if
      }  // if

    // System.out.println ( "By Swiss ID: --------------------------------------------------" );
    // summarize ();

    // Group the sequences by gene names.
    int group_id = 0;
    String group_gene = "";
    String seq_gene = "";
    for ( int i = 0; i < total; i ++ )

      if ( best [ i ].getGroup () == 0 )
      {
        sequence = best [ i ].getFastaSequence ();
        group_gene = sequence.getGene ().toLowerCase ();

        if ( group_gene.length () > 0 )
        {
          group_id = checkGene ( group_gene );

          if ( group_id == 0 )
            if ( isGeneMatches ( group_gene, i ) == true )  
              group_id = ++groups;

          if ( group_id > 0 )
          {
            best [ i ].setGroup ( group_id );
            best [ i ].setGroupId ( group_gene );

            // Assign matching genes to this group.
            if ( i + 1 < total )
              for ( int j = i + 1; j < total; j++ )
                if ( best [ j ].getGroup () == 0 )
                {
                  sequence = best [ j ].getFastaSequence ();
                  seq_gene = sequence.getGene ();
              
                  if ( seq_gene.toLowerCase ().equals ( group_gene ) == true )
                  {
                    best [ j ].setGroup ( group_id );
                    best [ j ].setGroupId ( seq_gene );
                  }  // if
                }  // if
          }  // if
        }  // if
      }  // if

    // System.out.println ( "By Gene name : --------------------------------------------------" );
    // summarize ();

    // Group the sequences by product/gene definition.
    group_id = 0;
    String group_def = "";
    String seq_def = "";
    for ( int i = 0; i < total; i ++ )

      if ( best [ i ].getGroup () == 0 )
      {
        sequence = best [ i ].getFastaSequence ();
        group_def = sequence.getDef ();

        if ( group_def.length () > 0 )
        {
          group_id = checkDef ( group_def );

          if ( group_id == 0 )
            if ( isDefMatches ( group_def, i ) == true )  
              group_id = ++groups;

          if ( group_id > 0 )
          {
            best [ i ].setGroup ( group_id );
            best [ i ].setGroupId ( group_def );
  
            // Assign matching genes to this group.
            if ( i + 1 < total )
              for ( int j = i + 1; j < total; j++ )
                if ( best [ j ].getGroup () == 0 )
                {
                  sequence = best [ j ].getFastaSequence ();
                  seq_def = sequence.getDef ().toLowerCase ();
                
                  if ( seq_def.equals ( group_def.toLowerCase () ) == true )
                  {
                    best [ j ].setGroup ( group_id );
                    best [ j ].setGroupId ( group_def );
                  }  // if
                }  // if
          }  // if
        }  // if
      }  // if

    // System.out.println ( "By Def: --------------------------------------------------" );
    // summarize ();

    // Add orphans to best match groups.
    int best_match = 0;
    for ( int i = 0; i < total; i++ )

      if ( best [ i ].getGroup () == 0 )
      {
        best_match = best [ i ].getBestMatch ();
        if ( best [ best_match ].getGroup () > 0 )
    
          if ( best [ i ].getPercentWords () >= 50 )
            best [ i ].setGroup ( best [ best_match ].getGroup () );
      }  // if

    // System.out.println ( "By Best match: --------------------------------------------------" );
    // summarize ();
  }  // method groupGenes


/******************************************************************************/
  public void alignGroup ( int group, String group_id )
  {
    msa.initialize ();

    System.out.print ( "---------------------------------------");
    System.out.println ( "---------------------------------------");
    if ( group > 0 )
      System.out.println ( "Group = " + group + " " + group_id );
    else
      System.out.println ( "Group name: " + group_id );
    System.out.println ();

    // Count the group members.
    int count = 0;
    for ( int i = 0; i < total; i++ )
      if ( best [ i ].getGroup () == group )  count++;

    // Check if no group members.
    if ( count <= 0 )  return;

    // Add the sequences to the alignment.
    for ( int i = 0; i < total; i++ )
      if ( best [ i ].getGroup () == group )
        msa.addBest ( best [ i ], identity );
  }  // method alignGroup


/*******************************************************************************/
  public String getGroupName ( int group )
  {
    String name = "";

    // Scan the group members.
    for ( int i = 0; i < total; i++ )
      if ( best [ i ].getGroup () == group )
      {
        name = best [ i ].getGroupId ();

        if ( name.length () > 0 )  return name + "_" + group;
      }  // if

    return "Group_" + group;
  }  // method getGroupName


/******************************************************************************/
  public void printInfo ()
  {
    FastaSequence sequence = null;

    System.out.print ( " #  Seq. Name  Gene     ID   Taxon.     Species   " );
    System.out.println ( "           group %ident Product" );
    for ( int i = 0; i < total; i++ )
    {
      sequence = best [ i ].getFastaSequence ();

      Format.intWidth ( i, 3 );
      System.out.print ( " " + sequence.getShortName ( 10 ) );
      System.out.print ( " " + LineTools.pad ( sequence.getGene (), 8 ) );
      System.out.print ( " " + LineTools.pad ( sequence.getId (), 4 ) );
      System.out.print ( " " + LineTools.pad ( sequence.getTaxonKey (), 10 ) );
      System.out.print ( " " + LineTools.pad ( sequence.getSpecies (), 20 ) );
      System.out.print ( "  g_" );
      Format.intWidthPost ( best [ i ].getGroup (), 3 );
      System.out.print ( "  " );
      Format.intWidth ( best [ i ].getPercentFirst (), 3 );
      System.out.print ( "%  " + sequence.getDef () );
      System.out.println ();
    }  // for
    System.out.println ();
  }  // method printInfo


/******************************************************************************/
  public void summarize ()
  {
    System.out.println ();
    System.out.println ( total + " protein sequences" );
    System.out.println ( families + " protein extended families" );
    System.out.println ( groups + " protein family groups" );
    System.out.println ( super_families + " protein super-families" );
    System.out.println ();
    System.out.println ( best [ 0 ].titleLine () );
    for ( int i = 0; i < total; i++ )
    {
      System.out.print ( LineTools.pad ( "" + i, 3 ) + "  " );
      best [ i ].summarize ();
    }  // for
    System.out.println ();

    printInfo ();
  }  // method summarize


/******************************************************************************/

}  // class Friends
