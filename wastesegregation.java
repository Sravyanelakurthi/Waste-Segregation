import com.fazecast.jSerialComm.SerialPort;
import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
public class SkynetRobotics {
private static final int TRIG_PIN = 12;
private static final int ECHO_PIN = 11;
private static final int POT_PIN = 0; // A0
private static final int MAX_DRY_VALUE = 20;
private static final int ULTRA_DISTANCE = 15;
private static long duration;
private static int distance = 0;
private static int soil = 0;
private static int fsoil;
private static LiquidCrystal lcd;
private static Servo servo1;
public static void main(String[] args) {
setup();
Timer timer = new Timer();
timer.schedule(new TimerTask() {
@Override
public void run() {
loop();
}
}, 0, 1000);
}
private static void setup() {
SerialPort serialPort = SerialPort.getCommPorts()[0];
serialPort.openPort();
lcd = new LiquidCrystal(0x27, 2, 1, 0, 4, 5, 6, 7, 3);
servo1 = new Servo(8);
System.out.println("Soil Sensor Ultrasonic Servo");
lcd.print("Skynet Robotics");
delay(1000);
}
private static void loop() {
lcd.clear();
lcd.setCursor(1, 0);
lcd.print("Dry Wet Waste");
lcd.setCursor(3, 1);
lcd.print("Segregator");
for (int i = 0; i < 2; i++) {
digitalWrite(TRIG_PIN, false);
delayMicroseconds(7);
digitalWrite(TRIG_PIN, true);
delayMicroseconds(10);
digitalWrite(TRIG_PIN, false);
delayMicroseconds(10);
duration = pulseIn(ECHO_PIN, true);
distance = (int) (duration * 0.034 / 2 + distance);
delay(10);
}
distance = distance / 2;
if (distance < ULTRA_DISTANCE && distance > 1) {
delay(1000);
for (int i = 0; i < 3; i++) {
soil = analogRead(POT_PIN);
soil = constrain(soil, 485, 1023);
fsoil = map(soil, 485, 1023, 100, 0) + fsoil;
delay(75);
}
fsoil = fsoil / 3;
System.out.print("Humidity: ");
System.out.print(fsoil);
System.out.print("%");
System.out.print(" Distance: ");
System.out.print(distance);
System.out.print(" cm");
if (fsoil > MAX_DRY_VALUE) {
delay(1000);
lcd.clear();
lcd.setCursor(0, 0);
lcd.print("Garbage Detected!");
lcd.setCursor(6, 1);
lcd.print("WET");
System.out.println(" ==>WET Waste ");
servo1.write(180);
delay(3000);
} else {
delay(1000);
lcd.clear();
lcd.setCursor(0, 0);
lcd.print("Garbage Detected!");
lcd.setCursor(6, 1);
lcd.print("DRY");
System.out.println(" ==>Dry Waste ");
servo1.write(0);
delay(3000);
}
servo1.write(90);
}
distance = 0;
fsoil = 0;
delay(1000);
}
private static void digitalWrite(int pin, boolean value) {
// Implement digital write logic
}
private static long pulseIn(int pin, boolean value) {
// Implement pulseIn logic
return 0;
}
private static int analogRead(int pin) {
// Implement analog read logic
return 0;
}
private static int constrain(int value, int min, int max) {
return Math.max(min, Math.min(value, max));
}
private static int map(int x, int in_min, int in_max, int out_min, int out_max) {
return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
}
private static void delay(long milliseconds) {
try {
Thread.sleep(milliseconds);
} catch (InterruptedException e) {
e.printStackTrace();
}
}
private static void delayMicroseconds(long microseconds) {
try {
Thread.sleep(microseconds / 1000);
} catch (InterruptedException e) {
e.printStackTrace();
}
}
