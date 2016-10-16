import java.util.concurrent.TimeUnit;
import com.google.gson.JsonObject;
import org.apache.edgent.providers.direct.DirectProvider;
import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.Topology;
import org.apache.edgent.connectors.iot.IotDevice;
import org.apache.edgent.connectors.iot.QoS;
import org.apache.edgent.connectors.iotp.IotpDevice;
import java.io.File;
public class RangeSensorApplication {
private static String deviceCfg=null;
    public static void main(String[] args) throws Exception {
	    if(args.length!=0){
		deviceCfg = args[0];

	}
        RangeSensor sensor = new RangeSensor();
        DirectProvider dp = new DirectProvider();
		Topology topology = dp.newTopology();
		IotDevice device = new IotpDevice(topology, new File(deviceCfg));	
        TStream<Double> distReadings = topology.poll(sensor, 1, TimeUnit.MILLISECONDS);

        TStream<Double> filteredReadings = distReadings.filter(reading -> reading < 50);

        //filteredReadings.print();
		TStream<JsonObject> sensorJSON = filteredReadings.map(v -> {

		   JsonObject j = new JsonObject();

		   j.addProperty("SensorName", "Range_Sensor");

		   j.addProperty("SensorReading", v.intValue());

		   return j;

		});
		
		
	device.events(sensorJSON, "sensors", QoS.FIRE_AND_FORGET);
	sensorJSON.print();
    dp.submit(topology);
    }
}

