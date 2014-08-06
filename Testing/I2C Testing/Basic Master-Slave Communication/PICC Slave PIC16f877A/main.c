#include <main.h>
char buffer[3];
int8 state; 

#INT_SSP
void  SSP_isr(void) 
{
   state = i2c_isr_state();

   if((state== 0 ) || (state== 0x80))

     i2c_read();

   if(state >= 0x80)

      i2c_write(buffer[0]);

   else if(state > 0)

      buffer[state - 1] = i2c_read();
}

void main()
{

   enable_interrupts(INT_SSP);
   enable_interrupts(GLOBAL);
   SET_TRIS_D(0);

   while(TRUE)
   {
      delay_ms(1000);
      printf("value is %s\n",buffer);
   }

}