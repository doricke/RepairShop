

/******************************************************************************/

public class FastaSequence extends Object
{


/******************************************************************************/

  private   String  name = "";		// name of sequence

  private   String  description = "";	// sequence description

  private   String  sequence = "";	// sequence

  private   String  species = "";	// organism species

  private   byte  sequence_type = 0;	// type of sequence


/******************************************************************************/
  // Constructor FastaSequence
  public FastaSequence ()
  {
    initialize ();
  }  // constructor FastaSequence


/******************************************************************************/
  // Initialize class variables.
  public void initialize ()
  {
    name = "";
    description = "";
    sequence = "";
    species = "";
    sequence_type = 0;
  }  // method initialize 


/******************************************************************************/
  public String getName ()
  {
    return name;
  }  // method getName


/******************************************************************************/
  public String getDescription ()
  {
    return description;
  }  // method getDescription


/******************************************************************************/
  public int getLength ()
  {
    return sequence.length ();
  }  // method getLength


/******************************************************************************/
  public String getSequence ()
  {
    return sequence;
  }  // method getSequence


/******************************************************************************/
  public String getSpecies ()
  {
    return species;
  }  // method getSpecies


/******************************************************************************/
  public byte getSequenceType ()
  {
    return sequence_type;
  }  // method getSequenceType


/******************************************************************************/
  public void setName ( String value )
  {
    name = value;
  }  // method setName


/******************************************************************************/
  public void setDescription ( String value )
  {
    description = value;
  }  // method setDescription


/******************************************************************************/
  public void setSequence ( String value )
  {
    sequence = value;
  }  // method setSequence


/******************************************************************************/
  public void setSpecies ( String value )
  {
    species = value;
  }  // method setSpecies


/******************************************************************************/
  public void setSequenceType ( byte value )
  {
    sequence_type = value;
  }  // method setSequenceType


/******************************************************************************/
  public void parseHeader ( String line )
  {
    // Check for no header line.
    if ( line.length () <= 0 )
    {
      System.out.println ( "FastaSequence.parseHeader: *Warning* No header line." );
      return;
    }  // if

    // Check for invalid header line.
    if ( line.charAt ( 0 ) != '>' )
    {
      System.out.println ( "FastaSequence.parseHeader: *Warning* Invalid header line '" + line + "'" );
      return;
    }  // if

    int index1 = line.indexOf ( ' ' );
    int index2 = line.indexOf ( '\t' );
    if ( ( index2 > 0 ) && ( index2 < index1 ) )  index1 = index2;
    if ( ( index1 < 0 ) && ( index2 > 0 ) )  index1 = index2;

    if ( index1 < 0 )
    {
      name = line.substring ( 1 ).trim ();
      return;
    }  // if
 
    name = line.substring ( 1, index1 );

    if ( index1 + 1 < line.length () ) 
      description = line.substring ( index1 + 1 ).trim ();
  }  // method parseHeader


/******************************************************************************/
public String toString ()
{
  StringBuffer str = new StringBuffer ( 10000 );
  str.append ( ">" + name + " " + description + "\n" );

  for ( int index = 0; index < sequence.length (); index += 50 )
  {
    if ( index + 50 >= sequence.length () )
      str.append ( sequence.substring ( index ) + "\n" );
    else
      str.append ( sequence.substring ( index, index + 50 ) + "\n" );
  }  // for

  return str.toString ();
}  // method toString


/******************************************************************************/

}  // class FastaSequence
