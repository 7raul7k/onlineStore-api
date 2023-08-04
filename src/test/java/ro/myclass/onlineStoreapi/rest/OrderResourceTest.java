package ro.myclass.onlineStoreapi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.myclass.onlineStoreapi.dto.CancelOrderRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderRequest;
import ro.myclass.onlineStoreapi.dto.ProductCardRequest;
import ro.myclass.onlineStoreapi.exceptions.CustomerNotFoundException;
import ro.myclass.onlineStoreapi.exceptions.ListEmptyException;
import ro.myclass.onlineStoreapi.exceptions.OrderNotFoundException;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.services.OrderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderResourceTest  {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderResource orderResource;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc restMockMvc;

    @BeforeEach
    void initialConfig(){ restMockMvc = MockMvcBuilders.standaloneSetup(orderResource).build();}

    @Test
    public void getAllOrders() throws Exception {
        Faker faker = new Faker();

        List<Order> orderList = new ArrayList<>();

        for(int i = 0 ; i < 10 ; i++){

            orderList.add(Order.builder().orderDetails(new ArrayList<>()).id((long) i).build());
        }

        doReturn(orderList).when(orderService).getAllOrder();

        restMockMvc.perform(get("/api/v1/order/getAllOrder")).andExpect(status().isOk());



    }

    @Test
    public void getAllOrderBadRequest() throws Exception{

        doThrow(ListEmptyException.class).when(orderService).getAllOrder();

        restMockMvc.perform(get("/api/v1/order/getAllOrder")).andExpect(status().isBadRequest());

    }

    @Test
    public void addOrder() throws Exception {

        ProductCardRequest cardRequest = ProductCardRequest.builder().productId(1).quantity(2).build();

        List<ProductCardRequest> productCardRequests = new ArrayList<>();
        productCardRequests.add(cardRequest);

        CreateOrderRequest createOrderRequest = CreateOrderRequest.builder().productCardRequests(productCardRequests).customerId(1).build();

        doNothing().when(orderService).addOrder(createOrderRequest);

        restMockMvc.perform(post("/api/v1/order/addOrder").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createOrderRequest))).andExpect(status().isOk());
    }

    @Test
    public void addOrderBadRequest() throws Exception{



        restMockMvc.perform(post("/api/v1/order/addOrder").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new CreateOrderRequest()))).andExpect(status().isBadRequest());
    }

    @Test
    public void cancelOrder() throws Exception{
        Customer customer = Customer.builder().id(1L).fullName("Stoica Ionut").email("stoicaionut@gmail.com").password("stoicaionut2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(customer).build();

        Product product = Product.builder().id(1L).name("Iphone 14X").price(5000).stock(250).build();



        OrderDetail orderDetail = OrderDetail.builder().id(1L).order(order).quantity(2).product(product).price(10000).build();

        order.addOrderDetails(orderDetail);
        customer.addOrder(order);

        CancelOrderRequest cancelOrderRequest = CancelOrderRequest.builder().orderId(1).customerId(1).build();

        doNothing().when(orderService).cancelOrder(cancelOrderRequest);

        restMockMvc.perform(delete("/api/v1/order/cancelOrder").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(cancelOrderRequest))).andExpect(status().isOk());

    }

    @Test
    public void cancelOrderBadRequest() throws Exception{
        doThrow(CustomerNotFoundException.class).when(orderService).cancelOrder(new CancelOrderRequest());

        restMockMvc.perform(delete("/api/v1/order/cancelOrder").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new CreateOrderRequest()))).andExpect(status().isBadRequest());

    }

    @Test

    public void updateOrder() throws Exception{

        Customer customer = Customer.builder().id(1L).fullName("Stoica Ionut").email("stoicaionut@gmail.com").password("stoicaionut2023").build();

        Order order = Order.builder().id(1L).customer(customer).orderDetails(new ArrayList<>()).build();

        doNothing().when(orderService).updateOrder(order);

        restMockMvc.perform(put("/api/v1/order/updateOrder").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order))).andExpect(status().isOk());

    }

    @Test
    public void updateOrderBadRequest() throws Exception{

        doThrow(OrderNotFoundException.class).when(orderService).updateOrder(new Order());

        restMockMvc.perform(put("/api/v1/order/updateOrder").contentType(MediaType.ALL).content(objectMapper.writeValueAsBytes(new Order()))).andExpect(status().isBadRequest());


    }

    @Test
    public void getOrderByCustomerId() throws Exception{
        Customer customer = Customer.builder().id(1L).fullName("Stoica Ionut").email("stoicaionut@gmail.com").password("stoicaionut2023").build();


        List<Order> orderList = new ArrayList<>();

        for(int i = 0 ; i< 10;i++){

            orderList.add(Order.builder().customer(customer).id((long) i).orderDetails(new ArrayList<>()).build());
        }

        doReturn(orderList).when(orderService).getOrderByCustomerId(1);

        restMockMvc.perform(get("/api/v1/order/getOrderByCustomerId?customerId=1").contentType(MediaType.ALL)).andExpect(status().isOk());

    }

    @Test
    public void getOrderByCustomerIdBadRequest() throws Exception{

        doThrow(OrderNotFoundException.class).when(orderService).getOrderByCustomerId(1);


        restMockMvc.perform(get("/api/v1/order/getOrderByCustomerId?customerId=1").contentType(MediaType.ALL)).andExpect(status().isBadRequest());

    }

    @Test
    public void getOrderByIdandCustomerId() throws Exception{

        Customer customer = Customer.builder().id(1L).fullName("Stoica Ionut").email("stoicaionut@gmail.com").password("stoicaionut2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(customer).build();

        doReturn(order).when(orderService).getOrderbyIdAndCustomerId(1L,1L);

        restMockMvc.perform(get("/api/v1/order/getOrderByIdandCustomerId?id=1&customerId=1").contentType(MediaType.ALL)).andExpect(status().isOk());


    }

    @Test
    public void getOrderByIdandCustomerIdBadRequest() throws Exception{

        doThrow(OrderNotFoundException.class).when(orderService).getOrderbyIdAndCustomerId(1L,1L);

        restMockMvc.perform(get("/api/v1/order/getOrderByIdandCustomerId?id=1&customerId=1").contentType(MediaType.ALL)).andExpect(status().isBadRequest());


    }

    @Test
    public void getOrderById() throws Exception{

        Customer customer = Customer.builder().id(1L).fullName("Stoica Ionut").email("stoicaionut@gmail.com").password("stoicaionut2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(customer).build();

        doReturn(order).when(orderService).getOrderById(1L);

        restMockMvc.perform(get("/api/v1/order/getOrderById?id=1").contentType(MediaType.ALL)).andExpect(status().isOk());

    }

    @Test
    public void getOrderByIdBadRequest() throws Exception{

        doThrow(OrderNotFoundException.class).when(orderService).getOrderById(1L);

        restMockMvc.perform(get("/api/v1/order/getOrderById?id=1").contentType(MediaType.ALL)).andExpect(status().isBadRequest());

    }

    @Test
    public void sortOrderListByDate() throws Exception{

        Customer customer = Customer.builder().id(1L).fullName("Stoica Ionut").email("stoicaionut@gmail.com").password("stoicaionut2023").build();


        List<Order> orderList = new ArrayList<>();

        for(int i = 0 ; i< 10;i++){

            orderList.add(Order.builder().customer(customer).id((long) i).orderDetails(new ArrayList<>()).build());
        }

        doReturn(orderList).when(orderService).sortOrderListByDate(1);

        restMockMvc.perform(get("/api/v1/order/getSortOrderListbyDate?customerId=1").contentType(MediaType.ALL)).andExpect(status().isOk());

    }

    @Test
    public void sortOrderListByDateBadRequest() throws Exception{

        doThrow(ListEmptyException.class).when(orderService).sortOrderListByDate(1);

        restMockMvc.perform(get("/api/v1/order/getSortOrderListbyDate?customerId=1").contentType(MediaType.ALL)).andExpect(status().isBadRequest());

    }





}