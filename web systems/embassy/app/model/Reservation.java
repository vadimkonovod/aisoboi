package model;

import org.bson.Document;
import org.bson.types.ObjectId;
import play.libs.Json;

public class Reservation {
  public String id;
  public String timeSlotId;
  public String name;
  public String idNumber;
  public String url;
  public String timeSlotUrl;

  public static Reservation fromJson(String json) {
    //System.out.println(json);
    //System.out.println(ticketNode.asText());
    Reservation reservation = Json.fromJson(Json.parse(json), Reservation.class);
    if (reservation.id != null && reservation.url == null) {
      reservation.url = "/time-slots/" + reservation.id + "/reservation";
    }
    return reservation;
  }

  public String toJson() {
    return Json.toJson(this).toString();
  }

  public Document toMongoDocument() {
    Document doc = new Document()
      .append("name", name == null ? "" : name)
      .append("timeSlotId", timeSlotId == null ? "" : timeSlotId)
      .append("idNumber", idNumber == null ? "" : idNumber);

    if (id != null) {
      if (url == null) {
        doc.append("url", "/time-slots/" + id + "/reservation");
      }
      if (timeSlotUrl == null) {
        doc.append("timeSlotUrl", "/time-slots/" + id);
      }
      doc.append("_id", new ObjectId(id));
    }

    return doc;
  }

}