import com.mongodb.client.*;
import org.bson.Document;

public class MongoDBHelper {

    private static final String URL = "mongodb://localhost:27017"; 
    private static final String DB_NAME = "bloodbank"; 
    private static final String COLLECTION_NAME = "donors"; 
    
    private static MongoClient mongoClient = MongoClients.create(URL);
    private static MongoDatabase database = mongoClient.getDatabase(DB_NAME);
    private static MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

    public static void donateBlood(String name, String bloodGroup, String contact) {
        Document doc = new Document("name", name)
                        .append("bloodGroup", bloodGroup)
                        .append("contact", contact);
        collection.insertOne(doc);
    }

    public static MongoCollection<Document> getCollection() {
        return collection;
    }
}
