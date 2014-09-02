// Base Module
// s - show, t - test, a - act

#include <Wire.h>
#include <NewPing.h>

#define TRIGGER_PIN  12
#define ECHO_PIN     11
#define MAX_DISTANCE 200
 
NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

int led = 13;
int mode;
int cm;

void setup()
{
  Wire.begin(11);                  // join i2c bus with address #11
  Wire.onReceive(receiveEvent);   // register events
  Wire.onRequest(requestEvent);
  Serial.begin(9600);
  pinMode(led, OUTPUT);   
  Serial.println("start");
}

void loop()
{
  delay(100);
}

// function that executes whenever data is received from master
void receiveEvent(int howMany)
{
  //two types of messages - s and t1  

  char command;
  int action;
  Serial.println("read");
  //if got one byte
  if (howMany == 1){    
    command = Wire.read ();
    if(command == 's'){     
         show();
    }
    Serial.println("at 1");
  }   
    
  //if got two bytes
  if (howMany == 2){
    command = Wire.read ();
    if(command == 't' || command == 'a'){     
        action= Wire.read ();
        play(action);
    }
    Serial.println("at 2");
  }

  // throw away any garbage
  while (Wire.available () > 0) {
    Wire.read ();
  }
}

// show device
void show(){
    digitalWrite(led, HIGH);
    delay(5000);               // wait for 5 seconds
    digitalWrite(led, LOW);
}

// play action
void play(int action){
    switch (action) {
    case 1://cm
      mode = 1;
      cm = sonar.ping()/US_ROUNDTRIP_CM;
      break;
    case 2://go forward
      break;
    default: 
      // if nothing else matches, do the default
      break;
  }
}

void requestEvent()
{
  Serial.println("write");
  switch (mode) {
    case 1://cm
      Wire.write(cm);
      Serial.println("wrote");
      break;
    case 2://
      break;
    default: 
      // if nothing else matches, do the default
      break;
  }
}
