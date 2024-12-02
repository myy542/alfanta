
package alfanta;
import java.sql.SQLException;
import java.util.Scanner;


public class Alfanta{

    public static void main(String[] args) throws SQLException {
       
       
         Scanner sc = new Scanner(System.in);
    boolean exit = true;
    do{
        System.out.println("\n-----------------------------------------");
        System.out.println("WELCOME TO LOAN MANAGEMENT APP");
        System.out.println("-----------------------------------------");
        System.out.println("1. Applicants");
        System.out.println("2. Loans");
        System.out.println("3. Records");
        System.out.println("4. Exit");

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
       
        switch(choice){
            case 1:
                applicant ap = new applicant();
                ap.applicantsMenu();
                break;
               
            case 2:
                LOANS ln = new LOANS();
                ln.loansMenu();
                break;
               
            case 3:
                RECORDS lr = new RECORDS();
                lr.recordsMenu();
                break;

            case 4:
                System.out.print("Exit Selected... Type 'yes' to continue: ");
                String resp = sc.next();
                if(resp.equalsIgnoreCase("Yes")){
                exit = false;
                }
                break;
           
        }
       }while(exit);
    }
   
    }
    
    

