package app.util;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoConnector {

    private final String mongoDriver = "org.mongodb.driver";
    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public MongoConnector(String host, int port, String ddbbName){
        initLogger();
        mongoClient = new MongoClient(host, port);
        mongoDatabase = mongoClient.getDatabase(ddbbName);
    }

    public MongoCollection<Document> getCollection(String docName){
        return mongoDatabase.getCollection(docName);
    }

    private void initLogger() {
        Logger mongoLogger = Logger.getLogger(mongoDriver);
        mongoLogger.setLevel(Level.SEVERE);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
