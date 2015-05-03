
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.io.File;
import java.util.Vector;

// import AnalysisMaster;
// import InputTools;


/*******************************************************************************/
/**
 *
 * @author	Darrell O. Ricke, PhD
 * @version	1.0
 */

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

public class RemoteBlast extends JFrame
{
  public static int WIDTH = 800;
  public static int HEIGHT = 672;

  private static String TITLE = "Remote Analysis Software";

  private static String openTitle = "Select file";

  private static String runTitle = "Start";


  private boolean fileSelected = false;			// file selected flag


  Container frameContainer;

  // Swing components:
  JPanel [] panels = new JPanel [ 12 ];

  JMenuBar menuBar = new JMenuBar ();

  JMenu fileMenu = new JMenu ( "File" );

  JMenuItem fileExit = new JMenuItem ( "Exit" );

  JTextField fileField        = new JTextField ( "", 20 );
  JTextField sourceField      = new JTextField ( "", 20 );
  JTextField destinationField = new JTextField ( "", 20 );
  JTextField databaseField    = new JTextField ( "", 20 );

  String [] programs1 = { "blastn"
                        , "blastx"
                        , "blastp"
                        , "tblastn"
                        , "tblastx"
                        , "fasta"
                        , "fasty"
                        , "tfasta"
                        , "tfasty"
                        , "motifs"
                        };

  String [] programs2 = { "S-W"
                        , "Tera-BLASTN"
                        , "Tera-BLASTX"
                        , "Tera-BLASTP"
                        , "Tera-TBLASTN"
                        , "Tera-TBLASTX"
                        , "framesearch"
                        , "pfam"
                        };

  JRadioButton [] programs1Button = new JRadioButton [ programs1.length ];
  JRadioButton [] programs2Button = new JRadioButton [ programs2.length ];

  JRadioButton gmhmmButton   = new JRadioButton ( "gmhmm" );
  JRadioButton genscanButton = new JRadioButton ( "genscan" );
  JRadioButton fgeneshButton = new JRadioButton ( "fgenesh" );

  String [] decyphers = { "160.62.226.103"
                        };

  String [] morpheusLower = { "morpheus1"
                            , "morpheus2"
                            , "morpheus3"
                            // , "morpheus4"
                            // , "morpheus5"
                            , "morpheus6"
                            , "morpheus7"
                            , "morpheus8"
                            , "morpheus9"
                            , "morpheus10"
                            , "morpheus11"
                            , "morpheus12"
                            , "morpheus13"
                            , "morpheus14"
                            , "morpheus15"
                            , "morpheus16"
                            , "morpheus17"
                            , "morpheus18"
                            };

  String [] morpheusUpper = 
                          { "morpheus28"
                          , "morpheus29"
                          , "morpheus30"
                          , "morpheus31"
                          , "morpheus32"
                          , "morpheus33"
                          , "morpheus34"
                          , "morpheus35"
                          , "morpheus36"
                          , "morpheus37"
                          , "morpheus38"
                          , "morpheus39"
                          , "morpheus40"
                          , "morpheus41"
                          , "morpheus42"
                          };

  String [] greeks =  
                     // { "hades" 
                     // , "hestia" 
                     // , "poseidon" 
                     // , "demeter" 
                     { "apollo"
                     , "artemis"
                     , "ares"
                     , "athena"
                     , "hephaestus"
                     // , "aphrodite"
                     , "dionysus"
                     , "persephone" 
                     };

  String [] hp_lower = 
                    { "nabriclu1"
                    , "nabriclu2"
                    , "nabriclu3"
                    , "nabriclu4"
                    , "nabriclu5"
                    , "nabriclu6"
                    , "nabriclu7"
                    , "nabriclu8"
                    , "nabriclu9"
                    , "nabriclu10"
                    };

  String [] hp_upper = 
                     { "nabriclu11"
                     , "nabriclu12"
                     , "nabriclu13"
                     , "nabriclu14"
                     , "nabriclu15"
                     , "nabriclu16"
                     , "nabriclu17"
                     , "nabriclu18"
                     , "nabriclu19"
                     , "nabriclu20"
                     };

