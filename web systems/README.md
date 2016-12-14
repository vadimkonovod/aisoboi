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
                            "date": 12-12-2016,
                            "slots": ["09:15", "10:30", "11:45", "12:45", "16:30"]
                          },
                          {
                            "date": 13-12-2016,
                            "slots": ["14:30", "15:45", "16:15"]
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


**Method:** `PUT` <br /> 
**Description:** `Endpoint for booking a free time slot.`

    body:
      application/json:
        example:  {
                    "name": "Kasper Schmeichel",
                    "date": "12-12-2016",
                    "time": "9:30"
                  }

    responses:
      201:
        description: Returned in case time slot was successfully booked.
        headers: 
          Location:
            description: relative URL to the booked time slot.
            example: time-slots/{id}
            type: string
        body:
          application/json:
            example: {
                       "id": 11,
                       "date": "12-12-2016",
                       "time": "9:30",
                       "name": "Kasper Schmeichel"
                     }

      409:
        description: Indicates that the request could not be processed because of conflict in the request.
        body:
          application/json:
            example: {
                      "code": 409,
                      "message": "Time slot is already taken"
                     }

      400:
        description: Server cannot process the request due to a client error.
        body:
          application/json:
            example: {
                      "code": 400,
                      "message": "Unparsable payload"
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
                       "id": 11,
                       "date": "12-12-2016",
                       "time": "9:30",
                       "name": "Kasper Schmeichel"
                     }

      404:
        description: Returned in case time slot not found.
        body:  
          application/json:
            example: {
                      "code": 404,
                      "message": "Time slot with ID 123 not found"
                     }


**Method:** `DELETE` <br />
**Description:** `Endpoint for releasing of the time slot by its id.`

    uriParameters:
      id:
        description: time slot id.
        type: integer
        required: true

    responses:
      204:
        description: Returned in case time slot was successfully released.
        body: NO CONTENT

      404:
        description: Returned in case time slot not found.
        body:  
          application/json:
            example: {
                      "code": 404,
                      "message": "Time slot with ID 123 not found"
                     }
