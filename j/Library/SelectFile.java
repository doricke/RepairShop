
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

public class SelectFile extends JFrame
{
  JFrame parent;

  public SelectFile ()
  {
    super ( "Select a File of DNA Sequence(s)" );
    setSize ( 350, 200 );
    addWindowListener ( new WindowHandler () );
    parent = this;

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
  }  // constructor SelectFile

}  // class SelectFile
