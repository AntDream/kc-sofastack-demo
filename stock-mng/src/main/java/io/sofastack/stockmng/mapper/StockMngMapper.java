package io.sofastack.stockmng.mapper;

import io.sofastack.stockmng.model.ProductInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockMngMapper {
    @Select(
        "select stock_tb.product_code as productCode, name, price, sum(user_orders.count) as ownedCount, description\n" +
            "from stock_tb left outer join (select product_code, count from order_tb where user_name = #{userName}) as user_orders\n" +
            "                      on stock_tb.product_code = user_orders.product_code where stock_tb.user_name=#{userName} \n" +
            "group by stock_tb.product_code;")
    List<ProductInfo> query(@Param("userName") String userName);

    @Select("select price from stock_tb where product_code = #{productCode} and user_name = #{userName}")
    Double queryProductPrice(@Param("productCode") String productCode, @Param("userName") String userName);

    @Select("select sum(1) from stock_tb where user_name = #{userName}")
    Integer getStockRecordCountForUserName(@Param("userName") String userName);

    @Insert(
        "insert into stock_tb (product_code, name, description, price, count, user_name) values (#{productCode}, "
            + "#{name}, #{description}, #{price}, #{count}, #{userName})")
    void insertStockRecord(@Param("productCode") String productCode, @Param("name") String name,
                           @Param("description") String description, @Param("price") Double price,
                           @Param("count") Integer count, @Param("userName") String userName);

    @Insert("insert into order_tb (user_name, product_code, count) values (#{userName}, #{productCode}, #{count})")
    void purchase(@Param("userName") String userName, @Param("productCode") String productCode, @Param("count") int count);
}
