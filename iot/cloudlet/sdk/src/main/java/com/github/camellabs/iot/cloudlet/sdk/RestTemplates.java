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
package com.github.camellabs.iot.cloudlet.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static java.util.Collections.singletonList;

public final class RestTemplates {

    private final static Logger LOG = LoggerFactory.getLogger(RestTemplates.class);

    private RestTemplates() {
    }

    public static RestTemplate defaultRestTemplate() {
        LOG.debug("Creating new default RestTemplate instance.");
        ObjectMapper objectMapper = new ObjectMapper().
                configure(FAIL_ON_UNKNOWN_PROPERTIES, false).configure(FAIL_ON_EMPTY_BEANS, false).
                setSerializationInclusion(NON_NULL);
        objectMapper.getSerializationConfig().getDefaultVisibilityChecker().
                withFieldVisibility(PUBLIC_ONLY);
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        return new RestTemplate(singletonList(jacksonConverter));
    }

}
