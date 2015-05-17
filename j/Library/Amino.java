
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

public class Amino extends Object
{
  private static final char amino_acids [] = { 'G', 'A', 'S', 'P', 'V', 'T', 'C', 'L', 'N', 'D', 
        'K', 'E', 'M', 'H', 'F', 'R', 'Y', 'W' };

  public static double Glycine = 3.0*Atom.H + 2.0*Atom.C + Atom.O + Atom.N;

  public static double Alanine = 5.0*Atom.H + 3.0*Atom.C + Atom.O + Atom.N;

  public static double Valine = 9.0*Atom.H + 5.0*Atom.C + Atom.O + Atom.N;

  public static double Leucine = 11.0*Atom.H + 6.0*Atom.C + Atom.O + Atom.N;

  public static double Isoleucine = 11.0*Atom.H + 6.0*Atom.C + Atom.O + Atom.N;

  public static double Serine = 5.0*Atom.H + 3.0*Atom.C + 2.0*Atom.O + Atom.N;

  public static double Threonine = 7.0*Atom.H + 4.0*Atom.C + 2.0*Atom.O + Atom.N;


  public static double Cysteine = 5.0*Atom.H + 3.0*Atom.C + Atom.O + Atom.N + Atom.S;

  public static double Methionine = 9.0*Atom.H + 5.0*Atom.C + Atom.O + Atom.N + Atom.S;

  public static double Aspartic_acid = 5.0*Atom.H + 4.0*Atom.C + 3.0*Atom.O + Atom.N;

  public static double Asparagine = 6.0*Atom.H + 4.0*Atom.C + 2.0*Atom.O + 2.0*Atom.N;


  public static double Glutamic_acid = 7.0*Atom.H + 5.0*Atom.C + 3.0*Atom.O + Atom.N;

  public static double Glutamine = 8.0*Atom.H + 5.0*Atom.C + 2.0*Atom.O + 2.0*Atom.N;

  public static double Arginine = 12.0*Atom.H + 6.0*Atom.C + Atom.O + 4.0*Atom.N;

  public static double Lysine = 12.0*Atom.H + 6.0*Atom.C + Atom.O + 2.0*Atom.N;

  public static double Histidine = 7.0*Atom.H + 6.0*Atom.C + Atom.O + 3.0*Atom.N;

  public static double Phenylalanine = 9.0*Atom.H + 9.0*Atom.C + Atom.O + Atom.N;

  public static double Tyrosine = 9.0*Atom.H + 9.0*Atom.C + 2.0*Atom.O + Atom.N;

  public static double Tryptophan = 10.0*Atom.H + 11.0*Atom.C + Atom.O + 2.0*Atom.N;

  public static double Proline = 7.0*Atom.H + 5.0*Atom.C + Atom.O + Atom.N;

  public static double main_chain = 2.0*Atom.H + 2.0*Atom.C + Atom.O + Atom.N;


  public static double Gly = Glycine;
  public static double Ala = Alanine;
  public static double Val = Valine;
  public static double Leu = Leucine;
  public static double Ile = Isoleucine;
  public static double Ser = Serine;
  public static double Thr = Threonine;
  public static double Cys = Cysteine;
  public static double Met = Methionine;
  public static double Asp = Aspartic_acid;
  public static double Asn = Asparagine;
  public static double Glu = Glutamic_acid;
  public static double Gln = Glutamine;
  public static double Arg = Arginine;
  public static double Lys = Lysine;
  public static double His = Histidine;
  public static double Phe = Phenylalanine;
  public static double Tyr = Tyrosine;
  public static double Trp = Tryptophan;
  public static double Pro = Proline;

  public static double G = Glycine;
  public static double A = Alanine;
  public static double V = Valine;
  public static double L = Leucine;
  public static double I = Isoleucine;
  public static double S = Serine;
  public static double T = Threonine;
  public static double C = Cysteine;
  public static double M = Methionine;
  public static double D = Aspartic_acid;
  public static double N = Asparagine;
  public static double E = Glutamic_acid;
  public static double Q = Glutamine;
  public static double R = Arginine;
  public static double K = Lysine;
  public static double H = Histidine;
  public static double F = Phenylalanine;
  public static double Y = Tyrosine;
  public static double W = Tryptophan;
  public static double P = Proline;

  public static int MAX_UNIQUE = 18;


/******************************************************************************/
  public static char getLetter ( int i )
  {
    if ( ( i >= 0 ) && ( i < amino_acids.length ) )

      return amino_acids [ i ];

    else

      return ' ';
  }  // method getLetter


/******************************************************************************/
  public static char [] getLetters ()
  {
    return amino_acids;
  }  // method getLetters


/******************************************************************************/
  public static double [] getMasses ()
  {
    double mass_values [] = { G, A, S, P, V, T, C, L, N, D, K, E, M, H, F, R, Y, W };

    return mass_values;
  }  // method getMasses 


/******************************************************************************/
  public static void main ( String [] args )
  {
    System.out.println ( "A = " + A );
    System.out.println ( "C = " + C );
    System.out.println ( "D = " + D );
    System.out.println ( "E = " + E );
    System.out.println ( "F = " + F );
    System.out.println ( "G = " + G );
    System.out.println ( "H = " + H );
    System.out.println ( "I = " + I );
    System.out.println ( "K = " + K );
    System.out.println ( "L = " + L );
    System.out.println ( "M = " + M );
    System.out.println ( "N = " + N );
    System.out.println ( "P = " + P );
    System.out.println ( "Q = " + Q );
    System.out.println ( "R = " + R );
    System.out.println ( "S = " + S );
    System.out.println ( "T = " + T );
    System.out.println ( "V = " + V );
    System.out.println ( "W = " + W );
    System.out.println ( "Y = " + Y );

    double [] massValues = getMasses ();
    char   [] aminoLetters = getLetters ();

    for ( int i = 0; i < MAX_UNIQUE; i++ )

      System.out.println ( (i+1) + " " + aminoLetters [ i ] + " " + massValues [ i ] );

/*
    System.out.println ();
    System.out.println ( "Side chain R mass:" );
    System.out.println ( "A = " + (A - main_chain) );
    System.out.println ( "C = " + (C - main_chain) );
    System.out.println ( "D = " + (D - main_chain) );
    System.out.println ( "E = " + (E - main_chain) );
    System.out.println ( "F = " + (F - main_chain) );
    System.out.println ( "G = " + (G - main_chain) );
    System.out.println ( "H = " + (H - main_chain) );
    System.out.println ( "I = " + (I - main_chain) );
    System.out.println ( "K = " + (K - main_chain) );
    System.out.println ( "L = " + (L - main_chain) );
    System.out.println ( "M = " + (M - main_chain) );
    System.out.println ( "N = " + (N - main_chain) );
    System.out.println ( "P = " + (P - main_chain) );
    System.out.println ( "Q = " + (Q - main_chain) );
    System.out.println ( "R = " + (R - main_chain) );
    System.out.println ( "S = " + (S - main_chain) );
    System.out.println ( "T = " + (T - main_chain) );
    System.out.println ( "V = " + (V - main_chain) );
    System.out.println ( "W = " + (W - main_chain) );
    System.out.println ( "Y = " + (Y - main_chain) );
*/
  }  // method main

}  // method Amino

