
#include <stdio.h>
#include <math.h>

/* This program implements a general purpose backpropagation neural network. */
/* 
  Author::    	Darrell O. Ricke, Ph.D.  (mailto: d_ricke@yahoo.com)
  Copyright:: 	Copyright (c) 2000 Darrell O. Ricke, Ph.D., Paragon Software
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

#define  S_EOF            1     /* End Of File encountered */
#define  S_EOL            2     /* End of Line encountered */
#define  S_NORMAL         3     /* normal function termination */
#define  S_NO_INTEGER     4     /* No integer found on scan line */
#define  S_OPEN_FAILED    5     /* file open failed */

#define  ALPHA            0.5   /* learning rate constant */

#define  EPSILON          0.1   /* convergence tolerance on each output value */

#define  INPUT_LAYER      0     /* input layer of the neural network */

#define  MAX_INPUTS       100   /* maximum number of input nodes */

#define  MAX_LAYER_NODES  100   /* maximum number of nodes per layer */

#define  MAX_LAYERS       5     /* maximum number of neural network layers */

#define  MAX_LINE         132   /* maximum text line length */

#define  MAX_OUTPUTS      100   /* maximum number of input nodes */

#define  MAX_PATTERNS     100   /* number of input patterns */

#define  MAX_RANDOM       (32768 * 65536 - 1)    /* 2^31-1 - maximum random number from rand */


/******************************************************************************/

/* Inputs and outputs for one training pattern. */
typedef struct {
  float  input [ MAX_INPUTS ];    /* single pattern inputs */

  float  output [ MAX_OUTPUTS ];    /* single pattern outputs */
} t_pattern;


/* Neural Network training patterns */
typedef struct {
  long  total;    /* number of training patterns */

  long  current;    /* current training pattern */

  t_pattern  io [ MAX_PATTERNS ];    /* training patterns */
} t_patterns;


/* Neural network node */
typedef struct {
  float  weighted_sum;    /* sum of weighted inputs */

  float  output;          /* node output */

  float  delta;           /* backpropagation delta */

  float  delta_weight [ MAX_LAYER_NODES ];  /* current deltas */

  float  weight [ MAX_LAYER_NODES ];        /* weights for inputs */
} t_node;


/* Neural network layer */
typedef struct {
  t_node  node [ MAX_LAYER_NODES ];         /* network layer */
} t_layer;


/* Neural network */
typedef struct {
  int  layers;                          /* number of layers */

  int  nodes_per_layer [ MAX_LAYERS ];  /* number of nodes in each layer */

  t_layer  layer [ MAX_LAYERS ];        /* neural network nodes */
} t_network;



/******************************************************************************/
/* Backpropagation Neural Network main program */
main ( )
{
  t_network  network;  /* neural network */

  t_patterns  patterns;  /* training patterns */


  printf ( "Backpropagation neural network simulation.\n\n" );

  /* Read in the network structure & training patterns */
  read_network ( &network, &patterns );

  initialize_network ( &network );  /* initialize the neural network */

  print_network ( &network );  /* print out the initial network configuration */

  cycle_patterns ( &network, &patterns );  /* cycle on input patterns */

  print_outputs ( &network, &patterns );    /* Print out the output patterns */

  print_network ( &network );  /* print out the final network configuration */

  printf ( "\nExiting neural network program.\n" );
}  /* main */


