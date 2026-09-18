import java.util.Scanner;

public class User {
    String name, Productname, location;
    String mobileNo;

    User(String name, String Productname, String mobileNo, String location) {
        this.name = name;
        this.Productname = Productname;
        this.mobileNo = mobileNo;
        this.location = location;
    }

    void input(Scanner op) {
        System.out.println("Enter your name");
        name = op.nextLine();

        System.out.println("Enter your productname");
        Productname = op.nextLine();

        System.out.println("Enter your mobile number");
        mobileNo = op.nextLine();

        System.out.println("Enter the location, where the object was lost");
        location = op.nextLine();
    }
}