import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import org.apache.edgent.function.Supplier;

/**
 * Every time get() is called, TempSensor generates a temperature reading.
 */
public class RangeSensor implements Supplier<Double> {
private final static float SOUND_SPEED = 340.29f;  // speed of sound in m/s
private final static int TRIG_DURATION_IN_MICROS = 10; // trigger duration of 10 micro s
private final static int WAIT_DURATION_IN_MILLIS = 600; // wait 60 milli s
private final static int TIMEOUT = 2100;
private final static Double infinite=9999.99;
private GpioController gpio = GpioFactory.getInstance();
private Pin echoRPin = RaspiPin.GPIO_05; // PI4J custom numbering (pin 18 on RPi2)
private Pin trigRPin = RaspiPin.GPIO_04; // PI4J custom numbering (pin 16 on RPi2) 
private final GpioPinDigitalInput echoPin;
private final GpioPinDigitalOutput trigPin;
public RangeSensor(){
      echoPin = gpio.provisionDigitalInputPin( echoRPin );
      trigPin = gpio.provisionDigitalOutputPin( trigRPin ); 

} 


@Override
public Double get()  {
try{
 Thread.sleep( WAIT_DURATION_IN_MILLIS );    
  

}
catch (InterruptedException ex) {
                System.err.println( "Interrupt during trigger" );
            }
return getDistance();
}


public Double getDistance()  {
        triggerSensor();
        waitForSignal();
        Double duration = measureSignal();
        
        return duration * SOUND_SPEED / ( 2 * 10000 );
    }


  public void triggerSensor() {
        try {
            trigPin.high();
            Thread.sleep( 0, TRIG_DURATION_IN_MICROS * 1000 );
            trigPin.low();
        } catch (InterruptedException ex) {
            System.err.println( "Interrupt during trigger" );
        }
    }
	
   public void waitForSignal()  {
        int countdown = TIMEOUT;
        
        while( echoPin.isLow() && countdown > 0 ) {
            countdown--;
        }
        
      /*  if( countdown <= 0 ) {
            throw new TimeoutException( "Timeout waiting for signal start" );

       }*/ 
    }	
	
	 private Double measureSignal()  {
        int countdown = TIMEOUT;
        long start = System.nanoTime();
        while( echoPin.isHigh() && countdown > 0 ) {
            countdown--;
        }
        long end = System.nanoTime();
       
        if( countdown <= 0 ) {
            return infinite;
        }
       else
        return (Double)Math.ceil( ( end - start ) / 1000.0 );  // Return micro seconds
    }








        



        


static class TimeoutException extends Exception {

        private final String reason;
        
        public TimeoutException( String reason ) {
            this.reason = reason;
        }
        
        @Override
        public String toString() {
            return this.reason;
        }
    }


}
