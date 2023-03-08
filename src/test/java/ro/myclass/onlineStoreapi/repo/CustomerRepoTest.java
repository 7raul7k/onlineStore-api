package ro.myclass.onlineStoreapi.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ro.myclass.onlineStoreapi.OnlineStoreApiApplication;
import ro.myclass.onlineStoreapi.dto.CustomerDTO;
import ro.myclass.onlineStoreapi.models.Customer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OnlineStoreApiApplication.class)
@Transactional
class CustomerRepoTest {

    @Autowired
    public CustomerRepo customerRepo;

    @BeforeEach
    public void clean() {
        customerRepo.deleteAll();
    }


    @Test
    public void getCustomerByEmail(){
        CustomerDTO customerDTO = new CustomerDTO("popescuvlad@gmail.com","popescuvlad@gmail.com2023","Popescu Vlad");
        Customer customer = Customer.builder().fullName(customerDTO.getFullName())
                .email(customerDTO.getEmail())
                .password(customerDTO.getPassword())
                .build();

        customerRepo.save(customer);

        assertEquals(customer,this.customerRepo.getCustomerByEmail("popescuvlad@gmail.com").get());
    }

    @Test
    public void getAllCustomers() {
        CustomerDTO customerDTO = new CustomerDTO("popescuvlad@gmail.com", "popescuvlad@gmail.com2023", "Popescu Vlad");
        CustomerDTO customerDTO1 = new CustomerDTO("ionescu.andrei@gmail.com", "ionescu.andrei@gmail.com2023", "Ionescu Andrei");
        CustomerDTO customerDTO2 = new CustomerDTO("suciuflorin@gmail.com", "suciuflorin@gmail.com2023", "Suciu Florin");
        Customer customer = Customer.builder().fullName(customerDTO.getFullName())
                .email(customerDTO.getEmail())
                .password(customerDTO.getPassword())
                .build();
        Customer customer1 = Customer.builder().fullName(customerDTO1.getFullName())
                .email(customerDTO1.getEmail())
                .password(customerDTO1.getPassword())
                .build();
        Customer customer2 = Customer.builder().fullName(customerDTO2.getFullName())
                .email(customerDTO2.getEmail())
                .password(customerDTO2.getPassword())
                .build();
        customerRepo.save(customer);
        customerRepo.save(customer1);
        customerRepo.save(customer2);
        List<Customer> list = new ArrayList<>();
        list.add(customer);
        list.add(customer1);
        list.add(customer2);

        assertEquals(list,this.customerRepo.getAllCustomers());


    }

    @Test
    public void getCustomerById(){
        CustomerDTO customerDTO = new CustomerDTO("popescuvlad@gmail.com","popescuvlad@gmail.com2023","Popescu Vlad");
        Customer customer = Customer.builder().fullName(customerDTO.getFullName())
                .email(customerDTO.getEmail())
                .password(customerDTO.getPassword())
                .build();
       customerRepo.save(customer);

       assertEquals(customer,this.customerRepo.getCustomerById(1).get());
    }
}