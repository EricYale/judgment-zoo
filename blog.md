# Module 3: Judgment Zoo

Will you be naughty or nice? Put your money where your morals are and decide the fate of a zoo animal with your wallet.


[**Video demo**](./gallery/demo.mp4)

Either feed or punish the Minecraft Fox by inserting cash into a black box. You have god-like power over a virtual creature. Must we be nice to digital animals? What will people choose? Will their curiosity overcome their disdain for violence? What about when no one's looking?

## Conception

I was inspired by works such as "The Strange Case of Dr. Jekyll and Mr. Hyde" which explore themes of the duality of humanity, and how people can be "bad" or "good," or both at the same time.

The earliest vision for this project was a social commentary on "dance for me monkey"–type media. Think MrBeast or Squid Games. A prerecorded actor would be either punished (kicked) or helped (fed) depending on what slot viewers put money into.

Because of difficulties casting actors, I pivoted to a virtual representation of this theme instead. Rigging and animating a 3D model of an animal/human was outside my area of expertise, so I decided to fall back to the classic video game of Minecraft.

If you observe children (or adults) playing Minecraft, you'll notice vastly different playstyles. Some prefer building houses and treating creatures with respect, yet others prefer violence-based approaches to the game, killing every creature they encounter, whether they are hostile or not.

This installation questions what would happen if one's actions are more or less directly associated to their consequences. In real life, most people don't go araound killing animals. Instead, the biggest choices we make are with our money. Buying a factory-farmed chicken breast may seem less directly connected to the killing of an animal; what if we represented this consequence through a video game and a money receptacle?

## Implementation

I designed a mysterious box with holes, perfectly sized for U.S. currency.

<img src="./gallery/box.jpg" width="300">

First-time viewers of this installation might wonder what happen when you put a dollar bill into the "skull" slot or the "heart" slot. One only finds out what happens when they make a choice.

<img src="./gallery/payment.jpg" width="300">

An ultrasonic distance sensor detects the insertion of a dollar bill, and sends a UDP packet to my Minecraft server.

<img src="./gallery/wiring.jpg" width="300">

The Minecraft server makes something happen in the virtual world depending on the distance reading. If the dollar bill is closer to the sensor, it will harm the Fox by shooting it with pointy arrows. If the money is further, it will feed the Fox a chicken.

## Fulfillment of Requirements

### DIY Sensor

I combinatorially reconfigured an ultrasonic sensor to turn into a makeshift payment receiver. By sensing the distance from the wall to the nearest object, it can tell which slot currency is inserted into.

It's not perfect; one could trick the system by inserting a piece of paper, or inserting but not dropping a dollar bill. However it is a cheap approach for a simple art installation.

### Air-Gap

The ESP32 connects to a UDP server running alongside the Minecraft server Java process. This means that the box could be anywhere in the world and the installation would still work.

If battery power were to be added to the box, a performance artist could walk around the installation area and solicit "donations", which would not be possible if a data wire were required.

The wirelessness of this project creates an "unseen effect"; a viewer may initially think the Minecraft display and the box are disconnected, and only learn about the cause-and-effect when they make a donation.
