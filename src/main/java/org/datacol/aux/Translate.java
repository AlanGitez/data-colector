package org.datacol.aux;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;


public class Translate {

    public static JsonArray translateAll(RowSet<Row> rows){

        JsonArray jsonArray = new JsonArray();
        for(Row row : rows){
            jsonArray.add(row.toJson());
        }

        return jsonArray;
    }

    public static JsonObject translate(Row row){

        return row.toJson();
    }
}
