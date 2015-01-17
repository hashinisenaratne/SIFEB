/*
This code can be used on the main module to check the interfaces' 4 connections.
SCL should stay high and have low pulses.
SDA should stay low and have high pulses.
 */

void setup() {                
  //clear all select lines
  pinMode(A0, OUTPUT);
  pinMode(A1, OUTPUT);  
  pinMode(A2, OUTPUT);  
  pinMode(A3, OUTPUT); 

  digitalWrite(A0, HIGH);
  digitalWrite(A1, HIGH);
  digitalWrite(A2, HIGH);
  digitalWrite(A3, HIGH);
}


void loop() {              
 
  pinMode(A4, INPUT);  
  pinMode(A5, OUTPUT);
  digitalWrite(A5, LOW);
  
  delay(500);
  
  pinMode(A4, OUTPUT);
  digitalWrite(A4, LOW);
  pinMode(A5, INPUT);
  
  delay(2000);
}
