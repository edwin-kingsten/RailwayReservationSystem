package Model;

public class TicketDisplayer {
	private TrainDisplayer trainDisplayer;
	private BookedTicket ticket;
		
	public TicketDisplayer(TrainDisplayer trainDisplayer, BookedTicket ticket) {
		super();
		this.trainDisplayer = trainDisplayer;
		this.ticket = ticket;
	}
	
	public TrainDisplayer getTrainDisplayer() {
		return trainDisplayer;
	}
	public BookedTicket getTicket() {
		return ticket;
	}
	
	
	
}
