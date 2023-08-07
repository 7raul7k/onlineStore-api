package ro.myclass.onlineStoreapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.myclass.onlineStoreapi.dto.CreateOrderDetailRequest;
import ro.myclass.onlineStoreapi.dto.OrderDTO;
import ro.myclass.onlineStoreapi.dto.OrderDetailDTO;
import ro.myclass.onlineStoreapi.dto.ProductCardRequest;
import ro.myclass.onlineStoreapi.exceptions.CustomerNotFoundException;
import ro.myclass.onlineStoreapi.exceptions.ListEmptyException;
import ro.myclass.onlineStoreapi.exceptions.OrderNotFoundException;
import ro.myclass.onlineStoreapi.exceptions.ProductNotFoundException;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.repo.CustomerRepo;
import ro.myclass.onlineStoreapi.repo.OrderDetailRepo;
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
class OrderDetailServiceTest {


    @Mock
    private OrderDetailRepo orderDetailRepo;


    @Mock
    private OrderRepo orderRepo;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private OrderDetailService orderDetailService;


    @Captor
    ArgumentCaptor<OrderDetail> argumentCaptor;


    @Test
    public void getAllOrderDetail(){

        OrderDetail orderDetail1 = OrderDetail.builder().order(new Order()).product(new Product()).price(200).quantity(15).build();
        OrderDetail orderDetail2 = OrderDetail.builder().order(new Order()).product(new Product()).price(200).quantity(15).build();
        OrderDetail orderDetail3 = OrderDetail.builder().order(new Order()).product(new Product()).price(200).quantity(15).build();
        OrderDetail orderDetail4 = OrderDetail.builder().order(new Order()).product(new Product()).price(200).quantity(15).build();

        List<OrderDetail> orderDetailList = new ArrayList<>();

        orderDetailList.add(orderDetail1);
        orderDetailList.add(orderDetail2);
        orderDetailList.add(orderDetail3);
        orderDetailList.add(orderDetail4);

        doReturn(orderDetailList).when(orderDetailRepo).getAllOrderDetails();
        assertEquals(orderDetailList,this.orderDetailService.getAllOrderDetail());
    }

    @Test
    public void getAllOrderDetailError(){

        doReturn(new ArrayList<>()).when(orderDetailRepo).getAllOrderDetails();

        assertThrows(ListEmptyException.class,()->{
           this.orderDetailService.getAllOrderDetail();
        });
    }

