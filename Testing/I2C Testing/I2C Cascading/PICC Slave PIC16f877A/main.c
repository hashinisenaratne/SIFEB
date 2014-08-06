
#include <main.h>

char buffer[3];
int8 state; 
int addReceived = 0;
#byte SSPADD = 0x93
#INT_SSP

void  SSP_isr(VOID) 
{
   state = i2c_isr_state ();

   IF ( (state == 0) || (state == 0x80))
   i2c_read () ;

   IF (state >= 0x80)
      i2c_write (buffer[0]) ;

   else IF (state > 0)
   {
      buffer[state - 1] = i2c_read ();

      IF (buffer[state - 1] == 'H')	//switch on the AND gate
      {
         output_high (PIN_A0) ;
      }

      ELSE IF (buffer[state - 1] == 'L')	//switch off the AND gate
      {
         output_low (PIN_A0) ;
      }
      
      ELSE IF (buffer[state - 1] == 'C')	//command to change address
      {
         addReceived = 1;
      }      
      
      ELSE IF(addReceived == 1)
      {
         SSPADD = buffer[state - 1];
         addReceived = 0;
      }
   }
}

void main()
{
   enable_interrupts (INT_SSP) ;
   enable_interrupts (GLOBAL) ;
   SET_TRIS_D (0) ;
   SET_TRIS_A (0) ;

   WHILE (TRUE)
   {
      delay_ms (1000) ;
      printf ("value is %s\n", buffer);
   }
}