  String [] sun  = { "sun" };
  String [] sgi  = { "sgi" };

  JRadioButton morpheusLowerButton      = new JRadioButton ( "Morpheus1" );
  JRadioButton morpheusUpperButton      = new JRadioButton ( "Morpheus2" );
  JRadioButton greeksButton      = new JRadioButton ( "Greeks" );

  JRadioButton hpLowerButton  = new JRadioButton ( "HP Lower" );
  JRadioButton hpUpperButton  = new JRadioButton ( "HP Upper" );

  JRadioButton sunButton         = new JRadioButton ( "Sun System" );
  JRadioButton sgiButton         = new JRadioButton ( "SGI System" );
  JRadioButton decypherButton    = new JRadioButton ( "TL Decyphers" );

  ButtonGroup programType = new ButtonGroup ();

  JButton openButton   = new JButton ( openTitle );

  JButton runButton    = new JButton ( runTitle );

  String [] databases = { "ecoli.nt"
                        , "pdbaa"
                        , "rs_human.aas"
                        , "uniprot_sprot"
                        , "uniprot_trembl"
                        , "vector"
                        };

  String [] gs_models = { "Arabidopsis", "Maize", "HumanIso" };
  String [] gm_models = { "Athalian", "Celegans", "Chicken", "Chlamydo", "Drosophi", "Human", "Rice" };
  String [] fg_models = { "Monocots", "Dicots" };

  String [] cpu_numbers = { "1", "2", "4", "8", "16", "32", "64" };

  JList cpuNumbersList;
  JList databaseList;
  JList gsModelsList;
  JList gmModelsList;
  JList fgModelsList;

  // JDialog box.
  JDialog dialog;

  AnalysisMaster analysis_master = new AnalysisMaster ();


/*******************************************************************************/
  public RemoteBlast ()
  {
    super ( TITLE );

    updateDatabases ();

    // Initialization code.
    for ( int i = 0; i < panels.length; ++i )

      panels [ i ] = new JPanel ();

    for ( int i = 0; i < programs1.length; i++ )
  
      programs1Button [ i ] = new JRadioButton ( programs1 [ i ] );
  
    for ( int i = 0; i < programs2.length; i++ )
  
      programs2Button [ i ] = new JRadioButton ( programs2 [ i ] );
  
    // blastnButton.setSelected ( true );
    programs1Button [ 2 ].setSelected ( true );

    // greeksButton.setSelected ( true );
    hpLowerButton.setSelected ( false );
    sunButton.setSelected ( true );

    programType.add ( gmhmmButton );
    programType.add ( genscanButton );
    programType.add ( fgeneshButton );

    for ( int i = 0; i < programs1.length; i++ )

      programType.add ( programs1Button [ i ] );

    for ( int i = 0; i < programs2.length; i++ )

      programType.add ( programs2Button [ i ] );

    buildGUI ();
    setupEventHandlers ();
  }  // method RemoteBlast


/*******************************************************************************/
  private void updateDatabases ()
  {
  }  // method updateDatabases


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

    databaseList = new JList ( databases ); 

    databaseList.setSelectedIndex ( 1 );
    databaseList.setVisibleRowCount ( 3 );
    databaseField.replaceSelection ( databases [ 1 ] );

    JScrollPane databasePane = new JScrollPane ( databaseList );

    cpuNumbersList = new JList ( cpu_numbers );
    cpuNumbersList.setSelectedIndex ( 1 );
    cpuNumbersList.setVisibleRowCount ( 3 );
    JScrollPane cpuNumbersPane = new JScrollPane ( cpuNumbersList );

    fgModelsList = new JList ( fg_models );
    fgModelsList.setVisibleRowCount ( 1 );
    JScrollPane fgModelsPane = new JScrollPane ( fgModelsList );

    gmModelsList = new JList ( gm_models );
    gmModelsList.setVisibleRowCount ( 1 );
    JScrollPane gmModelsPane = new JScrollPane ( gmModelsList );

