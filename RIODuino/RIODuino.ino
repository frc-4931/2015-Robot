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

#define UNKNOWN_ALLIANCE 0
#define BLUE_ALLIANCE 1
#define RED_ALLIANCE 2
#define MAX_STACK_HEIGHT 5
#define UNKNOWN_COLOR 0x00FF00
#define BLUE_COLOR 0x0000FF
#define RED_COLOR 0xFF0000

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
    alliance = (byte) (data & 0xF);
    height = (byte) (data >> 4);
  }
  
  int lightCount = (float) height / MAX_STACK_HEIGHT * LED_COUNT;
  long lightColor = UNKNOWN_COLOR;
  if (alliance == BLUE_ALLIANCE)
  {
    lightColor = BLUE_COLOR;
  }
  else if (alliance == RED_ALLIANCE)
  {
    lightColor = RED_COLOR;
  }
  
  for (int i = 0; i < lightCount; i++)
  {
    setColor(i, lightColor);
  }
  update();
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
  controller.show();
}
