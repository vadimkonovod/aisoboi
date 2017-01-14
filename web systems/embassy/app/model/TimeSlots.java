package models;

import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import play.Logger;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static com.mongodb.client.model.Filters.eq;

/**
 * @author Vadzim_Kanavod
 */
public class TimeSlots {

  private static final String TIME_SLOTS_COLLECTION_NAME = "timeslots";
  MongoCollection<Document> timeSlotsCollection;

  public TimeSlots(MongoDatabase db) {
    timeSlotsCollection = db.getCollection(TIME_SLOTS_COLLECTION_NAME);
  }

  public CompletableFuture<ArrayList> getTimeSlots() {
    CompletableFuture<ArrayList> result = new CompletableFuture<>();

    timeSlotsCollection.find().into(new ArrayList<>(),
      (ArrayList<Document> docs, Throwable t)-> {
        if (t != null) {
          Logger.error("Error getting time slots", t);
        }

        ArrayList<TimeSlot> timeSlots = new ArrayList<>();
        if (docs != null) {
          Logger.debug("Collecting time slots.");
          docs.forEach(doc -> {
            TimeSlot timeSlot = TimeSlot.fromJson(doc.toJson());
            timeSlot.setId(doc.getObjectId("_id").toString());
            if (timeSlot.getId() != null && timeSlot.getUrl() == null) {
              timeSlot.setUrl("/time-slots/" + timeSlot.getId());
            }
            if (timeSlot.getId() != null && !timeSlot.getIsFree()) {
              timeSlot.setReservationUrl(timeSlot.getUrl() + "/reservation");
            }
            timeSlots.add(timeSlot);
          });
        } else {
          Logger.info("There are no any time slots.");
        }
        result.complete(timeSlots);
      }
    );

    return result;
  }

  public CompletableFuture<String> addTimeSlot(TimeSlot timeSlot) {
    CompletableFuture<String> result = new CompletableFuture<>();
    timeSlotsCollection.insertOne(timeSlot.toMongoDocument(),
      (Void res, final Throwable t) -> {
        if (t != null) {
          Logger.error("Not inserted", t);
          result.complete("error");
        } else {
          Logger.info("Trying to insert time slot: {}", timeSlot.toJson());
          result.complete("success");
        }
      }
    );

    return result;
  }

  public CompletableFuture<TimeSlot> getById(String id) {
    CompletableFuture<TimeSlot> result = new CompletableFuture<>();

    timeSlotsCollection.find(eq("_id", new ObjectId(id))).first(
      (Document doc, Throwable t)-> {
        TimeSlot timeSlot = null;
        if (t != null) {
          Logger.error("Error getting time-slot by id", t);
        }
        if (doc != null) {
          Logger.debug("Get time slot successfully: {}", id);
          timeSlot = TimeSlot.fromJson(doc.toJson());
          timeSlot.setId(doc.getObjectId("_id").toString());
        } else {
          Logger.warn("Get time slot failed: {}", id);
        }
        result.complete(timeSlot != null ? TimeSlot.fromJson(timeSlot.toJson()) : null);
      }
    );

    return result;
  }
}
