/*
This code is to test the lcd connected to the main module cct as at 17.01.2015
 
  The circuit:
 * LCD RS pin to digital pin 9
 * LCD Enable pin to digital pin 8
 * LCD D4 pin to digital pin 5
 * LCD D5 pin to digital pin 4
 * LCD D6 pin to digital pin 3
 * LCD D7 pin to digital pin 2
 * LCD back light pin to digital pin 6
 * LCD R/W pin to ground
 */
 
#define LCD_REGISTERSELECT 9
#define LCD_ENABLE 8
#define LCD_DATA4 5
#define LCD_DATA5 4
#define LCD_DATA6 3
#define LCD_DATA7 2
#define LCD_BACKLIGHT 6

// include the library code:
#include <LiquidCrystal.h>

// initialize the library with the numbers of the interface pins
LiquidCrystal lcd(LCD_REGISTERSELECT, LCD_ENABLE, LCD_DATA4, LCD_DATA5, LCD_DATA6, LCD_DATA7);
byte lcd_error = 0;

void setup() {
  pinMode(LCD_BACKLIGHT,OUTPUT);
  digitalWrite(LCD_BACKLIGHT,HIGH);
  // set up the LCD's number of columns and rows: 
  lcd.begin(16, 2);
  // Print a message to the LCD.
  lcdMessage(0,"     SiFEB     ",0);
}

void loop() {
  lcdMessage(1,"Program Running",0);
  delay(10000);
  
  lcdMessage(1,"     ERROR     ",1);  
  
  // print the number of seconds since reset:
  //lcd.print(millis()/1000);
}

void lcdMessage(int row, char message[], byte error){
  // set the cursor to column 0, line 1
  // (note: line 1 is the second row, since counting begins with 0):
  lcd.setCursor(0, row);
  digitalWrite(LCD_BACKLIGHT,HIGH);
  lcd.print(message);
  if(error == 1){
    while (true) {
      digitalWrite(LCD_BACKLIGHT,HIGH);
      delay(500);
      digitalWrite(LCD_BACKLIGHT,LOW);
      delay(500);
    }
  }
}

