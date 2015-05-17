import java.awt.*;
import java.applet.Applet;

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

public class AminoAcidDemo extends Applet {
  AminoAcid amino;
  Label acidLabel;
  TextField acidEdit;
  Label hEngelLabel;
  Label hydroLabel;
  Label hKyteLabel;
  Label pkaLabel;
  Label areaLabel;
  Label aromaticLabel;
  Label nonPolarLabel;
  Label polarLabel;
  Label chargedLabel;
  Label positiveLabel;
  Label smallLabel;
  Label tinyLabel;
  Label aliphaticLabel;
  Label structureLabel;
  Label massLabel;
  Label volLabel;
  Button button;


  public void init() {
    amino = new AminoAcid();
    button = new Button( "Go!" );
    acidLabel = new Label( "Amino Acid:" );
    acidEdit = new TextField( "A" );
    hEngelLabel = new Label( "Hydrophobocity (Engleman):" );
    hydroLabel = new Label( "Hydrophobocity:" );
    hKyteLabel = new Label( "Hydrophobocity(Kyte):" );
    pkaLabel = new Label( "pKa:" );
    areaLabel = new Label( "Residue Surface Area:" );
    aromaticLabel = new Label( "Aromatic?:" );
    nonPolarLabel = new Label( "Non-Polar?:" );
    polarLabel = new Label( "Polar?:" );
    chargedLabel = new Label( "Charged?:" );
    positiveLabel = new Label( "Positive?:" );
    smallLabel = new Label( "Small?:" );
    tinyLabel = new Label( "Tiny?:" );
    aliphaticLabel = new Label( "Aliphatic?:" );
    structureLabel = new Label( "Side-Chain Structure:" );
    massLabel = new Label( "Mass:" );
    volLabel = new Label( "Residue Volume:" );

    Panel p1 = new Panel();
    Panel p2 = new Panel();
    Panel p3 = new Panel();
    p1.add( acidLabel );
    p1.add( acidEdit );
    p2.add( button );

    p3.setLayout( new GridLayout( 0, 1 ));
    

    p3.add( hEngelLabel);
    p3.add( hydroLabel);
    p3.add( hKyteLabel);
    p3.add( pkaLabel);
    p3.add( areaLabel);
    p3.add( aromaticLabel);
    p3.add( nonPolarLabel);
    p3.add( polarLabel);
    p3.add( chargedLabel);
    p3.add( positiveLabel);
    p3.add( smallLabel);
    p3.add( tinyLabel);
    p3.add( aliphaticLabel);
    p3.add( structureLabel);
    p3.add( massLabel);
    p3.add( volLabel);
   


    add("North", p1 );
    add("Center", p2 );
    add("South", p3 );

  }

  public boolean action(Event e, Object arg) {
    Object target = e.target;
    char a;               
    if (target == button ) {
      a = acidEdit.getText().charAt(0);

      try {
      hEngelLabel.setText( "Hydrophobocity (Engelman): "+String.valueOf( amino.hydrophobicityEngelman( a )));
      hydroLabel.setText( "Hydrophobocity :"+ String.valueOf( amino.hydrophobicity( a )));
      hKyteLabel.setText( "Hydrophobocity (Kyte): "+String.valueOf( amino.hydrophobicityKyte( a )));
      pkaLabel.setText( "pKa: "+ String.valueOf( amino.pKa( a )));
      areaLabel.setText( "Residue Surface Area: "+ String.valueOf( amino.residueSurfaceArea( a )));
      aromaticLabel.setText( "Aromatic? " + String.valueOf( amino.isAromatic( a )));
      nonPolarLabel.setText( "Non-Polar? " + String.valueOf( amino.isNonPolar( a )));
      polarLabel.setText( "Polar? " + String.valueOf( amino.isPolar( a )));
      chargedLabel.setText( "Charged? " + String.valueOf( amino.isCharged( a )));
      positiveLabel.setText( "Positive? " + String.valueOf( amino.isPositive( a )));
      smallLabel.setText( "Small? " + String.valueOf( amino.isSmall( a )));
      tinyLabel.setText( "Tiny? " + String.valueOf( amino.isTiny( a )));
      aliphaticLabel.setText( "Aliphatic? " + String.valueOf( amino.isAliphatic( a )));
      structureLabel.setText( "Side-Chain Structure: "+amino.sideChainStructure( a ));
      massLabel.setText( "Mass: "+String.valueOf( amino.mass( a )));
      volLabel.setText( "Residue Volume: "+ String.valueOf( amino.residueVolume(a)));
      repaint();
      } catch ( NotAnAminoAcidException ex ) {
	System.out.println( "not an acid");
      }	
      return true;
    }
		
    return false;

  }



}
