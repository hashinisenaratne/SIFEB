// Line Following Module Code
// s - show, t - test, a - act, A - address, B - on selectout, C - off selectout
// mode 1- initial(ask is device there), 2 - send an address to set, 3 - send command to on (B)/off (C) select or request to act or test
// mode 3 - request byte (distance), a1 - on LED, a2 - off LED
#include <Wire.h>

/* Pin Definitions*/
#define LED  2    
#define SELECT_IN 4    
#define SELECT_OUT 3

//sensors from left to right
#define S0 11
#define S1 10
#define S2 9
#define S3 8
#define S4 A0
#define S5 A1
#define S6 A3
#define S7 A2



int mode = 1;  //initial mode
boolean ledshow=false;
byte type = '4';
byte address = 0;
byte deviation=0;
char tmpDeviation;

void setup()
{
  // set pins to input and output modes
  pinMode(LED, OUTPUT);
  pinMode(SELECT_IN, INPUT);
  pinMode(SELECT_OUT, OUTPUT);
  
  Serial.begin(9600);
  
  //begin i2c communication with default address
  Wire.begin(10);                  
  Wire.onReceive(receiveEvent);   // register events
  Wire.onRequest(requestEvent);
  //Serial.println("address 10");// TEST
    
  //waite unttil the slave is selected
  while (digitalRead(SELECT_IN) == HIGH) {
    delay(10);
    //Serial.println("address 10");// TEST
  }
  
  //when selected change the default address to another
  Wire.begin(11);                  
  Wire.onReceive(receiveEvent);   // register events
  Wire.onRequest(requestEvent);
  //Serial.println("address 11");// TEST
  
}

void loop()
{
  
  //handling show command - On LED for 3 seconds
  if(ledshow == true){       
    digitalWrite(LED, HIGH);
    delay(3000);               // wait for 3 seconds
    digitalWrite(LED, LOW);
    ledshow = false;
  }
  
  //Serial.println(address);//TEST
  
  tmpDeviation = 0;
  
  if(digitalRead(S0)){
    tmpDeviation += -8; 
  }
  if(digitalRead(S1)){
    tmpDeviation += -4;
  }
  if(digitalRead(S2)){
    tmpDeviation += -2;
  }
  if(digitalRead(S3)){
    tmpDeviation += -1;
  }
  if(digitalRead(S4)){
    tmpDeviation += 1;
  }
  if(digitalRead(S5)){
    tmpDeviation += 2;
  }
  if(digitalRead(S6)){
    tmpDeviation += 4;
  }
  if(digitalRead(S7)){
    tmpDeviation += 8;
  }
  
  deviation = tmpDeviation + 128;
  
  Serial.println((int)deviation);//testing
}

// function that executes whenever data is received from master
void receiveEvent(int howMany)
{
  //types of messages - s - show, B- On selectout, C- Off selectout, t1 - test 1, a1 - act 1  

  char command;
  int id;
  int add;
  
  //Address assigning
  if(mode == 2){
    add = Wire.read ();  
    address = add;  
    Wire.begin(add);                  // set new address
    Wire.onReceive(receiveEvent);   // register events
    Wire.onRequest(requestEvent);
    Serial.println(address);
    mode = 3;
  }
  
  //listening mode
  else if(mode == 3){
    
    command = Wire.read ();
    
    if(command == 'B'){
      digitalWrite(SELECT_OUT, HIGH); //On selectout
    }
    
    else if(command == 'C'){
      digitalWrite(SELECT_OUT, LOW); //Off selectout
    }
      
    //if got one byte
    else if (howMany == 1){
      if(command == 's'){     // show
           show();
      }
    }
      
    //if got two bytes
    else if (howMany == 2){
      //real time capability demonstration
      if(command == 't'){     // test
          id= Wire.read ();
          test(id);
      }
      //actions in programs
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
      Wire.write(type); //send type
      mode = 2;         //will receive an address soon      
      break;
    case 2:
      break;
    case 3:              //sending distance on request
      Wire.write(deviation);
      break;
    default:
      break;
  }
}

// show device
void show(){
    ledshow = true;
}

// actions
void act(int id){
    switch (id) {
    case '1':      
      digitalWrite(LED, HIGH);
      break;
    case '2':    
      digitalWrite(LED, LOW);
      break;
    default:
      break;
  }
}

// tests
void test(int id){
    switch (id) {
    case '1':      
      show();
      break;
    default:
      break;
  }
}