    @Test
    public void addOrderDetail() throws Exception {

        Customer customer = Customer.builder().id(1L).fullName("Stanciu Marian").email("stanciumarian@gmail.com").password("stanciumarian2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(new Customer()).build();

        Product product = Product.builder().id(1L).price(640).name("Razer Microphone for streaming").image(new byte[23]).stock(400).build();
        CreateOrderDetailRequest createOrderDetailRequest = CreateOrderDetailRequest.builder().orderId(1).customerId(1).productId(1).price(250).quantity(2).build();

     doReturn(Optional.of(customer)).when(customerRepo).getCustomerById(1L);

        doReturn(Optional.of(order)).when(orderRepo).getOrderByIdAndCustomerId(1L,1L);


        OrderDetail orderDetail = OrderDetail.builder().order(order).id(1L).quantity(100).product(product).price(200).build();

        doReturn(Optional.of(orderDetail)).when(orderDetailRepo).findOrderDetailByProductIdAndOrderId(1L,1L);

        doReturn(Optional.of(product)).when(productRepo).getProductByName(product.getName());
        this.orderDetailService.addOrderDetail(createOrderDetailRequest);

        verify(orderDetailRepo,times(1)).saveAndFlush(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue(),argumentCaptor.getValue());

    }

    @Test
    public void addOrderDetailCustomerError(){

        doReturn(Optional.empty()).when(customerRepo).getCustomerById(1L);

        assertThrows(CustomerNotFoundException.class,()->{
            this.orderDetailService.addOrderDetail(CreateOrderDetailRequest.builder().customerId(1).build());
        });
    }

    @Test
    public void addOrderDetailOrderError(){
        Customer customer = Customer.builder().id(1L).fullName("Stanciu Marian").email("stanciumarian@gmail.com").password("stanciumarian2023").build();


        doReturn(Optional.of(customer)).when(customerRepo).getCustomerById(1L);

        doReturn(Optional.empty()).when(orderRepo).getOrderByIdAndCustomerId(1L,1L);

        assertThrows(OrderNotFoundException.class,()->{

            this.orderDetailService.addOrderDetail(CreateOrderDetailRequest.builder().orderId(1).customerId(1).build());
        });

    }

    @Test
    public void addOrderDetailError(){
        Customer customer = Customer.builder().id(1L).fullName("Stanciu Marian").email("stanciumarian@gmail.com").password("stanciumarian2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(new Customer()).build();


        doReturn(Optional.of(customer)).when(customerRepo).getCustomerById(1L);

        doReturn(Optional.of(order)).when(orderRepo).getOrderByIdAndCustomerId(1L,1L);

        doReturn(Optional.empty()).when(orderDetailRepo).findOrderDetailByProductIdAndOrderId(1L,1L);

        assertThrows(OrderNotFoundException.class,()->{
            this.orderDetailService.addOrderDetail(CreateOrderDetailRequest.builder().orderId(1).customerId(1).productId(1).build());

        });

    }

    @Test
    public void addOrderDetailProductError(){
        Customer customer = Customer.builder().id(1L).fullName("Stanciu Marian").email("stanciumarian@gmail.com").password("stanciumarian2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(new Customer()).build();

        Product product = Product.builder().id(1L).price(640).name("Razer Microphone for streaming").image(new byte[23]).stock(400).build();
        CreateOrderDetailRequest createOrderDetailRequest = CreateOrderDetailRequest.builder().orderId(1).customerId(1).productId(1).price(250).quantity(2).build();

        OrderDetail orderDetail = OrderDetail.builder().order(order).id(1L).quantity(100).product(product).price(200).build();

        doReturn(Optional.of(customer)).when(customerRepo).getCustomerById(1L);

        doReturn(Optional.of(order)).when(orderRepo).getOrderByIdAndCustomerId(1L,1L);

        doReturn(Optional.of(orderDetail)).when(orderDetailRepo).findOrderDetailByProductIdAndOrderId(1L,1L);

        doReturn(Optional.empty()).when(productRepo).getProductByName("Razer Microphone for streaming");

        assertThrows(ProductNotFoundException.class,()->{
            this.orderDetailService.addOrderDetail(createOrderDetailRequest);
        });
    }

    @Test
    public void deleteOrderDetailOk(){

        OrderDetail orderDetail = OrderDetail.builder().order(new Order()).product(new Product()).price(200).quantity(15).build();

        doReturn(Optional.of(orderDetail)).when(orderDetailRepo).findOrderDetailByProductIdAndOrderId(1L,1L);

        this.orderDetailService.deleteOrderDetail(1,1);

        verify(orderDetailRepo,times(1)).delete(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue(),orderDetail);
    }

    @Test
    public void deleteOrderDetailError(){
        doReturn(Optional.empty()).when(orderDetailRepo).findOrderDetailByProductIdAndOrderId(1L,1L);

        assertThrows(OrderNotFoundException.class,()->{
            this.orderDetailService.deleteOrderDetail(1,1);
        });
    }

    @Test
    public void findOrderDetailByProductIdandOrderId(){
        OrderDetail orderDetail = OrderDetail.builder().order(new Order()).product(new Product()).price(200).quantity(15).build();

        doReturn(Optional.of(orderDetail)).when(orderDetailRepo).findOrderDetailByProductIdAndOrderId(1L,1L);

        assertEquals(orderDetail,this.orderDetailService.findOrderDetailByProductIdAndOrderId(1,1));

    }

    @Test
    public void findOrderDetailByProductIdAndOrderIdError(){
        doReturn(Optional.empty()).when(orderDetailRepo).findOrderDetailByProductIdAndOrderId(1,1);

        assertThrows(OrderNotFoundException.class,()->{
            this.orderDetailService.deleteOrderDetail(1,1);
        });
    }

    @Test
    public void updateOrderDetail(){
        Customer customer = Customer.builder().id(1L).fullName("Stanciu Marian").email("stanciumarian@gmail.com").password("stanciumarian2023").build();

        OrderDTO orderDTO = OrderDTO.builder().id(1).productCardRequests(new ArrayList<>()).customerId(1).build();
        Product product = Product.builder().id(1L).price(640).name("Razer Microphone for streaming").image(new byte[23]).stock(400).build();

        OrderDetail orderDetail = OrderDetail.builder().order(new Order()).product(new Product()).price(200).quantity(15).build();

        ProductCardRequest productCardRequest = ProductCardRequest.builder().productId(1).quantity(1).build();
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder().orderId(1).productCardRequest(productCardRequest).build();
        doReturn(Optional.of(orderDetail)).when(orderDetailRepo).findOrderDetailByProductIdAndOrderId(1L,1L);

        doReturn(Optional.of(product)).when(productRepo).getProductById((long) productCardRequest.getProductId());

        this.orderDetailService.updateOrderDetail(orderDetailDTO);

        verify(orderDetailRepo,times(1)).saveAndFlush(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue(),orderDetail);

    }

    @Test
    public void updateOrderDetailOrderDetailError(){
        Product product = Product.builder().id(1L).price(640).name("Razer Microphone for streaming").image(new byte[23]).stock(400).build();

        OrderDTO orderDTO = OrderDTO.builder().id(1).customerId(1).productCardRequests(new ArrayList<>()).build();
        ProductCardRequest productCardRequest = ProductCardRequest.builder().productId(1).quantity(1).build();
       OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder().orderId(1).productCardRequest(productCardRequest).build();
        doReturn(Optional.empty()).when(orderDetailRepo).findOrderDetailByProductIdAndOrderId(1L,1L);

        assertThrows(OrderNotFoundException.class,()->{
            this.orderDetailService.updateOrderDetail(orderDetailDTO);
        });
    }

    @Test
    public void updateOrderDetailProductError(){

        Customer customer = Customer.builder().id(1L).fullName("Stanciu Marian").email("stanciumarian@gmail.com").password("stanciumarian2023").build();

        Order order = Order.builder().id(1L).orderDate(LocalDate.now()).customer(new Customer()).build();

        Product product = Product.builder().id(1L).price(640).name("Razer Microphone for streaming").image(new byte[23]).stock(400).build();

        OrderDetail orderDetail = OrderDetail.builder().order(order).id(1L).quantity(100).product(product).price(200).build();

        OrderDTO orderDTO = OrderDTO.builder().id(1).productCardRequests(new ArrayList<>()).customerId(1).build();

        doReturn(Optional.of(orderDetail)).when(orderDetailRepo).findOrderDetailByProductIdAndOrderId(1L,1L);
        ProductCardRequest productCardRequest = ProductCardRequest.builder().productId(1).quantity(1).build();
        doReturn(Optional.empty()).when(productRepo).getProductById(1);
        assertThrows(ProductNotFoundException.class,()->{
            this.orderDetailService.updateOrderDetail(OrderDetailDTO.builder().orderId(1).productCardRequest(productCardRequest).build());
        });
    }

}