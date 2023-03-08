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
import ro.myclass.onlineStoreapi.dto.ProductDTO;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OnlineStoreApiApplication.class)
@Transactional
class OrderDetailRepoTest {

    @Autowired
    OrderDetailRepo orderDetailRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ProductRepo productRepo;
    @BeforeEach
    public void clean(){
        orderDetailRepo.deleteAll();
    }

    @Test
    public void getOrderDetailByOrderId(){
        CustomerDTO customerDTO = new CustomerDTO("popescuvlad@gmail.com","popescuvlad@gmail.com2023","Popescu Vlad");
        Customer customer = Customer.builder().fullName(customerDTO.getFullName())
                .email(customerDTO.getEmail())
                .password(customerDTO.getPassword())
                .build();
        customerRepo.save(customer);

        Order order = new Order((long) 1, LocalDate.now(),customer);

        orderRepo.save(order);
        ProductDTO productDTO = new ProductDTO("casti gaming HyperX",400,"https://wwww.picsart.ro/casti-gaming-hyperx",500);
        Product product = Product.builder().name(productDTO.getName()).
                stock(productDTO.getStock())
                .image(productDTO.getImage())
                .build();
         productRepo.save(product);

        OrderDetail orderDetail = new OrderDetail((long) 1,250,2,order,product);

        orderDetailRepo.save(orderDetail);

        List<OrderDetail> list = new ArrayList<>();
        list.add(orderDetail);

        assertEquals(list,this.orderDetailRepo.getOrderDetailByOrderId(1));

    }

    @Test
    public void findOrderDetailByProductIdAndOrderId(){
        CustomerDTO customerDTO = new CustomerDTO("popescuvlad@gmail.com","popescuvlad@gmail.com2023","Popescu Vlad");
        Customer customer = Customer.builder().fullName(customerDTO.getFullName())
                .email(customerDTO.getEmail())
                .password(customerDTO.getPassword())
                .build();
        customerRepo.save(customer);

        Order order = new Order((long) 1, LocalDate.now(),customer);

        orderRepo.save(order);
        ProductDTO productDTO = new ProductDTO("casti gaming HyperX",400,"https://wwww.picsart.ro/casti-gaming-hyperx",500);
        Product product = Product.builder().name(productDTO.getName()).
                stock(productDTO.getStock())
                .image(productDTO.getImage())
                .build();
        productRepo.save(product);

        OrderDetail orderDetail = new OrderDetail((long) 1,250,2,order,product);

        orderDetailRepo.save(orderDetail);

        assertEquals(orderDetail,this.orderDetailRepo.findOrderDetailByProductIdAndOrderId(1,1).get());
    }

    @Test
    public void getAllOrderDetails(){
        CustomerDTO customerDTO = new CustomerDTO("popescuvlad@gmail.com","popescuvlad@gmail.com2023","Popescu Vlad");
        Customer customer = Customer.builder().fullName(customerDTO.getFullName())
                .email(customerDTO.getEmail())
                .password(customerDTO.getPassword())
                .build();
        customerRepo.save(customer);

        Order order = new Order((long) 1, LocalDate.now(),customer);

        orderRepo.save(order);
        ProductDTO productDTO = new ProductDTO("casti gaming HyperX",400,"https://wwww.picsart.ro/casti-gaming-hyperx",500);
        Product product = Product.builder().name(productDTO.getName()).
                stock(productDTO.getStock())
                .image(productDTO.getImage())
                .build();
        productRepo.save(product);

        OrderDetail orderDetail = new OrderDetail((long) 1,250,2,order,product);

        OrderDetail orderDetail1 = new OrderDetail((long) 2,500,4,order,product);
        orderDetailRepo.save(orderDetail);
        orderDetailRepo.save(orderDetail1);

        List<OrderDetail> list = new ArrayList<>();
        list.add(orderDetail);
        list.add(orderDetail1);

        assertEquals(list,this.orderDetailRepo.getAllOrderDetails());
    }
}