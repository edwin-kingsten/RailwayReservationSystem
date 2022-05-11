package Interactor;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import Exceptions.InvalidUserName;
import Exceptions.UserNameAlreadyExists;
import Model.Credential;
import Model.User;

public class UserInteractorImpl implements UserInteractor{
	DatabaseHandler db = null;
	MongoDatabase userDatadb = null;
	MongoCollection<Document> users = null;
	
	public UserInteractorImpl() {
		db = DatabaseHandler.getInstance();
		userDatadb = db.getUserDatadb();
		users = userDatadb.getCollection("Users");
	}
	
	
	public void RegisterUser(User user) throws UserNameAlreadyExists {
		
		if(isUserAlreadyExist(user.getName())) {
			throw new UserNameAlreadyExists();
		}
		
		Document userDoc = new Document();
		
		userDoc.put("Name" , user.getName());
		userDoc.put("Password", user.getPassword());
		userDoc.put("Age" , user.getAge());
		userDoc.put("Gender" , user.getGender().name());
		userDoc.put("Email-id" , user.getEmailId());
		userDoc.put("MobileNumber" , user.getMobileNo());
		userDoc.put("TicketIds" , new ArrayList());
		userDoc.put("RefundIds", new ArrayList());
		
		users.insertOne(userDoc);
	}
	
	public boolean isUserAlreadyExist(String userName) {
		if(users.find(Filters.eq("Name", userName)).first() != null) {return true;}
		
		return false;
	}
	
	public boolean isValidUser(Credential credential) {
		boolean isValid = false;
		
		Document userDoc = users.find(Filters.eq("Name", credential.getName())).first();
		
		if(userDoc != null && ((String)userDoc.get("Password")).equals(credential.getPassword())) {
			isValid = true;
		}
		
		return isValid;
	}
	
	public void addTicketId(String userName , String ticketId) throws InvalidUserName {
		if(!isUserAlreadyExist(userName)) {
			throw new InvalidUserName();
		}
		
		users.updateOne(Filters.eq("Name" , userName), Updates.push("TicketIds", ticketId));
	}
	
	public void addRefundId(String userName , String RefundId) throws InvalidUserName {
		if(!isUserAlreadyExist(userName)) {
			throw new InvalidUserName();
		}
		
		users.updateOne(Filters.eq("Name" , userName), Updates.push("RefundIds", RefundId));
	}
   
	public List<String> getTicketIds(String userName) throws InvalidUserName {
		if(!isUserAlreadyExist(userName)) {
			throw new InvalidUserName();
		}
		List<? extends String> ticketIds = users.find(Filters.eq("Name" , userName)).first().getList("TicketIds", new String().getClass());
		
		return (List<String>) ticketIds;
	}
	
	public List<String> getRefundIds(String userName) throws InvalidUserName {
		if(!isUserAlreadyExist(userName)) {
			throw new InvalidUserName();
		}
		List<? extends String> refundIds = users.find(Filters.eq("Name" , userName)).first().getList("RefundIds", new String().getClass());
		
		return (List<String>) refundIds;
	}
}
