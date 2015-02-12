/*
  SD card basic file example
 
 This example shows how to create and destroy an SD card file   
 The circuit:
 * SD card attached to SPI bus as follows:
 ** MOSI - pin 11
 ** MISO - pin 12
 ** CLK - pin 13
 ** CS - pin 4
 
 created   Nov 2010
 by David A. Mellis
 modified 9 Apr 2012
 by Tom Igoe
 
 This example code is in the public domain.
 
 ***CANNOT USE LONG FILE NAMES
 */
#include <SD.h>
#define programFile "program.txt"

Sd2Card card;
const int chipSelect = 10;
int once = 0;\
int sizeByteArr = 1000;
byte bytearr[sizeByteArr];

void setup()
{
 // Open serial communications and wait for port to open:
  Serial.begin(9600);
   while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }


  Serial.print("Initializing SD card...");
  // On the Ethernet Shield, CS is pin 4. It's set as an output by default.
  // Note that even if it's not used as the CS pin, the hardware SS pin 
  // (10 on most Arduino boards, 53 on the Mega) must be left as an output 
  // or the SD library functions will not work. 
  pinMode(chipSelect, OUTPUT);

  if (!SD.begin(chipSelect)) {
    Serial.println("initialization failed!");
    return;
  }
  Serial.println("initialization done.");
}

void loop()
{
  if(once == 0){
  
    createFile();
    writeFile();
    readFile();
    removeFile();   
  
    once ++;
  }
}


void removeFile(){

  if (SD.exists(programFile)) {
    Serial.println("File exists.");
  }
  else {
    Serial.println("File doesn't exist.");
  }
 
  // delete the file:
  Serial.println("Removing File...");
  SD.remove(programFile);

  // Check to see if the file exists: 
  if (SD.exists(programFile)) {
    Serial.println("File exists.");
  }
  else {
    Serial.println("File doesn't exist.");  
  }
}


void writeFile(){
  File myFile = SD.open(programFile, FILE_WRITE);
  
  // if the file opened okay, write to it:
  if (myFile) {
    Serial.print("Writing to File...");
    myFile.println("testing 1, 2, 3.");
    myFile.println("testing 4 5 6.");
    myFile.println("testing 7 8 9.");
    // close the file:
    myFile.close();
    Serial.println("done.");
  } else {
    // if the file didn't open, print an error:
    Serial.println("error opening file");
  }
}

void readFile(){
  File myFile = SD.open(programFile);
  if (myFile) {
    Serial.println("reading:");
    
    // read from the file until there's nothing else in it:
    while (myFile.available()) {
        Serial.write(myFile.read());
    }
    // close the file:
    myFile.close();
  } else {
    // if the file didn't open, print an error:
    Serial.println("error opening file");
  }
}


void readFileToByteArray(){
  File myFile = SD.open(programFile);
  if (myFile) {
    Serial.println("reading:");
    int i = 0;
    // read from the file until there's nothing else in it:
    while (myFile.available()) {
        bytearr[i] = myFile.read();
        Serial.write(bytearr[i]);
        i++;
    }
    // close the file:
    myFile.close();
  } else {
    // if the file didn't open, print an error:
    Serial.println("error opening file");
  }
}

void writeByteArrayToFile(){
  File myFile = SD.open(programFile, FILE_WRITE);
  
  // if the file opened okay, write to it:
  if (!myFile) {
    Serial.println("error opening file");
    createFile();
  }
  Serial.print("Writing to File...");
  for(int i = 0; i < sizeByteArr; i++){
    myFile.println(bytearr[i]);
  }
  // close the file:
  myFile.close();
  Serial.println("done.");
}



void createFile(){
    
  if (SD.exists(programFile)) {
    Serial.println("File exists.");
  }
  else {
    Serial.println("File doesn't exist.");
  }

  // open a new file and immediately close it:
  Serial.println("Creating file...");
  File myFile = SD.open(programFile, FILE_WRITE);
  myFile.close();

  // Check to see if the file exists: 
  if (SD.exists(programFile)) {
    Serial.println("File exists.");
  }
  else {
    Serial.println("File doesn't exist.");  
  }
}

