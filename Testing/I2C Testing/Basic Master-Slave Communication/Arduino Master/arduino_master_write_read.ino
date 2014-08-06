#include <Wire.h>

void setup()
{
  Wire.begin();        // join i2c bus (address optional for master)
  Serial.begin(9600);  // start serial for output
}

void loop()
{
 
    Wire.beginTransmission(10); // transmit to device #10
    Wire.write('A'); 
    Wire.write('A'); 
    Wire.write('A'); 
    Serial.print(Wire.endTransmission());    // stop transmitting
  
    delay(1000);
    
    Wire.beginTransmission(11); // transmit to device #11
    Wire.write('B'); 
    Wire.write('B'); 
    Wire.write('B'); 
    Serial.print(Wire.endTransmission());    // stop transmitting
  
  
  Wire.requestFrom(10, 3);    // request 3 bytes from slave device #10
  while(Wire.available())    // slave may send less than requested
  { 
    char c = Wire.read(); // receive a byte as character
    Serial.print("read1- ");
    Serial.println(c);         // print the character
  }
  
  Wire.requestFrom(11, 3);    // request 6 bytes from slave device #11
  while(Wire.available())    // slave may send less than requested
  { 
    char c = Wire.read(); // receive a byte as character
    Serial.print("read2- ");
    Serial.println(c);         // print the character
  }
}
