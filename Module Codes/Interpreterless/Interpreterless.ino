#include <Wire.h>

/*decoder pin definition*/
#define DECODER_1 A0
#define DECODER_2 A1
#define DECODER_3 A2
#define DECODER_4 A3

/*instruction types*/
#define BasicInstruction 'b'
#define ConditionalInstruction 'c'
#define JumpInstruction 'j'
#define EndInstruction 'e'

/*comparison types*/
#define Equal '='
#define Inequal '!'
#define GreaterThanOrEqual '.'
#define Greater '>'
#define LesserThanOrEqual ','
#define Lesser '<'

// binary expansion to power up each line
byte decoder[16][4] = {
  {
    0, 0, 0, 0  }
  , {
    0, 0, 0, 1  }
  , {
    0, 0, 1, 0  }
  , {
    0, 0, 1, 1  }
  , 
  {
    0, 1, 0, 0  }
  , {
    0, 1, 0, 1  }
  , {
    0, 1, 1, 0  }
  , {
    0, 1, 1, 1  }
  ,  
  {
    1, 0, 0, 0  }
  , {
    1, 0, 0, 1  }
  , {
    1, 0, 1, 0  }
  , {
    1, 0, 1, 1  }
  , 
  {
    1, 1, 0, 0  }
  , {
    1, 1, 0, 1  }
  , {
    1, 1, 1, 0  }
  , {
    1, 1, 1, 1  }
};

