// ComboBoxEditorExample.java
//
// Page 198

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class ComboBoxEditorExample implements ComboBoxEditor
{
  Hashtable hashtable;

  ImagePanel panel;

  ImageIcon questionIcon;

  public ComboBoxEditorExample ( Hashtable h, String defaultTitle )
  {
    hashtable = h;

    Icon defaultIcon = ((JLabel) hashtable.get ( defaultTitle) ).getIcon ();

    panel = new ImagePanel ( defaultIcon, defaultTitle );

    questionIcon = new ImageIcon ( "question.gif" );
  }  // method ComboBoxEditorExample


  public void setItem ( Object anObject )
  {
    if ( anObject != null )
    {
      panel.setText ( anObject.toString () );

      JLabel label = (JLabel) hashtable.get ( anObject.toString () );

      if ( label != null )
        panel.setIcon ( label.getIcon () );
      else
        panel.setIcon ( questionIcon );
    }  // if 
  }  // method setItem


  public Component getEditorComponent () 
  {
    return panel;
  }  // method getEditorComponent


  public Object getItem ()
  {
    return panel.getText ();
  }  // method getItem


  public void selectAll ()
  {
    panel.selectAll ();
  }  // selectAll


  public void addActionListener ( ActionListener l )
  {
    panel.addActionListener ( l );
  }  // method addActionListener


  public void removeActionListener ( ActionListener listener )
  {
    panel.removeActionListener ( listener );
  }  // method removeActionListener


  // We create our own ineer class to handle setting and 
  // repainting the image and the text.
  class ImagePanel extends JPanel
  {
    JLabel imageIconLabel;
    JTextField textField;

    public ImagePanel ( Icon icon, String text )
    {
      setLayout ( new BorderLayout () );

      imageIconLabel = new JLabel ( icon );
      imageIconLabel.setBorder ( new BevelBorder ( BevelBorder.RAISED ) );

      textField = new JTextField ( text );
      textField.setColumns ( 45 );
      textField.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );

      add ( imageIconLabel, BorderLayout.WEST );
      add ( textField, BorderLayout.EAST );
    }  // method ImagePanel


    public void setText ( String s )
    {
      textField.setText ( s );
    }  // method setText


    public String getText ()
    {
      return ( textField.getText () );
    }  // method getText


    public void setIcon ( Icon i )
    {
      imageIconLabel.setIcon ( i );
      repaint ();
    }  // method setIcon


    public void selectAll ()
    {
      textField.selectAll ();
    }  // method selectAll


    public void addActionListener ( ActionListener listener )
    {
      textField.addActionListener ( listener );
    }  // method addActionListener


    public void removeActionListener ( ActionListener listener )
    {
      textField.removeActionListener ( listener );
    }  // method removeActionListener

  }  // class ImagePanel


}  // class ComboBoxEditorExample
