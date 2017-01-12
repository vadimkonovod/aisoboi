package model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import play.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.mongodb.client.model.Filters.eq;

public class Reservations {

  private static final String RESERVATIONS_COLLECTION_NAME = "reservations";
  MongoCollection<Document> reservationsCollection;
  TimeSlots timeSlots;

  public Reservations(MongoDatabase db, model.TimeSlots timeSlots) {
    this.reservationsCollection = db.getCollection(RESERVATIONS_COLLECTION_NAME);
    this.timeSlots = timeSlots;
  }

  public CompletableFuture<ArrayList> getReservations() {
    CompletableFuture<ArrayList> result = new CompletableFuture<>();
    Logger.debug("Finding reservations");

    reservationsCollection.find().map(Document::toJson).map(Reservation::fromJson)
      .into(new ArrayList<Reservation>(),
        (ArrayList<Reservation> reservations, Throwable t)-> {
          if (t != null) {
            Logger.error("Error getting reservations", t);
          }
          result.complete(reservations);
        }
      );

    return result;
  }

  public CompletableFuture<ArrayList<Reservation>> getAllReservations() {
    CompletableFuture<ArrayList<Reservation>> result = new CompletableFuture<>();
    Logger.info("Getting all reservations");
    reservationsCollection.find().into(new ArrayList<>(),
      (ArrayList<Document> docs, Throwable t)-> {
        if (t != null) {
          Logger.error("Error getting reservation for time slot", t);
        }
        ArrayList<Reservation> reservations = new ArrayList<>();
        if (docs != null) {
          docs.forEach(doc -> {
            Reservation reservation = Reservation.fromJson(doc.toJson());
            reservation.id = doc.getObjectId("_id").toString();
            if (reservation.id != null && reservation.url == null) {
              reservation.url = "/time-slots/" + reservation.id + "/reservation";
            }
            reservations.add(reservation);
          });
        }
        result.complete(reservations);
      });

    return result;
  }

  public CompletableFuture<ArrayList<Reservation>> getReservationForTimeSlot(String timeSlotId) {
    CompletableFuture<ArrayList<Reservation>> result = new CompletableFuture<>();
    Logger.info("Finding reservation for time slot {}", timeSlotId);
    reservationsCollection.find(eq("timeSlotId", timeSlotId)).into(new ArrayList<>(),
      (ArrayList<Document> docs, Throwable t)-> {
        if (t != null) {
          Logger.error("Error getting reservation for time slot", t);
        }
        ArrayList<Reservation> reservations = new ArrayList<>();
        if (docs != null) {
          docs.forEach(doc -> {
            Reservation reservation = Reservation.fromJson(doc.toJson());
            reservation.id = doc.getObjectId("_id").toString();
            if (reservation.id != null && reservation.url == null) {
              reservation.url = "/time-slots/" + reservation.id + "/reservation";
            }
            reservations.add(reservation);
          });
        }
        result.complete(reservations);
      });

    return result;
  }
  
  public CompletableFuture<Reservation> getById(String id) {

    CompletableFuture<Reservation> result = new CompletableFuture<>();

    reservationsCollection.find(eq("_id", new ObjectId(id))).first(
      (Document doc, Throwable t)-> {
        Reservation reservation = null;
        if (t != null) {
          Logger.error("Error getting reservation by id", t);
        }
        if (doc != null) {
          Logger.debug("Get reservation successfully: {}", id);
          reservation = Reservation.fromJson(doc.toJson());
          reservation.id = doc.getObjectId("_id").toString();
        } else {
          Logger.warn("Get reservation failed: {}", id);
        }
        result.complete(reservation != null ? Reservation.fromJson(reservation.toJson()) : null);
      }
    );

    return result;
  }

  public List<Reservation> fromJson(String reservationsJson) {
    ObjectMapper objectMapper = new ObjectMapper();
    ArrayList<Reservation> reservations = null;
    try {
      reservations = objectMapper.readValue(
        reservationsJson,
        objectMapper.getTypeFactory().constructCollectionType(
          ArrayList.class, Reservation.class));
    } catch (JsonParseException e) {
      Logger.error("Reservations from json JsonParseException", e);
    } catch (JsonMappingException e) {
      Logger.error("Reservation from json JsonMappingException", e);
    } catch (IOException e) {
      Logger.error("Reservation from json IOException", e);
    }

    return reservations;
  }
}