package com.volvo.project.tests.rest;

import com.volvo.project.components.*;
import com.volvo.project.components.datatdriventesting.DataProviderArguments;
import com.volvo.project.components.datatdriventesting.TestDataProvider;
import com.volvo.project.components.rest.RestUtils;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static com.volvo.project.components.rest.RestUtils.convertLongObjectToIntObject;

public class RestApiTest extends TestBase {

        @DataProviderArguments(file = "/rest/RestApiTest/getIndexController.json")
        @Test(groups = "rest", dataProvider = "getDataFromJson", dataProviderClass = TestDataProvider.class)
        public void getIndexController(JSONObject jsonObject){
            //preconditions:
            testNameParameter.set(RestUtils.getValuesFromJson(jsonObject));

            given()
                    .when().get("v1/function-groups")
                    .then().
                    assertThat().
                    statusCode((Integer)convertLongObjectToIntObject(jsonObject.get("code")));
        }
    }