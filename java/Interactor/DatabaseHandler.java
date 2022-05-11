package Interactor;

import com.mongodb.client.MongoDatabase;

import java.util.logging.Level;

import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.MongoClient;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


public class DatabaseHandler {
	 
	private static DatabaseHandler soleInstance = null;
	
	private MongoClient client = null; 	
	private MongoDatabase traindb = null;
	private MongoDatabase stationdb = null; 
	private MongoDatabase seatsdb = null;
	private MongoDatabase usersDatadb = null;
	private MongoDatabase ticketsDatadb = null;
	
	
	private DatabaseHandler() {
		Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.OFF); 
		client = new MongoClient("localhost" , 27017);
	}
	
	public MongoDatabase getTrainsDb() {
		if (traindb == null) {
			traindb = client.getDatabase("Trains");
		}
		return traindb;
	}
	
	public MongoDatabase getStationsDb() {
		if(stationdb == null) {
			stationdb = client.getDatabase("Stations");
		}
		
		return stationdb;
	}
	
	public MongoDatabase getSeatsdb() {
		if(seatsdb == null) {
			seatsdb = client.getDatabase("Seats");
		}
		
		return seatsdb;
	}
	
	public MongoDatabase getUserDatadb() {
		if(usersDatadb == null) {
			usersDatadb = client.getDatabase("UserData");
		}
		
		return usersDatadb;
	}
	
	public MongoDatabase getTicketdb() {
		if(ticketsDatadb == null) {
			ticketsDatadb = client.getDatabase("TicketsData");
		}
		
		return ticketsDatadb;
	}
	
	public static DatabaseHandler getInstance() {
		if(soleInstance == null) {
			soleInstance = new DatabaseHandler();
		}
		
		return soleInstance;
	}
}
