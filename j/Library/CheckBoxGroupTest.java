import java.awt.*;

public class CheckboxGroupTest extends Frame
{
  public CheckboxGroupTest ()
  {
    setTitle ( "CheckboxGroupTest" );
    Panel p = new Panel ();
    p.setLayout ( new FlowLayout () );
    
    CheckboxGroup g = new CheckboxGroup ();
    p.add ( small =      new Checkbox ( "Small", g, false ) );
    p.add ( medium =     new Checkbox ( "Medium", g, true ) );
    p.add ( large  =     new Checkbox ( "Large", g, false ) );
    p.add ( extraLarge = new Checkbox ( "Extra large", g, false ) );
    add ( "North", p );
    
    fox = new FoxCanvas ();
    add ( "Center", fox );
    
    Choice style = new Choice ();
    style.addItem ( "Times Roman" );
    style.addItem ( "Helvetica" );
    style.addItem ( "Courier" );
    style.addItem ( "Zapf Dingbats" );
    style.addItem ( "Dialog" );
    style.addItem ( "DialogInput" );
    add ( "South", style );
  }  /* CheckboxGroupTest */
  
  public boolean handleEvent ( Event evt )
  {
    if ( evt.id == Event.WINDOW_DESTROY )  System.exit ( 0 );
    return super.handleEvent ( evt );  
  }  /* handleEvent */
  
  public boolean action ( Event evt, Object arg )
  {
    if ( evt.target.equals ( style ) )  fox.setStyle ( ( String ) arg );
    else
      if ( evt.target.equals ( small ) )  fox.setSize ( 8 );
      else 
        if ( evt.target.equals ( medium ) )  fox.setSize ( 10 );
        else
          if ( evt.target.equals ( large ) )  fox.setSize ( 14 );
          else
            if ( evt.target.equals ( extraLarge ) )  fox.setSize ( 18 );
            else
              return super.action ( evt, arg );
    return true;  
  }  /* action */
  
  public static void main ( String [] args )
  {
    Frame f = new CheckboxGroupTest ();
    f.resize ( 300, 200 );
    f.show ();  
  }  /* main */
  
  private FoxCanvas fox;
  private Checkbox small;
  private Checkbox medium;
  private Checkbox large;
  private Checkbox extraLarge;
  
  private Choice style;
  
}  /* class CheckboxGroupTest */


class FoxCanvas extends Canvas
{
  public FoxCanvas ()
  {
    setSize ( 10 );
  }  /* FoxCanvas */
  
  public void setSize ( int p )
  {
    setFont ( new Font ( "Helvetica", Font.PLAIN, p ) );
    repaint ();
  }  /* setSize */
  
  public void setStyle ( String s )
  {
    setFont ( new Font ( s, Font.PLAIN, 18 ) );
    repaint ();
  }  /* setStyle */
  
  public void paint ( Graphics g )
  {
    g.drawString 
        ( "The quick brown fox jumps over the lazy dog.", 0, 50 );
  }  /* paint */
}  /* class FoxCanvas */

