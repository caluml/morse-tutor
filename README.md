# morse-tutor

## What is it?
A simple app to help you get faster at copying (receiving) Morse (CW).

## What isn't it?
It doesn't help you learn the characters, and it doesn't help you send.
Receiving is the hard part. If you can receive successfully, you'll have no problem sending.

Learn the characters at https://en.wikipedia.org/wiki/Morse_code

## Requirements
   * Maven (if building from source)
   * JDK (6+) to build. JRE or JDK to run
   * Only tested on Linux

## To build
```mvn clean compile```

## To run
   * Either: ```mvn clean compile```, then ```./run.sh``` or
   * Use the precompiled jar: ```java -jar 40 20 800```

Escape to exit, space to repeat a character.

## Stats
It times how long it takes you to get the symbol correct.
When you press escape, it gives you stats of:
   * Which symbols you got wrong, and how many times.
   * The average time for each symbol.
   * Some overall stats. E.g.
```
Start:             Sat Jul 30 17:54:33 BST 2016
End:               Sat Jul 30 17:57:09 BST 2016
Elapsed:           2.6073666 minutes
Right:             60
Wrong:             15
% correct:         80.0
Total chars/min:   23.011724
Correct chars/min: 23.011724
```

## Weighting
Every time you get one wrong, it increases the symbol's weighting. When you get one right, it decreases the weighting.
Letters are randomly picked, and the higher the symbol's weighting, the more likely it is to be selected.
This will help you learn the symbols you get wrong most often.

## Limitations/Notes
   * It uses a JFrame as I couldn't read a single key-press from stdin.
   * It only uses Java 6, although there's some nice stuff in later versions that could make it nicer.
   * It picks randomly, so it can pick the same symbol multiple times in a row.
   * If you don't want to practice numbers, or punctuation, comment out the relevant lines in Symbols.java and rebuild.

## Suggestions
   * If you're learning to copy CW, don't learn the symbols slowly, and increase the speed. Start off with the "dit length" small so the characters are fast. Something of 40 or under is best. Then just work on getting the recognition time down.

## Morse? In this day and age?
Yes - people still use it.
If you want some real Morse to listen to, there should be some around these frequencies
   * http://websdr.ewi.utwente.nl:8901/?tune=7028lsb (Look between 7.000 and 7.050)
   * http://websdr.ewi.utwente.nl:8901/?tune=14015usb (Look between 14.000 and 14.050)

Pull requests welcome.
