#include <Ultrasonic.h>
#include <WiFi.h>
#include <WiFiUdp.h>

WiFiUDP udp;
Ultrasonic ultrasonic(27, 14);

void setup() {
  Serial.begin(115200);

  WiFi.mode(WIFI_MODE_STA);

  Serial.println("Beginning wifi");
  WiFi.begin("yale wireless");
  Serial.println("Waiting for wifi connect");
  while (WiFi.status() != WL_CONNECTED) {
      delay(300);
  }
  Serial.println("Wifi connected");
}

void loop() {
  int val = ultrasonic.read();
  Serial.println(val);
  
  udp.beginPacket("10.66.69.213", 4445);
  udp.print(val);
  udp.endPacket();
  
  delay(200);
}

