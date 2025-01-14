package edu.nps.moves.examples;

import java.io.*;
import java.net.*;

import edu.nps.moves.dis.*;
import edu.nps.moves.disutil.CoordinateConversions;
import edu.nps.moves.disutil.DisTime;
import java.util.concurrent.TimeUnit;

/**
 * Creates and sends ESPDUs in IEEE binary format.
 *
 * @author DMcG
 */
public class EspduSender {

    public static final int NUMBER_TO_SEND = 100;

    public static final String DEFAULT_MULTICAST_GROUP = "239.1.2.3";

    public static final int DIS_DESTINATION_PORT = 3000;

    public static final int DIS_HEARTBEAT_SECS = 10;

    public static void main(String args[]) throws UnknownHostException, IOException, RuntimeException, InterruptedException {

        InetAddress destinationIp = InetAddress.getByName(DEFAULT_MULTICAST_GROUP);

        MulticastSocket socket = new MulticastSocket(DIS_DESTINATION_PORT);
        socket.joinGroup(destinationIp);

        EntityStatePdu espdu = new EntityStatePdu();

        // Initialize values in the Entity State PDU object. The exercise ID is 
        // a way to differentiate between different virtual worlds on one network.
        // Note that some values (such as the PDU type and PDU family) are set
        // automatically when you create the ESPDU.
        espdu.setExerciseID((short) 1);

        // The EID is the unique identifier for objects in the world. This 
        // EID should match up with the ID for the object specified in the 
        // VMRL/x3d/virtual world.
        EntityID entityID = espdu.getEntityID();
        entityID.setSite(1);  // 0 is apparently not a valid site number, per the spec
        entityID.setApplication(1);
        entityID.setEntity(2);

        // Set the entity type. SISO has a big list of enumerations, so that by
        // specifying various numbers we can say this is an M1A2 American tank,
        // the USS Enterprise, and so on. We'll make this a tank. There is a 
        // separate project elsehwhere in this project that implements DIS 
        // enumerations in C++ and Java, but to keep things simple we just use
        // numbers here.
        EntityType entityType = espdu.getEntityType();
        entityType.setEntityKind((short) 1);      // Platform (vs lifeform, munition, sensor, etc.)
        entityType.setCountry(225);              // USA
        entityType.setDomain((short) 1);          // Land (vs air, surface, subsurface, space)
        entityType.setCategory((short) 1);        // Tank
        entityType.setSubcategory((short) 1);     // M1 Abrams
        entityType.setSpec((short) 3);            // M1A2 Abrams

        DisTime disTime = DisTime.getInstance(); // TODO explain

        // ICBM coordinates for my office
        double lat = 36.595517;
        double lon = -121.877000;

        // Loop through sending N ESPDUs
        System.out.println("Sending " + NUMBER_TO_SEND + " ESPDU packets to " + destinationIp.toString() + ". One packet every " + DIS_HEARTBEAT_SECS + " seconds.");
        for (int idx = 0; idx < NUMBER_TO_SEND; idx++) {
            // DIS time is a pain in the ass. DIS time units are 2^31-1 units per
            // hour, and time is set to DIS time units from the top of the hour. 
            // This means that if you start sending just before the top of the hour
            // the time units can roll over to zero as you are sending. The receivers
            // (escpecially homegrown ones) are often not able to detect rollover
            // and may start discarding packets as dupes or out of order. We use
            // an NPS timestamp here, hundredths of a second since the start of the
            // year. The DIS standard for time is often ignored in the wild; I've seen
            // people use Unix time (seconds since 1970) and more. Or you can
            // just stuff idx into the timestamp field to get something that is monotonically
            // increasing.

            // Note that timestamp is used to detect duplicate and out of order packets. 
            // That means if you DON'T change the timestamp, many implementations will simply
            // discard subsequent packets that have an identical timestamp. Also, if they
            // receive a PDU with an timestamp lower than the last one they received, they
            // may discard it as an earlier, out-of-order PDU. So it is a good idea to
            // update the timestamp on ALL packets sent.
            // An alterative approach: actually follow the standard. It's a crazy concept,
            // but it might just work.
            int timestamp = disTime.getDisAbsoluteTimestamp();
            espdu.setTimestamp(timestamp);

            double disCoordinates[] = CoordinateConversions.getXYZfromLatLonDegrees(lat, lon, 1.0);
            Vector3Double location = espdu.getEntityLocation();
            location.setX(disCoordinates[0]);
            location.setY(disCoordinates[1]);
            location.setZ(disCoordinates[2]);

            // Optionally, we can do some rotation of the entity
            /*
            Orientation orientation = espdu.getEntityOrientation();
            float psi = orientation.getPsi();
            psi = psi + idx;
            orientation.setPsi(psi);
            orientation.setTheta((float)(orientation.getTheta() + idx /2.0));
             */
            // You can set other ESPDU values here, such as the velocity, acceleration,
            // and so on.
            

            DatagramPacket packet = new DatagramPacket(espdu.marshal(), espdu.getLength(), destinationIp, socket.getLocalPort());
            socket.send(packet);

            location = espdu.getEntityLocation();

            System.out.println("Sent ESPDU #" + idx + " Site,App,Id=[" + entityID.getSite() + ", " + entityID.getApplication() + ", " + entityID.getEntity() + "]");
            System.out.println(" DIS coordinates location=[" + location.getX() + ", " + location.getY() + ", " + location.getZ() + "]");
            double lla[] = CoordinateConversions.xyzToLatLonDegrees(location.toArray());
            System.out.println(" Location (lat/lon/alt): [" + lla[0] + ", " + lla[1] + ", " + lla[2] + "]");

            System.out.println("Sleeping for heartbeat of " + DIS_HEARTBEAT_SECS + " seconds.");
            Thread.sleep(TimeUnit.SECONDS.toMillis(DIS_HEARTBEAT_SECS));
        }
    }
}