// Master Module Address Assignment template

#include <Wire.h>

#define DECODER_1 A0
#define DECODER_2 A1
#define DECODER_3 A2
#define DECODER_4 A3
#define DATA 12
#define CLOCK 13

// binary expansion to power up each line
byte decoder[16][4] = {
 {0, 0, 0, 0}, {0, 0, 0, 1}, {0, 0, 1, 0}, {0, 0, 1, 1}, 
 {0, 1, 0, 0}, {0, 1, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 1},  
 {1, 0, 0, 0}, {1, 0, 0, 1}, {1, 0, 1, 0}, {1, 0, 1, 1}, 
 {1, 1, 0, 0}, {1, 1, 0, 1}, {1, 1, 1, 0}, {1, 1, 1, 1}
};

// branch wise structuring {{slave1address,slave1type,UpdatePC}, {slave2address,slave2type,UpdatePC}, {slave3address,slave3type,UpdatePC}}
// UpdatePC = 0 (No update), UpdatePC = 1 (Connected), UpdatePC = else (Disconnected Address)
byte slaveStructure[15][3][3] = {
 {{0,0,0}, {0,0,0}, {0,0,0}}, {{0,0,0}, {0,0,0}, {0,0,0}}, {{0,0,0}, {0,0,0}, {0,0,0}}, {{0,0,0}, {0,0,0}, {0,0,0}}, 
 {{0,0,0}, {0,0,0}, {0,0,0}}, {{0,0,0}, {0,0,0}, {0,0,0}}, {{0,0,0}, {0,0,0}, {0,0,0}}, {{0,0,0}, {0,0,0}, {0,0,0}}, 
 {{0,0,0}, {0,0,0}, {0,0,0}}, {{0,0,0}, {0,0,0}, {0,0,0}}, {{0,0,0}, {0,0,0}, {0,0,0}}, {{0,0,0}, {0,0,0}, {0,0,0}}, 
 {{0,0,0}, {0,0,0}, {0,0,0}}, {{0,0,0}, {0,0,0}, {0,0,0}}, {{0,0,0}, {0,0,0}, {0,0,0}}
};

// {starting address, terminal rank - starting from 1}
// 7 slaves addresses for each branch
byte branchDetails[15][2] = {
  {8,0}, {15,0}, {22,0}, {29,0}, {36,0},
  {43,0}, {50,0}, {57,0}, {64,0}, {71,0},
  {78,0}, {85,0}, {92,0}, {99,0}, {106,0}
};

byte branches = 15;
byte slavesPerBranch = 3;
byte address_to_assign = 0;
boolean updatedPC = true;

void setup()
{
  pinMode(DECODER_1, OUTPUT);
  pinMode(DECODER_2, OUTPUT);
  pinMode(DECODER_3, OUTPUT);
  pinMode(DECODER_4, OUTPUT);
  
  Wire.begin();
  Serial.begin(9600);
  
  while(Serial.available() <= 0){
    continue;
  }
  updateAddressAllocation();
  printStructure();
}

void loop()
{
//  while(Serial.available() <= 0){
//    continue;
//  }
//  Serial.read();
  printStructure();
  updateAddressAllocation();
  if(!updatedPC){
    sendStructure();
  }
  delay(2000); 
  
}

/* Functions Related to Address Allocation */

