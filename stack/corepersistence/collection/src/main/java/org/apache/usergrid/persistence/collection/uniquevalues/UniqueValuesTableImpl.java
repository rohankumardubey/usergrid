/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.usergrid.persistence.collection.uniquevalues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


public class UniqueValuesTableImpl implements UniqueValuesTable {
    private static final Logger logger = LoggerFactory.getLogger( UniqueValuesTableImpl.class );

    @Override
    public UUID lookupOwner(String entityType, String propertyName, String propertyValue) {
        return null;
    }

    @Override
    public void reserve(UUID owner, String entityType, String propertyName, String propertyValue) {
    }

    @Override
    public void commit(UUID owner, String entityType, String propertyName, String propertyValue) {
    }

    @Override
    public void cancel(String entityType, String propertyName, String propertyValue) {
    }
}