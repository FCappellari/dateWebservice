package persistence;

import java.net.UnknownHostException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import model.User;

public enum MongoDBHelper {
	INSTANCE;

	private DB db;
	private Datastore datastore;

	/*private final String SERVER_URL = "...";
	private final int SERVER_PORT = ...;
	private final String USERNAME= "...";
	private final String PASSWORD = "...";
	private final String DATABASE_NAME = "...";*/	

	private MongoDBHelper() {

	    MongoClientURI uri  = new MongoClientURI("mongodb://sa:sa@ds045054.mongolab.com:45054/teste");
		MongoClient mongoClient = new MongoClient(uri);
		this.db = mongoClient.getDB("teste");
		
		Morphia morphia = new Morphia();	

		this.datastore = morphia.createDatastore(mongoClient, "teste");

		morphia.mapPackage("package");

	}

	public DB getDB() {
	    return this.db;
	}

	public Datastore getDatastore() {
	    return this.datastore;
	}
}
