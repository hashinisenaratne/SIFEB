/*
 * Project name:
     Slave
 * Description:
     Slave code for PIC 2 PIC I2C Communication
 * Configuration:
     MCU:             PIC18F452
 * NOTES:
*/

//------------------------------------------------------------------------------
const Addy = 0x04;                    // set I2C device address
const Delay_Time = 250;               // port check delay
//------------------------------------------------------------------------------
//                      Global Processing Variables
//------------------------------------------------------------------------------
unsigned short j;                      // just dummy for buffer read
unsigned short rxbuffer;               //
unsigned short tx_data;                //
//------------------------------------------------------------------------------
void Init(){
  ADCON1 = 0;                          // All ports set to digital
  TRISA = 0;                           // Set PORTA as output
  TRISB = 0;                           // Set PORTB as output
  TRISC = 0xFF;                        // Set PORTC as input
  SSPADD =  Addy;                      // Get address (7bit). Lsb is read/write flag
  SSPCON1 = 0x36;                       // Set to I2C slave with 7-bit address
  PIE1.SSPIF = 1;                      // enable SSP interrupts
  INTCON = 0xC0;                       // enable INTCON.GIE

  UART1_Init(9600);
}
//------------------------------------------------------------------------------
void interrupt(){                      // I2C slave interrupt handler
  if (PIR1.SSPIF == 1){                // I2C Interrupt
    PORTB.B4 = ~PORTB.B4;             // check entering interrupt
    PIR1.SSPIF = 0;                    // reset SSP interrupt flag

    //transmit data to master
    if (SSPSTAT.R_W == 1){             // Read request from master
      SSPBUF = tx_data;                // Get data to send
      SSPCON1.CKP = 1;                  // Release SCL line
      j = SSPBUF;                      // That's it
      return;
    }
    if (SSPSTAT.BF == 0){              // all done,
      j = SSPBUF;                      // Nothing in buffer so exit
      return;
    }

    //recieve data from master
    if (SSPSTAT.D_A == 1){             // Data [not address]
      rxbuffer = SSPBUF;               // get data
      tx_data = rxbuffer;
      j = SSPBUF;                      // read buffer to clear flag [address]
      UART1_Write_Text(rxbuffer);      //write serial data
      return;
    }
  }
  j = SSPBUF;                              // read buffer to clear flag [address]
}
//------------------------------------------------------------------------------
void main(){
  Init();
  while(1){
    Delay_ms(Delay_Time);
  }
}