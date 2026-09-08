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

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.quarkus.panache.common.Sort;

@Path("/food")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FoodResource {

    @GET
    public List<FoodResponse> list() {
        return Food.<Food>listAll(Sort.by("name")).stream()
                .map(FoodResponse::from)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Food food = Food.findById(id);
        if (food == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(FoodResponse.from(food)).build();
    }

    @POST
    @Transactional
    public Response create(@Valid FoodRequest request) {
        Food food = new Food();
        food.name = request.name;
        food.restaurantName = request.restaurantName;
        food.price = request.price;
        food.persist();
        return Response.created(URI.create("/food/" + food.id))
                .entity(FoodResponse.from(food))
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Food entity = Food.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("search/{name}")
    public Response getByName(@PathParam("name") String name) {
        Food food = Food.find("name", name).firstResult();
        if (food == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(FoodResponse.from(food)).build();
    }

    @GET
    @Path("restaurant/{restaurantName}")
    public List<FoodResponse> listByRestaurant(@PathParam("restaurantName") String restaurantName) {
        return Food.<Food>find("restaurantName", Sort.by("name"), restaurantName).list().stream()
                .map(FoodResponse::from)
                .collect(Collectors.toList());
    }
}
