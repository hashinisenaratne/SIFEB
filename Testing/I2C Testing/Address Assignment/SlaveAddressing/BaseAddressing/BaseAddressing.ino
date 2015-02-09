// Slave addressing template
// s - show, t - test, a - act, A - address, B - on selectout, C - off selectout
// mode 1- initial(ask is device there), 2 - send an address to set, 3 - send command to on (B)/off (C) select or request to act or test

#include <Wire.h>

#define LED  15    //change accordingly
#define SELECT_IN A3    //change accordingly
#define SELECT_OUT A2    //change accordingly

int mode = 1;  //initial mode
boolean ledshow=false;
byte type = 1;
byte address = 0;

void setup()
{
  pinMode(LED, OUTPUT);
  pinMode(SELECT_IN, INPUT);
  pinMode(SELECT_OUT, OUTPUT);
  
  Serial.begin(9600);
  
  Wire.begin(10);                  // unconfigured
  Wire.onReceive(receiveEvent);   // register events
  Wire.onRequest(requestEvent);
  Serial.println("address 10");
  
  delay(5000);
  
  while (digitalRead(SELECT_IN) == HIGH) {
    delay(10);    //change accordingly
    Serial.println("address 10");
  }
  
  //NEED TO TAKE CARE AT MASTER. WARNNING: I2C POWER UP MAY NOT FINISHED WHEN MASTER BIGIN COMMUNICATION
  Wire.begin(11);                  // selected
  Wire.onReceive(receiveEvent);   // register events
  Wire.onRequest(requestEvent);
  Serial.println("address 11");
  
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
  
  Serial.println(address);
}

// function that executes whenever data is received from master
void receiveEvent(int howMany)
{
  //types of messages - s - show, A - address assign, t1 - test 1, a1 - act 1  

  char command;
  int id;
  int add;
  
  if(mode == 2){
    add = Wire.read ();  
    address = add;  
    Wire.begin(add);                  // set new address
    Wire.onReceive(receiveEvent);   // register events
    Wire.onRequest(requestEvent);
    Serial.println(address);
    mode = 3;
  }
  
  else if(mode == 3){
    
    command = Wire.read ();
    
    if(command == 'B'){
      digitalWrite(SELECT_OUT, HIGH); //On selectout
    }
    
    else if(command == 'C'){
      digitalWrite(SELECT_OUT, LOW); //Off selectout
    }
    
    //NO NEED TO RESET AS ADDRESSING IS DONE BRANCH WISE
//    else if(command == 'R'){          // Reset in order to do addressing ??? NOT RESETING CORRECTLY
//      Wire.begin(10);                  // unconfigured
//      Wire.onReceive(receiveEvent);   // register events
//      Wire.onRequest(requestEvent);  
//      digitalWrite(SELECT_OUT, LOW); //Off selectout
//    }
      
    //if got one byte
    else if (howMany == 1){
      if(command == 's'){     // show
           show();
      }
    }
      
    //if got two bytes
    else if (howMany == 2){
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
    case 1: 
      Wire.write(type); 
      mode = 2; //will receive an address soon      
      break;
    case 2:
      break;
    case 3:  
      Wire.write(address);  // tempory
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


