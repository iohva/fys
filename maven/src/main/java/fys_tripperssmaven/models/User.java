
package fys_tripperssmaven.models;

public class User {
    
    public final String ADMIN = "admin";
    public final String SERVICE = "service";
    public final String COMPENSATION = "compensation";
    
    private final int id;
    private final int role;
    
    private String firstName;
    private String infix;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    
    public User(int pId, String pFirstName, String pInfix, 
                String pLastName, String pDateOfBirth, String pPhoneNumber, 
                String pEmail, String pUsername, String pPassword, int pRole){
        
        id = pId;
        firstName = pFirstName;
        infix = pInfix;
        lastName = pLastName;
        dateOfBirth = pDateOfBirth;
        phoneNumber = pPhoneNumber;
        email = pEmail;
        username = pUsername;
        password = pPassword;
        role = pRole;
    }

    public User(int pId, int pRole) {
        id = pId;
        role = pRole;
    }
    
    public int getId(){
        return id;
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public void setFirstname(String pFirstName){
        firstName = pFirstName;
    }
    
    public String getInfix(){
        return infix;   
    }
    
    public void setInfix(String pInfix){
        infix = pInfix;
    }
    
    public String getLastName(){
        return lastName;   
    }
    
    public void setLastName(String pLastName){
        lastName = pLastName;
    }
    
    public String getDateOfBirth(){
        return dateOfBirth;   
    }
    
    public void setDateOfBirth(String pDateOfBirth){
        dateOfBirth = pDateOfBirth;
    }
    
    public String getPhoneNumber(){
        return phoneNumber;   
    }
    
    public void setPhoneNumber(String pPhoneNumber){
        dateOfBirth = pPhoneNumber;
    }
    
    public String getEmail(){
        return email;
    }
    
    public void setEmail(String pEmail){
        email = pEmail;
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setUsername(String pUsername){
        username = pUsername;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String pPassword){
        password = pPassword;
    }
    
    public int getRole(){
        return role;
    }
    
    public String getFullRole(){
        switch (role) {
            case 1:
                return ADMIN;
            case 2:
                return SERVICE;
            case 3:
                return COMPENSATION;
            default:
                return "empty role";
        }
    }
}
