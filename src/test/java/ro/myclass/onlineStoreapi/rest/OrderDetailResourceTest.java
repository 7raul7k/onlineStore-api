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
import ro.myclass.onlineStoreapi.dto.*;
import ro.myclass.onlineStoreapi.exceptions.CustomerNotFoundException;
import ro.myclass.onlineStoreapi.exceptions.ListEmptyException;
import ro.myclass.onlineStoreapi.exceptions.OrderNotFoundException;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.services.OrderDetailService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderDetailResourceTest {

    @Mock
    private OrderDetailService orderDetailService;

    @InjectMocks
    private OrderDetailResource orderDetailResource;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc restMockMvc;

    @BeforeEach
    void initialConfig(){ restMockMvc = MockMvcBuilders.standaloneSetup(orderDetailResource).build();}

    @Test
    public void getAllOrderDetails() throws Exception {

        Faker faker  = new Faker();

        List<OrderDetail> orderDetailList = new ArrayList<>();

        for(int i = 0 ; i < 10 ; i++){

            orderDetailList.add(OrderDetail.builder().id((long) i).price(faker.number().randomDigit()).product(new Product()).quantity(faker.number().randomDigit()).build());
        }

        doReturn(orderDetailList).when(orderDetailService).getAllOrderDetail();

        restMockMvc.perform(get("/api/v1/orderDetail/getAllOrderDetail")).andExpect(status().isOk());

    }

    @Test
    public void getAllOrderDetailsBadRequest() throws Exception{

        doThrow(ListEmptyException.class).when(orderDetailService).getAllOrderDetail();

        restMockMvc.perform(get("/api/v1/orderDetail/getAllOrderDetail")).andExpect(status().isBadRequest());

    }

    @Test
    public void addOrderDetail() throws Exception {

        Customer customer = Customer.builder().id(1L).fullName("Stanciu Marian").email("stanciumarian@gmail.com").password("stanciumarian2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(new Customer()).build();

        Product product = Product.builder().id(1L).price(640).name("Razer Microphone for streaming").image(new byte[23]).stock(400).build();
        CreateOrderDetailRequest createOrderDetailRequest = CreateOrderDetailRequest.builder().orderId(1).customerId(1).productId(1).price(250).quantity(2).build();


        doNothing().when(orderDetailService).addOrderDetail(createOrderDetailRequest);

        restMockMvc.perform(post("/api/v1/orderDetail/addOrderDetail").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createOrderDetailRequest))).andExpect(status().isOk());

    }


    @Test
    public void addOrderDetailBadRequest() throws Exception{

        doThrow(CustomerNotFoundException.class).when(orderDetailService).addOrderDetail(new CreateOrderDetailRequest());

        restMockMvc.perform(post("/api/v1/orderDetail/addOrderDetail").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new CreateOrderRequest()))).andExpect(status().isBadRequest());



    }

    @Test
    public void deleteOrderDetail() throws Exception{


        doNothing().when(orderDetailService).deleteOrderDetail(1,1);


        restMockMvc.perform(delete("/api/v1/orderDetail/deleteOrderDetail?productId=1&orderId=1")).andExpect(status().isOk());


    }

    @Test
    public void deleteOrderDetailBadRequest() throws Exception{

        doThrow(OrderNotFoundException.class).when(orderDetailService).deleteOrderDetail(1,1);

        restMockMvc.perform(delete("/api/v1/orderDetail/deleteOrderDetail?productId=1&orderId=1")).andExpect(status().isBadRequest());

    }

    @Test
    public void updateOrderDetail() throws Exception{

        ProductCardRequest productCardRequest = ProductCardRequest.builder().productId(1).quantity(1).build();
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder().productCardRequest(productCardRequest).orderId(1).build();
        doNothing().when(orderDetailService).updateOrderDetail(orderDetailDTO);

        restMockMvc.perform(put("/api/v1/orderDetail/updateOrderDetail").content(objectMapper.writeValueAsString(orderDetailDTO)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    public void updateOrderDetailBadRequest() throws Exception{
    ProductCardRequest productCardRequest = ProductCardRequest.builder().productId(1).quantity(1).build();
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder().orderId(1).productCardRequest(productCardRequest).build();

        doThrow(OrderNotFoundException.class).when(orderDetailService).updateOrderDetail(orderDetailDTO);

        restMockMvc.perform(put("/api/v1/orderDetail/updateOrderDetail").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(orderDetailDTO))).andExpect(status().isBadRequest());


    }

    @Test
    public void findOrderDetailByProductIdAndOrderId()throws Exception{

        Customer customer = Customer.builder().id(1L).fullName("Stanciu Marian").email("stanciumarian@gmail.com").password("stanciumarian2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(new Customer()).build();

        Product product = Product.builder().id(1L).price(640).name("Razer Microphone for streaming").image(new byte[23]).stock(400).build();

        OrderDetail orderDetail = OrderDetail.builder().order(order).id(1L).quantity(100).product(product).price(200).build();


        doReturn(orderDetail).when(orderDetailService).findOrderDetailByProductIdAndOrderId(1,1);


        restMockMvc.perform(get("/api/v1/orderDetail/getOrderDetailByProductIdandOrderId?productId=1&orderId=1")).andExpect(status().isOk());


    }

    @Test
    public void findOrderDetailByProductIdAndOrderIdBadRequest()throws Exception {
        doThrow(OrderNotFoundException.class).when(orderDetailService).findOrderDetailByProductIdAndOrderId(1,1);


        restMockMvc.perform(get("/api/v1/orderDetail/getOrderDetailByProductIdandOrderId?productId=1&orderId=1")).andExpect(status().isBadRequest());

    }
}