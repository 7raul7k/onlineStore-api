package ro.myclass.onlineStoreapi.repo;

import org.aspectj.weaver.ast.Or;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OnlineStoreApiApplication.class)
@Transactional
class OrderDetailRepoTest {

    @Autowired
    OrderDetailRepo orderDetailRepo;
    @BeforeEach
    public void clean(){
        orderDetailRepo.deleteAll();

    }

    @Test
    public void getOrderDetailByProductIdAndPrice(){

        Product product = Product.builder().name("mouse lg s2314j")
                .id(2L)
                .stock(600)
                .price(300)
                .image("https://linkpicture.com/en/?")
                .build();
        Order order = Order.builder().customer(Customer.builder().fullName("Eduard Miculescu").email("eduardmiculescu@gmail.com").password("eduardeduard").build()).id(1L).build();
        OrderDetail  orderDetail=OrderDetail.builder().quantity(2).product(product).price(600.0).order(order).build();

        orderDetailRepo.save(orderDetail);

        assertEquals(orderDetail,this.orderDetailRepo.getOrderDetailByProductIdAndPrice(product.getId(), orderDetail.getPrice()).get() );
    }

//    @Test
//    public void findOrderDetailByProductId(){
//        Product product = Product.builder().name("TV samsung 24 inch").stock(250).price(5000).id(3L).build();
//
//        Customer customer = Customer.builder().email("popescuadrian@gmail.com").password("popoescupopescu").build();
//        Order order = Order.builder().id(2L).orderDate(LocalDate.now()).customer(customer).id(1L).build();
//
//
//        OrderDetail orderDetail = OrderDetail.builder().order(order).product(product).price(5000.0).quantity(1).build();
//
//        orderDetailRepo.save(orderDetail);
//
//        assertEquals(Optional.of(orderDetail),this.orderDetailRepo.findOrderDetailByProductIdAndOrderId(product.getId(), order.getId()));
//
//    }

//    @Test
//    public void getAllOrderDetails(){
//        Order order = Order.builder().id(1L).orderDate(LocalDate.of(2021,1,17)).customer( Customer.builder().email("popescuandrei@gmail.com").password("popoescu32popescu").fullName("Popescu Andrei").id(1L).build()).build();
//
//        Product product = Product.builder().id(5L).name("Casti gaming xtrify").stock(250).price(400).build();
//
//        OrderDetail orderDetail = OrderDetail.builder().order(order).product(product).price(250).quantity(1).build();
//
//
//        orderDetailRepo.save(orderDetail);
//
//        List<OrderDetail> list = new ArrayList<>();
//        list.add(orderDetail);
//
//        assertEquals(list,this.orderDetailRepo.getAllOrderDetails());
//    }
//
}