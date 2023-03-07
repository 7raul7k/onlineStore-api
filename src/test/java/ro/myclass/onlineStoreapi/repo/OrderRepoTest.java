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
import ro.myclass.onlineStoreapi.models.Order;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OnlineStoreApiApplication.class)
@Transactional
class OrderRepoTest {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CustomerRepo customerRepo;

    @BeforeEach
    public void clean(){
        orderRepo.deleteAll();
    }

    @Test
    public void getOrderByCustomerId(){

       Customer customer = new Customer("eduard.mocanu@gmail.com","eduard.mocanu@gmail.com2022","Eduard Mocanu");

        customerRepo.save(customer);

        Order order = new Order((long) 1 , LocalDate.now(),customer);

        orderRepo.save(order);


        assertEquals(order,this.orderRepo.getOrderById(1).get());

    }

    @Test
    public void getOrderByCustomerEmailAndOrderDate(){

        Customer customer = new Customer("eduard.mocanu@gmail.com","eduard.mocanu@gmail.com2022","Eduard Mocanu");
        customerRepo.save(customer);

        LocalDate localDate = LocalDate.now();

        Order order = new Order((long) 1,localDate,customer);

        orderRepo.save(order);

        assertEquals(order,this.orderRepo.getOrderByCustomerEmailAndOrderDate("eduard.mocanu@gmail.com",localDate).get());
    }

    @Test
    public void getOrderByCustomerIdAndAndOrderId(){
        Customer customer = new Customer("eduard.mocanu@gmail.com","eduard.mocanu@gmail.com2022","Eduard Mocanu");
        customerRepo.save(customer);

        Order order = new Order((long) 1 ,LocalDate.now(),customer);

        orderRepo.save(order);

        assertEquals(order,this.orderRepo.getOrderByCustomerIdAndAndId(customer.getId(), (long) 1 ).get());

    }

    @Test
    public void getOrderById(){
        Customer customer = new Customer("eduard.mocanu@gmail.com","eduard.mocanu@gmail.com2022","Eduard Mocanu");
        customerRepo.save(customer);

        Order order = new Order((long) 1 ,LocalDate.now(),customer);

        orderRepo.save(order);

        assertEquals(order,this.orderRepo.getOrderById(1).get());

    }

}