    gsModelsList = new JList ( gs_models );
    gsModelsList.setVisibleRowCount ( 1 );
    JScrollPane gsModelsPane = new JScrollPane ( gsModelsList );

    panels [ 0 ].setLayout ( new GridLayout ( 4, 3 ) );
    for ( int i = 0; i < programs1.length; i++ )

      panels [ 0 ].add ( programs1Button [ i ] );
    panels [ 0 ].setBorder ( new TitledBorder ( "Step 1 - Select program name(s)" ) );

    for ( int i = 0; i < programs2.length; i++ )

      panels [ 1 ].add ( programs2Button [ i ] );
    panels [ 1 ].setBorder ( new TitledBorder ( "Step 1 - TimeLogic Decypher programs" ) );

    panels [ 2 ].add ( fgeneshButton );
    panels [ 2 ].add ( fgModelsPane );
    panels [ 2 ].setBorder ( new TitledBorder ( "Step 1a - FgeneSH option" ) );

    panels [ 3 ].add ( gmhmmButton );
    panels [ 3 ].add ( gmModelsPane );
    panels [ 3 ].setBorder ( new TitledBorder ( "Step 1a - GeneMark.HMM option" ) );

    panels [ 4 ].add ( genscanButton );
    panels [ 4 ].add ( gsModelsPane );
    panels [ 4 ].setBorder ( new TitledBorder ( "Step 1a - Genscan option" ) );

    panels [ 5 ].setLayout ( new GridLayout ( 3, 2 ) );
    panels [ 5 ].add ( morpheusLowerButton );
    panels [ 5 ].add ( morpheusUpperButton );
    panels [ 5 ].add ( greeksButton );
    panels [ 5 ].add ( sunButton );
    panels [ 5 ].add ( sgiButton );
    panels [ 5 ].add ( hpLowerButton );
    panels [ 5 ].add ( hpUpperButton );
    panels [ 5 ].add ( decypherButton );
    panels [ 5 ].setBorder ( new TitledBorder ( "Step 2 - Select system names" ) );

    panels [ 6 ].add ( cpuNumbersPane );
    panels [ 6 ].setBorder ( new TitledBorder ( "Step 3 - Select number of CPUs per System" ) );

    panels [ 7 ].add ( databasePane );
    panels [ 7 ].add ( databaseField );
    panels [ 7 ].setBorder ( new TitledBorder ( "Step 4 - Select Database Name" ) );

    panels [ 8 ].add ( openButton );
    panels [ 8 ].add ( fileField );
    panels [ 8 ].setBorder ( new TitledBorder ( "Step 5 - Identify List of Sequence Names" ) );

    panels [ 9 ].add ( sourceField );
    panels [ 9 ].setBorder ( new TitledBorder ( "Step 6 - Identify Source Location" ) );

    panels [ 10 ].add ( destinationField );
    panels [ 10 ].setBorder ( new TitledBorder ( "Step 7 - Identify Destination Location" ) );

    panels [ 11 ].add ( runButton );
    panels [ 11 ].setBorder ( new TitledBorder ( "Step 8 - Start the analysis" ) );

    frameContainer = getContentPane ();
    frameContainer.setLayout ( new GridLayout ( 6, 2 ) );

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

    openButton.addActionListener ( new ButtonHandler () );

    runButton.addActionListener ( new ButtonHandler () );

    databaseList.addListSelectionListener ( new ListHandler () );

    // cpuNumbersList.addListSelectionListener ( new ListHandler () );
  }  // method setupEventHandlers


/*******************************************************************************/
  private Vector addNames ( Vector names, String [] more_names )
  {
    for ( int i = 0; i < more_names.length; i++ )

      names.add ( more_names [ i ] );

    return names;
  }  // method addNames


/*******************************************************************************/
  private String [] vectorToStrings( Vector list )
  {
    // Validate list.
    if ( list == null )  return null;

    String [] strings = new String [ list.size () ];

    // Copy the vector to the string array.
    for ( int i = 0; i < list.size (); i++ )

      strings [ i ] = (String) list.elementAt ( i );

    return strings;
  }  // method vectorToStrings


