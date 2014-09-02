#include <18F452.h>
#device ADC=16

#FUSES NOWDT                 	//No Watch Dog Timer
#FUSES WDT128                	//Watch Dog Timer uses 1:128 Postscale
#FUSES NOBROWNOUT            	//No brownout reset
#FUSES NOLVP                 	//No low voltage prgming, B3(PIC16) or B5(PIC18) used for I/O

#use delay(crystal=4000000)
#use FIXED_IO( B_outputs=PIN_B4,PIN_B3,PIN_B2,PIN_B1,PIN_B0 )
#use i2c(Slave,Slow,sda=PIN_C4,scl=PIN_C3,force_hw,address=0x14)

