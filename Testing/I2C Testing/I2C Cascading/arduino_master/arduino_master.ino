#include <Wire.h>
/*
LIST OF COMMANDS
----------------
a - Send data to device 11 (address 0x16)
A - Request data from device 11 (address 0x16)
b - Send data to device 10 (address 0x14)
B - Request data from device 10 (address 0x14)

H - turn on pin 12 and 13 of arduino
L - turn off pin 12 and 13 of arduino

h - Ask device 11 to switch on I2C
l - Ask device 11 to switch off I2C

c - Ask device 11 to change address to 15 (address 0x1E)

n - Send data to device 15 (address 0x1E)
N - Request data from device 15 (address 0x1E)

*/

char incomingByte = 0;   // for incoming serial data
int dPin = 12;
int cPin = 13;

void setup() {
  Wire.begin();
  Serial.begin(9600);     // opens serial port, sets data rate to 9600 bps
  pinMode(dPin, OUTPUT);
  pinMode(cPin, OUTPUT);
}

void loop() {
  // send data only when you receive data:
  if (Serial.available() > 0) {
    // read the incoming byte:
    incomingByte = Serial.read();

    // say what you got:
    //Serial.print("I received: ");
    //Serial.println(incomingByte, DEC);

    if(incomingByte == 'a'){  
      Serial.println("Sending data to A...");
      Wire.beginTransmission(11); // transmit to device #11
      Wire.write('A'); 
      Wire.write('A'); 
      Wire.write('A'); 
      Serial.print("State: ");
      Serial.println(Wire.endTransmission());    // stop transmitting
    }

    else if(incomingByte == 'b'){
      Serial.println("Sending data to B...");
      Wire.beginTransmission(10); // transmit to device #10
      Wire.write('B'); 
      Wire.write('B'); 
      Wire.write('B');
      Serial.print("State: "); 
      Serial.println(Wire.endTransmission());    // stop transmitting
    }

    else if(incomingByte == 'H'){
      Serial.println("Switching on Arduino PINS(12&13)...");
      digitalWrite(dPin, HIGH);
      digitalWrite(cPin, HIGH);
      Serial.println("DONE");
    }

    else if(incomingByte == 'L'){
      Serial.println("Switching off Arduino PINS(12&13)...");
      digitalWrite(dPin, LOW);
      digitalWrite(cPin, LOW);
      Serial.println("DONE");
    }

    else if(incomingByte == 'h'){
      Serial.println("Asking A to switch on I2C...");
      Wire.beginTransmission(11); // transmit to device #11
      Wire.write('H'); 
      Serial.print("State: ");
      Serial.println(Wire.endTransmission());    // stop transmitting
    }

    else if(incomingByte == 'l'){
      Serial.println("Asking A to switch off I2C...");
      Wire.beginTransmission(11); // transmit to device #11
      Wire.write('L'); 
      Serial.print("State: ");
      Serial.println(Wire.endTransmission());    // stop transmitting
    }

    else if(incomingByte == 'A'){
      Serial.println("Requesting data from A...");
      Wire.requestFrom(11, 3);    // request 6 bytes from slave device #11
      while(Wire.available())    // slave may send less than requested
      { 
        char c = Wire.read(); // receive a byte as character
        Serial.print("readA- ");
        Serial.println(c);         // print the character
      }
    }

    else if(incomingByte == 'B'){
      Serial.println("Requesting data from B...");
      Wire.requestFrom(10, 3);    // request 3 bytes from slave device #10
      while(Wire.available())    // slave may send less than requested
      { 
        char c = Wire.read(); // receive a byte as character
        Serial.print("readB- ");
        Serial.println(c);         // print the character
      }
    }
    
    else if(incomingByte == 'c'){
      Serial.println("Asking A to change address...");
      Wire.beginTransmission(11); // transmit to device #11
      Wire.write('C'); 
      Wire.write((byte)0x1E);
      Serial.print("State: ");
      Serial.println(Wire.endTransmission());    // stop transmitting
    }
    
    else if(incomingByte == 'n'){  
      Serial.println("Sending data to new A...");
      Wire.beginTransmission(15); // transmit to device #15
      Wire.write('X'); 
      Wire.write('X'); 
      Wire.write('X'); 
      Serial.print("State: ");
      Serial.println(Wire.endTransmission());    // stop transmitting
    }
    
    else if(incomingByte == 'N'){
      Serial.println("Requesting data from new A...");
      Wire.requestFrom(15, 3);    // request 6 bytes from slave device #15
      while(Wire.available())    // slave may send less than requested
      { 
        char c = Wire.read(); // receive a byte as character
        Serial.print("readnewA- ");
        Serial.println(c);         // print the character
      }
    }

    else if(incomingByte == '\r'){
      //skipping carriage return
    }

    else if(incomingByte == 'a'){
      Serial.println("Unknown Command.");
    }
  }
}










