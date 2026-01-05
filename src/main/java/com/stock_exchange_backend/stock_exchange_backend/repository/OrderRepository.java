package com.stock_exchange_backend.stock_exchange_backend.repository;

import com.stock_exchange_backend.stock_exchange_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
    SELECT o FROM Order o
    WHERE o.ticker = :ticker
      AND o.pricePerShare <= :price
      AND o.quantity > 0
      AND o.type = 'BUYING'
      AND o.status = 'ACTIVE'
      AND o.traderId <> :traderId
    ORDER BY o.pricePerShare DESC, o.createdAt ASC
""")
    List<Order> findMatchingBuyingOrders(
            @Param("ticker") String ticker,
            @Param("traderId") String traderId,
            @Param("price") double maxPrice
    );

    @Query("""
    SELECT o FROM Order o
    WHERE o.ticker = :ticker
      AND o.pricePerShare >= :price
      AND o.quantity > 0
      AND o.type = 'SELLING'
      AND o.status = 'ACTIVE'
        AND o.traderId <> :traderId
    ORDER BY o.pricePerShare ASC, o.createdAt ASC
""")
    List<Order> findMatchingSellingOrders(
            @Param("ticker") String ticker,
            @Param("traderId") String traderId,
            @Param("price") double minPrice
    );

    @Query("""
    SELECT o FROM Order o
    WHERE o.status = 'ACTIVE'
""")
    List<Order> findAllActiveOrders();
}
