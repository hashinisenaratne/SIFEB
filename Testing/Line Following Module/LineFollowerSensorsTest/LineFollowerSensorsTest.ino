/*
  LineFollower Module Test
 */

// define sensor pins
#define sensor1 A2
#define sensor2 A3
#define sensor3 A1
#define sensor4 A0
#define sensor5 8
#define sensor6 9
#define sensor7 10
#define sensor8 11

void setup() {
  pinMode(sensor1, INPUT);
  pinMode(sensor2, INPUT);
  pinMode(sensor3, INPUT);
  pinMode(sensor4, INPUT);
  pinMode(sensor5, INPUT);
  pinMode(sensor6, INPUT);
  pinMode(sensor7, INPUT);
  pinMode(sensor8, INPUT);
  Serial.begin(9600);
}

void loop() {
  Serial.print(digitalRead(sensor1));  
  Serial.print(digitalRead(sensor2));
  Serial.print(digitalRead(sensor3));
  Serial.print(digitalRead(sensor4));
  Serial.print(digitalRead(sensor5));
  Serial.print(digitalRead(sensor6));
  Serial.print(digitalRead(sensor7));
  Serial.println(digitalRead(sensor8));
  
  delay(1000);
}



