package model;

import com.fasterxml.jackson.databind.JsonNode;
import org.bson.Document;
import org.bson.types.ObjectId;
import play.libs.Json;

import java.time.ZonedDateTime;

public class TimeSlot {
  public String id;
  public ZonedDateTime date;
  public Boolean isFree;
  public String url;
  public String reservationUrl;

  public TimeSlot() {}

  public String getDate() {
    return date.toString();
  }

  public void setDate(String dateString) {
    date = ZonedDateTime.parse(dateString);
  }

  public static TimeSlot fromJson(String json) {
    JsonNode timeSlotNode = Json.parse(json);
    //System.out.println(json);
    //System.out.println(timeSlotNode.asText());
    TimeSlot timeSlot = Json.fromJson(timeSlotNode, TimeSlot.class);
    if (timeSlot.id != null && timeSlot.url == null) {
      timeSlot.url = "/time-slots/" + timeSlot.id;
    }
    if (timeSlot.id != null && !timeSlot.isFree) {
      timeSlot.reservationUrl = timeSlot.url + "/reservation";
    }
    return timeSlot;
  }

  public String toJson() {
    return Json.toJson(this).toString();
  }

  public Document toMongoDocument() {
    Document doc = new Document()
      .append("date", date.toString())
      .append("isFree", isFree);

    if (id != null) {
      if (url == null) {
        doc.append("url", "/time-slots/" + id);
      }
      if (!isFree) {
        doc.append("reservationUrl", "/time-slots/" + id + "/reservation");
      } else {
        doc.append("reservationUrl", null);
      }
      doc.append("_id", new ObjectId(id));
    }
    return doc;
  }

}