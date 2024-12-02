
package alfanta;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;

class LOANS {
   
  public void loansMenu() throws SQLException {
        Scanner sc = new Scanner(System.in);
        String response;
        do {
            System.out.println("\n------------------------");
            System.out.println("Welcome to Loans Panel");
            System.out.println("------------------------");
            System.out.println("1. Add Loan");
            System.out.println("2. View Loans");
            System.out.println("3. Update Loan");
            System.out.println("4. Delete Loan");
            System.out.println("5. Exit");

            System.out.print("Enter Selection: ");
            int choice = sc.nextInt();
            LOANS ln = new LOANS();

            switch (choice) {
                case 1:
                    ln.addLoan();
                    ln.viewLoans();
                    break;
                case 2:
                    ln.viewLoans();
                    break;
                case 3:
                    ln.viewLoans();
                    ln.updateLoan();
                    ln.viewLoans();
                    break;
                case 4:
                    ln.viewLoans();
                    ln.deleteLoan();
                    ln.viewLoans();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
            System.out.print("Do you want to continue? (Yes/No): ");
            response = sc.next();
        } while (response.equalsIgnoreCase("Yes"));
    }

      public void addLoan() throws SQLException {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        // Applicant ID validation
        System.out.print("Applicant ID: ");
        int applicantId = sc.nextInt();
        while (conf.getSingleValue("SELECT a_id FROM tbl_applicant WHERE a_id=?", applicantId) == 0) {
            System.out.println("Invalid Applicant ID. Please enter a valid Applicant ID.");
            applicantId = sc.nextInt();
        }

        // Loan Amount validation
        System.out.print("Loan Amount: ");
        double loanAmount = sc.nextDouble();
        while (loanAmount <= 0) {
            System.out.println("Loan amount must be positive. Please enter a valid loan amount.");
            loanAmount = sc.nextDouble();
        }

        // Interest Rate validation
        System.out.print("Interest Rate: ");
        double interestRate = sc.nextDouble();
        while (interestRate <= 0) {
            System.out.println("Interest rate must be positive. Please enter a valid interest rate.");
            interestRate = sc.nextDouble();
        }

        // Loan Status validation (Choices)
        System.out.println("Choose Loan Status:");
        System.out.println("1. Approved");
        System.out.println("2. Rejected");
        System.out.println("3. Pending");
        System.out.print("Enter your choice (1-3): ");
        int statusChoice = sc.nextInt();
        String loanStatus;
        switch (statusChoice) {
            case 1:
                loanStatus = "Approved";
                break;
            case 2:
                loanStatus = "Rejected";
                break;
            case 3:
                loanStatus = "Pending";
                break;
            default:
                System.out.println("Invalid choice. Setting status to 'Pending' by default.");
                loanStatus = "Pending";
                break;
        }

        // Loan Term validation
        System.out.print("Loan Term (in months): ");
        int loanTerm = sc.nextInt();
        while (loanTerm <= 0) {
            System.out.println("Loan term must be a positive number of months. Please enter a valid term.");
            loanTerm = sc.nextInt();
        }

        // Loan Type validation (Choices)
        System.out.println("Choose Loan Type:");
        System.out.println("1. Personal");
        System.out.println("2. Mortgage");
        System.out.println("3. Auto");
        System.out.print("Enter your choice (1-3): ");
        int typeChoice = sc.nextInt();
        String loanType;
        switch (typeChoice) {
            case 1:
                loanType = "Personal";
                break;
            case 2:
                loanType = "Mortgage";
                break;
            case 3:
                loanType = "Auto Mobile";
                break;
            default:
                System.out.println("Invalid choice. Setting type to 'personal' by default.");
                loanType = "personal";
                break;
        }

        // Disbursal Date validation (Format check)
        System.out.print("Disbursal Date (YYYY-MM-DD): ");
        String disbursalDate = sc.next();
        while (!isValidDate(disbursalDate)) {
            System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            disbursalDate = sc.next();
        }

        // SQL query to insert the loan data into the database
        String qry = "INSERT INTO TBL_Loans (a_id, Loan_Amount, Interest_Rate, Loan_Status, Loan_Term, Loan_Type, Disbursal_Date) VALUES (?, ?, ?, ?, ?, ?, ?)";
       
        // Insert record using the config class method
        conf.addRecord(qry, applicantId, loanAmount, interestRate, loanStatus, loanTerm, loanType, disbursalDate);
       
        // Success message
        System.out.println("Loan added successfully.");
    }

    // Helper method to validate the date format (YYYY-MM-DD)
    public boolean isValidDate(String date) {
        // Regex to match the date format YYYY-MM-DD
        String regex = "^(\\d{4})-(\\d{2})-(\\d{2})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(date).matches();
    }


    public void viewLoans() {
        String loanQuery = "SELECT * FROM TBL_Loans";
        String[] loanHeaders = {"Loan ID", "Applicant ID", "Loan Amount", "Interest Rate", "Loan Status", "Loan Term", "Loan Type", "Disbursal Date"};
        String[] loanColumns = {"Loan_ID", "a_id", "Loan_Amount", "Interest_Rate", "Loan_Status", "Loan_Term", "Loan_Type", "Disbursal_Date"};
        config conf = new config();
        conf.viewRecords(loanQuery, loanHeaders, loanColumns);
    }

     public void updateLoan() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();

    System.out.print("Enter Loan ID to update: ");
    int loanId = sc.nextInt();

    while (conf.getSingleValue("SELECT Loan_ID FROM TBL_Loans WHERE Loan_ID=?", loanId) == 0) {
        System.out.println("Selected Loan ID doesn't exist!");
        System.out.print("Select Loan ID again: ");
        loanId = sc.nextInt();
    }

    System.out.print("New Applicant ID: ");
    int applicantId = sc.nextInt();
    while (conf.getSingleValue("SELECT a_id FROM tbl_applicant WHERE a_id=?", applicantId) == 0) {
        System.out.println("Invalid Applicant ID. Please enter a valid Applicant ID.");
        applicantId = sc.nextInt();
    }

    System.out.print("New Loan Amount: ");
    double loanAmount = sc.nextDouble();
    while (loanAmount <= 0) {
        System.out.println("Loan amount must be positive. Please enter a valid loan amount.");
        loanAmount = sc.nextDouble();
    }

    System.out.print("New Interest Rate: ");
    double interestRate = sc.nextDouble();
    while (interestRate <= 0) {
        System.out.println("Interest rate must be positive. Please enter a valid interest rate.");
        interestRate = sc.nextDouble();
    }

    System.out.print("New Loan Status (Approved/Rejected/Pending): ");
    String loanStatus = sc.next();
    while (!loanStatus.equalsIgnoreCase("Approved") && !loanStatus.equalsIgnoreCase("Rejected") && !loanStatus.equalsIgnoreCase("Pending")) {
        System.out.println("Invalid Loan Status. Please enter 'Approved', 'Rejected', or 'Pending'.");
        loanStatus = sc.next();
    }

    System.out.print("New Loan Term (in months): ");
    int loanTerm = sc.nextInt();
    while (loanTerm <= 0) {
        System.out.println("Loan term must be a positive number of months. Please enter a valid term.");
        loanTerm = sc.nextInt();
    }

    System.out.print("New Loan Type (personal/mortgage/auto): ");
    String loanType = sc.next();
    while (!loanType.equalsIgnoreCase("personal") && !loanType.equalsIgnoreCase("mortgage") && !loanType.equalsIgnoreCase("auto")) {
        System.out.println("Invalid Loan Type. Please enter 'personal', 'mortgage', or 'auto'.");
        loanType = sc.next();
    }

    System.out.print("New Disbursal Date (YYYY-MM-DD): ");
    String disbursalDate = sc.next();
    while (!isValidDate(disbursalDate)) {
        System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
        disbursalDate = sc.next();
    }

    String qry = "UPDATE TBL_Loans SET a_id = ?, Loan_Amount = ?, Interest_Rate = ?, Loan_Status = ?, Loan_Term = ?, Loan_Type = ?, Disbursal_Date = ? WHERE Loan_ID = ?";

    conf.updateRecord(qry, applicantId, loanAmount, interestRate, loanStatus, loanTerm, loanType, disbursalDate, loanId);

    System.out.println("Loan updated successfully.");
}

    public void deleteLoan() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        System.out.print("Enter Loan ID to delete: ");
        int loanId = sc.nextInt();

        while (conf.getSingleValue("SELECT Loan_ID FROM TBL_Loans WHERE Loan_ID=?", loanId) == 0) {
            System.out.println("Selected Loan ID doesn't exist!");
            System.out.print("Select Loan ID again: ");
            loanId = sc.nextInt();
        }

        String qry = "DELETE FROM TBL_Loans WHERE Loan_ID=?";
        conf.deleteRecord(qry, loanId);
        System.out.println("Loan deleted successfully.");
    }
}