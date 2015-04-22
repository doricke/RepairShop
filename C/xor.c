

#include <stdio.h>
#include <math.h>

/* This program implements a simple exclusize OR (XOR) neural network. */
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

#define  ALPHA            0.5   /* learning rate constant */

#define  MAX_LAYER_NODES  3     /* maximum number of nodes per layer */

#define  MAX_LAYERS       3     /* maximum number of neural network layers */

#define  MAX_PATTERNS     4     /* number of input patterns */


/* Neural network node */
typedef struct {
  float  weighted_sum;    /* sum of weighted inputs */

  float  output;          /* node output */

  float  delta;           /* backpropagation delta */
} t_node;


/* Neural network layer */
typedef struct {
  float  weight [ MAX_LAYER_NODES ];        /* weights for inputs */

  float  delta_weight [ MAX_LAYER_NODES ];  /* current deltas */

  t_node  node [ MAX_LAYER_NODES ];         /* network layer */
} t_layer;


/* Neural network */
typedef struct {
  int  layers;                          /* number of layers */

  int  nodes_per_layer [ MAX_LAYERS ];  /* number of nodes in each layer */

  t_layer  layer [ MAX_LAYERS ];        /* neural network nodes */
} t_network;



/* XOR main program */
main ( )
{
  t_network  network;  /* neural network */

  printf ( "XOR neural network simulation.\n" );

  initialize_network ( &network );  /* initialize the neural network */

  cycle_patterns ( &network );  /* cycle on input patterns */

  printf ( "Exiting XOR program.\n" );
}  /* main */


/* This function intializes the neural network. */
initialize_network ( network )
t_network  *network;  /* neural network */
{
  int  layer;  /* current layer */
  int  node;   /* current layer node index */

  printf ( "initialize_network\n" );

  /* Initialize the network topology. */
  (*network).layers = 3;
  (*network).nodes_per_layer [ 0 ] = 2;
  (*network).nodes_per_layer [ 1 ] = 2;
  (*network).nodes_per_layer [ 2 ] = 1;

  /* Initialize each neural network layer. */
  for ( layer = 0; layer < MAX_LAYERS; layer++ )

    /* Initialize each node on each layer of the network. */
    for ( node = 0; node < MAX_LAYER_NODES; node++ )
    {
      /* Initialize node interconnection weight data. */
      (*network).layer [ layer ].weight       [ node ] = 0.5;
      (*network).layer [ layer ].delta_weight [ node ] = 0.5;

      /* Initialize a neural network node */
      (*network).layer [ layer ].node [ node ].weighted_sum = 0.0;
      (*network).layer [ layer ].node [ node ].output       = 1.0;
      (*network).layer [ layer ].node [ node ].delta        = 0.0;
    }  /* for (node) */

  printf ( "exit initialize_network\n" );

}  /* initialize_network */


/* This function prints out a snapshot of the network. */
print_network ( network )
t_network  *network;  /* neural network */
{
  int  layer;  /* current layer */
  int  node;   /* current node */

  printf ("/nlayer  node  weight  delta_weight  weighted_sum  delta  output\n" )
;

  for ( layer = 0; layer < (*network).layers; layer++ )

    for ( node = 0; node <= (*network).nodes_per_layer [ layer ]; node++ )

      printf ( "%d  %d  %f  %f  %f  %f  %f\n", layer, node,
          (*network).layer [ layer ].weight [ node ],
          (*network).layer [ layer ].delta_weight [ node ],
          (*network).layer [ layer ].node [ node ].weighted_sum,
          (*network).layer [ layer ].node [ node ].delta,
          (*network).layer [ layer ].node [ node ].output );
}  /* print_network */


