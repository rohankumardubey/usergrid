/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.usergrid.rest.applications.queues;


import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.usergrid.rest.security.annotations.CheckPermissionsForPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.apache.usergrid.mq.QueueManager;
import org.apache.usergrid.mq.QueueSet;
import org.apache.usergrid.rest.AbstractContextResource;

import org.apache.commons.lang.StringUtils;

import com.sun.jersey.api.json.JSONWithPadding;
import com.sun.jersey.core.provider.EntityHolder;


@Component
@Scope("prototype")
@Produces({
        MediaType.APPLICATION_JSON, "application/javascript", "application/x-javascript", "text/ecmascript",
        "application/ecmascript", "text/jscript"
})
public class QueueSubscriberResource extends AbstractContextResource {

    static final Logger logger = LoggerFactory.getLogger( QueueSubscriberResource.class );

    QueueManager mq;
    String queuePath = "";
    String subscriberPath = "";


    public QueueSubscriberResource() {
    }


    public QueueSubscriberResource init( QueueManager mq, String queuePath ) {
        this.mq = mq;
        this.queuePath = queuePath;
        return this;
    }


    public QueueSubscriberResource init( QueueManager mq, String queuePath, String subscriberPath ) {
        this.mq = mq;
        this.queuePath = queuePath;
        this.subscriberPath = subscriberPath;
        return this;
    }


    @Path("{subPath}")
    public QueueSubscriberResource getSubPath( @Context UriInfo ui, @PathParam("subPath") String subPath )
            throws Exception {

        logger.info( "QueueSubscriberResource.getSubPath" );

        return getSubResource( QueueSubscriberResource.class ).init( mq, queuePath, subscriberPath + "/" + subPath );
    }


    @CheckPermissionsForPath
    @GET
    public JSONWithPadding executeGet( @Context UriInfo ui, @QueryParam("start") String firstSubscriberQueuePath,
                                       @QueryParam("limit") @DefaultValue("10") int limit,
                                       @QueryParam("callback") @DefaultValue("callback") String callback )
            throws Exception {

        logger.info( "QueueSubscriberResource.executeGet: " + queuePath );

        QueueSet results = mq.getSubscribers( queuePath, firstSubscriberQueuePath, limit );

        return new JSONWithPadding( results, callback );
    }


    @CheckPermissionsForPath
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONWithPadding executePost( @Context UriInfo ui, EntityHolder<Map<String, Object>> body,
                                        @QueryParam("callback") @DefaultValue("callback") String callback )
            throws Exception {

        logger.info( "QueueSubscriberResource.executePost: " + queuePath );

        return executePut( ui, body, callback );
    }


    @CheckPermissionsForPath
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONWithPadding executePut( @Context UriInfo ui, EntityHolder<Map<String, Object>> body,
                                       @QueryParam("callback") @DefaultValue("callback") String callback )
            throws Exception {

        logger.info( "QueueSubscriberResource.executePut: " + queuePath );

        Map<String, Object> json = body.getEntity();
        if ( StringUtils.isNotBlank( subscriberPath ) ) {
            return new JSONWithPadding( mq.subscribeToQueue( queuePath, subscriberPath ), callback );
        }
        else if ( ( json != null ) && ( json.containsKey( "subscriber" ) ) ) {
            String subscriber = ( String ) json.get( "subscriber" );
            return new JSONWithPadding( mq.subscribeToQueue( queuePath, subscriber ), callback );
        }
        else if ( ( json != null ) && ( json.containsKey( "subscribers" ) ) ) {
            @SuppressWarnings("unchecked") List<String> subscribers = ( List<String> ) json.get( "subscribers" );
            return new JSONWithPadding( mq.addSubscribersToQueue( queuePath, subscribers ), callback );
        }

        return null;
    }


    @CheckPermissionsForPath
    @DELETE
    public JSONWithPadding executeDelete( @Context UriInfo ui,
                                          @QueryParam("callback") @DefaultValue("callback") String callback )
            throws Exception {

        logger.info( "QueueSubscriberResource.executeDelete: " + queuePath );

        if ( StringUtils.isNotBlank( subscriberPath ) ) {
            return new JSONWithPadding( mq.unsubscribeFromQueue( queuePath, subscriberPath ), callback );
        }

        return null;
    }
}
