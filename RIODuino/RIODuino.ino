/*
 * This program uses the third-party library NeoPixel.
 * You must download and install this library in order to compile and upload the code.
 * You can download it for free at http://github.com/adafruit/Adafruit_NeoPixel
 */

#include <Adafruit_NeoPixel.h>
#include <Wire.h>

#define LED_PIN 8
#define LED_COUNT 60

#define DEVICE_ADDRESS 1
#define STACK_HEIGHT_REGISTER 1
#define ALLIANCE_REGISTER 2

#define BLUE_ALLIANCE 0
#define RED_ALLIANCE 1
#define BLUE_COLOR 0x0000FF
#define RED_COLOR 0xFF0000
#define MAX_STACK_HEIGHT 5

Adafruit_NeoPixel controller = Adafruit_NeoPixel
(
  LED_COUNT,
  LED_PIN,
  NEO_GRB + NEO_KHZ800
);

byte height = 0;
byte alliance = 0;

void setup()
{
  Wire.begin(DEVICE_ADDRESS);
  controller.begin();
  clearAll();
  update();
}

void loop()
{
  if (Wire.available() >= 2) 
  {
    
  }
  
  update();
}

void hRequest()
{
  byte regId = Wire.read();
}

void hReceive(int count)
{
  if (count == 2) // Byte reading
  {  
    byte regId = Wire.read();
    byte data = Wire.read();
      
    if (regId == STACK_HEIGHT_REGISTER) 
    {
      height = data;
    }
    if (regId == ALLIANCE_REGISTER) 
    {
      alliance = data;
    }
  }
}

void setColor(int led, long color)
{
  controller.setPixelColor(led, color);
}

void setAll(long color)
{
  for (int i = 0; i < LED_COUNT; i++)
  {
    setColor(i, color);
  }
}

void clearColor(int led)
{
  setColor(led, 0);
}

void clearAll()
{
  setAll(0);
}

void update()
{
  int lightCount = (float) height / MAX_STACK_HEIGHT * LED_COUNT;
  long lightColor = alliance ? BLUE_COLOR : RED_COLOR;
  
  for (int i = 0; i < lightCount; i++)
  {
    setColor(i, lightColor);
  }
  
  controller.show();
}




