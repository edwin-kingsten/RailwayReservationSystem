package Model;

import java.util.List;

public class User {
	private String name;
	private String password;
	private Gender gender;
	private Integer age;
	private String emailId;
	private String mobileNo;
	
	public User(String name, String password ,Gender gender ,  Integer age, String emailId, String mobileNo) {
		super();
		this.name = name;
		this.password = password;
		this.gender = gender;
		this.age = age;
		this.emailId = emailId;
		this.mobileNo = mobileNo;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}
    
	public Gender getGender() {
		return gender;
	}

	public Integer getAge() {
		return age;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getMobileNo() {
		return mobileNo;
	}	
}