// branch wise structuring {{slave1address,slave1type,UpdatePC}, {slave2address,slave2type,UpdatePC}, {slave3address,slave3type,UpdatePC}}
// UpdatePC = 0 (No update), UpdatePC = 1 (Connected), UpdatePC = else (Disconnected Address)
byte slaveStructure[15][3][3] = {
  {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , 
  {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , 
  {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , 
  {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
  , {
    {
      0,0,0    }
    , {
      0,0,0    }
    , {
      0,0,0    }
  }
};

// {starting address, terminal rank - starting from 1}
// 7 slaves addresses for each branch
byte branchDetails[15][2] = {
  {
    8,0  }
  , {
    15,0  }
  , {
    22,0  }
  , {
    29,0  }
  , {
    36,0  }
  ,
  {
    43,0  }
  , {
    50,0  }
  , {
    57,0  }
  , {
    64,0  }
  , {
    71,0  }
  ,
  {
    78,0  }
  , {
    85,0  }
  , {
    92,0  }
  , {
    99,0  }
  , {
    106,0  }
};

byte branches = 15;
byte slavesPerBranch = 3;
boolean updatedPC = true;

unsigned long lastSec=0;

char instructionRegister[20];//first byte contains the valid length
char responseRegister[10]; //first byte contains the valid length
char program[1000];
int programLength;
int instructionStartPositionCounter = 0;
boolean isProgrammeRunning=false;

void setup() {
  Wire.begin();
  Serial.begin(9600);     // opens serial port, sets data rate to 9600 bps

  pinMode(DECODER_1, OUTPUT);
  pinMode(DECODER_2, OUTPUT);
  pinMode(DECODER_3, OUTPUT);
  pinMode(DECODER_4, OUTPUT);

  resetDecoder();
  updateAddressAllocation();
  if(!updatedPC){
    sendStructureDiff();
  }

}

void loop() {

  if(millis()-lastSec >= 5000)
  {
    lastSec = millis();
    updateAddressAllocation();
    if(!updatedPC){
      sendStructureDiff();
    }
  }
  else if(millis()<lastSec)
  {
    lastSec = 0;
  }

  if(Serial.available())
  {
    char mode = Serial.read();

    switch(mode)
    {

    case 'd':
      {
        while (Serial.available() <= 0) continue;
        instructionRegister[0] = Serial.read();
        for( int i=1; i<instructionRegister[0]; i++)
        {
          while (Serial.available() <= 0) continue;
          instructionRegister[i] = Serial.read();
        }
        executeInstruction();
        break;
      }
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
        //Serial.println("programme running");//Test
        runProgramme();
        break;
      }
      
     case 's':
      {
        sendStructure();
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
  isProgrammeRunning = true;
  instructionStartPositionCounter = 0;
  while(true)
  {
    for(int i=0;i<program[instructionStartPositionCounter];i++)
    {
      instructionRegister[i] = program[instructionStartPositionCounter + i];
    }
    /*Serial.print("IR :");
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
    Serial.println();*/
    executeInstruction();
    if(!isProgrammeRunning)
    {
     break;
    }
    delay(10); //create a delay between I2C commands
  }
}


void executeInstruction()
{
  switch(instructionRegister[1])
  {
  case BasicInstruction : 
    {
      //Serial.println("Basic");//Test
      executeI2CForBasic();
      instructionStartPositionCounter+=instructionRegister[0];
      break; 
    }
  case ConditionalInstruction : 
    {
      //Serial.println("Conditional");//Test
      if(executeI2CForConditional())
      {
        //Serial.println("TRUE");//Test
        instructionStartPositionCounter+=instructionRegister[0];
      }
      else
      {
        //Serial.println("FALSE");//Test
        int* jump = (int*)&instructionRegister[4];
        instructionStartPositionCounter += *jump;
      }
      break; 
    }
  case JumpInstruction : 
    {
      //Serial.println("Jump");//Test
      int* jump = (int*)&instructionRegister[2];
      instructionStartPositionCounter += *jump;
      break; 
    }
  case EndInstruction : 
    {
      //Serial.println("End");//Test
      isProgrammeRunning = false;
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
  Wire.requestFrom(instructionRegister[2], instructionRegister[6]);
  int i=1;
  responseRegister[0] = instructionRegister[6];
  while(Wire.available())    // slave may send less than requested
  {
    responseRegister[i] = Wire.read();       // receive a byte
    i++;    
  }
  //Serial.println((int)responseRegister[1]);//test
  return evaluateComparison();
}

boolean evaluateComparison()
{
  switch (instructionRegister[3])
  {
  case Equal:
    {
      //Serial.println("Equal Check");//Test
      for(int i=responseRegister[0]; i>0; i--)
      {
        if(responseRegister[i] != instructionRegister[6+i])
        {
          return false;
        }
      }
      return true;
    }
  case Inequal:
    {
      //Serial.println("Inequal Check");//Test
      for(int i=responseRegister[0]; i>0; i--)
      {
        if(responseRegister[i] == instructionRegister[6+i])
        {
          return false;
        }
      }
      return true;
    } 
  case GreaterThanOrEqual:
    {
      //Serial.println(">= Check");//Test
      for(int i=responseRegister[0]; i>0; i--)
      {
        if((byte)responseRegister[i] < (byte)instructionRegister[6+i])
        {
          return false;
        }
      }
      return true;
    } 
  case Greater:
    {
      //Serial.println("> Check");//Test
      for(int i=responseRegister[0]; i>0; i--)
      {
        if((byte)responseRegister[i] <= (byte)instructionRegister[6+i])
        {
          return false;
        }
      }
      return true;
    }
  case LesserThanOrEqual:
    {
      //Serial.println("<= Check");//Test
      for(int i=responseRegister[0]; i>0; i--)
      {
        if((byte)responseRegister[i] > (byte)instructionRegister[6+i])
        {
          return false;
        }
      }
      return true;
    }
  case Lesser:
    {
      //Serial.println("< Check");//Test
      for(int i=responseRegister[0]; i>0; i--)
      {
        if((byte)responseRegister[i] >= (byte)instructionRegister[6+i])
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


/* Functions Related to Address Allocation */

// update structure along with address allocation to change structure during run time
void updateAddressAllocation(){

  for (int i = 0; i < branches; i++) {
    int address = branchDetails[i][0];
    boolean hasSlave = false;
    boolean newSlave = false;
    byte type = 0;
    int j =0;                    // slave position along the path i
    decode(i);                  // activate select line of first module of the branch
    delay(50);                  //change accordingly
    resetDecoder();             // set low - select pin  

    Wire.beginTransmission(11);  // check if a new module is present
    if(Wire.endTransmission()==0){
      hasSlave = true;
      newSlave = true;
    }

    if(!newSlave){
      Wire.beginTransmission(address);  // check if the module is present
      if(Wire.endTransmission()==0){
        hasSlave = true;
      }
    }

    if(!hasSlave){
      branchDetails[i][1] = 0;
    }

    while(hasSlave){

      if(newSlave){
        Wire.requestFrom(11, 1);    // request type from slave
        while(Wire.available())
        { 
          type = Wire.read();       // receive type
        }

        Wire.beginTransmission(11); // send address to set
        Wire.write(address); 
        Wire.endTransmission();

        updatedPC = false;

        if(slaveStructure[i][j][0] != 0){ // a module has been disconnected and placed another module there
          slaveStructure[i][j][2] = slaveStructure[i][j][0];
        }
        else{           
          slaveStructure[i][j][2] = 1;
        }
        slaveStructure[i][j][0] = address;
        slaveStructure[i][j][1] = type;
      }

      Wire.beginTransmission(address); // activate the select line of next module
      Wire.write('B'); 
      Wire.endTransmission();

      delay(50);                  //change accordingly      

      Wire.beginTransmission(address); // set low - select line
      Wire.write('C'); 
      Wire.endTransmission(); 

      newSlave = false;
      hasSlave = false;

      Wire.beginTransmission(11);  // check if a new module is present
      if(Wire.endTransmission()==0){
        hasSlave = true;
        newSlave = true;
      }

      if(!newSlave){
        Wire.beginTransmission(address + 1);  // check if the module is present
        if(Wire.endTransmission()==0){
          hasSlave = true;
        }
      }     

      branchDetails[i][1] = j+1;
      address ++;      
      j++;
    }

    for(int k=j; k<slavesPerBranch; k++){
      if(slaveStructure[i][k][0] != 0){
        updatedPC = false;
        slaveStructure[i][k][2] = slaveStructure[i][k][0];
      }
      else{           
        slaveStructure[i][k][2] = 0;
      }
      slaveStructure[i][k][0] = 0;
      slaveStructure[i][k][1] = 0;
    }
  }
  resetDecoder();
}


//Send Address Structure Diff updates to PC 
void sendStructureDiff(){
  if(!updatedPC){
    for (int i = 0; i < branches; i++) {
      for (int j = 0; j < slavesPerBranch; j++) {
        if(slaveStructure[i][j][2] != 0){
          if(slaveStructure[i][j][2] == 1){  // a new module is connected            
            Serial.print('c');
            Serial.print((char)slaveStructure[i][j][0]);//address
            Serial.println((char)slaveStructure[i][j][1]);//type
          }
          else{    // a module has been disconnected            
            Serial.print('d');
            Serial.print((char)slaveStructure[i][j][2]);//address
            Serial.println('#');
            if(slaveStructure[i][j][0] != 0){  // also anew device has been connected
              Serial.print('c');
              Serial.print((char)slaveStructure[i][j][0]);//address
              Serial.println((char)slaveStructure[i][j][1]);//type
            }
          }
          slaveStructure[i][j][2] = 0;
        }
      }
    }
    updatedPC = true;
    Serial.println("###");
  }  
}


//Send Address Structure updates to PC 
void sendStructure(){
    for (int i = 0; i < branches; i++) {
      for (int j = 0; j < slavesPerBranch; j++) {
        if(slaveStructure[i][j][0] != 0){  // a module is connected          
          Serial.print('c');
          Serial.print((char)slaveStructure[i][j][0]);//address
          Serial.println((char)slaveStructure[i][j][1]);//type
          slaveStructure[i][j][2] = 0;
        }
      }
    }
    updatedPC = true;
    Serial.println("###");
}

// check whether there are new slaves added in run time
boolean checkForNewSlaves(){
  boolean newSlave = false;
  Wire.beginTransmission(11);  // check if a new module is present
  if(Wire.endTransmission()==0){
    newSlave = true;
  }
  return newSlave;
}

/* Functions related to Decoder */

// select lines through decoder
void decode(int line)
{
  selectDecoderLines(decoder[line][3],decoder[line][2],decoder[line][1],decoder[line][0]);
}

// reset the decoder
void resetDecoder()
{
  selectDecoderLines(decoder[branches][3],decoder[branches][2],decoder[branches][1],decoder[branches][0]);
}

// select a line through decoder given the binary sequence
void selectDecoderLines(byte line1, byte line2, byte line3, byte line4){
  digitalWrite(DECODER_1, line1);
  digitalWrite(DECODER_2, line2); 
  digitalWrite(DECODER_3, line3);
  digitalWrite(DECODER_4, line4); 
}

/* Function to Print Structure */
void printStructure(){  
  for(int i=0;i<branches;i++){
    for(int j=0; j<slavesPerBranch; j++){      
      Serial.print(slaveStructure[i][j][0]);
      Serial.print(" - ");
      Serial.print(slaveStructure[i][j][1]);
      Serial.print(" , ");
    }
    Serial.print(" terminal ");
    Serial.println(branchDetails[i][1]);
  }
  Serial.println();
}



