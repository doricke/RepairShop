import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.io.*;

public class ClipTextApp extends Frame 
{
  Font defaultFont = new Font ( "default", Font.PLAIN, 12 );
  int  screenWidth = 400;
  int  screenHeight = 400;
  Toolkit toolkit;
  int baseline;
  int  lineSize;
  FontMetrics fm;
  Canvas canvas = new MyCanvas ();
  Vector text = new Vector ();
  int topLine;

  public static void main ( String args [] )
  {
    ClipTextApp app = new ClipTextApp ();
  }  // method main

  public ClipTextApp ()
  {
    super ( "ClipTextApp" );
    setup ();
    setSize ( screenWidth, screenHeight );
    addWindowListener ( new WindowEventHandler () );
    show ();
  }  // constructor ClipTextApp 

  void setup ()
  {
    setupMenuBar ();
    setupFontData ();
    text.addElement ( "" );
    add ( "Center", canvas );
  }  // method setup

  void setupMenuBar ()
  {
    MenuBar menuBar = new MenuBar ();
    Menu fileMenu = new Menu ( "File" );
    Menu editMenu = new Menu ( "Edit" );

    MenuItem fileExit = new MenuItem ( "Exit" );
    MenuItem editCopy = new MenuItem ( "Copy" );
    MenuItem editPaste = new MenuItem ( "Paste" );

    fileExit.addActionListener ( new MenuItemHandler () );
    editCopy.addActionListener ( new MenuItemHandler () );
    editPaste.addActionListener ( new MenuItemHandler () );

    fileMenu.add ( fileExit );
    editMenu.add ( editCopy );
    editMenu.add ( editPaste );
    menuBar.add ( fileMenu );
    menuBar.add ( editMenu );

    setMenuBar ( menuBar );
  }  // method setupMenuBar

  void setupFontData ()
  {
    setFont ( defaultFont );
    toolkit = getToolkit ();
    fm = toolkit.getFontMetrics ( defaultFont );
    baseline = fm.getLeading () + fm.getAscent ();
    lineSize = fm.getHeight ();
  }  // method setupFontData

  public void paint ( Graphics g )
  {
    canvas.repaint ();
  }  // method paint

  void copyToClipboard ()
  {
    // Copy the string to the clipboard
    String toClipboard = "Clipboard String";
    StringSelection ss = new StringSelection ( toClipboard );
    Clipboard clip = toolkit.getSystemClipboard ();
    clip.setContents ( ss, ss );
  }  // method copyToClipboard

  void pasteFromClipboard ()
  {
    // Get the system clipboard using the toolkit
    Clipboard clip = toolkit.getSystemClipboard ();

    // Get the clipboard contents
    Transferable contents = clip.getContents ( ClipTextApp.this );
    text.removeAllElements ();

    if ( contents == null )  text.addElement ( "The clipboard is empty." );
    else
    {
      // If the contents support the string data flavor then retrieve and 
      // parse the data contained on the clipboard
      if ( contents.isDataFlavorSupported ( DataFlavor.stringFlavor ) )
      {
        try
        {
          String data = (String) contents.getTransferData 
              ( DataFlavor.stringFlavor );

          if ( data == null )  text.addElement ( "null" );
          else
          {
            StringTokenizer st = new StringTokenizer ( data, "\n" );

            while ( st.hasMoreElements () )

              text.addElement ( st.nextToken () );
          }  // else
        }  // try
        catch ( IOException ex )
        {
          text.addElement ( "IOException " + ex );
        }  // matcha
        catch ( UnsupportedFlavorException ex )
        {
          text.addElement ( "UnsupportedFlavorException " + ex );
        }  // catch
      }  // if 
      else
        text.addElement ( "Wrong flavor." );
    }  // else

    repaint ();
  }  // method pasteFromClipboard

  class MenuItemHandler implements ActionListener 
  {
    public void actionPerformed ( ActionEvent ev )
    {
      String s = ev.getActionCommand ();

      if ( s == "Exit" )
      {
        System.exit ( 0 );
      }
      else 
        if ( s == "Copy" )  copyToClipboard ();
        else 
          if ( s == "Paste" )  pasteFromClipboard ();
          else
            System.out.println ( "Unknown command: '" + s + "'" );
    }  // method actionPerformed
  }  // class MenuItemHandler

  class WindowEventHandler extends WindowAdapter 
  {
    public void windowClosing ( WindowEvent e )
    {
      System.exit ( 0 );
    }  // method windowClosing
  }  // class WindowEventHandler

  class MyCanvas extends Canvas
  {
    public void paint ( Graphics g )
    {
      topLine = 0;
      int numLines = text.size ();
      screenHeight = getSize ().height;

      int y = baseline * 2;
      int x = y;

      for ( int i = topLine; ( i < numLines ) && ( y < screenHeight + lineSize ); ++i )
      {
        g.drawString ( (String) text.elementAt (i), x, y );
        y += lineSize;
      }  // for
    }  // method paint
  }  // class MyCanvas

}  // class ClipTextApp

