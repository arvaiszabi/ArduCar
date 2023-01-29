#include <SoftwareSerial.h>
SoftwareSerial bluetooth(0,1); 

const int in_motLA = 5 ;
const int in_motLB = 6 ;
const int in_motRA = 9 ;
const int in_motRB = 10;
char inputC;

void setup()
{
  pinMode(in_motLA,OUTPUT);  
  pinMode(in_motLB,OUTPUT);
  pinMode(in_motRA,OUTPUT);
  pinMode(in_motRB,OUTPUT);
  bluetooth.begin(115200);
  Serial.begin(115200);
}

void loop()
{
  if (bluetooth.available() > 0) 
  {
    inputC = (bluetooth.read());
    //Serial.print(inputC);
    switch (inputC)
    {
      case 'F':
        forward();
        break;

      case 'B':
        backward();
        break;

      case 'L':
        turnleft();
        break;

      case 'R':
        turnright();
        break;

      case 'S':
        stopping();
        break;
    }
  }
}

void forward()
{
  digitalWrite(in_motLA,HIGH) ; //előre megy  
  digitalWrite(in_motRB,HIGH) ;
}

void backward()
{
  digitalWrite(in_motLB,HIGH) ;   //hátra megy
  digitalWrite(in_motRA,HIGH) ;
}

void turnleft()
{
  digitalWrite(in_motLB,HIGH) ; //balra fordul
  digitalWrite(in_motRB,HIGH) ;  
}

void turnright()
{
  digitalWrite(in_motLA,HIGH) ;   //jobbra fordul
  digitalWrite(in_motRA,HIGH) ;
}
void stopping()
{
  digitalWrite(in_motLA,LOW);
  digitalWrite(in_motRB,LOW);  
  digitalWrite(in_motLB,LOW);
  digitalWrite(in_motRA,LOW);
}
