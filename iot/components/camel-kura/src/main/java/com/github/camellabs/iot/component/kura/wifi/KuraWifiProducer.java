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
package com.github.camellabs.iot.component.kura.wifi;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.eclipse.kura.net.wifi.WifiAccessPoint;

import java.util.List;

public class KuraWifiProducer extends DefaultProducer {

    public KuraWifiProducer(KuraWifiEndpoint endpoint) {
        super(endpoint);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        List<WifiAccessPoint> wifiAccessPoints = getEndpoint().wifiAccessPoints();
        exchange.getIn().setBody(wifiAccessPoints);
    }

    @Override
    public KuraWifiEndpoint getEndpoint() {
        return (KuraWifiEndpoint) super.getEndpoint();
    }

}