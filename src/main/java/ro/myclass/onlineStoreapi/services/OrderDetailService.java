package ro.myclass.onlineStoreapi.services;

import org.springframework.stereotype.Service;
import ro.myclass.onlineStoreapi.dto.CreateOrderDetailRequest;
import ro.myclass.onlineStoreapi.exceptions.CustomerNotFoundException;
import ro.myclass.onlineStoreapi.exceptions.ListEmptyException;
import ro.myclass.onlineStoreapi.exceptions.OrderNotFoundException;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.repo.CustomerRepo;
import ro.myclass.onlineStoreapi.repo.OrderDetailRepo;
import ro.myclass.onlineStoreapi.repo.OrderRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {

    private OrderDetailRepo orderDetailRepo;

    private OrderRepo orderRepo;

    private CustomerRepo customerRepo;


    public OrderDetailService(OrderDetailRepo orderDetailRepo, OrderRepo orderRepo, CustomerRepo customerRepo) {
        this.orderDetailRepo = orderDetailRepo;
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
    }

    public List<OrderDetail> getAllOrderDetail(){

        List<OrderDetail> orderDetailList = this.orderDetailRepo.getAllOrderDetails();

        if(orderDetailList.isEmpty()){
            throw new ListEmptyException();
        }else{
            return orderDetailList;
        }
    }

    @Transactional
    public void addOrderDetail(CreateOrderDetailRequest orderDetailRequest){

        Optional<Customer> customerOptional = this.customerRepo.getCustomerById(orderDetailRequest.getCustomerId());

        if(customerOptional.isEmpty()){
            throw new CustomerNotFoundException();
        }



        Optional<Order> orderOptional = this.orderRepo.getOrderByIdAndCustomerId(orderDetailRequest.getOrderId(),customerOptional.get().getId());

        if(orderOptional.isEmpty()){
            throw new OrderNotFoundException();
        }


        Optional<OrderDetail> orderDetail= this.orderDetailRepo.findOrderDetailByProductIdAndOrderId(orderDetailRequest.getOrderDetail().getProduct().getId(), orderDetailRequest.getOrderId());

        OrderDetail orderDetailUpdate = orderDetailRequest.getOrderDetail();

        if(orderDetailUpdate.getPrice()  > 0){
            orderDetail.get().setPrice(orderDetailUpdate.getPrice());
        }if(orderDetailUpdate.getQuantity() > 0){

            orderDetail.get().setQuantity(orderDetailUpdate.getQuantity());
        }if(orderDetailUpdate.getOrder() !=null){
        orderDetail.get().setOrder(orderOptional.get());
        }

        orderDetailRepo.saveAndFlush(orderDetail.get());

    }

    @Transactional
    public void deleteOrderDetail(int productId,int orderId){

        Optional<OrderDetail> orderDetail = this.orderDetailRepo.findOrderDetailByProductIdAndOrderId((long) productId,(long) orderId);

        if(orderDetail.isEmpty()){

            throw new OrderNotFoundException();
        }else{
            this.orderDetailRepo.delete(orderDetail.get());
        }
    }

    public OrderDetail  findOrderDetailByProductIdAndOrderId(long productId,long orderId){

        Optional<OrderDetail> orderDetail = this.orderDetailRepo.getOrderDetailByProductIdAndPrice(productId,orderId);

        if(orderDetail.isEmpty()){

            throw new OrderNotFoundException();
        }else{
           return orderDetail.get();
        }
    }

    public OrderDetail getOrderDetailByProductIdAndPrice(long productID,double price){

        Optional<OrderDetail> orderDetail = this.orderDetailRepo.getOrderDetailByProductIdAndPrice(productID,price);

        if(orderDetail.isEmpty()){

            throw new OrderNotFoundException();
        }else{
            return orderDetail.get();
        }

    }

}
