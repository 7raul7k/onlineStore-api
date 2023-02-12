package ro.myclass.onlineStoreapi.services;

import org.springframework.stereotype.Service;
import ro.myclass.onlineStoreapi.exceptions.CustomerNotFoundException;
import ro.myclass.onlineStoreapi.exceptions.CustomerWasFoundException;
import ro.myclass.onlineStoreapi.exceptions.ListEmptyException;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.repo.CustomerRepo;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {

        this.customerRepo = customerRepo;
    }

    public void showAllCustomers(){
        List<Customer> customerList = this.customerRepo.getAllCustomers();

        if(customerList.isEmpty()){
            throw new ListEmptyException();
        }else{
            for (Customer m : customerList){
                System.out.println(m);
            }
        }
    }
    public void addCustomer(Customer customer){
        Optional<Customer> optionalCustomer = this.customerRepo.getCustomerByEmail(customer.getEmail());

        if(optionalCustomer.isEmpty()){
           this.customerRepo.save(customer);
        }else{
            throw new CustomerWasFoundException();
        }
    }

    public void removeCustomer(String email){
        Optional<Customer> customer = this.customerRepo.getCustomerByEmail(email);

        if(customer.isEmpty()){
            throw new CustomerNotFoundException();
        }else{
            this.customerRepo.delete(customer.get());
        }
    }

    public Customer returnCustomerByEmail(String email){
        return this.customerRepo.getCustomerByEmail(email).get();
    }
}
