// Master Module Address Assignment template

#include <Wire.h>

#define DECODER_1 A0
#define DECODER_2 A1
#define DECODER_3 A2
#define DECODER_4 A3
#define DATA 12
#define CLOCK 13
int cPin = 13;
byte decoder[16][4] = {
 {0, 0, 0, 0}, {0, 0, 0, 1}, {0, 0, 1, 0}, {0, 0, 1, 1}, 
 {0, 1, 0, 0}, {0, 1, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 1},  
 {1, 0, 0, 0}, {1, 0, 0, 1}, {1, 0, 1, 0}, {1, 0, 1, 1}, 
 {1, 1, 0, 0}, {1, 1, 0, 1}, {1, 1, 1, 0}, {1, 1, 1, 1}
};

byte address_to_assign = 0;

void setup()
{
  pinMode(DECODER_1, OUTPUT);
  pinMode(DECODER_2, OUTPUT);
  pinMode(DECODER_3, OUTPUT);
  pinMode(DECODER_4, OUTPUT);
  
  Wire.begin();
}

void loop()
{

}

void addressing(){
  int address = 12;
  for (int i = 0; i < 16; i++) {
    boolean hasSlave = false;
    
    decode(i);                  // activate select line of first module of the branch
    delay(10);                  //change accordingly
    resetDecoder();             // set low - select pin
    
    Wire.requestFrom(11, 1);    // request 1 bytes from slave
    while(Wire.available())
    { 
      hasSlave = Wire.read();   // receive a byte
    }
     
    while(hasSlave){
      Wire.beginTransmission(11); // set device to address setting mode
      Wire.write('A'); 
      Wire.endTransmission();
      
      Wire.beginTransmission(11); // send address to set
      Wire.write(address); 
      Wire.endTransmission(); 
      
      Wire.beginTransmission(address); // activate the select line of next module
      Wire.write('B'); 
      Wire.endTransmission();
      
      delay(10);                  //change accordingly      
      
      Wire.beginTransmission(address); // set low - select line
      Wire.write('C'); 
      Wire.endTransmission(); 
      
      Wire.requestFrom(11, 1);    // check if the module is present
      while(Wire.available())
      { 
        hasSlave = Wire.read();   // receive a byte
      }
      
      address ++;      
    }
  }
}



// functions to select lines through decoder
void decode(int line)
{
  selectDecoderLines(decoder[line][3],decoder[line][2],decoder[line][1],decoder[line][0]);
}

void resetDecoder()
{
  selectDecoderLines(decoder[15][3],decoder[15][2],decoder[15][1],decoder[15][0]);
}

void selectDecoderLines(byte line1, byte line2, byte line3, byte line4){
  digitalWrite(DECODER_1, line1);
  digitalWrite(DECODER_2, line2); 
  digitalWrite(DECODER_3, line3);
  digitalWrite(DECODER_4, line4); 
}



