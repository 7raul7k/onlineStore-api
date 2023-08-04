package ro.myclass.onlineStoreapi.rest;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.myclass.onlineStoreapi.dto.CancelOrderRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderResponse;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.services.OrderService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/order")
@Slf4j
public class OrderResource {

    private OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/getAllOrder")
    public ResponseEntity<List<Order>> getAllOrder(){

        List<Order> orders = this.orderService.getAllOrder();

        log.info("REST request to get all orders {}",orders);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


    @PostMapping("/addOrder")
    public ResponseEntity<CreateOrderResponse> addOrder(@RequestBody CreateOrderRequest createOrderRequest){

        this.orderService.addOrder(createOrderRequest);

        log.info("REST request to add order {}",createOrderRequest);

        return new ResponseEntity<>(new CreateOrderResponse("Order was created"),HttpStatus.OK);
    }

    @DeleteMapping("/cancelOrder")
    public ResponseEntity<CreateOrderResponse> deleteOrder(@RequestBody CancelOrderRequest cancelOrderRequest){

        this.orderService.cancelOrder(cancelOrderRequest);

        log.info("REST request to cancel order {}",cancelOrderRequest);

        return new ResponseEntity<>(new CreateOrderResponse("Order was deleted"),HttpStatus.OK);
    }

    @PutMapping("/updateOrder")
    public ResponseEntity<CreateOrderResponse> updateOrder(Order order){

        this.orderService.updateOrder(order);

        log.info("REST request to update order {}",order);

        return new ResponseEntity<>(new CreateOrderResponse("Order was updated"),HttpStatus.OK);
    }

    @GetMapping("/getOrderByCustomerId")
    public ResponseEntity<List<Order>> getOrderByCustomerId(@RequestParam int customerId){

        List<Order> order = this.orderService.getOrderByCustomerId(customerId);

        log.info("REST request to get order by customer id {}",customerId);

        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @GetMapping("/getOrderByIdandCustomerId")
    public ResponseEntity<Order> getOrderbyIdAndCustomerId(@RequestParam int id,@RequestParam int customerId){

        Order order = this.orderService.getOrderbyIdAndCustomerId(id,customerId);

        log.info("REST request to get order by id and customer id {}",id,customerId);

        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @GetMapping("/getOrderById")
    public ResponseEntity<Order> getOrderById(@RequestParam int id){

        Order order = this.orderService.getOrderById(id);

        log.info("REST request to get order by id {}",id);

        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @GetMapping("/getSortOrderListbyDate")
    public ResponseEntity<List<Order>> sortOrderListByDate(@RequestParam  int customerId){

        List<Order> orders = this.orderService.sortOrderListByDate(customerId);

        log.info("REST request to get sorted order list byy customer id {}",customerId);

        return new ResponseEntity<>(orders,HttpStatus.OK);
    }



}
