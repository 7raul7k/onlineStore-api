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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OnlineStoreApiApplication.class)
@Transactional
class OrderRepoTest {

    @Autowired
    OrderRepo orderRepo;




    @BeforeEach
    public void clean(){
        orderRepo.deleteAll();

    }

    @Test
    public void getOrderByCustomerId(){

        Order order = Order.builder().orderDate(LocalDate.now()).customer(Customer.builder().fullName("Popescu Vlad").email("popescuvlad@gmail.com").password("popescuvlad@gmail.com2023").id(1L).id((long) 1).build()).build();

        orderRepo.save(order);

        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        assertEquals(orderList,this.orderRepo.getOrderByCustomerId(1));

    }

//    @Test
//    public void getOrderByIdAndCustomerId() {
//        Customer customer = Customer.builder().email("alexandru.toma3223@gmail.com").password("alextoma@gmail.com2022").fullName("Toma Alexandru").id((long) 1).build();
//
//
//        Order order = Order.builder().orderDate(LocalDate.now()).customer(customer).build();
//
//        orderRepo.save(order);
//
//        assertEquals(order,this.orderRepo.getOrderByIdAndCustomerId(1, customer.getId()).get());
//    }




}