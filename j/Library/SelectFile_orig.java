
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;

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

public class SelectFile_orig extends JFrame
{
  JFrame parent;

  public SelectFile_orig ()
  {
    super ( "Select a File of DNA Sequence(s)" );
    setSize ( 350, 200 );
    addWindowListener ( new WindowHandler () );
    parent = this;

    Container c = getContentPane ();
    c.setLayout ( new FlowLayout () );

    JButton openButton = new JButton ( "Open" );
    JButton dirButton  = new JButton ( "Directory" );

    final JLabel statusbar =
        new JLabel ( "Output of your selection will go here" );

    // Create a file chooser that opens up as an "Open" dialog.
    openButton.addActionListener ( 
        new ActionListener ()
        {
          public void actionPerformed ( ActionEvent ae )
          {
            JFileChooser chooser = new JFileChooser ();

            int option = chooser.showOpenDialog ( parent );

            if ( option == JFileChooser.APPROVE_OPTION )
            {
              statusbar.setText ( "You chose " + ( (chooser.getSelectedFile () != null) ?
                  chooser.getSelectedFile ().getName () : "nothing" ) );
            }  // if 
            else
            {
              statusbar.setText ( "You canceled." );
            }  // else
          }  // method actionPerformed
        }  // ActionListener
    );

    // Create a file chooser that allows you to select a directory.
    dirButton.addActionListener ( 
        new ActionListener () 
        {
          public void actionPerformed ( ActionEvent ae )
          {
            JFileChooser chooser = new JFileChooser ();

            chooser.setFileSelectionMode ( JFileChooser.DIRECTORIES_ONLY );

            int option = chooser.showOpenDialog ( parent );

            if ( option == JFileChooser.APPROVE_OPTION )
            {
              statusbar.setText ( "You chose " + ( (chooser.getSelectedFile () != null) ?
                  chooser.getSelectedFile ().getName () : "nothing" ) );
            }  // if 
            else
            {
              statusbar.setText ( "You canceled." );
            }  // else
          }  // method actionPerformed
        }  // ActionListener
    );

    c.add ( openButton );
    c.add ( dirButton );
    c.add ( statusbar );
  }  // constructor SelectFile_orig

  public static void main ( String args [] )
  {
    SelectFile_orig sf = new SelectFile_orig ();
    sf.setVisible ( true );
  }  // method main


  public class WindowHandler extends WindowAdapter 
  {
    public void windowClosing ( WindowEvent e )
    {
      System.exit ( 0 );
    }  // method windowClosing

  }  // class WindowHandler

}  // class SelectFile_orig
