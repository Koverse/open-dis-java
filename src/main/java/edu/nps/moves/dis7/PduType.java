//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.nps.moves.dis7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public enum PduType
{
  OTHER(0, "Other"),
  ENTITY_STATE(1, "Entity State"),
  FIRE(2, "Fire"),
  DETONATION(3, "Detonation"),
  COLLISION(4, "Collision"),
  SERVICE_REQUEST(5, "Service Request"),
  RESUPPLY_OFFER(6, "Resupply Offer"),
  RESUPPLY_RECEIVED(7, "Resupply Received"),
  RESUPPLY_CANCEL(8, "Resupply Cancel"),
  REPAIR_COMPLETE(9, "Repair Complete"),
  REPAIR_RESPONSE(10, "Repair Response"),
  CREATE_ENTITY(11, "Create Entity"),
  REMOVE_ENTITY(12, "Remove Entity"),
  START_RESUME(13, "Start/Resume"),
  STOP_FREEZE(14, "Stop/Freeze"),
  ACKNOWLEDGE(15, "Acknowledge"),
  ACTION_REQUEST(16, "Action Request"),
  ACTION_RESPONSE(17, "Action Response"),
  DATA_QUERY(18, "Data Query"),
  SET_DATA(19, "Set Data"),
  DATA(20, "Data"),
  EVENT_REPORT(21, "Event Report"),
  COMMENT(22, "Comment"),
  ELECTROMAGNETIC_EMISSION(23, "Electromagnetic Emission"),
  DESIGNATOR(24, "Designator"),
  TRANSMITTER(25, "Transmitter"),
  SIGNAL(26, "Signal"),
  RECEIVER(27, "Receiver"),
  IFF_ATC_NAVAIDS(28, "IFF/ATC/NAVAIDS"),
  UNDERWATER_ACOUSTIC(29, "Underwater Acoustic"),
  SUPPLEMENTAL_EMISSION_ENTITY_STATE(30, "Supplemental Emission / Entity State"),
  INTERCOM_SIGNAL(31, "Intercom Signal"),
  INTERCOM_CONTROL(32, "Intercom Control"),
  AGGREGATE_STATE(33, "Aggregate State"),
  ISGROUPOF(34, "IsGroupOf"),
  TRANSFER_CONTROL(35, "Transfer Control"),
  ISPARTOF(36, "IsPartOf"),
  MINEFIELD_STATE(37, "Minefield State"),
  MINEFIELD_QUERY(38, "Minefield Query"),
  MINEFIELD_DATA(39, "Minefield Data"),
  MINEFIELD_RESPONSE_NAK(40, "Minefield Response NAK"),
  ENVIRONMENTAL_PROCESS(41, "Environmental Process"),
  GRIDDED_DATA(42, "Gridded Data"),
  POINT_OBJECT_STATE(43, "Point Object State"),
  LINEAR_OBJECT_STATE(44, "Linear Object State"),
  AREAL_OBJECT_STATE(45, "Areal Object State"),
  TSPI(46, "TSPI"),
  APPEARANCE(47, "Appearance"),
  ARTICULATED_PARTS(48, "Articulated Parts"),
  LE_FIRE(49, "LE Fire"),
  LE_DETONATION(50, "LE Detonation"),
  CREATE_ENTITY_R(51, "Create Entity-R"),
  REMOVE_ENTITY_R(52, "Remove Entity-R"),
  START_RESUME_R(53, "Start/Resume-R"),
  STOP_FREEZE_R(54, "Stop/Freeze-R"),
  ACKNOWLEDGE_R(55, "Acknowledge-R"),
  ACTION_REQUEST_R(56, "Action Request-R"),
  ACTION_RESPONSE_R(57, "Action Response-R"),
  DATA_QUERY_R(58, "Data Query-R"),
  SET_DATA_R(59, "Set Data-R"),
  DATA_R(60, "Data-R"),
  EVENT_REPORT_R(61, "Event Report-R"),
  COMMENT_R(62, "Comment-R"),
  RECORD_R(63, "Record-R"),
  SET_RECORD_R(64, "Set Record-R"),
  RECORD_QUERY_R(65, "Record Query-R"),
  COLLISION_ELASTIC(66, "Collision-Elastic"),
  ENTITY_STATE_UPDATE(67, "Entity State Update"),
  DIRECTED_ENERGY_FIRE(68, "Directed Energy Fire"),
  ENTITY_DAMAGE_STATUS(69, "Entity Damage Status");

  private final int value;
  private final String description;

  private PduType(int value, String description) {
    this.value = value;
    this.description = description;
  }

  public int getValue() {
    return this.value;
  }

  public String getDescription() {
    return this.description;
  }

  public static int getEnumBitWidth() {
    return 8;
  }

  public static PduType getEnumForValue(int i) {
    PduType[] var1 = values();
    int var2 = var1.length;

    for (PduType val : var1) {
      if (val.getValue() == i) {
        return val;
      }
    }

    System.err.println("No enumeration found for value " + i + " of enumeration DISPDUType");
    return null;
  }

  public void marshal(DataOutputStream dos) throws IOException {
    dos.writeByte(this.getValue());
  }

  public void marshal(ByteBuffer byteBuffer) throws Exception {
    byteBuffer.put((byte)this.getValue());
  }

  public static PduType unmarshalEnum(DataInputStream dis) throws Exception {
    return getEnumForValue(dis.readByte());
  }

  public static PduType unmarshalEnum(ByteBuffer byteBuffer) throws Exception {
    return getEnumForValue(byteBuffer.get());
  }

  public int getMarshalledSize() {
    return 1;
  }

  public String toString() {
    return "DISPDUType " + this.getValue() + " " + this.name();
  }
}
