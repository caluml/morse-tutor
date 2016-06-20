# morse-tutor

A simple app to help you get faster at copying Morse (CW).
It doesn't help you learn the characters, and it doesn't help you send. But if you can receive successfully, you'll have no problem sending.

Learn the characters at https://en.wikipedia.org/wiki/Morse_code

Requirements:
   * Maven
   * JDK (6+)
   * Only tested on Linux

Run with run.sh. Escape to exit, space to repeat a character.

Limitations/Notes
   * It's currently got hardcoded volume levels and dit-dah durations - I'm not quite sure what the equivalent WPM would be.
See Note.java for the volume, and MorseTutor.java for the durations.
   * It uses a JFrame as I couldn't read a single key-press from stdin.

If you want some real Morse to listen to, there should be some around these frequencies (yes - people still use it)
   * http://websdr.ewi.utwente.nl:8901/?tune=7028lsb (Look between 7.000 and 7.050)
   * http://websdr.ewi.utwente.nl:8901/?tune=14015usb (Look between 14.000 and 14.050)

Pull requests welcome.
