import java.util.concurrent.TimeUnit;

import org.apache.edgent.providers.direct.DirectProvider;
import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.Topology;

public class RangeSensorApplicationQuarks {
    public static void main(String[] args) throws Exception {
        RangeSensor sensor = new RangeSensor();
        DirectProvider dp = new DirectProvider();
        Topology topology = dp.newTopology();
        TStream<Double> distReadings = topology.poll(sensor, 1, TimeUnit.MILLISECONDS);

        TStream<Double> filteredReadings = distReadings.filter(reading -> reading < 3000 );

        filteredReadings.print();
        dp.submit(topology);
    }
}
