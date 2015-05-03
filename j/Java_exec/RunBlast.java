
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

public class RunBlast extends JFrame
{
  public static int WIDTH = 800;
  public static int HEIGHT = 672;

  private static String TITLE = "TMRI Remote BLAST Analysis Software";

  private static String openTitle = "Select file";

  private static String runTitle = "Start";


  private boolean fileSelected = false;			// file selected flag


  Container frameContainer;

  // Swing components:
  JPanel [] panels = new JPanel [ 8 ];

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
                        };

  JRadioButton [] programs1Button = new JRadioButton [ programs1.length ];

  String [] intel1 = { "zeus"
                     , "hera"
                     , "cronus"
                     , "rhea"
                     , "hyperion" 
                     , "thea"
                     , "oceanus"
                     // , "tethys"
                     , "coeus"
                     , "phoebe" 
                     , "crius" 
                     , "mnemosyne" 
                     , "iapetus" 
                     // , "themis" 
                     };

  String [] intel2 = { 
                       "hades" 
                     , "hestia" 
                     , "poseidon" 
                     , "demeter" 
                     , "apollo"
                     , "artemis"
                     , "ares"
                     , "athena"
                     , "hephaestus"
                     , "aphrodite"
                     // , "dionysus"
                     // , "persephone" 
                     };

  String [] sgi  = { "sgi" };

  String [] apple = { "apple" };

  String [] orange = { "orange" };

  String [] sun = { "sun" };

  String [] pear = { "pear" };

  JRadioButton intel1Button = new JRadioButton ( "Intel Titons Cluster" );
  JRadioButton intel2Button = new JRadioButton ( "Intel Greek Cluster" );
  JRadioButton appleButton  = new JRadioButton ( "Sun System Apple" );
  JRadioButton orangeButton = new JRadioButton ( "Sun System Orange" );
  JRadioButton sunButton  = new JRadioButton ( "Sun Solaris System" );
  JRadioButton pearButton = new JRadioButton ( "Sun System Pear" );
  JRadioButton sgiButton   = new JRadioButton ( "SGI System" );

  ButtonGroup programType = new ButtonGroup ();

  JButton openButton   = new JButton ( openTitle );

  JButton runButton    = new JButton ( runTitle );

  String [] databases = 
                        { "bct.aa"
                        , "bct.nt"
                        , "Bot_101600"
                        , "Cok_050701"
                        , "est"
                        , "Fus_101600"
                        , "genpept"
                        , "mito.aa"
                        , "Myriad_V8"
                        , "nr"
                        , "nt"
                        , "pataa"
                        , "patnt"
                        , "pdbaa"
                        , "pdbnt"
                        , "rice_chip"
                        , "rice_fl"
                        , "RiceUnigene_1"
                        , "sts"
                        , "swissprot"
                        , "vector"
                        , "wormpep"
                        , "yeast.aa"
                        , "yeast.nt"
                        };

  String [] cpu_numbers = { "1", "2", "3", "4", "5", "6", "7", "8", "10", "12", "14", "16", "18", "20", "22", "24" };

  JList cpuNumbersList;
  JList databaseList;

  // JDialog box.
  JDialog dialog;

  AnalysisMaster analysis_master = new AnalysisMaster ();


