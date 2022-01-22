Feature: Validating Hotel API's
  @CreateBooking @Regression
  Scenario Outline: Verify if Booking is being Succesfully created using CreateBooking
    Given create booking with payload "<firstname>" "<lastname>" "<totalprice>" "<depositpaid>" "<checkindates>" "<checkoutdate>" "<additionalneeds>"
    When user calls "Booking" with "POST" http request
    Then the API call for "create booking" returned success with status code 200
    And "booking.firstname" in response body is "sam"
    And "booking.lastname" in response body is "schaap"
    And verify bookingid created "BookingId"

    Examples:
      | firstname | lastname | totalprice | depositpaid | checkindates | checkoutdate | additionalneeds
      | sam       | schaap   | 200        | true          | 2013-02-02   | 2013-02-12   | Breakfast


  @UpdateBooking @Regression
  Scenario Outline: Verify if Booking is being Succesfully updated using UpdateBooking
    Given Create Token with username "admin" and password "password123" "CreateToken"
    Given update booking with payload "<firstname>" "<lastname>" "<totalprice>" "<depositpaid>" "<checkindates>" "<checkoutdate>" "<additionalneeds>"
    Then user calls "BookingId" with "PUT" http request
    Then the API call for "update booking" returned success with status code 200
    And "firstname" in response body is "dav"
    And "lastname" in response body is "van persie"

    Examples:
      | firstname | lastname   | totalprice | depositpaid | checkindates | checkoutdate | additionalneeds |
      | dav       | van persie | 200          | true        | 2022-01-01   | 2022-02-01   | lunch,breakfast |


  @UpdatePartialBooking @Regression
  Scenario Outline: Verify if Booking is being Succesfully partially updated using UpdatePartialBooking
    Given update partial booking with payload "<firstname>" "<lastname>"
    When user calls "BookingId" with "PATCH" http request
    Then the API call for "update booking" returned success with status code 200
    And "firstname" in response body is "test"
    And "lastname" in response body is "popov"
    Examples:
      | firstname | lastname |
      | test      | popov    |


  @DeleteBooking @Regression
  Scenario: Verify if Delete Booking functionality is working
    Given Delete Booking "BookingId" with method "DELETE"
    When the API call for "delete booking" returned success with status code 201
    Then verify bookingid deleted "BookingId"


