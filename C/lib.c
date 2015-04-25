/* c language routines from "The C Programming Language" */
/* Brain W. Kernighan & Dennis M. Ritchie */
/* Prentice-Hall Software Series */
/* Copyright (c) 1978 by Bell Telephone Laboratories, Incorporated. */

#include <stdio.h>


/* Convert string s to double */
double atof (s)
char s[];
{
  double val, power;
  int i, sign;

  /* skip white space */
  for (i=0; s[i]==' ' || s[i]=='\n' || s[i]=='\t'; i++) ;

  sign = 1;
  if (s[i] == '+' || s[i] == '-')  /* sign */
    sign = (s[i++]=='+') ? 1 : -1;

  for (val = 0; s[i] >= '0' && s[i] <= '9'; i++)
    val = 10 * val + s[i] - '0';

  if (s[i] == '.') i++;

  for (power = 1; s[i] >= '0' && s[i] <= '9'; i++)
  {
    val = 10 * val + s[i] - '0';
    power *= 10;
  }  /* for */

  return (sign * val / power);
}  /* atof */


/* Find x in v[0] ... v[n-1] */
binary (x, v, n)
int x, v[], n;
{
  int low, high, mid;

  low = 0;
  high = n - 1;
  while ( low <= high )
  {
    mid = (low+high) / 2;

    if (x < v[mid])  high = mid - 1;
    else
      if (x > v[mid])  low = mid + 1;
      else
        return (mid);	/* found match */
  }  /* while */
  return (-1);
}  /* binary */


/* Copy s1 to s2; assume s2 is big enough */
copy (s1, s2)
char s1[], s2[];
{
  int i = 0;

  while ( ( s2[i] = s1[i] ) != '\0' )
    ++i;
}  /* copy */


/* Get the next integer from input */
getint (pn)
int *pn;
{
  int c, sign;

  /* skip white space */
  while ((c = getch()) == ' ' || c == '\n' || c == '\t') ;

  /* record sign */
  sign = 1;
  if (c == '+' || c == '-')
  {
    sign = (c=='+') ? 1 : -1;
    c = getch ();
  }  /* if */

  for (*pn = 0; c >= '0' && c <= '9'; c = getch())
    *pn = 10 * *pn + c - '0';

  *pn *= sign;

  if (c != EOF)  ungetch (c);
  return (c);
}  /* getint */


/* Get line into s, return length */
getline (s, lim)
char s[];
int lim;
{
  int c, i;

  i = 0;
  while (--lim > 0 && (c=getchar()) != EOF && c != '\n')
    s[i++] = c;

  if ( c == '\n' )  s[i++] = c;
  s[i]='\0';
  return (i);
}  /* getline */


/* Get the next word from input */
#define  LETTER  'a' 
#define  DIGIT   '0'
getword (w, lim)
char *w;
int lim;
{
  int c, t;

  if (type(c = *w++ = getch()) != LETTER)
  {
    *w = '\0';
    return (c);
  }  /* if */

  while (--lim > 0)
  {
    t = type(c = *w++ = getch());

    if (t != LETTER && t != DIGIT)
    {
      ungetch (c);
      break;
    }  /* if */
  }  /* while */

  *(w-1) = '\0';
  return (LETTER);
}  /* getword */


/* Convert n to characters in s. */
itoa (n, s)
char s[];
int n;
{
  int i, sign;

  if ((sign = n) < 0)  /* record sign */
    n = -n;

  /* Generate the digits in reverse order. */
  i = 0;
  do 
  {
    s[i++] = n % 10 + '0';	/* get next digit */
  }
  while ((n /= 10) > 0);	/* delete it */

  if (sign < 0)  s[i++] = '-';

  s[i] ='\0';

  reverse (s);
}  /* itoa */


/* Convert c to lower case; ASCII only */
lower (c)
int c;
{
  if (c >= 'A' && c <= 'Z')

    return ( c + 'a' - 'A' );

  else

    return (c);
}  /* lower */


/* Reverse string s in place. */
reverse(s)
char s[];
{
  int  c, i, j;

  for (i = 0, j = strlen(s)-1; i < j; i++, j--)
  {
    c = s[i];
    s[i] = s[j];
    s[j] = c;
  }  /* for */
}  /* reverse */


/* Return <0 if s<t, 0 if s==t, >0 if s>t */
strcmp (s, t)
char *s, *t;
{
  for ( ; *s == *t; s++, t++)

    if (*s == '\0')  return (0);

  return (*s - *t);
}  /* strcmp */


/* Copy t to s */
strcpy (s, t)
char *s, *t;
{
  while (*s++ = *t++)  ;
}  /* strcpy */


/* Return length of string s */
strlen (s)
char *s;
{
  char *p = s;

  while (*p != '\0')  p++;

  return (p-s);
}  /* strlen */