/*******************************************************************************/
  private String [] getServerNames ( int number_of_cpus )
  {
    Vector names = new Vector ();

    if ( morpheusLowerButton.isSelected () )  names = addNames ( names, morpheusLower );
    if ( morpheusUpperButton.isSelected () )  names = addNames ( names, morpheusUpper );
    if ( greeksButton.isSelected () )  names = addNames ( names, greeks );
    if ( hpLowerButton.isSelected ()  )  names = addNames ( names, hp_lower );
    if ( hpUpperButton.isSelected ()  )  names = addNames ( names, hp_upper );
    if ( decypherButton.isSelected  ()  )  names = addNames ( names, decyphers );

    if ( sunButton.isSelected ()   )
    { 
      for ( int i = 0; i < number_of_cpus; i++ )
 
        names = addNames ( names, sun );
    }  // if

    if ( sgiButton.isSelected ()   )
    { 
      for ( int i = 0; i < number_of_cpus; i++ )
 
        names = addNames ( names, sgi );
    }  // if

    return vectorToStrings ( names );
  }  // method getServerNames


/*******************************************************************************/
  private boolean getSelections ()
  {
    // Determine which programs were selected.
    String program_name = "";

    for ( int i = 0; i < programs1.length; i++ )

      if ( programs1Button [ i ].isSelected () == true )  

        program_name = programs1 [ i ];

    for ( int i = 0; i < programs2.length; i++ )
    {
      if ( programs2Button [ i ].isSelected () == true )  

        program_name = programs2 [ i ];
    }  // for

    if ( gmhmmButton.isSelected () == true )  program_name = "gmhmm";
    if ( genscanButton.isSelected () == true )  program_name = "genscan";
    if ( fgeneshButton.isSelected () == true )  program_name = "fgenesh";

    if ( program_name.equals ( "fasta" ) == true )  program_name = "fasta34_t";
    if ( program_name.equals ( "fasty" ) == true )  program_name = "fasty34_t";
    if ( program_name.equals ( "tfasta" ) == true )  program_name = "tfasta34_t";
    if ( program_name.equals ( "tfasty" ) == true )  program_name = "tfasty34_t";

    String [] program_names = new String [ 1 ];
    program_names [ 0 ] = program_name;
    analysis_master.setProgramNames ( program_names );


    // Determine the number of CPUs per server to use.
    int number_of_cpus = 2;
    if ( cpuNumbersList.getSelectedValue () != null )
    
      number_of_cpus = InputTools.getInteger 
          ( (String) cpuNumbersList.getSelectedValue () );

    if ( ( number_of_cpus < 1 ) || ( number_of_cpus > 64 ) )
      number_of_cpus = 2;
    analysis_master.setCpusToUse ( number_of_cpus );


    // Determine which CPU servers were selected.
    String [] server_names = getServerNames ( number_of_cpus );
    analysis_master.setSystemNames ( server_names );


    // Determine which Gene Prediction models were selected.
    String [] fg_model_names = null;
    if ( fgModelsList.getSelectedValue () != null )
    {
      fg_model_names = new String [ 1 ];
      fg_model_names [ 0 ] = (String) fgModelsList.getSelectedValue ();
    }  // if 
    analysis_master.setFgeneshModelNames ( fg_model_names );

    String [] gm_model_names = null;
    if ( gmModelsList.getSelectedValue () != null )
    {
      gm_model_names = new String [ 1 ];
      gm_model_names [ 0 ] = (String) gmModelsList.getSelectedValue ();
    }  // if 
    analysis_master.setGmhmmModelNames ( gm_model_names );

    String [] gs_model_names = null;
    if ( gsModelsList.getSelectedValue () != null )
    {
      gs_model_names = new String [ 1 ];
      gs_model_names [ 0 ] = (String) gsModelsList.getSelectedValue ();
    }  // if 
    analysis_master.setGenscanModelNames ( gs_model_names );


    // Determine which databases were selected.
    String [] database_name = new String [ 1 ];
    databaseField.selectAll ();
    if ( databaseField.getSelectedText () == null )
    {
      showDialog ( "Please specify a database first" );
      return false;
    }  // if
    else
      database_name [ 0 ] = databaseField.getSelectedText ();
    analysis_master.setDatabaseNames ( database_name );


    // Check if the user has modified the file name.
    String file_name = "";
    fileField.selectAll ();
    if ( fileField.getSelectedText () != null )
      file_name = fileField.getSelectedText ();
    analysis_master.setListFileName ( file_name );


    // Determine the source path.
    String source_path = "";
    sourceField.selectAll ();
    if ( sourceField.getSelectedText () != null )
      source_path = sourceField.getSelectedText ();
    analysis_master.setSourcePath ( source_path );

    System.out.println ( "source_path = '" + source_path + "'" );
    System.out.println ( "getText = " + sourceField.getText () );
    System.out.println ( "getSelectedText = " + sourceField.getSelectedText () );

    // Determine the destination path.
    String destination_path = "";
    destinationField.selectAll ();
    if ( destinationField.getSelectedText () != null )
      destination_path = destinationField.getSelectedText ();
    analysis_master.setDestinationPath ( destination_path );

    return true;
  }  // method getSelections


