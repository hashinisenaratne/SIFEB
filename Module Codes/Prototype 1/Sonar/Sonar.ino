#include <Wire.h>
#include <NewPing.h>

#define TRIGGER_PIN  12  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ECHO_PIN     11  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define MAX_DISTANCE 200 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE); // NewPing setup of pins and maximum distance.
int led = 13;
byte distance;

void setup() {
  Wire.begin(11);                  // join i2c bus with address #11
  Wire.onReceive(receiveEvent); 
  Wire.onRequest(requestEvent);
  Serial.begin(9600); 
}

void loop() {
  delay(50);                      // Wait 50ms between pings (about 20 pings/sec). 29ms should be the shortest delay between pings.
  unsigned int uS = sonar.ping(); // Send ping, get ping time in microseconds (uS).
  distance = (byte)(uS / US_ROUNDTRIP_CM); // Convert ping time to distance in cm and print result (0 = outside set distance range)
  
}

void requestEvent()
{         
    if(distance == 0)
      distance = 200;
    Wire.write(distance);
    Serial.println(distance); 
}

void receiveEvent(int howMany)
{
  //two types of messages - s and t1  
  char command;
  int action;
  //if got one byte
  if (howMany == 1){    
    command = Wire.read ();
    if(command == 's'){     
         show();
    }
  }   
  /*  
  //if got two bytes
  if (howMany == 2){
    command = Wire.read ();
    if(command == 't' || command == 'a'){     
        action= Wire.read ();
        play(action);
    }
    Serial.println("at 2");
  }*/
}

// show device
void show(){
    digitalWrite(led, HIGH);
    delay(5000);               // wait for 5 seconds
    digitalWrite(led, LOW);
    Serial.println("Shown");
}
