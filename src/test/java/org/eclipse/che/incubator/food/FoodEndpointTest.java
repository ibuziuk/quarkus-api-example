/*
 * Copyright (c) 2022 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.incubator.food;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class FoodEndpointTest {

    @Test
    public void testListAll() {
        FoodResponse[] foods = given()
                .when().get("/food")
                .then().statusCode(200)
                .extract().as(FoodResponse[].class);
        assertEquals(5, foods.length);
        assertEquals("Apple pie", foods[0].name);
        assertEquals("Orange", foods[1].name);
        assertEquals("Sandwich", foods[2].name);
        assertEquals("Soup", foods[3].name);
        assertEquals("Strawberry cake", foods[4].name);
    }

    @Test
    public void testGetById() {
        FoodResponse food = given()
                .when().get("/food/1")
                .then().statusCode(200)
                .extract().as(FoodResponse.class);
        assertEquals(1, food.id);
        assertEquals("Orange", food.name);
        assertEquals("Fruit Bistro", food.restaurantName);
        assertEquals("0.99", food.price.toPlainString());
    }

    @Test
    public void testGetByName() {
        FoodResponse food = given()
                .when().get("/food/search/Orange")
                .then().statusCode(200)
                .extract().as(FoodResponse.class);
        assertEquals(1, food.id);
        assertEquals("Orange", food.name);
        assertEquals("Fruit Bistro", food.restaurantName);
        assertEquals("0.99", food.price.toPlainString());
    }

    @Test
    public void testListByRestaurant() {
        FoodResponse[] food = given()
                .when().get("/food/restaurant/Fruit Bistro")
                .then().statusCode(200)
                .extract().as(FoodResponse[].class);
        assertEquals(3, food.length);
        assertEquals("Apple pie", food[0].name);
        assertEquals("Orange", food[1].name);
        assertEquals("Strawberry cake", food[2].name);
    }

    @Test
    public void testDelete() {
        int id = given()
                .contentType("application/json")
                .body("{\"name\":\"Temporary\",\"restaurantName\":\"Test Kitchen\",\"price\":1.25}")
                .when().post("/food")
                .then().statusCode(201)
                .body("id", notNullValue())
                .extract().path("id");

        given()
                .when().delete("/food/" + id)
                .then().statusCode(204);

        given()
                .when().get("/food/" + id)
                .then().statusCode(404);

        FoodResponse[] foods = given()
                .when().get("/food")
                .then().statusCode(200)
                .extract().as(FoodResponse[].class);
        assertEquals(5, foods.length);
    }

    @Test
    public void testMissingFoodReturnsNotFound() {
        given().when().get("/food/999999").then().statusCode(404);
        given().when().get("/food/search/Unknown").then().statusCode(404);
    }

    @Test
    public void testCreateValidatesInput() {
        given()
                .contentType("application/json")
                .body("{\"name\":\"\",\"restaurantName\":\"Test Kitchen\",\"price\":-1}")
                .when().post("/food")
                .then().statusCode(400);

        io.restassured.response.Response response = given()
                .contentType("application/json")
                .body("{\"name\":\"Taco\",\"restaurantName\":\"Test Kitchen\",\"price\":2.50}")
                .when().post("/food")
                .then().statusCode(201)
                .body("name", equalTo("Taco"))
                .body("price", equalTo(2.50f))
                .extract().response();

        int id = response.path("id");
        assertTrue(response.header("Location").endsWith("/food/" + id));

        given().when().delete("/food/" + id).then().statusCode(204);
    }
}