/*******************************************************************************/
  private void done ()
  {
    // Dispose of the GUI interface.
    dispose ();
  }  // method done


/*******************************************************************************/
// Returns the index in list of target or -1.
  private int find ( String [] list, String target )
  {
    int index = -1;		// target string index in the list

    // Search the list serially
    int i = 0;
    while ( i < list.length )
    {
      // Check for the target
      if ( list [ i ].equals ( target ) )  return i;
      i++;
    }  // while 

    return index;
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
    JFileChooser chooser = new JFileChooser ( "." );

    int option = chooser.showOpenDialog ( this );

    if ( option == JFileChooser.APPROVE_OPTION )
    {
      if ( chooser.getSelectedFile () != null )
      {
        File file = chooser.getSelectedFile ();
        // parameters.setFileName ( file.getName () );
        fileField.replaceSelection ( file.getName () );
        fileSelected = true;

        // System.out.println ( "getAbsolutePath () = '" + file.getAbsolutePath () + "'" );
        // System.out.println ( "getParent () = '" + file.getParent () + "'" );
        // System.out.println ( "getPath () = '" + file.getPath () + "'" );

        sourceField.replaceSelection ( file.getParent () + "/" );
        destinationField.replaceSelection ( file.getParent () + "/" );
      }  // if 
    }  // if
  }  // method selectFile


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
  }  // method shutDown


/*******************************************************************************/
  public static void main ( String [] args )
  {
    RemoteBlast app = new RemoteBlast ();
  }  // method main


/*******************************************************************************/
  public class ListHandler implements ListSelectionListener
  {
    public void valueChanged ( ListSelectionEvent e )
    {
      if ( e.getSource ().equals ( databaseList ) )
      {
        databaseField.selectAll ();
        databaseField.replaceSelection ( (String) databaseList.getSelectedValue () );
      }  // if
    }  // method valueChanged

  }  // class ListHandler


/*******************************************************************************/
  public class WindowHandler extends WindowAdapter 
  {
    public void windowClosing ( WindowEvent e )
    {
      shutDown ();
      System.exit ( 0 );
    }  // method windowClosing
  }  // class WindowHandler


/*******************************************************************************/
  public class MenuItemHandler implements ActionListener
  {
    public void actionPerformed ( ActionEvent e )
    {
      shutDown ();
      System.exit ( 0 );
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
      else if ( cmd.equals ( runTitle ) )
      {
        // Ensure that a file has been selected first.
        if ( fileSelected )
        {
          if ( ! getSelections () )  return;

          // Hide the GUI interface.
          hide ();

          // Process the files.
          System.out.println ( "Run everything!" );

          analysis_master.startAnalysis ();

          done ();		// exit
        }
        else
          showDialog ( "Please select an input file first" );
      }  // else if 
    }  // method actionPerformed

  }  // class ButtonHandler


/*******************************************************************************/

}  // class RemoteBlast


