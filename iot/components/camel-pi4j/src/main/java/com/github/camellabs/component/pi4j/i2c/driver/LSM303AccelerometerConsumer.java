/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.camellabs.component.pi4j.i2c.driver;

import java.io.IOException;

import com.github.camellabs.component.pi4j.i2c.I2CConsumer;
import com.github.camellabs.component.pi4j.i2c.I2CEndpoint;
import com.pi4j.io.i2c.I2CDevice;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Thx @OlivierLD
 * More details about first implementation http://www.lediouris.net/RaspberryPI/readme.html
 * Code from https://github.com/OlivierLD/raspberry-pi4j-samples/blob/master/AdafruitI2C/src/adafruiti2c/sensor/AdafruitLSM303.java
 * 
 * device doc : http://cdn.sparkfun.com/datasheets/Sensors/Magneto/DM00026454.pdf
 */
public class LSM303AccelerometerConsumer extends I2CConsumer {
    private static final transient Logger LOG = LoggerFactory.getLogger(LSM303AccelerometerConsumer.class);

    // Those 2 next addresses are returned by "sudo i2cdetect -y 1", see above.
    public final static int LSM303_ADDRESS_ACCEL = (0x32 >> 1); // 0011001x,
                                                                // 0x19
    public final static int LSM303_REGISTER_ACCEL_CTRL_REG1_A = 0x20; // 00000111
                                                                      // rw
    public final static int LSM303_REGISTER_ACCEL_CTRL_REG4_A = 0x23; // 00000000
                                                                      // rw
    public final static int LSM303_REGISTER_ACCEL_OUT_X_L_A = 0x28;

    public LSM303AccelerometerConsumer(I2CEndpoint endpoint, Processor processor, I2CDevice device) {
        super(endpoint, processor, device);

    }

    private int accel12(byte[] list, int idx) {
        int n = (0x000000ff & list[idx]) | ((0x000000ff & list[idx + 1]) << 8); // Low,
                                                                                // high
                                                                                // bytes
        if (n > 32767)
            n -= 65536; // 2's complement signed
        return n >> 4; // 12-bit resolution
    }

    @Override
    protected void createBody(Exchange exchange) throws IOException {
        LSM303Value body = readingSensors();

        LOG.debug("" + body);

        exchange.getIn().setBody(body);
    }

    protected void doStart() throws Exception {
        super.doStart();
        getDevice().write(LSM303_REGISTER_ACCEL_CTRL_REG1_A, (byte)0x27); // 00100111
        getDevice().write(LSM303_REGISTER_ACCEL_CTRL_REG4_A, (byte)0x08);

    }

    private LSM303Value readingSensors() throws IOException {
        LSM303Value ret = new LSM303Value();
        byte[] accelData = {0, 0, 0, 0, 0, 0};

        int r = getDevice().read(LSM303_REGISTER_ACCEL_OUT_X_L_A | 0x80, accelData, 0, 6);
        if (r != 6) {
            System.out.println("Error reading accel data, < 6 bytes");
        }
        ret.setX(accel12(accelData, 0));
        ret.setY(accel12(accelData, 2));
        ret.setZ(accel12(accelData, 4));

        return ret;
    }
}
