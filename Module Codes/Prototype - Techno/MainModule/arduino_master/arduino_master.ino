#include <Wire.h>
#include <TimerOne.h>

/*

 LIST OF COMMANDS
 ----------------
 a - show
 b - forward
 c - reverse
 d - right
 e - left
 f - stop
 
 */

char incomingByte = 0;   // for incoming serial data
int dPin = 12;
int cPin = 13;


unsigned long lastSec=0;

//auto detection support variables
//edit according to the devices
int addresses[] = {
  10, 11};
boolean lastState[] = {
  false, false};

void setup() {
  Wire.begin();
  Serial.begin(9600);     // opens serial port, sets data rate to 9600 bps
  pinMode(dPin, OUTPUT);
  pinMode(cPin, OUTPUT);

}

void notifyAllChanges(void)
{
  //Serial.println("Checking...");
  boolean isChanged=false;
  for(int i=0; i< sizeof(addresses);i++)
  {
    boolean tmp =  notifySingleChange(i);
    if(tmp)
    {
      isChanged=true;
    }
  }

  if(isChanged)
  {
    Serial.println("##");
  }
}

//print two bytes, c or d & address as a byte followed by a new line.
boolean notifySingleChange(int addressIndex)
{
  Wire.beginTransmission(addresses[addressIndex]);
  if(Wire.endTransmission()==0)
  {
    if(!lastState[addressIndex])
    {
      Serial.print('c');
      lastState[addressIndex] = true;
      Serial.println((char)addresses[addressIndex]);
      return true;
    }
  }
  else
  {
    if(lastState[addressIndex])
    {
      lastState[addressIndex] = false;
      Serial.print('d');
      Serial.println((char)addresses[addressIndex]);
      return true;
    }
  }
  return false;
}


void loop() {


  if(millis()-lastSec >= 5000)
  {
    lastSec = millis();
    notifyAllChanges();
   // Serial.println("12");
  }

  // send data only when you receive data:
  if (Serial.available() > 0) {
    // read the incoming byte:
    incomingByte = Serial.read();

    // say what you got:
    //Serial.print("I received: ");
    //Serial.println(incomingByte, DEC);

    if(incomingByte == 'z'){  
      notifyAllChanges();
    }

    if(incomingByte == 'a'){  
      Wire.beginTransmission(10); // transmit to device #11
      Wire.write('s'); 
      Serial.print(Wire.endTransmission());    // stop transmitting
      Serial.println("#");
    }

    else if(incomingByte == 'b'){
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('t'); 
      Wire.write(1); 
      Serial.print(Wire.endTransmission());    // stop transmitting
      Serial.println("#");
    }

    else if(incomingByte == 'c'){
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('t'); 
      Wire.write(2); 
      Serial.print(Wire.endTransmission());    // stop transmitting
      Serial.println("#");
    }
    
    else if(incomingByte == 'B'){
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('a'); 
      Wire.write(1); 
      Serial.print(Wire.endTransmission());    // stop transmitting
      Serial.println("#");
    }

    else if(incomingByte == 'C'){
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('a'); 
      Wire.write(2); 
      Serial.print(Wire.endTransmission());    // stop transmitting
      Serial.println("#");
    }

    else if(incomingByte == 'd'){
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('t'); 
      Wire.write(3); 
      Serial.print(Wire.endTransmission());    // stop transmitting
      Serial.println("#");
    }

    else if(incomingByte == 'e'){
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('t'); 
      Wire.write(4); 
      Serial.print(Wire.endTransmission());    // stop transmitting
      Serial.println("#");
    }

    else if(incomingByte == 'f'){
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('t'); 
      Wire.write(5); 
      Serial.print(Wire.endTransmission());    // stop transmitting
      Serial.println("#");
    }

    if(incomingByte == 'g'){  
      Wire.beginTransmission(11); // transmit to device #11
      Wire.write('s'); 
      Serial.print(Wire.endTransmission());    // stop transmitting
      Serial.println("#");
    }

    else if(incomingByte == 'h'){
      /*Wire.beginTransmission(11); // transmit to device #11
       Wire.write('t'); 
       Wire.write(1); 
       Serial.print(Wire.endTransmission());    // stop transmitting
       Serial.println("#");
       */
      Wire.requestFrom(11, 1);    // request 1 byte from slave device #11
      while(Wire.available())    // slave may send less than requested
      { 
        int c = Wire.read();       // receive a byte
        Serial.println(c);         // print the character (PC needs to read)
      }
    }

    else if(incomingByte == 'r'){


      delay(2000);

      Wire.beginTransmission(10); // go foward
      Wire.write('a'); 
      Wire.write(1); 
      Wire.endTransmission(); 

      boolean close = false;
      while(!close){
        Wire.requestFrom(11, 1);    // request 6 bytes from slave device #11
        while(Wire.available())    // slave may send less than requested
        { 
          int cm = Wire.read();       // receive a byte
          if(cm < 100){
            close = true;

          }
        }

      }

      delay(500);




      Wire.beginTransmission(10); // stop
      Wire.write('a'); 
      Wire.write(5); 

      Wire.endTransmission();

      delay(500);

      Wire.beginTransmission(10); // turn right
      Wire.write('a'); 
      Wire.write(3); 
      Wire.endTransmission();

      delay(500);

      // Serial.println("15");

    }

    else if(incomingByte == '\r'){
      //skipping carriage return
    }

  }
}















