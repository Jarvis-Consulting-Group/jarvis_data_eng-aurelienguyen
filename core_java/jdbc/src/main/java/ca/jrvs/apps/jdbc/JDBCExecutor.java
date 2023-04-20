package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExecutor {
    public static void main(String... args){
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "hplussport",
                "postgres", "password");

        try{
            Connection connection = dcm.getConnection();
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM CUSTOMER");
//            while (resultSet.next()){
//                System.out.println(resultSet.getInt(1));
//            }
            CustomerDAO customerDAO = new CustomerDAO(connection);
//            Customer customer = new Customer();
//            customer.setFirstName("George");
//            customer.setLastName("Washington");
//            customer.setEmail("g.washington@wh.gov");
//            customer.setPhone("(555) 555-0932");
//            customer.setAddress("1234 Main St");
//            customer.setCity("Mount Vernon");
//            customer.setState("VA");
//            customer.setZipCode("22121");
//
//            customerDAO.create(customer);
//            Customer customer = customerDAO.findById(10000);
//            System.out.println(customer.getFirstName() + " " + customer.getLastName() + " " + customer.getEmail());
//            customer.setEmail("gwashington@wh.gov");
//            customer = customerDAO.update(customer);
//            System.out.println(customer.getFirstName() + " " + customer.getLastName() + " " + customer.getEmail());
//            customer.setEmail("gwashington@wh.gov");

            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Adams");
            customer.setEmail("j.adams@wh.gov");
            customer.setPhone("(555) 555-9846");
            customer.setAddress("1234 Main St");
            customer.setCity("Arlington");
            customer.setState("VA");
            customer.setZipCode("01243");

            Customer dbCustomer = customerDAO.create(customer);
            System.out.println(dbCustomer);
            dbCustomer = customerDAO.findById(dbCustomer.getId());
            System.out.println(dbCustomer);
            dbCustomer.setEmail("john.adams@wh.gov");
            dbCustomer = customerDAO.update(dbCustomer);
            System.out.println(dbCustomer);
            customerDAO.delete(dbCustomer.getId());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
