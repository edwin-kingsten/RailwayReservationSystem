package Interactor;

import java.util.List;

import Exceptions.InvalidUserName;
import Exceptions.UserNameAlreadyExists;
import Model.Credential;
import Model.User;

public interface UserInteractor {
	
	public void RegisterUser(User user) throws UserNameAlreadyExists;
	public boolean isUserAlreadyExist(String userName);
	public boolean isValidUser(Credential credential);
	public void addTicketId(String userName , String ticketId) throws InvalidUserName;
	public void addRefundId(String userName , String RefundId) throws InvalidUserName;
	public List<String> getTicketIds(String userName) throws InvalidUserName;
	public List<String> getRefundIds(String userName) throws InvalidUserName;
}
