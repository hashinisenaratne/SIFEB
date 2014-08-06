
_Init:

;mikrocTest.c,21 :: 		void Init(){
;mikrocTest.c,22 :: 		ADCON1 = 0;                          // All ports set to digital
	CLRF        ADCON1+0 
;mikrocTest.c,23 :: 		TRISA = 0;                           // Set PORTA as output
	CLRF        TRISA+0 
;mikrocTest.c,24 :: 		TRISB = 0;                           // Set PORTB as output
	CLRF        TRISB+0 
;mikrocTest.c,25 :: 		TRISC = 0xFF;                        // Set PORTC as input
	MOVLW       255
	MOVWF       TRISC+0 
;mikrocTest.c,26 :: 		SSPADD =  Addy;                      // Get address (7bit). Lsb is read/write flag
	MOVLW       4
	MOVWF       SSPADD+0 
;mikrocTest.c,27 :: 		SSPCON1 = 0x36;                       // Set to I2C slave with 7-bit address
	MOVLW       54
	MOVWF       SSPCON1+0 
;mikrocTest.c,28 :: 		PIE1.SSPIF = 1;                      // enable SSP interrupts
	BSF         PIE1+0, 3 
;mikrocTest.c,29 :: 		INTCON = 0xC0;                       // enable INTCON.GIE
	MOVLW       192
	MOVWF       INTCON+0 
;mikrocTest.c,31 :: 		UART1_Init(9600);
	MOVLW       51
	MOVWF       SPBRG+0 
	BSF         TXSTA+0, 2, 0
	CALL        _UART1_Init+0, 0
;mikrocTest.c,32 :: 		}
L_end_Init:
	RETURN      0
; end of _Init

_interrupt:

;mikrocTest.c,34 :: 		void interrupt(){                      // I2C slave interrupt handler
;mikrocTest.c,35 :: 		if (PIR1.SSPIF == 1){                // I2C Interrupt
	BTFSS       PIR1+0, 3 
	GOTO        L_interrupt0
;mikrocTest.c,36 :: 		PORTB.B4 = ~PORTB.B4;             // check entering interrupt
	BTG         PORTB+0, 4 
;mikrocTest.c,37 :: 		PIR1.SSPIF = 0;                    // reset SSP interrupt flag
	BCF         PIR1+0, 3 
;mikrocTest.c,40 :: 		if (SSPSTAT.R_W == 1){             // Read request from master
	BTFSS       SSPSTAT+0, 2 
	GOTO        L_interrupt1
;mikrocTest.c,41 :: 		SSPBUF = tx_data;                // Get data to send
	MOVF        _tx_data+0, 0 
	MOVWF       SSPBUF+0 
;mikrocTest.c,42 :: 		SSPCON1.CKP = 1;                  // Release SCL line
	BSF         SSPCON1+0, 4 
;mikrocTest.c,43 :: 		j = SSPBUF;                      // That's it
	MOVF        SSPBUF+0, 0 
	MOVWF       _j+0 
;mikrocTest.c,44 :: 		return;
	GOTO        L__interrupt9
;mikrocTest.c,45 :: 		}
L_interrupt1:
;mikrocTest.c,46 :: 		if (SSPSTAT.BF == 0){              // all done,
	BTFSC       SSPSTAT+0, 0 
	GOTO        L_interrupt2
;mikrocTest.c,47 :: 		j = SSPBUF;                      // Nothing in buffer so exit
	MOVF        SSPBUF+0, 0 
	MOVWF       _j+0 
;mikrocTest.c,48 :: 		return;
	GOTO        L__interrupt9
;mikrocTest.c,49 :: 		}
L_interrupt2:
;mikrocTest.c,52 :: 		if (SSPSTAT.D_A == 1){             // Data [not address]
	BTFSS       SSPSTAT+0, 5 
	GOTO        L_interrupt3
;mikrocTest.c,53 :: 		rxbuffer = SSPBUF;               // get data
	MOVF        SSPBUF+0, 0 
	MOVWF       _rxbuffer+0 
;mikrocTest.c,54 :: 		tx_data = rxbuffer;
	MOVF        _rxbuffer+0, 0 
	MOVWF       _tx_data+0 
;mikrocTest.c,55 :: 		j = SSPBUF;                      // read buffer to clear flag [address]
	MOVF        SSPBUF+0, 0 
	MOVWF       _j+0 
;mikrocTest.c,56 :: 		UART1_Write_Text(rxbuffer);      //write serial data
	MOVF        _rxbuffer+0, 0 
	MOVWF       FARG_UART1_Write_Text_uart_text+0 
	MOVLW       0
	MOVWF       FARG_UART1_Write_Text_uart_text+1 
	CALL        _UART1_Write_Text+0, 0
;mikrocTest.c,57 :: 		return;
	GOTO        L__interrupt9
;mikrocTest.c,58 :: 		}
L_interrupt3:
;mikrocTest.c,59 :: 		}
L_interrupt0:
;mikrocTest.c,60 :: 		j = SSPBUF;                              // read buffer to clear flag [address]
	MOVF        SSPBUF+0, 0 
	MOVWF       _j+0 
;mikrocTest.c,61 :: 		}
L_end_interrupt:
L__interrupt9:
	RETFIE      1
; end of _interrupt

_main:

;mikrocTest.c,63 :: 		void main(){
;mikrocTest.c,64 :: 		Init();
	CALL        _Init+0, 0
;mikrocTest.c,65 :: 		while(1){
L_main4:
;mikrocTest.c,66 :: 		Delay_ms(Delay_Time);
	MOVLW       3
	MOVWF       R11, 0
	MOVLW       138
	MOVWF       R12, 0
	MOVLW       85
	MOVWF       R13, 0
L_main6:
	DECFSZ      R13, 1, 1
	BRA         L_main6
	DECFSZ      R12, 1, 1
	BRA         L_main6
	DECFSZ      R11, 1, 1
	BRA         L_main6
	NOP
	NOP
;mikrocTest.c,67 :: 		}
	GOTO        L_main4
;mikrocTest.c,68 :: 		}
L_end_main:
	GOTO        $+0
; end of _main
