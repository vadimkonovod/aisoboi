package models;

import com.fasterxml.jackson.databind.JsonNode;
import org.bson.Document;
import org.bson.types.ObjectId;
import play.libs.Json;

import java.time.ZonedDateTime;

/**
 * @author Vadzim_Kanavod
 */
public class TimeSlot {
  private String id;
  private Boolean isFree;
  private ZonedDateTime date;
  private String url;
  private String reservationUrl;

  public TimeSlot() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Boolean getIsFree() {
    return isFree;
  }

  public void setIsFree(Boolean free) {
    isFree = free;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getReservationUrl() {
    return reservationUrl;
  }

  public void setReservationUrl(String reservationUrl) {
    this.reservationUrl = reservationUrl;
  }

  public String getDate() {
    return date.toString();
  }

  public void setDate(String dateString) {
    date = ZonedDateTime.parse(dateString);
  }

  public static TimeSlot fromJson(String json) {
    JsonNode timeSlotNode = Json.parse(json);
    TimeSlot timeSlot = Json.fromJson(timeSlotNode, TimeSlot.class);
    if (timeSlot.id != null && timeSlot.url == null) {
      timeSlot.url = "/time-slots/" + timeSlot.id;
    }
    if (timeSlot.id != null && !timeSlot.getIsFree()) {
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