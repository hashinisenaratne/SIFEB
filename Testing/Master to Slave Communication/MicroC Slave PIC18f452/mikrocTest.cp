#line 1 "G:/sem7/Final Year Project/Programs/SIFEB/Testing/Master to Slave Communication/MicroC Slave PIC18f452/mikrocTest.c"
#line 12 "G:/sem7/Final Year Project/Programs/SIFEB/Testing/Master to Slave Communication/MicroC Slave PIC18f452/mikrocTest.c"
const Addy = 0x04;
const Delay_Time = 250;



unsigned short j;
unsigned short rxbuffer;
unsigned short tx_data;

void Init(){
 ADCON1 = 0;
 TRISA = 0;
 TRISB = 0;
 TRISC = 0xFF;
 SSPADD = Addy;
 SSPCON1 = 0x36;
 PIE1.SSPIF = 1;
 INTCON = 0xC0;

 UART1_Init(9600);
}

void interrupt(){
 if (PIR1.SSPIF == 1){
 PORTB.B4 = ~PORTB.B4;
 PIR1.SSPIF = 0;


 if (SSPSTAT.R_W == 1){
 SSPBUF = tx_data;
 SSPCON1.CKP = 1;
 j = SSPBUF;
 return;
 }
 if (SSPSTAT.BF == 0){
 j = SSPBUF;
 return;
 }


 if (SSPSTAT.D_A == 1){
 rxbuffer = SSPBUF;
 tx_data = rxbuffer;
 j = SSPBUF;
 UART1_Write_Text(rxbuffer);
 return;
 }
 }
 j = SSPBUF;
}

void main(){
 Init();
 while(1){
 Delay_ms(Delay_Time);
 }
}
