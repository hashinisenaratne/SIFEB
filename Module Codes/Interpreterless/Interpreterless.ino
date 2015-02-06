#include <Wire.h>

#define BasicInstruction 'b'
#define ConditionalInstruction 'c'
#define JumpInstruction 'j'
#define EndInstruction 'e'
#define Equal '='
#define Inequal '!'
#define GreaterThanOrEqual '.'
#define Greater '>'
#define LesserThanOrEqual ','
#define Lesser '<'

char instructionRegister[20];//first byte contains the valid length
char responseRegister[10]; //first byte contains the valid length 
int instructionCounter = 0;

void setup() {
  Wire.begin();
  Serial.begin(9600);     // opens serial port, sets data rate to 9600 bps
}

void loop() {

  Serial.println("start");
  
  while (Serial.available() <= 0) continue;
  
  Serial.println("1");
  instructionRegister[0] = Serial.read();
  
  Serial.println("2");
  
  for(int i=1; i<instructionRegister[0]; i++)
  {
    while (Serial.available() <= 0) continue;
    instructionRegister[i] = Serial.read();
  }
  
  
  Serial.print("IR :");
for(int i=0; i<20; i++)
{
  Serial.print(instructionRegister[i]);
}
  Serial.println();
  Serial.print("RR :");
for(int i=0; i<20; i++)
{
  Serial.write(responseRegister[i]);
}
Serial.println();

}

void executeInstruction()
{
  switch(instructionRegister[1])
  {
  case BasicInstruction : 
    {
      executeI2CForBasic();
      instructionCounter++;
      break; 
    }
  case ConditionalInstruction : 
    {
      if(executeI2CForConditional())
      {
        instructionCounter++;
      }
      else
      {
        int* jump = (int*)&instructionRegister[4];
        instructionCounter += *jump;
      }
      break; 
    }
  case JumpInstruction : 
    {
      int* jump = (int*)&instructionRegister[2];
      instructionCounter += *jump;
      break; 
    }
  case EndInstruction : 
    {
      while(true)
      {
        continue;
      }
      break; 
    }
  } 
}

void executeI2CForBasic()
{
  
  Wire.beginTransmission(instructionRegister[2]);
  
  for(int i=3; i < instructionRegister[0]; i++)
  {
    Wire.write(instructionRegister[i]);
  }
  
  Wire.endTransmission();
}

boolean executeI2CForConditional()
{
  Wire.requestFrom(instructionRegister[2], instructionRegister[5]);
  int i=1;
  responseRegister[0] = instructionRegister[5];
  while(Wire.available())    // slave may send less than requested
  {     
    responseRegister[i] = Wire.read();       // receive a byte      
  }
  return evaluateComparison();
}

boolean evaluateComparison()
{
 switch (instructionRegister[3])
 {
  case Equal:
 {
  for(int i=responseRegister[0]; i>0; i--)
  {
    if(responseRegister[i] != instructionRegister[5+i])
    {
      return false;
    }
  }
  return true;
 }
 case Inequal:
 {
  for(int i=responseRegister[0]; i>0; i--)
  {
    if(responseRegister[i] == instructionRegister[5+i])
    {
      return false;
    }
  }
  return true;
 } 
 case GreaterThanOrEqual:
 {
  for(int i=responseRegister[0]; i>0; i--)
  {
    if(responseRegister[i] < instructionRegister[5+i])
    {
      return false;
    }
  }
  return true;
 } 
 case Greater:
 {
  for(int i=responseRegister[0]; i>0; i--)
  {
    if(responseRegister[i] <= instructionRegister[5+i])
    {
      return false;
    }
  }
  return true;
 }
case LesserThanOrEqual:
 {
  for(int i=responseRegister[0]; i>0; i--)
  {
    if(responseRegister[i] > instructionRegister[5+i])
    {
      return false;
    }
  }
  return true;
 }
 case Lesser:
 {
  for(int i=responseRegister[0]; i>0; i--)
  {
    if(responseRegister[i] >= instructionRegister[5+i])
    {
      return false;
    }
  }
  return true;
 }  
 }
}

/*
void sendI2C(String params)
{
  Wire.beginTransmission((int)params.charAt(1));
  for(int i=2; i<params.length();i++)
  {
    Wire.write(params.charAt(i));
  }
  Serial.print(Wire.endTransmission());    // stop transmitting
  Serial.println("#");
}

byte receiveI2C(String params)
{
  int c;
  Wire.requestFrom((int)params.charAt(1), 1);    // request 1 byte from slave device #11
  while(Wire.available())    // slave may send less than requested
  { 
    c = Wire.read();       // receive a byte
    Serial.print("h");
    Serial.println(c);         // print the character (PC needs to read)
    Serial.println("##");        
  }
}
*/

