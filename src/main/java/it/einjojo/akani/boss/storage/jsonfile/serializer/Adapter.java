package it.einjojo.akani.boss.storage.jsonfile.serializer;

import com.google.gson.JsonSerializer;

public interface Adapter<T> extends JsonSerializer<T>, com.google.gson.JsonDeserializer<T> {
}