/******************************************************************************/
/* This function reads in the neural network architecture and training patterns . */
read_network ( network, patterns )
t_network  *network;  /* neural network */
t_patterns  *patterns;  /* input patterns and expected outputs for training */
{
  FILE  *input_file = NULL;    /* input file */
  int   count;
  int   index;
  char  line [ MAX_LINE ];     /* current input line */
  long  line_index;            /* line index */
  int   status = S_NORMAL;     /* function return status */
  int   value;                 /* get integer return value */


  /* Ask what the input file name is. */
  prompt_file ( &input_file );

  /* Read in the number of network layers. */
  get_line ( input_file, line );
  line_index = 0;
  get_integer ( line, &line_index, &count );
  (*network).layers = count;

  printf ( "\nThe neural network has %d layers.\n\n", (*network).layers );

  /* read in the number of nodes on each layer. */
  for ( count = 0; count < (*network).layers; count++ )
  {
    line_index = 0;
    get_line ( input_file, line );
    get_integer ( line, &line_index, &((*network).nodes_per_layer [ count ]) );
    printf ( "  Layer %d has %d nodes.\n", count + 1,
        (*network).nodes_per_layer [ count ] );
  }  /* for (layer) */

  /* Read in the training patterns. */
  printf ( "\n" );
  (*patterns).total = 0;
  (*patterns).current = 1;
  while ( status != S_EOF )
  {
    status = get_line ( input_file, line );

    if ( status == S_NORMAL )
    {
      line_index = 0;
      (*patterns).total++;

      /* Read in the inputs. */
      printf ( "Inputs " );
      for ( index = 1; index <= (*network).nodes_per_layer [ INPUT_LAYER ]; 
          index++ )
      {
        get_integer ( line, &line_index, &value );
        (*patterns).io [ (*patterns).total ].input [ index ] = value * 1.0;
        printf ( "%3.1f  ", 
            (*patterns).io [ (*patterns).total ].input [ index ] );
      }  /* for */

      /* Read in the outputs. */
      printf ( "  Outputs " );
      for ( index = 1; index <= (*network).nodes_per_layer
          [ (*network).layers - 1 ]; index++ )
      {
        get_integer ( line, &line_index, &value ); 
        (*patterns).io [ (*patterns).total ].output [ index ] = value * 1.0;
        printf ( "%3.1f  ", 
            (*patterns).io [ (*patterns).total ].output [ index ] );
      }  /* for */
      printf ( "\n" );
    }  /* if */
  }  /* while */
}  /* read_network */


/******************************************************************************/
/* This function intializes the neural network. */
initialize_network ( network )
t_network  *network;  /* neural network */
{
  int  layer;         /* current layer */
  int  lower_node;    /* lower layer node index */
  int  node;          /* current layer node index */
  int  seed;          /* random number generator seed number */

  printf ( "\nWhat is the random number generator seed integer? " );
  scanf ( "%d", &seed );
  srand ( seed );
  printf ( "\n" );

  for ( layer = 0; layer < seed; layer++ )
    rand ();    /* randomize */

  /* Initialize each neural network layer. */
  for ( layer = 0; layer < MAX_LAYERS; layer++ )

    /* Initialize each node on each layer of the network. */
    for ( node = 0; node < MAX_LAYER_NODES; node++ )
    {
      /* Initialize node interconnection weight data. */
      for ( lower_node = 0; lower_node < MAX_LAYER_NODES; lower_node++ )
      {
        (*network).layer [ layer ].node [ node ].weight [ lower_node ] = 
            ((( rand () * 1.0 ) / MAX_RANDOM) * 2.0 - 1.0 );
        (*network).layer [ layer ].node [ node ].delta_weight [ lower_node ] 
            = 0.0;
      }  /* for (lower_node) */

      /* Initialize a neural network node */
      (*network).layer [ layer ].node [ node ].weighted_sum = 0.0;
      (*network).layer [ layer ].node [ node ].output       = 1.0;
      (*network).layer [ layer ].node [ node ].delta        = 0.0;
    }  /* for (node) */

}  /* initialize_network */


/******************************************************************************/
/* This function prints out a snapshot of the network. */
print_network ( network )
t_network  *network;  /* neural network */
{
  int  layer;         /* current layer index */
  int  lower_node;    /* lower layer node index */
  int  node;          /* current node index */

  printf ( "\nNeural Network:\n" );

  printf ( "\nlayer  node  weighted_sum    delta    output\n" );

  printf ( "    (weight  delta_weight) ...\n" );

  /* Print out each layer. */
  for ( layer = 0; layer < (*network).layers; layer++ )
  {
    /* Print out the data on each node of the current layer. */
    for ( node = 0; node <= (*network).nodes_per_layer [ layer ]; node++ )
    {
      printf ( "  %d      %d      %7.4f    %7.4f   %7.4f\n", layer, node,
          (*network).layer [ layer ].node [ node ].weighted_sum,
          (*network).layer [ layer ].node [ node ].delta,
          (*network).layer [ layer ].node [ node ].output );

      /* Print out the network interconnection weights and delta weights. */
      if (( layer > 0 ) && ( node > 0 ))
      {
        printf ( "    " );
        for ( lower_node = 0; lower_node <= 
            (*network).nodes_per_layer [ layer - 1 ]; lower_node++ )

          printf ( "(%7.4f  %7.4f)  ", 
            (*network).layer [ layer ].node [ node ].weight [ lower_node ],
            (*network).layer [ layer ].node [ node ].delta_weight 
                [ lower_node ] );

        printf ( "\n" );
      }  /* if */
    }  /* for (node) */

    printf ( "\n" );  /* separate layers */
  }  /* for (layer) */
}  /* print_network */


