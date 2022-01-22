package stepDefinations;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.Booking;
import pojo.request.Authentication;
import resources.APIResources;
import resources.Utils;

public class StepDefination extends Utils {
    RequestSpecification res;
    ResponseSpecification resspec;
    Response response;
    static String bookingId;
    static String token;



    @Given("update booking with payload {string} {string} {string} {string} {string} {string} {string}")
    public void update_booking_with_payload(String firstname, String lastname, String totalprice,
                               String depositpaid, String checkin, String checkout, String additionalneeds) throws IOException {
        // Write code here that turns the phrase above into concrete actions
        HashMap<String,String> parameterNameValuePairs = new HashMap<String, String>();
        parameterNameValuePairs.put("id",bookingId);
        HashMap<String,String> headers = new HashMap<String, String>();
        headers.put("Cookie",token);
        headers.put("Authorisation","Basic YWRtaW46cGFzc3dvcmQxMjM=]");
        headers.put("Content-Type","application/json");
        headers.put("Accept","application/json");
        res = given().spec(requestSpecification()).pathParams(parameterNameValuePairs).headers(headers)
                .body(new Booking(firstname, lastname, Integer.valueOf(totalprice), Boolean.valueOf(depositpaid), checkin, checkout, additionalneeds));

    }

    @Given("update partial booking with payload {string} {string}")
    public void update_partial_booking(String firstname, String lastname) throws IOException {
        // Write code here that turns the phrase above into concrete actions
        HashMap<String,String> parameterNameValuePairs = new HashMap<String, String>();
        parameterNameValuePairs.put("id",bookingId);
        HashMap<String,String> headers = new HashMap<String, String>();
        headers.put("Cookie",token);
        headers.put("Authorisation","Basic YWRtaW46cGFzc3dvcmQxMjM=]");
        headers.put("Content-Type","application/json");
        headers.put("Accept","application/json");
        res = given().spec(requestSpecification()).pathParams(parameterNameValuePairs).headers(headers)
                .body(new Booking(firstname, lastname, null, null, null, null, null));

    }

    @When("user calls {string} with {string} http request")
    public void user_calls_with_http_request(String resource, String method) {

        //constructor will be called with value of resource which you pass
        APIResources resourceAPI = APIResources.valueOf(resource);

        resspec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

        // method will be passed based on the method parameter passed from feature file
        response = res.request(method, resourceAPI.getResource());

    }

    @Then("the API call for {string} returned success with status code {int}")
    public void the_API_call_got_success_with_status_code(String call, int int1) {
        // Write code here that turns the phrase above into concrete actions

        assertEquals(response.getStatusCode(), int1);


    }

    @Then("{string} in response body is not null")
    public void in_response_body_is_not_null(String keyValue) {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Booking is created with:  "+keyValue+" : "+getJsonPath(response, keyValue));
        assertNotNull(getJsonPath(response, keyValue));
    }

    @Then("{string} in response body is {string}")
    public void in_response_body_is(String keyValue, String value) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(getJsonPath(response, keyValue), value);
    }

    @Then("verify bookingid created {string}")
    public void verify_booking_id_created(String resource) throws IOException {
        bookingId = getJsonPath(response, "bookingid");
        HashMap<String,String> parameterNameValuePairs = new HashMap<String, String>();
        parameterNameValuePairs.put("id",bookingId);
        res = given().spec(requestSpecification()).pathParams(parameterNameValuePairs);
        user_calls_with_http_request(resource, "GET");
        String createdBookingFrstName = getJsonPath(response, "firstname");
        assertNotNull(createdBookingFrstName);

    }


    @Given("Delete Booking {string} with method {string}")
    public void delete_booking_Payload(String resource, String method) throws IOException {
        // Write code here that turns the phrase above into concrete actions

        HashMap<String,String> parameterNameValuePairs = new HashMap<String, String>();
        parameterNameValuePairs.put("id",bookingId);
        HashMap<String,String> headers = new HashMap<String, String>();
        headers.put("Cookie",token);
        headers.put("Authorisation","Basic YWRtaW46cGFzc3dvcmQxMjM=]");
        headers.put("Content-Type","application/json");
        headers.put("Accept","application/json");

        res = given().spec(requestSpecification()).pathParams(parameterNameValuePairs).headers(headers);;
        user_calls_with_http_request(resource, method);
    }


    @Given("Create Token with username {string} and password {string} {string}")
    public void createTokenToDelete(String username, String password, String resource) throws IOException {
        res = given().spec(requestSpecification()).body(new Authentication(username, password));
        user_calls_with_http_request(resource, "POST");
        token = "token=" + getJsonPath(response, "token");
        assertNotNull(token);
    }

    @Then("verify bookingid deleted {string}")
    public void verify_booking_id_deleted(String resource) throws IOException {

        HashMap<String,String> parameterNameValuePairs = new HashMap<String, String>();
        parameterNameValuePairs.put("id",bookingId);
        res = given().spec(requestSpecification()).pathParams(parameterNameValuePairs);
        user_calls_with_http_request(resource, "GET");
        assertEquals(response.getStatusCode(), 404);
    }

    @Given("create booking with payload {string} {string} {string} {string} {string} {string} {string}")
    public void createBookingWithPayload(String firstname, String lastname, String totalprice, String depositpaid, String checkin, String checkout, String additionalneeds) throws IOException {
        res = given().spec(requestSpecification())
                .body(new Booking(firstname, lastname, Integer.valueOf(totalprice) , Boolean.valueOf(depositpaid), checkin, checkout, additionalneeds));
    }




}