/* This function cycles on the input patterns until network is trained. */
cycle_patterns ( network )
t_network  *network;  /* neural network */
{
  long  cycle = 0;  /* Current number of training cycles */

  int  pattern;  /* current pattern index */

  float  input [ MAX_PATTERNS ];  /* current input pattern */

  /* Network input patterns and expected outputs for four patterns */
  float  input1 [] = { 1.0, 1.0, 0.0, 0.0 };
  float  input2 [] = { 1.0, 0.0, 1.0, 0.0 };
  float  output [] = { 0.0, 1.0, 1.0, 0.0 };

  float  observed [ MAX_PATTERNS ];  /* observed output vector */

  /* observed network outputs for each pattern */
  float  outputs [ MAX_PATTERNS ] = { 0.0, 0.0, 0.0, 0.0 };

  printf ( "cycle_patterns\n" );

print_network ( network );

  /* Cycle through the patterns until the network is trained. */
  do
  {
    cycle++;
    for ( pattern = 0; pattern < MAX_PATTERNS; pattern++ )
    {
      /* Apply an input pattern to the neural network. */
      input [ 0 ] = input1 [ pattern ];
      input [ 1 ] = input2 [ pattern ];
      feed_forward ( network, input, observed );
      outputs [ pattern ] = observed [ 0 ];

print_network ( network );

      /* Train the neural network. */
      back_propagate ( network, output [ pattern ]);

print_network ( network );
    }  /* for (pattern */

    printf ( "Cycle %d outputs [ %f, %f, %f, %f ]\n", cycle,
        outputs [ 0 ], outputs [ 1 ], outputs [ 2 ], outputs [ 3 ] );
  }
  while ( ( outputs [ 0 ] != output [ 0 ] ) &&
          ( outputs [ 1 ] != output [ 1 ] ) &&
          ( outputs [ 2 ] != output [ 2 ] ) &&
          ( outputs [ 3 ] != output [ 3 ] ) );

  printf ( "exit cycle_patterns\n" );

}  /* cycle_patterns */


/* This function applies an input pattern to the neural network. */
feed_forward ( network, input, output )
t_network  *network;  /* neural network */
float  input [];   /* current input pattern */
float  output [];  /* current network output vector */
{
  int  layer;  /* current layer */

  int  lower_node;  /* current node of the lower network layer */

  int  node;  /* current network node */

  float  sigmoid ();  /* sigmoid function */

  float  weighted_sum;  /* computed weighted sum for a node */

  printf ( "feed_forward\n" );

  /* Set the output of the input nodes. */
  for ( node = 1; node <= (*network).nodes_per_layer [ 0 ]; node++ )
    (*network).layer [ 0 ].node [ node ].output = input [ node - 1 ];

  /* Process all of the layers in the network except for the input layer. */
  for ( layer = 1; layer < (*network).layers; layer++ )

    /* Process each node in the layer expect for the theta node. */
    for ( node = 1; node <= (*network).nodes_per_layer [ layer ]; node++ )
    {
      weighted_sum = 0.0;

printf ("feed_forward, layer %d, node %d\n", layer, node);

      /* Apply all of the outputs of the lower layer as inputs. */
      for ( lower_node = 0; lower_node <= (*network).nodes_per_layer 
          [ layer - 1 ]; lower_node++ )

        /* Weighted sum term = weight * output */
        weighted_sum += (*network).layer [ layer ].weight [ lower_node ] *
            (*network).layer [ layer - 1 ].node [ lower_node ].output;

      /* Save the computed weighted sum. */
      (*network).layer [ layer ].node [ node ].weighted_sum = weighted_sum;

      /* Node output is F(weighted_sum) */
      (*network).layer [ layer ].node [ node ].output =
          sigmoid ( weighted_sum );

printf ("feed_forward, weighted_sum = %f, output = %f\n", weighted_sum,
  (*network).layer [ layer ].node [ node ].output);
    }  /* for (node) */

  /* Return the output vector of the neural network. */
  for ( node = 1; node <= (*network).nodes_per_layer [ (*network).layers - 1 ]; 
      node++ )
    output [ node - 1 ] = 
        (*network).layer [ (*network).layers - 1 ].node [ node ].output;
for (node = 1; node <= (*network).nodes_per_layer [ (*network).layers - 1 ]; 
    node++ )
  printf ( "output [ %d ] = %f  ", node-1, output [ node-1 ] );
  printf ( "exit feed_forward\n" );
}  /* feed_forward */


