package br.cesul.musicbattle.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;

public final class MongoConfig {
    private static MongoDatabase db;
    private MongoConfig(){}

    public static MongoDatabase getDatabase() {
        if (db == null) {
            var pojo = PojoCodecProvider.builder().automatic(true).build();
            var settings = MongoClientSettings.builder()
                    .codecRegistry(CodecRegistries.fromRegistries(
                            MongoClientSettings.getDefaultCodecRegistry(),
                            CodecRegistries.fromProviders(pojo)
                    ))
                    .build();
            MongoClient client = MongoClients.create(settings);
            db = client.getDatabase("cesul_music_battle");
        }
        return db;
    }
}
