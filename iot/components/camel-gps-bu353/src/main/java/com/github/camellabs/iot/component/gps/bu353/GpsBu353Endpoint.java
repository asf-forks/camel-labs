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
package com.github.camellabs.iot.component.gps.bu353;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

import java.util.Set;

@UriEndpoint(scheme = "gps-bu353", title = "GPS BU353", syntax = "gps-bu353:label", consumerClass = GpsBu353Consumer.class, label = "iot,messaging,gps")
public class GpsBu353Endpoint extends DefaultEndpoint {

    @UriParam(defaultValue = "5000", description = "How long the consumer should wait between each scan (in miliseconds).")
    private int scanningInterval = 5000;

    @UriParam(defaultValue = "new SerialGpsCoordinatesSource()")
    private GpsCoordinatesSource gpsCoordinatesSource;

    public GpsBu353Endpoint(String endpointUri, GpsBu353Component component) {
        super(endpointUri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        throw new UnsupportedOperationException("GPS BU353 component supports only consumer endpoints.");
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new GpsBu353Consumer(this, processor);
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        gpsCoordinatesSource = resolveGpsCoordinatesSource();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    // Configuration getters and setters

    public int getScanningInterval() {
        return scanningInterval;
    }

    public void setScanningInterval(int scanningInterval) {
        this.scanningInterval = scanningInterval;
    }

    /**
     * GPS coordinates source. Usually serial port (/dev/ttyUSB0).
     */
    public GpsCoordinatesSource getGpsCoordinatesSource() {
        return gpsCoordinatesSource;
    }

    public void setGpsCoordinatesSource(GpsCoordinatesSource gpsCoordinatesSource) {
        this.gpsCoordinatesSource = gpsCoordinatesSource;
    }

    // Helpers

    protected GpsCoordinatesSource resolveGpsCoordinatesSource() {
        if(gpsCoordinatesSource == null) {
            Set<GpsCoordinatesSource> sources = getCamelContext().getRegistry().findByType(GpsCoordinatesSource.class);
            if(sources.size() == 1) {
                return sources.iterator().next();
            } else {
                return new SerialGpsCoordinatesSource();
            }
        } else {
            return gpsCoordinatesSource;
        }
    }

}
