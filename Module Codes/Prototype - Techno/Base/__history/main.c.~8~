#include <main.h>

/*

   Left motor :
   ccp2
   forward : B0=0   B1=1
   reverse : B0=1   B1=0
   
   Right motor :
   ccp1
   forward : B2=1   B3=0
   reverse : B2=0   B3=1
   
   LED connected to B4
   
   s - show, t - test, a - act
*/

#define MotorRight1 PIN_B2
#define MotorRight2 PIN_B3
#define MotorLeft1 PIN_B0
#define MotorLeft2 PIN_B1
#define LED PIN_B4

void go_forward();   //action 1
void go_backward();  //action 2
void turn_right();   //action 3
void turn_left();    //action 4
void stop();         //action 5
void show(); 

char buffer[2];
int8 state; 

#INT_SSP
void  SSP_isr(void) 
{
      state = i2c_isr_state();

      if((state== 0 ) || (state== 0x80)){
        i2c_read();
      }

      if(state >= 0x80){
         i2c_write(buffer[0]);
      }

      else if(state > 0){
         buffer[state - 1] = i2c_read();
         
         if(buffer[0] == 's'){
            show();
         }
         
         else if(state == 2){
            switch(buffer[1]) 
            { 
               case 1: 
                 go_forward();
                 break;
               case 2:
                 go_backward();
                 break;
               case 3:
                 turn_right();
                 break;
               case 4:
                 turn_left();
                 break;
               case 5:
                 stop();                            
                 break;
               default : 
                  break;
            }
         }
      }

}

void main()
{
   setup_timer_2(T2_DIV_BY_16,255,1);      //4.0 ms overflow, 4.0 ms interrupt

   setup_ccp1(CCP_PWM);
   setup_ccp2(CCP_PWM);
   set_pwm1_duty((int16)1021);
   set_pwm2_duty((int16)1021);

   enable_interrupts(INT_SSP);
   enable_interrupts(GLOBAL);
   
   SET_TRIS_B(0);


   while(TRUE)
   {
      OUTPUT_HIGH(LED);
      delay_ms(1000);
      OUTPUT_LOW(LED);
      delay_ms(1000);      
   }

}

void show(void)      //light led
{
      OUTPUT_HIGH(LED);
      delay_ms(5000);
      OUTPUT_LOW(LED);
}

void go_forward()      //forward
{
      OUTPUT_LOW(MotorLeft1);
      OUTPUT_HIGH(MotorLeft2);
      OUTPUT_HIGH(MotorRight1);
      OUTPUT_LOW(MotorRight2);   
      
      if(buffer[0] == 't'){
         delay_ms(5000);
         stop();
      }
}

void go_backward()      //reverse
{
      OUTPUT_HIGH(MotorLeft1);
      OUTPUT_LOW(MotorLeft2);
      OUTPUT_LOW(MotorRight1);
      OUTPUT_HIGH(MotorRight2); 
      
      if(buffer[0] == 't'){
         delay_ms(5000);
         stop();
      }
}

void stop()      //stop - not meaningful for test?
{
      OUTPUT_HIGH(MotorLeft1);
      OUTPUT_HIGH(MotorLeft2);
      OUTPUT_HIGH(MotorRight1);
      OUTPUT_HIGH(MotorRight2); 
}

void turn_right()      //turn right using 2 wheels (by 90 degree)
{
      OUTPUT_LOW(MotorLeft1);
      OUTPUT_HIGH(MotorLeft2);
      OUTPUT_LOW(MotorRight1);
      OUTPUT_HIGH(MotorRight2);
      
      delay_ms(5000);
      
      stop();
}

void turn_left()      //turn left using 2 wheels (by 90 degree)
{
      OUTPUT_HIGH(MotorLeft1);
      OUTPUT_LOW(MotorLeft2);
      OUTPUT_HIGH(MotorRight1);
      OUTPUT_LOW(MotorRight2); 
      
      delay_ms(5000);
      
      stop();
}
