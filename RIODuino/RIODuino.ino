/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 *
 * This program uses the third-party library NeoPixel.
 * You must download and install this library in order to compile and upload the code.
 * You can download it for free at http://github.com/adafruit/Adafruit_NeoPixel
 */

#include <Adafruit_NeoPixel.h>

#define LED_PIN 8
#define LED_COUNT 60

#define MAX_STACK_HEIGHT 5

Adafruit_NeoPixel controller = Adafruit_NeoPixel
(
  LED_COUNT,
  LED_PIN,
  NEO_GRB + NEO_KHZ800
);

long color = 0x000000;
int lightCount = 0;

void setup()
{
  Serial.begin(9600);
  controller.begin();
  clearAll();
  update();
}

void loop()
{
  if (Serial.available())
  {
    byte data = Serial.read();
    
    byte colorData = (byte) (data & 0xF);
    
    color = 0x000000;
    if (colorData & 0b100)
    {
      color += 0xFF0000;
    }
    if (colorData & 0b010)
    {
      color += 0x00FF00;
    }
    if (colorData & 0b001)
    {
      color += 0x0000FF;
    }
    
    byte height = (byte) (data >> 4);
    
    lightCount = (float) height / MAX_STACK_HEIGHT * LED_COUNT;
  
    setAll(color);
    update();
  }
}

void setColor(int led, long color)
{
  controller.setPixelColor(led, color);
}

void setAll(long color)
{
  for (int i = 0; i < lightCount; i++)
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
  controller.show();
}
