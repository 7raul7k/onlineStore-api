package ro.myclass.onlineStoreapi.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.myclass.onlineStoreapi.dto.*;
import ro.myclass.onlineStoreapi.exceptions.*;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.services.CustomerService;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class CustomerResourceTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerResource customerResource;

    private ObjectMapper mapper = new ObjectMapper();

    private MockMvc restMockMvc;

    @BeforeEach
    void initialConfig(){ restMockMvc = MockMvcBuilders.standaloneSetup(customerResource).build();}

    @Test
    public void getAllCustomers() throws Exception {
        Faker faker = new Faker();

        List<Customer> customerList = new ArrayList<>();

        for (int i = 0;i < 10;i++){
            customerList.add(new Customer(faker.lorem().sentence(),faker.lorem().sentence(),faker.lorem().sentence()));
        }

        doReturn(customerList).when(customerService).getAllCustomer();

        restMockMvc.perform(get("/api/v1/customer/getAllCustomers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customerList)))
                .andExpect(status().isOk());

    }

    @Test
    public void getAllCustomersBadRequest() throws Exception{
        doThrow(ListEmptyException.class).when(customerService).getAllCustomer();

        restMockMvc.perform(get("/api/v1/customer/getAllCustomers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCustomer() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder().email("ionescumihai@gmail.com").fullName("Ionescu Mihai").password("ionescuionescu").build();


        doNothing().when(customerService).addCustomer(customerDTO);

        restMockMvc.perform(post("/api/v1/customer/addCustomer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(customerDTO)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void addCustomerBadRequest() throws Exception{
        doThrow(CustomerWasFoundException.class).when(customerService).addCustomer(new CustomerDTO());

        restMockMvc.perform(post("/api/v1/customer/addCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new CustomerDTO())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void removeCustomer() throws Exception {
        Customer customer = Customer.builder().id(1L).fullName("Popa Darius").email("popadarius@gmail.com").password("popadarius").build();

        doNothing().when(customerService).removeCustomer(customer.getEmail());

        restMockMvc.perform(delete("/api/v1/customer/deleteCustomer/popadarius@gmail.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void removeCustomerBadRequest() throws Exception{
        doThrow(CustomerNotFoundException.class).when(customerService).removeCustomer("test");

        restMockMvc.perform(delete("/api/v1/customer/deleteCustomer/test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCustomerByEmail() throws Exception{

        Faker faker = new Faker();

        Customer customer = Customer.builder().fullName(faker.lorem().sentence()).password(faker.lorem().sentence()).email("protonmail@gmail.com").build();

        doReturn(customer).when(customerService).returnCustomerByEmail("protonmail@gmail.com");

        restMockMvc.perform(get("/api/v1/customer/getCustomerByEmail/protonmail@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer)))
                .andExpect(status().isFound());

}

    @Test
    public void getCustomerByEmailException() throws Exception{
        doThrow(CustomerNotFoundException.class).when(customerService).returnCustomerByEmail("test");

        restMockMvc.perform(get("/api/v1/customer/getCustomerByEmail/test")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status()
                .isBadRequest());

    }

    @Test
    public void updateQuantityProduct() throws Exception{
        ProductCardRequest productCardRequest = new ProductCardRequest(24,22);

        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest(22,21,productCardRequest);

        doNothing().when(customerService).updateQuantityProduct(updateOrderRequest);

        restMockMvc.perform(put("/api/v1/customer/updateQuantityProduct")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateOrderRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateQuantityProductBadRequest() throws Exception{
        doThrow(CustomerNotFoundException.class).when(customerService).updateQuantityProduct(new UpdateOrderRequest());

        restMockMvc.perform(put("/api/v1/customer/updateQuantityProduct")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new UpdateOrderRequest())));
    }

   @Test
    public void addOrder() throws Exception{
        Faker faker = new Faker();
        List<ProductCardRequest> productCardRequests = new ArrayList<>();

        for(int i = 0 ; i < 5 ; i++){
            productCardRequests.add(ProductCardRequest.builder().productId(faker.number().randomDigit()).quantity(faker.number().randomDigit()).build());
        }
        CreateOrderRequest createOrderRequest = CreateOrderRequest.builder().customerId(2).productCardRequests(productCardRequests).build();

        doNothing().when(customerService).addOrder(createOrderRequest);

        restMockMvc.perform(post("/api/v1/customer/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isOk());

   }

   @Test
    public void addOrderBadRequest() throws Exception{
        Faker faker = new Faker();
        List<ProductCardRequest> productCardRequests = new ArrayList<>();
        for(int i = 0 ; i < 3 ; i ++){
            productCardRequests.add(ProductCardRequest.builder().productId(faker.number().randomDigit()).quantity(faker.number().randomDigit()).build());
        }
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1,productCardRequests);
        doThrow(CustomerNotFoundException.class).when(customerService).addOrder(createOrderRequest);
        restMockMvc.perform(post("/api/v1/customer/addOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isBadRequest());
   }

   @Test
    public void cancelOrder() throws Exception{
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(1,2);

        doNothing().when(customerService).cancelOrder(cancelOrderRequest);

        restMockMvc.perform(delete("/api/v1/customer/cancelOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cancelOrderRequest)))
                .andExpect(status().isOk());


   }

   @Test
    public void cancelOrderBadRequest() throws Exception{
        doThrow(CustomerNotFoundException.class).when(customerService).cancelOrder(new CancelOrderRequest());

        restMockMvc.perform(delete("/api/v1/customer/cancelOrder").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new CancelOrderRequest()))).andExpect(status().isBadRequest());
   }

   @Test
    public void getSortedListOk() throws Exception{
        Customer customer = Customer.builder().fullName("Andrei Popescu").password("esihdgsad2").id(1L).build();

        Order order = Order.builder().id(1L).customer(customer).build();

       OrderDetail orderDetail = OrderDetail.builder()
               .order(order)
               .product(Product.builder().image(new byte[21])
                       .price(250)
                       .name("Gaming chair")
                       .stock(20)
                       .build()).build();
       order.addOrderDetails(orderDetail);

       customer.addOrder(order);

       List<OrderDetail> orderDetails = new ArrayList<>();
       orderDetails.add(orderDetail);
       doReturn(orderDetails).when(customerService).sortOrderByLocalDate(1);

       restMockMvc.perform(get("/api/v1/customer/getSortedOrderListByDate/1").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
   }


   @Test
    public void getSortedListBadRequest() throws Exception {
        doThrow(CustomerNotFoundException.class).when(customerService).sortOrderByLocalDate(1);

       restMockMvc.perform(get("/api/v1/customer/getSortedOrderListByDate/1").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest());
   }
}

