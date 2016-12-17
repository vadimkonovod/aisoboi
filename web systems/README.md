# Embassy of Singapore API #

**URL:** `/time-slots`

**Method:** `GET` <br />
**Description:** `Endpoint for fetching free time slots in given date range.`

    queryParameters:
      from:
        description: minimum date of time slot.
        type: date
        required: true

      to:
        description: maximum date of time slot.
        type: date
        required: true

    responses:
      200:
        body:
          application/json:
            example:  {
                        "time-slots": [
                          {
                            "id": 1,
                            "date": 12-12-2016,
                            "time": "09:00",
                            "is_free": true,
                            "links": {
                              "self": "/time-slots/1",
                              "reservation": "null"
                            }
                          },
                          {
                            "id": 3,
                            "date": 14-12-2016,
                            "time": "11:00",
                            "is_free": true,
                            "links": {
                              "self": "/time-slots/3",
                              "reservation": "null"
                            }
                          }
                        ]
                      }

      400:
        description: Returned in case invalid parameters are provided.
        body:  
          application/json:
            example: {
                      "code": 400,
                      "message": "Invalid params: 'from' date can't be greater than 'to' date"
                     }


**URL:** `/time-slots/{id}`

**Method:** `GET` <br />
**Description:** `Endpoint for fetching the time slot by its id.`

    uriParameters:
      id:
        description: time slot id.
        type: integer
        required: true

    responses:
      200:
        body:
          application/json:
            example: {
                        "id": 5,
                        "date": 11-12-2016,
                        "time": "12:00",
                        "is_free": false,
                        "links": {
                          "self": "/time-slots/5",
                          "reservation": "/reservations/2"
                        }
                     }

      404:
        description: Returned in case time slot not found.
        body:  
          application/json:
            example: {
                      "code": 404,
                      "message": "Time slot with ID 123 not found"
                     }


**URL:** `/reservations`

**Method:** `GET` <br />
**Description:** `Endpoint for fetching reservations in given date range.`

    queryParameters:
      from:
        description: minimum date of reservation.
        type: date
        required: true

      to:
        description: maximum date of reservation.
        type: date
        required: true

    responses:
      200:
        body:
          application/json:
            example:  {
                        "reservations": [
                          {
                            "id": 2,
                            "name": "Kasper Schmeichel",
                            "birtdate": "17-10-1989",
                            "id_number": "AB12345"
                            "time_slot_id": 1,
                            "links": {
                              "self": "/reservations/2",
                              "time-slot": "/time-slots/1"
                            }
                          },
                          {
                            "id": 5,
                            "name": "Peter Brooks",
                            "birtdate": "06-05-1996",
                            "id_number": "BA54321"
                            "time_slot_id": 15,
                            "links": {
                              "self": "/reservations/5",
                              "time-slot": "/time-slots/15"
                            }
                          }
                        ]
                      }

      400:
        description: Returned in case invalid parameters are provided.
        body:  
          application/json:
            example: {
                      "code": 400,
                      "message": "Invalid params: 'from' date can't be greater than 'to' date"
                     }

**Method:** `POST` <br /> 
**Description:** `Endpoint for making a reservation of free time slot.`

    body:
      application/json:
        example:  {
                    "name": "Kasper Schmeichel",
                    "birtdate": "17-10-1989",
                    "id_number": "AB12345"
                    "time_slot_id": "1"
                  }

    responses:
      201:
        description: Returned in case time slot was successfully booked.
        headers: 
          Location:
            description: relative URL to the created reservation.
            example: reservations/{id}
            type: string
        body:
          application/json:
            example: {
                        "id": 2,
                        "name": "Kasper Schmeichel",
                        "birtdate": "17-10-1989",
                        "id_number": "AB12345"
                        "time_slot_id": 1,
                        "links": {
                          "self": "/reservations/2",
                          "time-slot": "/time-slots/1"
                        }
                     }

      400:
        description: Server cannot process the request due to a client error.
        body:
          application/json:
            example: {
                      "code": 400,
                      "message": "Time slot is already taken"
                     }


**URL:** `/reservations/{id}`

**Method:** `GET` <br />
**Description:** `Endpoint for fetching the reservation by its id.`

    uriParameters:
      id:
        description: reservation id.
        type: integer
        required: true

    responses:
      200:
        body:
          application/json:
            example: {
                        "id": 2,
                        "name": "Kasper Schmeichel",
                        "birtdate": "17-10-1989",
                        "id_number": "AB12345"
                        "time_slot_id": 1,
                        "links": {
                          "self": "/reservations/2",
                          "time-slot": "/time-slots/1"
                        }
                     }

      404:
        description: Returned in case reservation not found.
        body:  
          application/json:
            example: {
                      "code": 404,
                      "message": "Reservation with ID 123 not found"
                     }

**Method:** `DELETE` <br />
**Description:** `Endpoint for releasing of the reservation by its id.`

    uriParameters:
      id:
        description: reservation id.
        type: integer
        required: true

    responses:
      204:
        description: Returned in case reservation was successfully released.
        body: NO CONTENT

      404:
        description: Returned in case reservation not found.
        body:  
          application/json:
            example: {
                      "code": 404,
                      "message": "Reservation with ID 12 not found"
                     }
