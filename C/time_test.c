
#include <stdio.h>
#include <time.h>

main ()
{
  char  *ctime ();
  struct tm *localtime ();
  struct tm tm_test;
  struct tm *tm_testp;
  long  *clock;

  long  time ();
  long  timer, time2;
  long  *tloc;

  printf ( "This is the time test program.\n" );

  time2 = 0;
  tloc = &time2;
  timer = time ( tloc );

  printf ( "After call to time\n" );
  printf ( "timer = %ld\n", timer );
  printf ( "tloc = %ld\n", *tloc );

  tm_testp = localtime ( tloc );

  printf ( "After call to localtime\n" );
  printf ( "tloc = %ld\n", *tloc );
  printf ( "tm_testp.tm_sec = %d\n", (*tm_testp).tm_sec );
  printf ( "tm_testp.tm_min = %d\n", (*tm_testp).tm_min );
  printf ( "tm_testp.tm_hour = %d\n", (*tm_testp).tm_hour );
  printf ( "tm_testp.tm_mday = %d\n", (*tm_testp).tm_mday );
  printf ( "tm_testp.tm_mon = %d\n", (*tm_testp).tm_mon );
  printf ( "tm_testp.tm_year = %d\n", (*tm_testp).tm_year );
  printf ( "tm_testp.tm_wday = %d\n", (*tm_testp).tm_wday );
  printf ( "tm_testp.tm_yday = %d\n", (*tm_testp).tm_yday );
  printf ( "tm_testp.tm_isdst = %d\n", (*tm_testp).tm_isdst );
}  /* main */


