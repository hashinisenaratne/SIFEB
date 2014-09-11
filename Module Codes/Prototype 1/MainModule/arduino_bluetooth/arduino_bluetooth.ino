#include <Wire.h>
#include <SoftwareSerial.h>

SoftwareSerial mySerial(10, 11); // RX, TX

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

void setup() {
  Wire.begin();
  mySerial.begin(9600);
  pinMode(dPin, OUTPUT);
  pinMode(cPin, OUTPUT);
}

void loop() {
  
  // send data only when you receive data:
  if (mySerial.available() > 0) {
    // read the incoming byte:
    incomingByte = mySerial.read();

    // say what you got:
    //Serial.print("I received: ");
    //Serial.println(incomingByte, DEC);

    if(incomingByte == 'a'){  
      Wire.beginTransmission(10); // transmit to device #11
      Wire.write('s'); 
      mySerial.println(Wire.endTransmission());    // stop transmitting
    }

    else if(incomingByte == 'b'){
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('t'); 
      Wire.write(1); 
      mySerial.println(Wire.endTransmission());    // stop transmitting
    }

    else if(incomingByte == 'c'){
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('t'); 
      Wire.write(2); 
      mySerial.println(Wire.endTransmission());    // stop transmitting
    }
    
    else if(incomingByte == 'd'){
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('t'); 
      Wire.write(3); 
      mySerial.println(Wire.endTransmission());    // stop transmitting
    }
    
    else if(incomingByte == 'e'){
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('t'); 
      Wire.write(4); 
      mySerial.println(Wire.endTransmission());    // stop transmitting
    }
    
    else if(incomingByte == 'f'){
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('t'); 
      Wire.write(5); 
      mySerial.println(Wire.endTransmission());    // stop transmitting
    }
    
    if(incomingByte == 'g'){  
      Wire.beginTransmission(11); // transmit to device #11
      Wire.write('s'); 
      mySerial.println(Wire.endTransmission());    // stop transmitting
    }
    
    else if(incomingByte == 'h'){
      /*Wire.beginTransmission(11); // transmit to device #11
      Wire.write('t'); 
      Wire.write(1); 
      Serial.println(Wire.endTransmission());    // stop transmitting
      */
      Wire.requestFrom(11, 1);    // request 1 byte from slave device #11
      while(Wire.available())    // slave may send less than requested
      { 
        int c = Wire.read();       // receive a byte
        mySerial.println(c);         // print the character (PC needs to read)
      }
    }
    else if(incomingByte == 'z'){
      Wire.beginTransmission(10); // go foward
      Wire.write('a'); 
      Wire.write(1); 
      Wire.endTransmission();
    }
    
    else if(incomingByte == 'r'){
      
      delay(2000);
      
      Wire.beginTransmission(10); // go foward
      Wire.write('a'); 
      Wire.write(1); 
      Wire.endTransmission(); 
      mySerial.println("go");
      boolean close = false;
      while(!close){
        Wire.requestFrom(11, 1);    // request 6 bytes from slave device #11
          while(Wire.available())    // slave may send less than requested
          { 
            int cm = Wire.read();       // receive a byte
            if(cm < 100){
              close = true;
              mySerial.println("sensed");
            }
          }
          mySerial.println("hr");
      }
      
      delay(500);
      
      mySerial.println("beforestop");
      
  
      Wire.beginTransmission(10); // stop
      Wire.write('a'); 
      Wire.write(5); 
      mySerial.print("stop state");
      mySerial.println(Wire.endTransmission());
      
      Wire.beginTransmission(10); // turn right
      Wire.write('a'); 
      Wire.write(3); 
      Wire.endTransmission();
    }

    else if(incomingByte == '\r'){
      //skipping carriage return
    }

  }
}