// address allocation for the whole slave system
//void addressAllocation(){
//  
//  for (int i = 0; i < 15; i++) {
//    int address = branchDetails[i][0];
//    boolean hasSlave = false;
//    byte type = 0;
//    int j =0;                    // slave position along the path i
//    decode(i);                  // activate select line of first module of the branch
//    delay(50);                  //change accordingly
//    resetDecoder();             // set low - select pin
//    
//    Wire.beginTransmission(11);  // check if the module is present
//    if(Wire.endTransmission()==0){
//      hasSlave = true;
//    }
//    else{
//      hasSlave = false;
//      branchDetails[i][1] = 0;
//    }
//     
//    while(hasSlave){
//      Wire.requestFrom(11, 1);    // request type from slave
//      while(Wire.available())
//      { 
//        type = Wire.read();       // receive type
//      }
//      
//      Wire.beginTransmission(11); // send address to set
//      Wire.write(address); 
//      Wire.endTransmission(); 
//      
//      Wire.beginTransmission(address); // activate the select line of next module
//      Wire.write('B'); 
//      Wire.endTransmission();
//      
//      delay(50);                  //change accordingly      
//      
//      Wire.beginTransmission(address); // set low - select line
//      Wire.write('C'); 
//      Wire.endTransmission(); 
//      
//      Wire.beginTransmission(11);  // check if the module is present
//      if(Wire.endTransmission()==0){
//        hasSlave = true;
//      }
//      
//      updatedPC = false;
//      slaveStructure[i][j][0] = address;
//      slaveStructure[i][j][1] = type;
//      slaveStructure[i][j][2] = 1;
//      branchDetails[i][1] = j+1;
//      address ++;      
//      j++;
//    }
//  }
//  resetDecoder();
//}

//NO NEED TO RESET AS ADDRESSING IS DONE BRANCH WISE
// reset all the slaves in the system to default addresses
//void resetAllSlaves(){
//  for (int i = 0; i < 15; i++) {
//    for (int i = 0; i < 3; i++) {
//      if(slaveStructure[i][j][0] != 0){
//        Wire.beginTransmission(slaveStructure[i][j][0]);
//        Wire.write('R'); // command to reset to default address
//        Wire.endTransmission();
//        slaveStructure[i][j][0] = 0;
//        slaveStructure[i][j][1] = 0;
//      }
//    }
//  }
//}

// check whether there are new slaves added in run time
boolean checkForNewSlaves(){
  boolean newSlave = false;
  Wire.beginTransmission(11);  // check if a new module is present
  if(Wire.endTransmission()==0){
    newSlave = true;
  }
  return newSlave;
}

// update structure allong with address allocation to changed structure during run time
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
        slaveStructure[i][k][2] = slaveStructure[i][j][0];
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

/* Functions to Print Structure */
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

/* Send Address Structure updates to PC */
void sendStructure(){
  if(!updatedPC){
    for (int i = 0; i < branches; i++) {
      for (int j = 0; j < slavesPerBranch; j++) {
        if(slaveStructure[i][j][2] != 0){
          if(slaveStructure[i][j][2] == 1){  // a new module is connected            
            Serial.print('c');
            Serial.print(slaveStructure[i][j][0]);//address
            Serial.println(slaveStructure[i][j][1]);//type
          }
          else{    // a module has been disconnected            
            Serial.print('d');
            Serial.println(slaveStructure[i][j][2]);//address
            if(slaveStructure[i][j][0] != 0){  // also anew device has been connected
              Serial.print('c');
              Serial.print(slaveStructure[i][j][0]);//address
              Serial.println(slaveStructure[i][j][1]);//type
            }
          }
          slaveStructure[i][j][2] = 0;
        }
      }
  }
  updatedPC = true;
  }
  
//   if(slaveStructure[0][0][0] == 8){
//    Wire.beginTransmission(8); // set low - select line
//    Wire.write('s'); 
//    Wire.endTransmission(); 
//    
//    delay(10000);
//    
//    Wire.beginTransmission(8); // set low - select line
//    Wire.write('a1'); 
//    Wire.endTransmission(); 
//    
//    delay(3000);
//    
//    Wire.beginTransmission(8); // set low - select line
//    Wire.write('a2'); 
//    Wire.endTransmission();
//    
//    delay(100);
//    
//    Wire.requestFrom(8, 1);    // request type from slave
//    while(Wire.available())
//    { 
//      Serial.println(Wire.read());       // receive type
//    }
//    
//  }
}
