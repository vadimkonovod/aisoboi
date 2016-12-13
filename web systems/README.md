# Embassy of Singapore API #

**URL: embassy/singapore/time-slots**

* **Method: GET**

**Description: Endpoint for fetching free time slots in specified date range.**

    headers:
      Accept:
        enum: [application/json]
        example: application/json

    queryParameters:
      from:
        description: Can be used for setting the position of the first result to retrieve.
        type: string
        required: true

      to:
        description: Can be used for setting the maximum number of results to retrieve.
        type: string
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
                      "message": "Invalid params: date 'to' must be greater than or equal to 'from'"
                     }


* **Method: POST**

**Description: Endpoint for booking a free time slot.**

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

      400:
        description: Indicates that the server cannot process the request due to a client error (e.g., malformed request syntax, invalid request).
        body:
          application/json:
            example: {
                      "code": 400,
                      "message": "Unparsable payload"
                     }

      406:
        description: Returned in case the Accept header contains media type unsupported by end point
        body:
          application/json:
            example: {
                      "code": 406,
                      "message": "Requested representation format is invalid"
                     }

      415:
        description: Returned in the event that the end point doesn't support the media-type specified in the Content-Type header.
        body:
          application/json:
            example: {
                      "code": 415,
                      "message": "Content-type should be specified. Available formats: application/xml, application/json"
                     }