/*******************************************************************************/
  public RunBlast ()
  {
    super ( TITLE );

    updateDatabases ();

    // Initialization code.
    for ( int i = 0; i < panels.length; ++i )

      panels [ i ] = new JPanel ();

    for ( int i = 0; i < programs1.length; i++ )
  
      programs1Button [ i ] = new JRadioButton ( programs1 [ i ] );
  
    // blastnButton.setSelected ( true );
    programs1Button [ 0 ].setSelected ( true );

    sgiButton.setSelected ( true );

    for ( int i = 0; i < programs1.length; i++ )

      programType.add ( programs1Button [ i ] );

    buildGUI ();
    setupEventHandlers ();
  }  // method RunBlast


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

    databaseList.setSelectedIndex ( 19 );
    databaseList.setVisibleRowCount ( 4 );
    databaseField.replaceSelection ( databases [ 19 ] );

    JScrollPane databasePane = new JScrollPane ( databaseList );

    cpuNumbersList = new JList ( cpu_numbers );
    cpuNumbersList.setSelectedIndex ( 1 );
    cpuNumbersList.setVisibleRowCount ( 4 );
    JScrollPane cpuNumbersPane = new JScrollPane ( cpuNumbersList );

    panels [ 0 ].setLayout ( new GridLayout ( 4, 3 ) );
    for ( int i = 0; i < programs1.length; i++ )

      panels [ 0 ].add ( programs1Button [ i ] );
    panels [ 0 ].setBorder ( new TitledBorder ( "Step 1 - Select program name(s)" ) );

    panels [ 1 ].setLayout ( new GridLayout ( 3, 2 ) );
    panels [ 1 ].add ( intel1Button );
    panels [ 1 ].add ( intel2Button );
    panels [ 1 ].add ( sgiButton );
    panels [ 1 ].add ( appleButton );
    panels [ 1 ].add ( orangeButton );
    panels [ 1 ].add ( pearButton );
    panels [ 1 ].add ( sunButton );
    panels [ 1 ].setBorder ( new TitledBorder ( "Step 2 - Select system names" ) );

    panels [ 2 ].add ( cpuNumbersPane );
    panels [ 2 ].setBorder ( new TitledBorder ( "Step 3 - Select number of CPUs per System" ) );

    panels [ 3 ].add ( databasePane );
    panels [ 3 ].add ( databaseField );
    panels [ 3 ].setBorder ( new TitledBorder ( "Step 4 - Select Database Name" ) );

    panels [ 4 ].add ( openButton );
    panels [ 4 ].add ( fileField );
    panels [ 4 ].setBorder ( new TitledBorder ( "Step 5 - Identify List of Sequence Names" ) );

    panels [ 5 ].add ( sourceField );
    panels [ 5 ].setBorder ( new TitledBorder ( "Step 6 - Identify Source Location" ) );

    panels [ 6 ].add ( destinationField );
    panels [ 6 ].setBorder ( new TitledBorder ( "Step 7 - Identify Destination Location" ) );

    panels [ 7 ].add ( runButton );
    panels [ 7 ].setBorder ( new TitledBorder ( "Step 8 - Start the analysis" ) );

    frameContainer = getContentPane ();
    frameContainer.setLayout ( new GridLayout ( 4, 2 ) );

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

    if ( intel1Button.isSelected () )  names = addNames ( names, intel1 );
    if ( intel2Button.isSelected () )  names = addNames ( names, intel2 );
    if ( appleButton.isSelected ()  )  names = addNames ( names, apple );
    if ( orangeButton.isSelected () )  names = addNames ( names, orange );
    if ( pearButton.isSelected ()  )  names = addNames ( names, pear );
    if ( sunButton.isSelected () )  names = addNames ( names, sun );

    if ( ( sgiButton.isSelected () ) ||
         ( appleButton.isSelected () ) ||
         ( pearButton.isSelected () ) ||
         ( sunButton.isSelected () ) ||
         ( orangeButton.isSelected () ) )
    { 
      for ( int i = 0; i < number_of_cpus; i++ )
      { 
        if ( sgiButton.isSelected () )
          names = addNames ( names, sgi );

        if ( appleButton.isSelected () )
          names = addNames ( names, apple );

        if ( pearButton.isSelected () )
          names = addNames ( names, pear );

        if ( sunButton.isSelected () )
          names = addNames ( names, sun );

        if ( orangeButton.isSelected () )
          names = addNames ( names, orange );
      }  // for
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
    RunBlast app = new RunBlast ();
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

}  // class RunBlast