/******************************************************************************/
/* This function initializes the input nodes for the current training pattern. */
set_inputs ( network, patterns )
t_network   *network;   /* neural network */
t_patterns  *patterns;  /* training patterns */
{
  long  node;    /* node index of input layer */

  /* Set the outputs of the input nodes. */
  for ( node = 1; node <= (*network).nodes_per_layer [ INPUT_LAYER ]; node++ )

    (*network).layer [ INPUT_LAYER ].node [ node ].output =
        (*patterns).io [ (*patterns).current ].input [ node ];
}  /* set_inputs */


/******************************************************************************/
/* This function evaluates the outputs of the neural network for convergence. */
/* A 1 is returned for convergence, otherwise 0. */
int  check_outputs ( network, patterns )
t_network   *network;   /* neural network */
t_patterns  *patterns;  /* training patterns */
{
  long  node;    /* node index of output layer */

  int  output_layer;    /* output layer index */


  output_layer = (*network).layers - 1;

  /* Check each output node. */
  for ( node = 1; node <= (*network).nodes_per_layer [ output_layer ]; node++ )

    /* Compare observed output to expected output. */
    if ( ( (*network).layer [ output_layer ].node [ node ].output <
           (*patterns).io [ (*patterns).current ].output [ node ] - EPSILON ) ||

         ( (*network).layer [ output_layer ].node [ node ].output >
           (*patterns).io [ (*patterns).current ].output [ node ] + EPSILON ) ) 

      return ( 0 );    /* output out of tolerance range */

  return ( 1 );    /* all outputs within tolerance */
}  /* check_outputs */


/******************************************************************************/
/* This function cycles on the input patterns until network is trained. */
cycle_patterns ( network, patterns )
t_network   *network;   /* neural network */
t_patterns  *patterns;  /* input patterns and expected outputs for training */
{
  int  convergence;  /* epoch convergence on all outputs */

  long  epoch = 0;  /* Current number of training cycles */


  /* Cycle through the patterns until the network is trained. */
  do
  {
    epoch++;
    convergence = 1;
    for ( (*patterns).current = 1; (*patterns).current <= (*patterns).total; 
        (*patterns).current++ )
    {
      /* Apply an input pattern to the neural network. */
      set_inputs ( network, patterns );

      feed_forward ( network );

      /* Train the neural network. */
      back_propagate ( network, patterns );

      convergence &= check_outputs ( network, patterns );
    }  /* for (pattern) */

    if ( ( epoch % 100 ) == 0 )
      printf ( "Epoch %d\n", epoch );
  }
  while ( ( convergence != 1 ) && ( epoch < 5000 ) );

}  /* cycle_patterns */


/******************************************************************************/
/* This function applies an input pattern to the neural network. */
feed_forward ( network )
t_network  *network;  /* neural network */
{
  int  layer;  /* current layer */

  int  lower_node;  /* current node of the lower network layer */

  int  node;  /* current network node */

  float  sigmoid ();  /* sigmoid function */

  float  weighted_sum;  /* computed weighted sum for a node */


  /* Process all of the layers in the network except for the input layer. */
  for ( layer = 1; layer < (*network).layers; layer++ )

    /* Process each node in the layer expect for the theta node. */
    for ( node = 1; node <= (*network).nodes_per_layer [ layer ]; node++ )
    {
      weighted_sum = 0.0;

      /* Apply all of the outputs of the lower layer as inputs. */
      for ( lower_node = 0; lower_node <= (*network).nodes_per_layer 
          [ layer - 1 ]; lower_node++ )

        /* Weighted sum term = weight * output */
        weighted_sum += 
            (*network).layer [ layer ].node [ node ].weight [ lower_node ] *
            (*network).layer [ layer - 1 ].node [ lower_node ].output;

      /* Save the computed weighted sum. */
      (*network).layer [ layer ].node [ node ].weighted_sum = weighted_sum;

      /* Node output is F(weighted_sum) */
      (*network).layer [ layer ].node [ node ].output =
          sigmoid ( weighted_sum );

    }  /* for (node) */
}  /* feed_forward */


/******************************************************************************/
/* This function computes the sigmoid value. */
float sigmoid ( input )
float  input;
{
  double exp ();       /* exponential function */
  double minus_input;  /* cast the input value into a double & negate */

  minus_input = - input;
  return ( 1.0 / ( 1.0 + exp ( minus_input ) ) );
}  /* sigmoid */


