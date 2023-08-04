package ro.myclass.onlineStoreapi.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ro.myclass.onlineStoreapi.OnlineStoreApiApplication;

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