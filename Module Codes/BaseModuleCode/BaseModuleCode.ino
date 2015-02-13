// Base Module Code
// s - show, t - test, a - act, A - address, B - on selectout, C - off selectout
// mode 1- initial(ask is device there), 2 - send an address to set, 3 - send command to on (B)/off (C) select or request to act or test
// a (act), t (test) : 1-forward, 2- reverse, 3-right, 4- left, 5- stop (no test to stop), 6-LED ON, 7-LED OFF
#include <Wire.h>

/* Pin Definitions*/
#define LED  15    
#define SELECT_IN A3    
#define SELECT_OUT A2    
#define MOTOR1_1 8
#define MOTOR1_2 9
#define MOTOR1_PWM 5
#define MOTOR2_1 10
#define MOTOR2_2 11
#define MOTOR2_PWM 6

int mode = 1;  //initial mode
boolean ledshow=false;
byte type = '1';
byte address = 0;
volatile boolean needToStop = false;

void setup()
{
  // set pins to input and output modes 
  pinMode(LED, OUTPUT);
  pinMode(SELECT_IN, INPUT);
  pinMode(SELECT_OUT, OUTPUT);  
  
  pinMode(MOTOR1_1,OUTPUT);
  pinMode(MOTOR1_2,OUTPUT);
  pinMode(MOTOR1_PWM,OUTPUT);
  pinMode(MOTOR2_1,OUTPUT);
  pinMode(MOTOR2_2,OUTPUT);
  pinMode(MOTOR2_PWM,OUTPUT);
  
  Serial.begin(9600);
  
  //begin i2c communication with default address
  Wire.begin(10);
  Wire.onReceive(receiveEvent);   // register events
  Wire.onRequest(requestEvent);
  //Serial.println("address 10");//TEST
  
  //giving some time to set up
  delay(5000);
  
  //waite unttil the slave is selected
  while (digitalRead(SELECT_IN) == HIGH) {
    delay(10);    
    //Serial.println("address 10");//TEST
  }
  
  //when selected change the default address to another
  Wire.begin(11);                  
  Wire.onReceive(receiveEvent);   // register events
  Wire.onRequest(requestEvent);
  //Serial.println("address 11");//TEST
  
}

void loop()
{
  delay(10);
  
  //handling show command - On LED for 3 seconds
  if(ledshow == true){        
    digitalWrite(LED, HIGH);
    delay(3000);             
    digitalWrite(LED, LOW);
    ledshow = false;
  }
  
  //handling stops after activating motors
  if(needToStop){
    delay(500);
    stopMotors();
    needToStop = false;
  }
  
  //Serial.println(address);//TEST
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
      if(command == 's'){     // show LED
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
  //Serial.println("write");//TEST
  switch (mode) {
    case 1: 
      Wire.write(type); //send type
      mode = 2;         //will receive an address soon      
      break;
    case 2:
      break;
    case 3:          //send whether ready to receive another message
    if(needToStop){
      Wire.write(0);
    }
    else{
      Wire.write(1);
    }
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
    case '1': forward();
      break;
    case '2': reverse();
      break;
    case '3': turnRight();
      needToStop = true;
      break;
    case '4':  turnLeft();
      needToStop = true;
      break;
    case '5':  stopMotors();
      break;
    case '6':
      digitalWrite(LED, HIGH);
      break;
    case '7':
      digitalWrite(LED, LOW);
      break;
    default:
      break;
  }
}

// tests
void test(int id){
    switch (id) {
    case '1': forward();
      needToStop = true;
      break;
    case '2': reverse();
      needToStop = true;
      break;
    case '3': turnRight();
      needToStop = true;
      break;
    case '4':  turnLeft();
      needToStop = true;
      break;
    case '5':  stopMotors();
      break;
    case '6':      
      show();
      break;
    default:
      break;
  }
}

// run the base forward
void forward(){
  digitalWrite(MOTOR1_PWM, HIGH);
  digitalWrite(MOTOR1_1, HIGH);    
  digitalWrite(MOTOR1_2, LOW);
  digitalWrite(MOTOR2_PWM, HIGH);
  digitalWrite(MOTOR2_1, HIGH);    
  digitalWrite(MOTOR2_2, LOW);
}

// run the base reverse
void reverse(){
  digitalWrite(MOTOR1_PWM, HIGH);
  digitalWrite(MOTOR1_1, LOW);    
  digitalWrite(MOTOR1_2, HIGH);
  digitalWrite(MOTOR2_PWM, HIGH);
  digitalWrite(MOTOR2_1, LOW);    
  digitalWrite(MOTOR2_2, HIGH);
}

// turn right the base
void turnRight(){
  digitalWrite(MOTOR1_PWM, HIGH);
  digitalWrite(MOTOR1_1, HIGH);    
  digitalWrite(MOTOR1_2, LOW);
  digitalWrite(MOTOR2_PWM, HIGH);
  digitalWrite(MOTOR2_1, LOW);    
  digitalWrite(MOTOR2_2, HIGH);
}

// turn left the base
void turnLeft(){
  digitalWrite(MOTOR1_PWM, HIGH);
  digitalWrite(MOTOR1_1, LOW);    
  digitalWrite(MOTOR1_2, HIGH);
  digitalWrite(MOTOR2_PWM, HIGH);
  digitalWrite(MOTOR2_1, HIGH);    
  digitalWrite(MOTOR2_2, LOW);
}

//fast stop
void stopMotors(){
  digitalWrite(MOTOR1_PWM, HIGH);
  digitalWrite(MOTOR1_1, HIGH);    
  digitalWrite(MOTOR1_2, HIGH);
  digitalWrite(MOTOR2_PWM, HIGH);
  digitalWrite(MOTOR2_1, HIGH);    
  digitalWrite(MOTOR2_2, HIGH);
}