/******************************************************************************/
/* This function computes the derivated of the sigmoid function value. */
float sigmoid_prime ( input )
float  input;
{
  float  sigmoid_of_input;  /* sigmoid ( input ) */

  sigmoid_of_input = sigmoid ( input );  /* only compute once */

  return ( sigmoid_of_input * ( 1.0 - sigmoid_of_input ) );
}  /* sigmoid_prime */


/******************************************************************************/
/* This function trains the neural network by backpropagation algorithm. */
back_propagate ( network, patterns )
t_network   *network;   /* neural network */
t_patterns  *patterns;  /* training patterns */
{
  float  delta;  /* computed delta for the current node */

  float  delta_weight;  /* computed delta weight for an interconnection */

  int  layer;  /* current layer */

  int  lower_node;  /* node index of node in a lower layer */

  int  node;  /* current neural network node */

  float  sigmoid_prime ();  /* sigmoid' function */

  float  sum_delta_weight;  /* sum of deltas * weights */

  int  upper_node;  /* node index of node in an upper layer */


  /* Process the output layer. */
  layer = (*network).layers - 1;
  for ( node = 1; node <= (*network).nodes_per_layer [ layer ]; node++ )
  {
    /* Compute the delta for an output node. */
    delta = ( (*patterns).io [ (*patterns).current ].output [ node ] -
        (*network).layer [ layer ].node [ node ].output ) * sigmoid_prime
        ( (*network).layer [ layer ].node [ node ].weighted_sum );

    (*network).layer [ layer ].node [ node ].delta = delta;

    /* Compute the delta weights for the incoming interconnections. */
    for ( lower_node = 0; lower_node <= (*network).nodes_per_layer 
        [ layer - 1 ]; lower_node++ )
    {
      delta_weight = ALPHA * delta * (*network).layer [ layer - 1 ]
          .node [ lower_node ].output;

      (*network).layer [ layer ].node [ node ].delta_weight [ lower_node ] 
          = delta_weight;

    }  /* for (lower_node) */
  }  /* for (node) */

  /* Process the hidden layers. */
  for ( layer = layer - 1; layer > 0; layer-- )
    for ( node = 1; node <= (*network).nodes_per_layer [ layer ]; node++ )
    {
      /* Compute the delta for a hidden node. */
      sum_delta_weight = 0.0;
      for ( upper_node = 0; 
          upper_node <= (*network).nodes_per_layer [ layer - 1 ]; upper_node++ )

        sum_delta_weight += 
            (*network).layer [ layer + 1 ].node [ upper_node ].delta * 
            (*network).layer [ layer + 1 ].node [ upper_node ].weight [ node ];

      delta = sum_delta_weight * sigmoid_prime 
          ( (*network).layer [ layer ].node [ node ].weighted_sum );

      (*network).layer [ layer ].node [ node ].delta = delta;

      /* Compute the delta weights for the incoming interconnections. */
      for ( lower_node = 0; lower_node <= (*network).nodes_per_layer 
          [ layer - 1 ]; lower_node++ )
      {
        delta_weight = ALPHA * delta * (*network).layer [ layer - 1 ]
            .node [ lower_node ].output;

        (*network).layer [ layer ].node [ node ].delta_weight [ lower_node ] 
            = delta_weight;
      }  /* for (lower_node) */
    }  /* for (node) */

  /* Update the interconnection weights. */
  for ( layer = 1; layer < (*network).layers; layer++ )
    for ( node = 1; node <= (*network).nodes_per_layer [ layer ]; node++ )
      for ( lower_node = 0; 
          lower_node <= (*network).nodes_per_layer [ layer - 1 ]; lower_node++ )
        (*network).layer [ layer ].node [ node ].weight [ lower_node ] +=
          (*network).layer [ layer ].node [ node ].delta_weight [ lower_node ];
 
}  /* back_propagate */


