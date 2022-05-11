package Model;

public class Credential {
	private String name;
	private String password;
	
	
	public Credential(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}


	public String getName() {
		return name;
	}


	public String getPassword() {
		return password;
	}
	
}
