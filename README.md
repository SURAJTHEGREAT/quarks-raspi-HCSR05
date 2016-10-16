javac -cp $EDGENT/samples/lib/'*':$PI4J_LIB/'*' -d bin/ *.java
javac -cp $EDGENT/samples/lib/'*':$PI4J_LIB/'*' -d bin/ RangeSensorApplicationQuarks.java RangeSensor.java
sudo java -cp $EDGENT/samples/lib/'*':$PI4J_LIB/'*':bin/ RangeSensorApplication
sudo java -cp $EDGENT/samples/lib/'*':$PI4J_LIB/'*':newBin/ RangeSensorApplication device.cfg
LINKS:
https://developer.ibm.com/recipes/tutorials/apache-quarks-on-pi-to-watson-iot-foundation/
http://edgent.apache.org/docs/samples.html
