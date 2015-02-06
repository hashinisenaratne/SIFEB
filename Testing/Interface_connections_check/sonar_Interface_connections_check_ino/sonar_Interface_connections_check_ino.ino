/*
  Sonar module interface check
 */
#include <NewPing.h>

#define TRIGGER_PIN  12  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ECHO_PIN     11  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define MAX_DISTANCE 200 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

int led = 13;
int i2c1 = A4;
int i2c2 = A5;
int SelectIn = 7;
int SelectOut = 8;

// the setup routine runs once when you press reset:
void setup() {                
  pinMode(led, OUTPUT); 
  pinMode(i2c1, OUTPUT); 
  pinMode(i2c2, OUTPUT);   
  pinMode(SelectOut, OUTPUT);
  pinMode(SelectIn, INPUT);  
  Serial.begin(9600); 
}


void loop() {
  digitalWrite(led, HIGH);   // turn the LED on 
  delay(1000);               // wait for a second
  digitalWrite(led, LOW);    // turn the LED off 
  digitalWrite(i2c1, HIGH);   // turn the i2c1 on 
  delay(1000);               // wait for a second
  digitalWrite(i2c1, LOW);    // turn the i2c1 off 
  digitalWrite(i2c2, HIGH);   // turn the i2c2 on
  delay(1000);               // wait for a second
  digitalWrite(i2c2, LOW);    // turn the i2c2 off
  
  unsigned int uS = sonar.ping(); // Send ping, get ping time in microseconds (uS).
  Serial.print("Ping: ");
  Serial.print(uS / US_ROUNDTRIP_CM); // Convert ping time to distance in cm and print result (0 = outside set distance range)
  Serial.println("cm");
  
  Serial.print(digitalRead(SelectIn)); // get sensor reading and print
  Serial.println();
  
  digitalWrite(SelectOut, HIGH);   // turn the select out on
  delay(1000);               // wait for a second
  digitalWrite(SelectOut, LOW);    // turn the select out off
  
}
