
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

// import Affy500;
// import Affy500Iterator;
// import Database;
// import DB_Objects;
// import Gene;
// import GeneIterator;
// import Plink;
// import PlinkIterator;
// import Row;

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

/*******************************************************************************/
public class GUI extends JFrame
{
  public static int WIDTH = 650;
  public static int HEIGHT = 720;

  private static String TITLE = "WGA Studies Data Loader";

  private static String openTitle = "Select file";

  private static String genesTitle = "Genes file";

  private static String plinkTitle = "PLINK results";

  private static String snpsTitle = "SNP Annotatons";


  private boolean fileSelected = false;			// file selected flag

  private Database database = new Database ();		// Relational database

  private int dataset_id = 0;				// dataset_id

  private DB_Objects db_objects = new DB_Objects ();	// Database interface

  private String file_name = "";			// file_name

  private int organism_id = 0;				// organism_id

  private Row row = new Row ();				// Database row object

  private String snp_platform = "";			// snp_platform


  Container frameContainer;

  // Swing components:
  JPanel [] panels = new JPanel [ 5 ];

  JMenuBar menuBar = new JMenuBar ();

  JMenu fileMenu = new JMenu ( "File" );

  JMenuItem fileExit = new JMenuItem ( "Exit" );

  JTextField datasetField      = new JTextField ( "", 20 );
  JTextField datasetDescField  = new JTextField ( "", 40 );
  JTextField fileField         = new JTextField ( "", 20 );
  JTextField organismField     = new JTextField ( "", 20 );

  JRadioButton affy500Button = new JRadioButton ( "Affy 500K V1" );
  JRadioButton ill317Button  = new JRadioButton ( "Illumina 317K" );
  JRadioButton affy100Button = new JRadioButton ( "Affy 100K" );

  ButtonGroup sequenceType = new ButtonGroup ();

  JButton plinkButton = new JButton ( plinkTitle );
  JButton geneButton  = new JButton ( genesTitle );
  JButton snpsButton  = new JButton ( snpsTitle );
  JButton openButton  = new JButton ( openTitle );

  String [] datasets;
  JList datasetsList;

  String [] organisms;
  JList organismsList;