/******************************************************************************/
/* This function prints the outputs of the neural network for the training patterns. */
print_outputs ( network, patterns )
t_network   *network;   /* neural network */
t_patterns  *patterns;  /* training patterns */
{
  long  node;    /* node index of output layer */

  int  output_layer;    /* output layer index */

  printf ( "\nTraining Patterns & Outputs Observed\n\n" );

  for ( (*patterns).current = 1; (*patterns).current <= (*patterns).total; 
      (*patterns).current++ )
  {
    /* Apply an input pattern to the neural network. */
    set_inputs ( network, patterns );

    feed_forward ( network );

    /* Print out the inputs. */
    printf ( "Pattern %3d:  Inputs ", (*patterns).current );
    for ( node = 1; node <= (*network).nodes_per_layer [ INPUT_LAYER ]; 
        node++ )

        printf ( "%3.1f  ", 
          (*patterns).io [ (*patterns).current ].input [ node ] );
 
    /* Print the outputs. */
    printf ( "Outputs  " );
    output_layer = (*network).layers - 1;
    for ( node = 1; node <= (*network).nodes_per_layer [ output_layer ]; node++ )

      printf ( "%4.2f  ", 
          (*network).layer [ output_layer ].node [ node ].output );

    printf ( "\n" );
  }  /* for (each pattern) */
}  /* print_outputs */


/******************************************************************************/
/* This function returns the current line. */
get_line (data, line)
FILE *data;  /* the data file to input from */
char line [];  /* the next line from the data file */
{
  int c;  /* current character */
  int index;  /* line index */

  for (index = 0;
       (index < MAX_LINE - 1) && ((c = getc (data)) != EOF) && (c != '\n');
       ++index)
    line [ index ] = c;

  line [ index ] = '\0';  /* terminate line */

/*  printf ("get_line, '%s'\n", line);  */

  if (c == EOF)  return (S_EOF);
  return (S_NORMAL);
}  /* get_line */


/******************************************************************************/
/* This function returns the next token from line. */
get_token ( line, line_index, token )
char	line [];	/* source line */
int	*line_index;	/* character to start at */
char	token [];	/* next token */
{
  int	index = 0;	/* array index of current token character */

  /* skip leading blanks or line feeds. */
  while ( (line [ *line_index ] == ' ') || (line [ *line_index ] == '\n') ||
          (line [ *line_index ] == '_') )
    (*line_index)++;

  token [ index ] = '\0';
  /* check for end of line. */
  if ( line [ *line_index ] == '\0' )
    return ( S_EOL );

  /* copy the token. */
  while ( (line [ *line_index + index ] != '\0') &&
	  (line [ *line_index + index ] != '\n') &&
	  (line [ *line_index + index ] != '_') &&
	  (line [ *line_index + index ] != ' ') )
  {
    token [ index ] = line [ *line_index + index ];
    ++index;
  }  /* while */

  *line_index = *line_index + index;
  token [ index ] = '\0';
  return ( S_NORMAL );
}  /* get_token */


/******************************************************************************/
/* This function opens the specified file for reading. */
open_file (name, input_file)
char  name [];		/* filename */
FILE  **input_file;	/* file to open for reading */
{
  FILE  *fopen ();	/* file open function */

  /* printf ("open_file, name = '%s'\n", name); */

  *input_file = fopen (name, "r");
  if (*input_file == NULL)
  {
    printf ("WARNING: could not open the file '%s'.\n");
    return (S_OPEN_FAILED);
  }  /* if */

  return (S_NORMAL);
}  /* open_file */


/******************************************************************************/
/* This function prompts for the input file name. */
prompt_file ( file_name )
FILE  **file_name;	/* input file */
{
  int   line_index;		/* line index for get_integer */
  char  response [ MAX_LINE ];	/* user's response */

  /* check  if the input file has been specified */
  while ( *file_name == NULL )
  {
    printf ("What is the name of Neural Network Input file? ");
    scanf ( "%s", response );
    open_file ( response, file_name );
  }  /* while */

}  /* prompt_file */


/******************************************************************************/
/* This function returns the next integer from line. */
get_integer (line, line_index, integer)
char line [];  /* the current line */
int *line_index;  /* the current line index */
int *integer;  /* the next integer from line */
{
  int sign = 1;  /* the sign of the integer */
  int status = S_NO_INTEGER;  /* fuction return status */

  *integer = 0;

  /* ignore leading spaces */
  while (line [ *line_index ] == ' ')
    (*line_index)++;

  /* check for a negative sign */
  if (line [ *line_index ] == '-')
  {
    sign = -1;
    (*line_index)++;
  }  /* minus sign */

  /* traverse the number */
  while ((line [ *line_index ] >= '0') &&
    (line [ *line_index ] <= '9'))
  {
    (*integer) = (*integer) * 10 + (line [ (*line_index)++ ] - '0');
    status = S_NORMAL;
  }  /* traverse */

  *integer = sign * (*integer);
  return (status);
}  /* get_integer */
