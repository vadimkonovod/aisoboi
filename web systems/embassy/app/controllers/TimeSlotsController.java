package controllers;

import play.cache.Cached;
import play.mvc.Controller;
import play.mvc.Result;

import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.async.client.*;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;

import javax.inject.Inject;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import play.libs.Json;

import models.TimeSlots;
import models.Reservations;

public class TimeSlotsController extends Controller {

  private static final String DB_NAME = "embassydb";

  MongoClient mongoClient;
  MongoDatabase database;
  TimeSlots timeSlots;
  Reservations reservations;

  @Inject
  public TimeSlotsController() {

    ClusterSettings clusterSettings = ClusterSettings.builder()
      .hosts(Collections.singletonList(new ServerAddress("localhost", 27017)))
      .description("Local Server")
      .build();

    ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
      .maxWaitQueueSize(2500)
      .maxSize(1500)
      .maxWaitTime(300, TimeUnit.SECONDS)
      .build();

    MongoClientSettings settings = MongoClientSettings.builder()
      .clusterSettings(clusterSettings)
      .connectionPoolSettings(connectionPoolSettings)
      //.readPreference(ReadPreference.nearest())
      .build();

    mongoClient = MongoClients.create(settings);
    database = mongoClient.getDatabase(DB_NAME);
    timeSlots = new TimeSlots(database);
    reservations = new Reservations(database, timeSlots);
  }

  @Cached(key = "timeslots_list")
  public CompletableFuture<Result> timeSlots() {
    CompletableFuture result = timeSlots.getTimeSlots();
    return result.thenApply((slots) -> ok(Json.toJson(slots)));
  }
}
