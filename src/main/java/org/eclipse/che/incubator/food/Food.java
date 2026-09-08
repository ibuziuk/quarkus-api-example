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

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Food extends PanacheEntity {

    @Column(length = 40, nullable = false)
    public String name;

    @Column(length = 40, nullable = false)
    public String restaurantName;

    @Column(nullable = false, precision = 10, scale = 2)
    public BigDecimal price;

}
