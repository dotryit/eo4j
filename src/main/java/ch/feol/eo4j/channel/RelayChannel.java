package ch.feol.eo4j.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import ch.feol.eo4j.RealBoiler;

public enum RelayChannel {
   
   C1(RaspiPin.GPIO_21, "Channel 1"),
   C2(RaspiPin.GPIO_22, "Channel 2"),
   C3(RaspiPin.GPIO_23, "Channel 3"),
   C4(RaspiPin.GPIO_27, "Channel 4"),
   C5(RaspiPin.GPIO_24, "Channel 5"),
   C6(RaspiPin.GPIO_28, "Channel 6"),
   C7(RaspiPin.GPIO_29, "Channel 7"),
   C8(RaspiPin.GPIO_25, "Channel 8");

   private static final Logger LOG = LoggerFactory.getLogger(RealBoiler.class);

   private static GpioController gpio;

   private GpioPinDigitalOutput output;

   private RelayChannel(Pin pin, String name) {
      output = initPin(pin, name);
   }

   private static GpioPinDigitalOutput initPin(Pin pin, String name) {
      if (gpio == null) {
         gpio = GpioFactory.getInstance();
      }
      GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(pin, name, PinState.HIGH);
      output.setShutdownOptions(true, PinState.HIGH);
      return output;
   }

   public boolean low() {
      if (output.isHigh()) {
         LOG.debug(output.getName() + " changed to low");
         output.low();
         return true;
      }
      return false;
   }

   public boolean high() {
      if (output.isLow()) {
         LOG.debug(output.getName() + " changed to high");
         output.high();
         return true;
      }
      return false;
   }
}
