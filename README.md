# ArduCar
## Small robotic car with Arduino and Android controll.

<p align="justify"> ArduCar is an Arduino based four wheel drived robotic car. Its body has designed by me and the parts printed with a Creality Ender 5 3D printer. 
The Arduino is instructed over Bluetooth connection, any mobile phone or tablet - which running at least Android 11 - can run the controller GUI program.
The application is also built by my own, developed with Android Studio. The user can drive the car with buttons, or another - very intersting - way, utilizing 
the accelerometer built in the user's mobile device. Only have to tilt the device to the desired direction. I have built a little practice feature into it, so this 
control method should be activated with a button. Important restriction of this interface is the position of the device. If the orientation is bad the control panel 
is locked down and the car is stopped. In this situation it has to be reactivated in the correct direction. Unfortunately the motor driver H-Bridge IC doesn't 
support voltage adjusting by PWM signal. If it would capable for this, a further improvement can be done easily meaning that the speed would vary depending of the 
degree of tilt.</p>


> ## Main parts
  + Arduino Nano
  + HC-06 Bluetooth Modul
  + 3A Step-down DC/DC converter with LM2596 IC
  + 4 pcs of RM-17 motor (voltage range: 3-6V)
  + L9110S-M 2x H-Bridge motor driver modul
  + 4 pcs 18650 Li-ion battery cell (4S configuration)
