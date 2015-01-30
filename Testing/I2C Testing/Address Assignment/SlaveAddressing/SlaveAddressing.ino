// Slave addressing template
// s - show, t - test, a - act, A - address, B - on selectout, C - off selectout
// mode 1- initial(ask is device there), 2 - send an address to set, 3 - send command to on (B)/off (C) select or request to act or test

#include <Wire.h>

#define LED  10    //change accordingly
#define SELECT_IN 11    //change accordingly
#define SELECT_OUT 12    //change accordingly

int mode = 1;  //initial mode
boolean ledshow=false;

void setup()
{
  pinMode(LED, OUTPUT);
  pinMode(SELECT_IN, INPUT);
  pinMode(SELECT_OUT, OUTPUT);
  
  Wire.begin(10);                  // unconfigured
  Wire.onReceive(receiveEvent);   // register events
  Wire.onRequest(requestEvent);
  
  while (digitalRead(SELECT_IN) == LOW) {
    delay(10);    //change accordingly
  }
  //NEED TO TAKE CARE AT MASTER. WARNNING: I2C POWER UP MAY NOT FINISHED WHEN MASTER BIGIN COMMUNICATION
  Wire.begin(11);                  // selected
  Wire.onReceive(receiveEvent);   // register events
  Wire.onRequest(requestEvent);
  Serial.begin(9600);
}

void loop()
{
  delay(10);    //change accordingly
  
  if(ledshow == true){        //handling show command
    digitalWrite(LED, HIGH);
    delay(5000);               // wait for 5 seconds
    digitalWrite(LED, LOW);
    ledshow = false;
  }
}

// function that executes whenever data is received from master
void receiveEvent(int howMany)
{
  //types of messages - s - show, A - address assign, t1 - test 1, a1 - act 1  

  char command;
  int id;
  int add;
  
  if(mode == 1){    
    if(command == 'A'){
      mode = 2; //will receive an address soon
    }
  }
  
  else if(mode == 2){
    add = Wire.read ();    
    Wire.begin(add);                  // set new address
    Wire.onReceive(receiveEvent);   // register events
    Wire.onRequest(requestEvent);
    mode = 3;
  }
  
  else if(mode == 3){
    
    if(command == 'B'){
      digitalWrite(SELECT_OUT, HIGH); //On selectout
    }
    
    else if(command == 'C'){
      digitalWrite(SELECT_OUT, LOW); //Off selectout
    }
    
    else if(command == 'R'){          // Reset in order to do addressing 
      Wire.begin(10);                  // unconfigured
      Wire.onReceive(receiveEvent);   // register events
      Wire.onRequest(requestEvent);  
      digitalWrite(SELECT_OUT, LOW); //Off selectout
    }
      
    //if got one byte
    else if (howMany == 1){    
      command = Wire.read ();
      if(command == 's'){     // show
           show();
      }
    }
      
    //if got two bytes
    else if (howMany == 2){
      command = Wire.read ();
      if(command == 't'){     // test
          id= Wire.read ();
          test(id);
      }
      else if(command == 'a'){  // act
          id= Wire.read ();
          act(id);
      }
    }
  }
  

  // throw away any garbage
  while (Wire.available () > 0) {
    Wire.read ();
  }
}

// function that executes whenever data is requested by master
void requestEvent()
{
  Serial.println("write");
  switch (mode) {
    case 1://asks whether the module is present
      Wire.write(1);      
      break;
    case 2://
      break;
    default:
      break;
  }
}

// show device
void show(){
    ledshow = true;
}

// action
void act(int id){
    switch (id) {
    case 1://
      break;
    case 2://
      break;
    default:
      break;
  }
}

// test
void test(int id){
    switch (id) {
    case 1://
      break;
    case 2://
      break;
    default:
      break;
  }
}