  // JDialog box.
  JDialog dialog;


/*******************************************************************************/
  public GUI ()
  {
    super ( TITLE );
    database.connectDB ();
    db_objects.setDatabase ( database );

    // Set up the display lists. 
    // Create a Row object and link it to the database.
    row.setConnection ( database.getConnection () );

    updateDatasets ();
    updateOrganisms ();

    // Initialization code.
    for ( int i = 0; i < panels.length; ++i )

      panels [ i ] = new JPanel ();

    affy500Button.setSelected ( true );

    sequenceType.add ( affy500Button );
    sequenceType.add ( ill317Button );
    sequenceType.add ( affy100Button );

    buildGUI ();
    setupEventHandlers ();
  }  // method GUI


/*******************************************************************************/
  // Build the Dataset list from the database.
  private void updateDatasets ()
  {
    // Get the list of dataset names from the Data.
    int rows = row.countRows ( "Dataset" );
    row.setFetchSize ( rows );
    row.selectSQL ( "SELECT dataset_name FROM Dataset" +
        " ORDER BY dataset_name ASC" );
    datasets = null;
    datasets = row.getColumn ( rows );

    row.releaseResults ();
    row.clear ();
  }  // method updateDatasets


/*******************************************************************************/
  // Build the Dataset list from the database.
  private void updateOrganisms ()
  {
    // Get the list of dataset names from the Data.
    int rows = row.countRows ( "Organism" );
    row.setFetchSize ( rows );
    row.selectSQL ( "SELECT organism_name FROM Organism" +
        " ORDER BY organism_name ASC" );
    organisms = null;
    organisms = row.getColumn ( rows );

    row.releaseResults ();
    row.clear ();
  }  // method updateOrganisms


/*******************************************************************************/
  private void buildGUI () 
  {
    setupMenuBar ();
    layoutComponents ();
  }  // method buildGUI


/*******************************************************************************/
  private void setupMenuBar ()
  {
    fileMenu.add ( fileExit );

    menuBar.add ( fileMenu );

    setJMenuBar ( menuBar );
  }  // method setupMenuBar


/*******************************************************************************/
  private void layoutComponents ()
  {
    for ( int i = 0; i < panels.length; ++i )

      panels [ i ].removeAll ();

    datasetsList = new JList ( datasets ); 
    JScrollPane datasetsPane = new JScrollPane ( datasetsList );

    organismsList = new JList ( organisms ); 
    JScrollPane organismsPane = new JScrollPane ( organismsList );

    panels [ 0 ].add ( datasetsPane );
    panels [ 0 ].add ( datasetField );
    panels [ 0 ].add ( datasetDescField );
    panels [ 0 ].setBorder ( new TitledBorder ( "Step 1 - Identify Dataset Name & Description" ) );

    panels [ 1 ].setLayout ( new GridLayout ( 6, 1 ) );
    panels [ 1 ].add ( affy500Button );
    panels [ 1 ].add ( affy100Button );
    panels [ 1 ].add ( ill317Button );
    panels [ 1 ].setBorder ( new TitledBorder ( "Step 2 - Select SNP platform" ) );

    panels [ 2 ].add ( organismsPane );
    panels [ 2 ].add ( organismField );
    panels [ 2 ].setBorder ( new TitledBorder ( "Step 3 - Identify organism" ) );

    panels [ 3 ].add ( openButton );
    panels [ 3 ].add ( fileField );
    panels [ 3 ].setBorder ( new TitledBorder ( "Step 4 - Select input file" ) );

    panels [ 4 ].add ( plinkButton );
    panels [ 4 ].add ( geneButton );
    panels [ 4 ].add ( snpsButton );
    panels [ 4 ].setBorder ( new TitledBorder ( "Step 5 - Load data to database" ) );

    frameContainer = getContentPane ();
    frameContainer.setLayout ( new GridLayout ( 3, 2 ) );

    for ( int i = 0; i < panels.length; ++i )

      frameContainer.add ( panels [ i ] );

    setSize ( WIDTH, HEIGHT );
    show ();
  }  // method layoutComponents


/*******************************************************************************/
  void setupEventHandlers ()
  {
    addWindowListener ( new WindowHandler () );

    fileExit.addActionListener ( new MenuItemHandler () );

    plinkButton.addActionListener ( new ButtonHandler () );

    geneButton.addActionListener ( new ButtonHandler () );

    openButton.addActionListener ( new ButtonHandler () );

    snpsButton.addActionListener ( new ButtonHandler () );

    datasetsList.addListSelectionListener ( new ListHandler () );

    organismsList.addListSelectionListener ( new ListHandler () );
  }  // method setupEventHandlers


/*******************************************************************************/
  private boolean checkDataset ()
  {
    // Identify the Dataset
    datasetField.selectAll ();
    if ( datasetField.getSelectedText () == null )
    {
      showDialog ( "Please select a dataset first" );
      return false;
    }  // if

    String dataset_description = "";
    if ( datasetDescField.getSelectedText () != null )
      dataset_description = datasetDescField.getSelectedText ();

    // Check the list for the selected item.
    int index = find ( datasets, datasetField.getSelectedText () );

    if ( index == -1 )
    {
      dataset_id = db_objects.newDataset 
          ( organism_id
          , datasetField.getSelectedText ()
          , dataset_description 
          , dataset_description 
          , snp_platform
          );

      System.out.println ( "newDataset called; dataset_id = " + dataset_id );
    }  // if
    else
    {
      dataset_id = db_objects.getDatasetId ( datasetField.getSelectedText () );
      System.out.println ( "getDataset called; dataset_id = " + dataset_id );
    }  // else

    return true;
  }  // method checkDataset


/*******************************************************************************/
  private boolean checkOrganism ()
  {
    // Identify the Organism
    organismField.selectAll ();
    if ( organismField.getSelectedText () == null )
    {
      showDialog ( "Please select a organism first" );
      return false;
    }  // if

    // Check the list for the selected item.
    int index = find ( organisms, organismField.getSelectedText () );
    if ( index == -1 )
    {
      db_objects.newOrganism ( organismField.getSelectedText () );
      organism_id = db_objects.getOrganismId ( organismField.getSelectedText () );

      // System.out.println ( "newOrganism called; organism_id = " + organism_id );
    }  // if
    else
    {
      organism_id = db_objects.getOrganismId ( organismField.getSelectedText () );
      // System.out.println ( "getOrganism called; organism_id = " + organism_id );
    }  // else

    return true;
  }  // method checkOrganism


/*******************************************************************************/
  private void getOrganismId ()
  {
    if ( organism_id > 0 )  return;

    // Check for Organism name (genus species).
    if ( organismField.getSelectedText () != null )
    
      organism_id = db_objects.getOrganismId ( organismField.getSelectedText () );

System.out.println ( "GUI.getOrganismId = " + organism_id );
  }  // method getOrganismId


/*******************************************************************************/
  private boolean getSelections ()
  {
    // Check the selected Dataset.
    if ( checkDataset () == false )
    {
      showDialog ( "Please select target Dataset first" );
      return false;
    }  // if

    // Check the selected Organism.
    if ( checkOrganism () == false )
    {
      showDialog ( "Please select target Organism first" );
      return false;
    }  // if

    // Set up the type of sequences being imported.
    if ( affy500Button.isSelected () )  snp_platform = "Affy500K";
    if ( affy100Button.isSelected () )  snp_platform = "Affy100K";
    if ( ill317Button.isSelected  () )  snp_platform = "Illumina 317K";

    // Check if the user has modified the file name.
    fileField.selectAll ();
    if ( fileField.getSelectedText () != null )

      file_name = fileField.getSelectedText ();

    return true;
  }  // method getSelections


/*******************************************************************************/
  private void done ()
  {
    row.releaseConnection ();
    row = null;

    // Dispose of the GUI interface.
    dispose ();
    System.exit ( 0 );
  }  // method done


/*******************************************************************************/
  private void processGenes ( String genes_filename )
  {
    GeneIterator genes_file = new GeneIterator ();
    genes_file.setFileName ( genes_filename );
    genes_file.openFile ();

    Gene gene = genes_file.next ();		// skip the header line

    while ( genes_file.isEndOfFile () == false )
    {
      // Get the next Gene record from the file.
      gene = genes_file.next ();

      if ( gene != null )
      {
        int gene_id = db_objects.newGene
            ( gene.getSymbol ()
            , gene.getSymbol ()
            , gene.getGeneId ()
            , gene.getDescription ()
            );

        if ( gene_id > 0 )
        {
          int gene_map_id = db_objects.newGeneMap
              ( 2					// genome_id
              , gene_id
              , gene.getChromosome ()
              , gene.getChromosomeStart ()
              , gene.getChromosomeEnd ()
              );
        }  // if
      }  // if
    }  // while

    genes_file.closeFile ();
  }  // method processGenes


/*******************************************************************************/
  private void processSnps ( String snps_filename )
  {
    // Create the parser object
    Affy500Iterator snps_file = new Affy500Iterator ();

    snps_file.setFileName ( snps_filename );
    snps_file.openFile ();

    // Skip the header line.
    Affy500 affy500 = snps_file.next ();

    // Process the Affy500 files.
    while ( snps_file.isEndOfFile () == false )
    {
      // Get the next affy500.
      affy500 = snps_file.next ();

      if ( affy500 != null )
        if ( affy500.getAffyId ().length () > 0 )
      {
        // Insert the SNP record.
        int snp_id = db_objects.newSnp
            ( affy500.getAffyId ()
            , affy500.getRsId ()
            , affy500.getGeneSymbols ()
            , affy500.getGeneNames ()
            , affy500.getGeneIds ()
            );

         // Insert the SNP_Map record.
         db_objects.newSnpMap
             ( affy500.getChromosome ()
             , affy500.getPosition ()
             , 2			// NCBI Genome build 36
             , snp_id 
             );
      }  // if
    }  // while

    // Close the file.
    snps_file.closeFile ();
  }  // method processSnps


/*******************************************************************************/
// Returns the index in list of target or -1.
  private int find ( String [] list, String target )
  {
    // Search the list serially
    int i = 0;
    while ( i < list.length )
    {
      // Check for the target
      if ( list [ i ].equals ( target ) )  return i;
      i++;
    }  // while 

    return -1;
  }  // method find


/*******************************************************************************/
// Selects the first item in the list as the default if target is not found.
  private int select ( String [] list, String target )
  {
    // Find the target in the list.
    int index = find ( list, target );
    if ( index != -1 )  return index;
    return 0;
  }  // method select


/*******************************************************************************/
  public void selectFile ()
  {
    if ( ! getSelections () )  return;

    JFileChooser chooser = new JFileChooser ( "." );

    int option = chooser.showOpenDialog ( this );

    if ( option == JFileChooser.APPROVE_OPTION )
    {
      if ( chooser.getSelectedFile () != null )
      {
        file_name = chooser.getSelectedFile ().getName ();
        fileField.replaceSelection ( chooser.getSelectedFile ().getName () );
        fileSelected = true;
      }  // if 
    }  // if
  }  // method selectFile


/******************************************************************************/
  public void processPlink ( String plink_filename )
  {
    int snp_id = 0;		// SNP table key


    // Get the organism_id for the organism name.
    getOrganismId ();

    PlinkIterator plink_file = new PlinkIterator ();
    plink_file.setFileName ( plink_filename );
    plink_file.openFile ();

    Plink plink = plink_file.next ();		// Ignore header line

    while ( plink_file.isEndOfFile () == false )
    {
      // Get the next PLINK result from the file.
      plink = plink_file.next ();

      if ( plink != null )
      {
        // Find the SNP.
        snp_id = db_objects.getSnpId ( plink.getRsId () );

        // Insert the new record.
        db_objects.newSnpProbe ( dataset_id, plink );
      }  // if
    }  // while

    plink_file.closeFile ();
  }  // method processPlink


/*******************************************************************************/
    public void showDialog ( String message )
    {
      JFrame frame = new JFrame ();
      dialog = new JDialog ( frame, message, true );
      dialog.setSize ( 350, 150 );

      JLabel label = new JLabel ( message, JLabel.CENTER );
      dialog.getContentPane ().setLayout ( new BorderLayout () );
      dialog.getContentPane ().add ( label, BorderLayout.CENTER );

      JButton okay = new JButton ( "OK" );
      okay.addActionListener ( 
          new ActionListener ()
          {
            public void actionPerformed ( ActionEvent ae )
            {
              dialog.setVisible ( false );
              dialog.dispose ();
            }  // method actionPerformed
          }  // ActionListener
      );

      JPanel panel = new JPanel ();
      panel.add ( okay );
      dialog.getContentPane ().add ( panel, BorderLayout.SOUTH );
      dialog.setLocationRelativeTo ( frame );
      dialog.setVisible ( true );
      frame.setVisible ( true );
    }  // method showDialog


/*******************************************************************************/
  protected void shutDown ()
  {
    // Disconnect from the database.
    if ( database != null )
      database.disconnectDB ();
    database = null;
    db_objects = null;

    if ( row != null )
      row.releaseConnection ();
    row = null;

    System.exit ( 0 );
  }  // method shutDown


/*******************************************************************************/
  public static void main ( String [] args )
  {
    GUI app = new GUI ();
  }  // method main


/*******************************************************************************/
  public class ListHandler implements ListSelectionListener
  {
    public void valueChanged ( ListSelectionEvent e )
    {
      if ( e.getSource ().equals ( datasetsList ) )
      {
        datasetField.selectAll ();
        datasetField.replaceSelection ( (String) datasetsList.getSelectedValue () );
      }  // if

      if ( e.getSource ().equals ( organismsList ) )
      {
        organismField.selectAll ();
        organismField.replaceSelection ( (String) organismsList.getSelectedValue () );
      }  // if
    }  // method valueChanged

  }  // class ListHandler


/*******************************************************************************/
  public class WindowHandler extends WindowAdapter 
  {
    public void windowClosing ( WindowEvent e )
    {
      shutDown ();
    }  // method windowClosing
  }  // class WindowHandler


/*******************************************************************************/
  public class MenuItemHandler implements ActionListener
  {
    public void actionPerformed ( ActionEvent e )
    {
      shutDown ();
    }  // method actionPerformed
  }  // class MenuItemHandler


/*******************************************************************************/
  public class ButtonHandler implements ActionListener
  {
    public void actionPerformed ( ActionEvent e )
    {
      String cmd = e.getActionCommand ();

      if ( cmd.equals ( openTitle ) )
      {
        selectFile ();
      }  // if 
      else if ( cmd.equals ( snpsTitle ) )
      {
        // Ensure that a file has been selected first.
        if ( fileSelected )
        {
          // Hide the GUI interface.
          hide ();

          // Process the SNP Annotations.
          processSnps ( file_name );

          done ();		// exit
        }
        else
          showDialog ( "Please select an input file first" );
      }  // else if 
      else if ( cmd.equals ( plinkTitle ) )
      {
        // Ensure that a file has been selected first.
        if ( fileSelected )
        {
          // Hide the GUI interface.
          hide ();

          processPlink ( file_name );

          done ();		// exit
        }
        else
          showDialog ( "Please select an input file first" );
      }  // else if
      else if ( cmd.equals ( genesTitle ) )
      {
        // Ensure that a file has been selected first.
        if ( fileSelected )
        {
          // Hide the GUI interface.
          hide ();

          // Process the genes.
          processGenes ( file_name );

          done ();		// exit
        }
        else
          showDialog ( "Please select an input file first" );
      }  // else if
    }  // method actionPerformed

  }  // class ButtonHandler


/*******************************************************************************/

}  // class GUI


