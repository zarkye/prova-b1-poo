package br.cesul.musicbattle.repository;

import br.cesul.musicbattle.config.MongoConfig;
import br.cesul.musicbattle.model.Track;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class TrackRepository {
    private final MongoCollection<Track> col =
            MongoConfig.getDatabase().getCollection("tracks", Track.class);

    public List<Track> findAll() {
        var out = new ArrayList<Track>();
        col.find().into(out);
        return out;
    }

    public List<Track> findByQuery(String text) {
        if (text == null || text.isBlank()) return findAll();
        String esc = Document.parse("{x:\""+text+"\"}").getString("x");
        var out = new ArrayList<Track>();
        col.find(Filters.or(
                Filters.regex("title", ".*"+esc+".*", "i"),
                Filters.regex("artist"," .*"+esc+".*", "i")
        )).into(out);
        return out;
    }

    public void insert(Track t) { col.insertOne(t); }

    public void update(Track t) {
        col.replaceOne(Filters.eq("_id", t.getId()), t, new ReplaceOptions().upsert(false));
    }

    public void incrementVote(ObjectId id) {
        col.updateOne(Filters.eq("_id", id), new Document("$inc", new Document("votes", 1)));
    }
}