/* This function computes the sigmoid value. */
float sigmoid ( input )
float  input;
{
  double exp ();       /* exponential function */
  double minus_input;  /* cast the input value into a double & negate */

  printf ( "sigmoid\n" );

  minus_input = - input;
  return ( 1.0 / ( 1.0 + exp ( minus_input ) ) );
}  /* sigmoid */


/* This function computes the derivated of the sigmoid function value. */
float sigmoid_prime ( input )
float  input;
{
  float  sigmoid_of_input;  /* sigmoid ( input ) */

  sigmoid_of_input = sigmoid ( input );  /* only compute once */

  return ( sigmoid_of_input * ( 1.0 - sigmoid_of_input ) );
}  /* sigmoid_prime */


/* This function trains the neural network by backpropagation algorithm. */
back_propagate ( network, desired_output )
t_network  *network;  /* neural network */
float  desired_output;  /* desired network output from applied pattern */
{
  float  delta;  /* computed delta for the current node */

  float  delta_weight;  /* computed delta weight for an interconnection */

  int  layer;  /* current layer */

  int  lower_node;  /* node index of node in a lower layer */

  int  node;  /* current neural network node */

  float  sigmoid_prime ();  /* sigmoid' function */

  float  sum_delta_weight;  /* sum of deltas * weights */

  int  upper_node;  /* node index of node in an upper layer */


  printf ( "back_propagate\n" );

  /* Process the output layer. */
  layer = (*network).layers - 1;
  for ( node = 1; node <= (*network).nodes_per_layer [ layer ]; node++ )
  {
printf ( "back_p, node = %d, layer = %d\n", node, layer );

    /* Compute the delta for an output node. */
    delta = ( desired_output -
        (*network).layer [ layer ].node [ node ].output ) * sigmoid_prime
        ( (*network).layer [ layer ].node [ node ].weighted_sum );

    (*network).layer [ layer ].node [ node ].delta = delta;
printf ( "back_p, delta = %f\n", delta );

    /* Compute the delta weights for the incoming interconnections. */
    for ( lower_node = 0; lower_node <= (*network).nodes_per_layer 
        [ layer - 1 ]; lower_node++ )
    {
      delta_weight = ALPHA * delta * (*network).layer [ layer ]
          .node [ lower_node ].output;

printf ( "back_p, delta_weight [ %d ] = %f\n", lower_node, delta_weight );

      (*network).layer [ layer ].delta_weight [ node ] = delta_weight;

      /* Update the interconnection weight. */
      (*network).layer [ layer ].weight [ node ] += delta_weight;
    }  /* for (lower_node) */
  }  /* for (node) */

  /* Process the hidden layers. */
  for ( layer = layer - 1; layer > 0; layer-- )
    for ( node = 1; node <= (*network).nodes_per_layer [ layer ]; node++ )
    {
printf ( "back_p, node = %d, layer = %d\n", node, layer );

      /* Compute the delta for a hidden node. */
      sum_delta_weight = 0.0;
      for ( upper_node = 0; upper_node <= (*network).nodes_per_layer [ layer - 1
 ];
          upper_node++ )
        sum_delta_weight += 
            (*network).layer [ layer + 1 ].node [ upper_node ].delta * 
            (*network).layer [ layer + 1 ].weight [ node ];
      delta = sum_delta_weight * sigmoid_prime 
          ( (*network).layer [ layer ].node [ node ].weighted_sum );

printf ( "back_p, delta = %f, sum_delta_weight = %f\n", delta, sum_delta_weight 
);

      (*network).layer [ layer ].node [ node ].delta = delta;

      /* Compute the delta weights for the incoming interconnections. */
      for ( lower_node = 0; lower_node <= (*network).nodes_per_layer 
          [ layer - 1 ]; lower_node++ )
      {
        delta_weight = ALPHA * delta * (*network).layer [ layer ]
            .node [ lower_node ].output;

printf ( "back_p, delta_weight [ %d ] = %f\n", lower_node, delta_weight );

        (*network).layer [ layer ].delta_weight [ node ] = delta_weight;

        /* Update the interconnection weight. */
        (*network).layer [ layer ].weight [ node ] += delta_weight;
      }  /* for (lower_node) */
    }  /* for (node) */

  printf ( "exit back_propagate\n" );
}  /* back_propagate */
$ 
