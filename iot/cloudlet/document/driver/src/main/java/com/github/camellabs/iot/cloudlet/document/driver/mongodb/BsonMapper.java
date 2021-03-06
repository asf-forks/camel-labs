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
package com.github.camellabs.iot.cloudlet.document.driver.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.camel.TypeConverter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BsonMapper {

    private final TypeConverter typeConverter;

    @Autowired
    public BsonMapper(TypeConverter typeConverter) {
        this.typeConverter = typeConverter;
    }

    public static DBObject bsonToJson(DBObject bson) {
        DBObject json = new BasicDBObject(bson.toMap());
        Object id = json.get("_id");
        if (id != null) {
            json.removeField("_id");
            json.put("id", id.toString());
        }
        return json;
    }

    public static DBObject jsonToBson(DBObject json) {
        DBObject bson = new BasicDBObject(json.toMap());
        Object id = bson.get("id");
        if (id != null) {
            bson.removeField("id");
            bson.put("_id", new ObjectId(id.toString()));
        }
        return bson;
    }

    public DBObject pojoToBson(Object pojo) {
        return jsonToBson(typeConverter.convertTo(DBObject.class, pojo));
    }

}
