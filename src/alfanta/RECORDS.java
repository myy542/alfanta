
package alfanta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


class RECORDS {
   
    public config conf;

    public RECORDS() {
        conf = new config();
    }

    public void recordsMenu() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("\n------------------------");
            System.out.println("Welcome to Loan Records Panel");
            System.out.println("------------------------");
            System.out.println("1. Specific Loan Record");
            System.out.println("2. General Loan Report");
            System.out.println("3. Exit");

            System.out.print("Enter Selection: ");
            int choice = sc.nextInt();
            RECORDS lr = new RECORDS();

            switch (choice) {
                case 1:
                    lr.generateSpecificLoanRecord();
                    break;
                case 2:
                    lr.generateGeneralLoanReport();
                    break;
                case 3:
                    System.out.println("Exiting Loan Records Panel...");
                    return; // Exit the menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.print("Do you want to continue? (Yes/No): ");
            response = sc.next();
        } while (response.equalsIgnoreCase("Yes"));
    }

    public void generateSpecificLoanRecord() {
    Scanner sc = new Scanner(System.in);
    generateGeneralLoanReport();

    System.out.print("Enter Loan ID: ");
    int loanId = sc.nextInt();

    // Check if the Loan ID exists
    while (conf.getSingleValue("SELECT Loan_ID FROM TBL_Loans WHERE Loan_ID = ?", loanId) == 0) {
        System.out.print("Loan ID doesn't exist, try again: ");
        loanId = sc.nextInt();
    }

    // SQL query to fetch loan details
    String specificQuery = "SELECT l.Loan_ID, a.a_fname, l.Loan_Amount, l.Interest_Rate, l.Loan_Status, l.Loan_Term, l.Loan_Type, l.Disbursal_Date " +
                           "FROM TBL_Loans l " +
                           "JOIN tbl_applicant a ON l.a_id = a.a_id " +
                           "WHERE l.Loan_ID = ?";

    try (Connection conn = conf.connectDB();
         PreparedStatement findRow = conn.prepareStatement(specificQuery)) {
        findRow.setInt(1, loanId);

        try (ResultSet result = findRow.executeQuery()) {
            if (result.next()) {
                // Print Loan ID and Applicant Name above the table
                System.out.println("\n--------------------- LOAN RECORD -----------------------");
                System.out.printf("| %-30s : %-20d |\n", "Loan ID", result.getInt("Loan_ID"));
                System.out.printf("| %-30s : %-20s |\n", "Applicant First Name", result.getString("Name"));
                System.out.println("---------------------------------------------------------");

                // Print the remaining loan details as a table
                System.out.printf("| %-30s : %-20.2f |\n", "Loan Amount", result.getDouble("Loan_Amount"));
                System.out.printf("| %-30s : %-20.2f |\n", "Interest Rate", result.getDouble("Interest_Rate"));
                System.out.printf("| %-30s : %-20s |\n", "Loan Status", result.getString("Loan_Status"));
                System.out.printf("| %-30s : %-20d |\n", "Loan Term (months)", result.getInt("Loan_Term"));
                System.out.printf("| %-30s : %-20s |\n", "Loan Type", result.getString("Loan_Type"));
                System.out.printf("| %-30s : %-20s |\n", "Disbursal Date", result.getString("Disbursal_Date"));
                System.out.println("---------------------------------------------------------");
            } else {
                System.out.println("No records found for the given Loan ID.");
            }
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

    public void generateGeneralLoanReport() {
        // SQL query to fetch all loans and their applicants
        String generalQuery = "SELECT l.Loan_ID, a.a_fname, l.Loan_Amount, l.Interest_Rate, l.Loan_Status, l.Loan_Term, l.Loan_Type, l.Disbursal_Date " +
                              "FROM TBL_Loans l " +
                              "JOIN tbl_applicant a ON l.a_id = a.a_id";

        String[] generalHeaders = {"Loan ID", "Applicant First Name", "Loan Amount", "Interest Rate", "Status", "Term (months)", "Loan Type", "Disbursal Date"};
        String[] generalColumns = {"Loan_ID", "a_fname", "Loan_Amount", "Interest_Rate", "Loan_Status", "Loan_Term", "Loan_Type", "Disbursal_Date"};
                           
        conf.viewRecords(generalQuery, generalHeaders, generalColumns);
    }
   
}