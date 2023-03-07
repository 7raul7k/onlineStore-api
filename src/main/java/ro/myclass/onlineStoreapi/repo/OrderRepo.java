package ro.myclass.onlineStoreapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {

    @Query("select o from Order o where o.customer.id = ?1")
    List<Order> getOrderByCustomerId(long customerId);

   @Query("select o from Order o where o.customer.email = ?1 and o.orderDate = ?2")
   Optional<Order> getOrderByCustomerEmailAndOrderDate(String email, LocalDate order);

   @Query("select o from Order o where o.customer.id = ?1 and o.id = ?2")
   Optional<Order> getOrderByCustomerIdAndAndId(long customerId, long orderId);

   @Query("select o from Order o where o.id = ?1")
   Optional<Order> getOrderById(long id);





}
