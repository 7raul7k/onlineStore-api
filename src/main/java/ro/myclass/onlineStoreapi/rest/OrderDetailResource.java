package ro.myclass.onlineStoreapi.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.myclass.onlineStoreapi.dto.CreateOrderDetailRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderResponse;
import ro.myclass.onlineStoreapi.dto.OrderDetailDTO;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.services.OrderDetailService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/orderDetail")
@Slf4j
public class OrderDetailResource {

    private OrderDetailService orderDetailService;

    public OrderDetailResource(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping("/getAllOrderDetail")
    public ResponseEntity<List<OrderDetail>> getAllOrderDetail() {

        List<OrderDetail> orderDetailList = this.orderDetailService.getAllOrderDetail();

        log.info("REST request to get all orderDetail {}", orderDetailList);

        return new ResponseEntity<>(orderDetailList, HttpStatus.OK);
    }

    @PostMapping("/addOrderDetail")
    public ResponseEntity<CreateOrderResponse> addOrderDetail(@RequestBody CreateOrderDetailRequest createOrderDetailRequest) throws Exception {

        this.orderDetailService.addOrderDetail(createOrderDetailRequest);

        log.info("REST request to add orderDetail {}", createOrderDetailRequest);

        return new ResponseEntity<>(new CreateOrderResponse("Order detail was added!"), HttpStatus.OK);


    }

    @DeleteMapping("/deleteOrderDetail")
    public ResponseEntity<CreateOrderResponse> deleteOrderDetail(@RequestParam int productId, @RequestParam int orderId) {

        this.orderDetailService.deleteOrderDetail(productId, orderId);

        log.info("REST request to delete orderDetail by productId and orderId {}", productId, orderId);

        return new ResponseEntity<>(new CreateOrderResponse("Order was deleted"), HttpStatus.OK);
    }


    @PutMapping("/updateOrderDetail")
    public ResponseEntity<CreateOrderResponse> updateOrderDetail(@RequestParam OrderDetailDTO orderDetailDTO) {

        this.orderDetailService.updateOrderDetail(orderDetailDTO);

        log.info("REST request to update orderDetail {}", orderDetailDTO);

        return new ResponseEntity<>(new CreateOrderResponse("OrderDetail was updated"), HttpStatus.OK);
    }

    @GetMapping("/getOrderDetailByProductIdandOrderId")
    public ResponseEntity<OrderDetail> findOrderDetailByProductIdAndOrderId(@RequestParam int productId,@RequestParam int orderId) {

        OrderDetail order = this.orderDetailService.findOrderDetailByProductIdAndOrderId((long) productId,(long) orderId);

        log.info("REST request to get orderDetail by productIdAndOrderId {}",productId,orderId);

        return new ResponseEntity<>(order,HttpStatus.OK);

    }

    


    }
