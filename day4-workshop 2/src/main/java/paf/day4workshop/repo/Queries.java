package paf.day4workshop.repo;

public interface Queries {
    public static final String SQL_ADD_ORDER = """
        INSERT INTO orders(order_date,customer_name,ship_address,notes,tax)
        VALUES (?,?,?,?,?)
        """;
    public static final String SQL_ADD_ORDER_DETAIL = """
        INSERT INTO orders_details(product,unit_price,discount,quantity,order_id)
        VALUES (?,?,?,?,?)
        """;
    public static final String SQL_GET_ID = """
        SELECT LAST_INSERT_ID()
        """;
}
