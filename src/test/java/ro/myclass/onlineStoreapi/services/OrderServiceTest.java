package ro.myclass.onlineStoreapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.myclass.onlineStoreapi.dto.CancelOrderRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderRequest;
import ro.myclass.onlineStoreapi.dto.ProductCardRequest;
import ro.myclass.onlineStoreapi.exceptions.*;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.repo.CustomerRepo;
import ro.myclass.onlineStoreapi.repo.OrderRepo;
import ro.myclass.onlineStoreapi.repo.ProductRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepo orderRepo;

    @Mock
    CustomerRepo customerRepo;

    @Mock
    ProductRepo productRepo;

    @InjectMocks
    OrderService orderService;



    @Captor
    ArgumentCaptor<Order> captor;

    @Test
    public void getAllOrdersOk(){
        Order order1 = Order.builder().orderDate(LocalDate.now()).orderDetails(new ArrayList<>()).customer(new Customer()).build();
        Order order2 = Order.builder().orderDate(LocalDate.now()).orderDetails(new ArrayList<>()).customer(new Customer()).build();
        Order order3 = Order.builder().orderDate(LocalDate.now()).orderDetails(new ArrayList<>()).customer(new Customer()).build();
        Order order4 = Order.builder().orderDate(LocalDate.now()).orderDetails(new ArrayList<>()).customer(new Customer()).build();

        List<Order> orders = new ArrayList<>();

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);

        doReturn(orders).when(orderRepo).getAllOrder();

        assertEquals(orders,this.orderService.getAllOrder());
    }

    @Test
    public void getAllOrdersError(){

        doReturn(new ArrayList<>()).when(orderRepo).getAllOrder();

        assertThrows(ListEmptyException.class,()->{
            this.orderService.getAllOrder();
        });


    }

    @Test
    public void addOrderCustomer(){

        Customer customer = Customer.builder().id(1L).fullName("Andrei Florin").email("andreiflorin@gmail.com").password("andreiflorin2023").build();


        Product product = Product.builder().id(1L).name("TV samsung 40 inch 4K").stock(300).price(2500).build();



        doReturn(Optional.of(customer)).when(customerRepo).findById(1L);
        doReturn(Optional.of(product)).when(productRepo).findById(1L);
        ProductCardRequest cardRequest = ProductCardRequest.builder().productId(1).quantity(2).build();

        List<ProductCardRequest> productCardRequests = new ArrayList<>();
        productCardRequests.add(cardRequest);

        CreateOrderRequest createOrderRequest = CreateOrderRequest.builder().productCardRequests(productCardRequests).customerId(1).build();


        this.orderService.addOrder(createOrderRequest);

        verify(orderRepo,times(1)).save(captor.capture());

        assertEquals(captor.getValue(),captor.getValue());



    }



    @Test
    public void addOrderCustomerNotFoundException(){
        doReturn(Optional.empty()).when(customerRepo).findById(0L);

        assertThrows(CustomerNotFoundException.class,()->{
            this.orderService.addOrder(new CreateOrderRequest());
        });
    }

    @Test
    public void addOrderProductNotFoundException(){
        Customer customer = Customer.builder().id(1L).fullName("Popescu Alex").build();


        doReturn(Optional.of(customer)).when(customerRepo).findById(1L);

        List<ProductCardRequest> list = new ArrayList<>();

        Product product = Product.builder().stock(500).id(1L).build();


        list.add(ProductCardRequest.builder().productId(Math.toIntExact(product.getId())).quantity(0).build());

//        doReturn(Optional.empty()).when(productRepo).getProductById(1L);

        assertThrows(ProductNotFoundException.class,()->{

            this.orderService.addOrder(CreateOrderRequest.builder().customerId(1).productCardRequests(list).build());

        });
    }

    @Test
    public void addOrderStockNotAvailableException(){
        Customer customer = Customer.builder().id(1L).fullName("Popescu Alex").build();

        doReturn(Optional.of(customer)).when(customerRepo).findById(1L);

        List<ProductCardRequest> list = new ArrayList<>();

        Product product = Product.builder().stock(1).id(2L).price(250).name("casti").build();

        list.add(ProductCardRequest.builder().productId(2).quantity(9).build());

        doReturn(Optional.of(product)).when(productRepo).findById(2L);

        assertThrows(StockNotAvailableException.class,()->{

            this.orderService.addOrder(CreateOrderRequest.builder().customerId(1).productCardRequests(list).build());

        });

    }
    @Test
    public void cancerOrderOk(){
        Customer customer = Customer.builder().id(1L).fullName("Stoica Ionut").email("stoicaionut@gmail.com").password("stoicaionut2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(customer).build();

        Product product = Product.builder().id(1L).name("Iphone 14X").price(5000).stock(250).build();



        OrderDetail orderDetail = OrderDetail.builder().id(1L).order(order).quantity(2).product(product).price(10000).build();

        order.addOrderDetails(orderDetail);
        customer.addOrder(order);

        doReturn(Optional.of(customer)).when(customerRepo).getCustomerById(1L);
        doReturn(Optional.of(product)).when(productRepo).getProductById(1L);
        CancelOrderRequest cancelOrderRequest = CancelOrderRequest.builder().orderId(1).customerId(1).build();

        this.orderService.cancelOrder(cancelOrderRequest);

        verify(orderRepo,times(1)).delete(captor.capture());

        assertEquals(captor.getValue(),captor.getValue());





    }
    @Test
    public void cancelOrderException(){
        doReturn(Optional.empty()).when(customerRepo).getCustomerById(1L);

        CancelOrderRequest cancelOrderRequest = CancelOrderRequest.builder().orderId(1).customerId(1).build();

        assertThrows(CustomerNotFoundException.class,()->{
            this.orderService.cancelOrder(cancelOrderRequest);

        });

    }

    @Test
    public void updateOrder(){
        Customer customer = Customer.builder().id(1L).fullName("Stoica Ionut").email("stoicaionut@gmail.com").password("stoicaionut2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(customer).build();

        doReturn(Optional.of(order)).when(orderRepo).getOrderByIdAndCustomerId(1L,1L);

        this.orderService.updateOrder(order);
        verify(orderRepo,times(1)).saveAndFlush(captor.capture());

        assertEquals(captor.getValue(),order);

    }

    @Test
    public void updateOrderError(){
        Customer customer = Customer.builder().id(1L).fullName("test").password("test").email("test").build();


        doReturn(Optional.empty()).when(orderRepo).getOrderByIdAndCustomerId(1L,1L);

        assertThrows(OrderNotFoundException.class,()->{
            this.orderService.updateOrder(Order.builder().customer(customer).orderDate(LocalDate.now()).id(1L).build());
        });
    }

    @Test
    public void getOrderByCustomerId(){
        Customer customer = Customer.builder().id(1L).fullName("Stoica Ionut").email("stoicaionut@gmail.com").password("stoicaionut2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(customer).build();
        Order order2 = Order.builder().id(2L).orderDate(LocalDate.now()).customer(customer).build();
        Order order3 = Order.builder().id(3L).orderDate(LocalDate.now()).customer(customer).build();

        List<Order> orderList = new ArrayList<>();

        orderList.add(order);
        orderList.add(order2);
        orderList.add(order3);
        doReturn(orderList).when(orderRepo).getOrderByCustomerId(1L);

        assertEquals(orderList,this.orderService.getOrderByCustomerId(1));


    }

    @Test
    public void getOrderByCustomerIdError(){

        doReturn(new ArrayList<>()).when(orderRepo).getOrderByCustomerId(1L);

        assertThrows(ListEmptyException.class,()->{
            this.orderService.getOrderByCustomerId(1);
        });
    }

    @Test
    public void getOrderByIdAndCustomerID(){

        Customer customer = Customer.builder().id(1L).fullName("Stoica Ionut").email("stoicaionut@gmail.com").password("stoicaionut2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(customer).build();


        doReturn(Optional.of(order)).when(orderRepo).getOrderByIdAndCustomerId(1L,1L);

        assertEquals(order,this.orderService.getOrderbyIdAndCustomerId(1,1));


    }

    @Test
    public void getOrderByIdAndCustomerIdError(){


        doReturn(Optional.empty()).when(orderRepo).getOrderByIdAndCustomerId(1L,1L);

        assertThrows(OrderNotFoundException.class,()->{
            this.orderService.getOrderbyIdAndCustomerId(1,1);
        });
    }

    @Test
    public void getOrderById(){

        Customer customer = Customer.builder().id(1L).fullName("Stoica Ionut").email("stoicaionut@gmail.com").password("stoicaionut2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(customer).build();

        doReturn(Optional.of(order)).when(orderRepo).getOrderById(1L);

        assertEquals(order,this.orderService.getOrderById(1L));

    }

    @Test
    public void getOrderByIdError(){

        doReturn(Optional.empty()).when(orderRepo).getOrderById(1L);

        assertThrows(OrderNotFoundException.class,()->{
            this.orderService.getOrderById(1);
        });
    }

    @Test
    public void getSortedOrderList(){
        Customer customer = Customer.builder().id(1L).fullName("Stoica Ionut").email("stoicaionut@gmail.com").password("stoicaionut2023").build();


        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(customer).build();
        Order order2 = Order.builder().id(2L).orderDate(LocalDate.now()).customer(customer).build();
        Order order3 = Order.builder().id(3L).orderDate(LocalDate.now()).customer(customer).build();

        List<Order> orderList = new ArrayList<>();

        orderList.add(order);
        orderList.add(order2);
        orderList.add(order3);

        doReturn(orderList).when(orderRepo).sortOrderListByDate(1L);

        assertEquals(orderList,this.orderService.sortOrderListByDate(1));
    }

    @Test
    public void getSortedOrderListError(){

        doReturn(new ArrayList<>()).when(orderRepo).sortOrderListByDate(1L);

        assertThrows(ListEmptyException.class,()->{
            this.orderService.sortOrderListByDate(1);
        });
    }






}