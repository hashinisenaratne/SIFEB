// Sonar Module Code
// s - show, t - test, a - act, A - address, B - on selectout, C - off selectout
// mode 1- initial(ask is device there), 2 - send an address to set, 3 - send command to on (B)/off (C) select or request to act or test
// mode 3 - request byte (distance), a1 - on LED, a2 - off LED
#include <Wire.h>
#include <NewPing.h>

#define TRIGGER_PIN  12  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ECHO_PIN     11  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define MAX_DISTANCE 200 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.
#define LED  13    //change accordingly
#define SELECT_IN 7    //change accordingly
#define SELECT_OUT 8    //change accordingly

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

int mode = 1;  //initial mode
boolean ledshow=false;
byte type = '2';
byte address = 0;
byte distance;

void setup()
{
  pinMode(LED, OUTPUT);
  pinMode(SELECT_IN, INPUT);
  pinMode(SELECT_OUT, OUTPUT);
  
  Serial.begin(9600);
  
  Wire.begin(10);                  // unconfigured
  Wire.onReceive(receiveEvent);   // register events
  Wire.onRequest(requestEvent);
  Serial.println("address 10");   // TESTING
  
  while (digitalRead(SELECT_IN) == HIGH) {
    delay(10);    //change accordingly
    Serial.println("address 10"); // TESTING
  }
  
  //NEED TO TAKE CARE AT MASTER. WARNNING: I2C POWER UP MAY NOT FINISHED WHEN MASTER BIGIN COMMUNICATION
  Wire.begin(11);                  // selected
  Wire.onReceive(receiveEvent);   // register events
  Wire.onRequest(requestEvent);
  Serial.println("address 11"); // TESTING
  
}

void loop()
{
  delay(50);                      // Wait 50ms between pings (about 20 pings/sec). 29ms should be the shortest delay between pings.
  unsigned int uS = sonar.ping(); // Send ping, get ping time in microseconds (uS).
  distance = (byte)(uS / US_ROUNDTRIP_CM); // Convert ping time to distance in cm and print result (0 = outside set distance range)
    //Serial.print(distance); // TESTING
     // Serial.println("cm"); // TESTING
  
  if(ledshow == true){        //handling show command
    digitalWrite(LED, HIGH);
    delay(5000);               // wait for 5 seconds
    digitalWrite(LED, LOW);
    ledshow = false;
  }
  
  //Serial.println(address); // TESTING
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
      if(distance == 0)
      {
      distance = 200;
      }
    Wire.write(distance);  //sending distance 
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
    case 1:      
      digitalWrite(LED, HIGH);
      break;
    case 2:    
      digitalWrite(LED, LOW);
      break;
    default:
      break;
  }
}

// test
void test(int id){
    switch (id) {
    case 1: 
      break;
    case 2:
      break;
    default:
      break;
  }
}
