#include <Wire.h>

#define DECODER_1 A0
#define DECODER_2 A1
#define DECODER_3 A2
#define DECODER_4 A3
byte decoder[16][4] = {
 {0, 0, 0, 0}, {0, 0, 0, 1}, {0, 0, 1, 0}, {0, 0, 1, 1}, 
 {0, 1, 0, 0}, {0, 1, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 1},  
 {1, 0, 0, 0}, {1, 0, 0, 1}, {1, 0, 1, 0}, {1, 0, 1, 1}, 
 {1, 1, 0, 0}, {1, 1, 0, 1}, {1, 1, 1, 0}, {1, 1, 1, 1}
};

void setup() {
  pinMode(DECODER_1, OUTPUT);
  pinMode(DECODER_2, OUTPUT);
  pinMode(DECODER_3, OUTPUT);
  pinMode(DECODER_4, OUTPUT);
}


void loop() {
  for (int i = 0; i < 16; i++) {
    decode(i);
    delay(2000);    //change accordingly
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

void selectDecoderLines(int line1, int line2, int line3, int line4){
  digitalWrite(DECODER_1, line1);
  digitalWrite(DECODER_2, line2); 
  digitalWrite(DECODER_3, line3);
  digitalWrite(DECODER_4, line4); 
}















