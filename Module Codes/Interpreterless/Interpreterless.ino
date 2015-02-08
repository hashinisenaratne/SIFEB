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
char program[1000];
int programLength;
int instructionStartPositionCounter = 0;

void setup() {
  Wire.begin();
  Serial.begin(9600);     // opens serial port, sets data rate to 9600 bps




}

void loop() {

  if(Serial.available())
  {
    char mode = Serial.read();

    switch(mode)
    {
    case 'u':
      {
        receiveAndStoreProgram();
        for( int i=0; i<programLength; i++)
        {
          Serial.print(program[i]);
        }
        break;
      }
      
      case 'r':
      {
        Serial.println("programme running");
        runProgramme();
        break;
      }
    } 
  }
  /*
  Serial.println("start");
   
   responseRegister[0] = 1;
   responseRegister[1] = 65;
   
   while (Serial.available() <= 0) continue;
   instructionRegister[0] = Serial.read();
   
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
   

  executeInstruction();*/
}


void receiveAndStoreProgram()
{
  while (Serial.available() <= 0) continue;
  programLength = Serial.read();
  for( int i=0; i<programLength; i++)
  {
    while (Serial.available() <= 0) continue;
    program[i] = Serial.read();
  }
}

void runProgramme()
{
  instructionStartPositionCounter = 0;
  while(true)
  {
    for(int i=0;i<program[instructionStartPositionCounter];i++)
    {
      instructionRegister[i] = program[instructionStartPositionCounter + i];
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
    executeInstruction();
  }
}


void executeInstruction()
{
  switch(instructionRegister[1])
  {
  case BasicInstruction : 
    {
      Serial.println("Basic");
      executeI2CForBasic();
      instructionStartPositionCounter+=instructionRegister[0];
      break; 
    }
  case ConditionalInstruction : 
    {
      Serial.println("Conditional");
      if(executeI2CForConditional())
      {
        Serial.println("TRUE");
        instructionStartPositionCounter+=instructionRegister[0];
      }
      else
      {
        Serial.println("FALSE");
        int* jump = (int*)&instructionRegister[4];
        instructionStartPositionCounter += *jump;
      }
      break; 
    }
  case JumpInstruction : 
    {
      Serial.println("Jump");
      int* jump = (int*)&instructionRegister[2];
      instructionStartPositionCounter += *jump;
      break; 
    }
  case EndInstruction : 
    {
      Serial.println("End");
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
    i++;    
  }
  return evaluateComparison();
}

boolean evaluateComparison()
{
  switch (instructionRegister[3])
  {
  case Equal:
    {
      Serial.println("Equal Check");
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
      Serial.println("Inequal Check");
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
      Serial.println(">= Check");
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
      Serial.println("> Check");
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
      Serial.println("<= Check");
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
      Serial.println("< Check");
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



