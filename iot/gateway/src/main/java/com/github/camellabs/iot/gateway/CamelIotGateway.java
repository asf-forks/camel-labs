/**
 * Licensed to the Camel Labs under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The licenses this file to You under the Apache License, Version 2.0
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
package com.github.camellabs.iot.gateway;

import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.github.camellabs.iot.gateway.CamelIotGatewayConstants.HEARTBEAT_ENDPOINT;
import static org.apache.camel.LoggingLevel.INFO;

@SpringBootApplication
public class CamelIotGateway extends FatJarRouter {

    @Autowired(required = false)
    RouteBuilderCallback[] routeBuilderCallbacks;

    @Override
    public void configure() throws Exception {
        if(routeBuilderCallbacks != null) {
            for (RouteBuilderCallback builderCallback : routeBuilderCallbacks) {
                builderCallback.beforeRoutesDefinition(this);
            }
        }

        from("timer:heartbeat?delay={{camellabs.iot.gateway.heartbeat.rate:5000}}").to(HEARTBEAT_ENDPOINT);
        from(HEARTBEAT_ENDPOINT).log(INFO, "Heartbeat", "Ping!");
    }

}
