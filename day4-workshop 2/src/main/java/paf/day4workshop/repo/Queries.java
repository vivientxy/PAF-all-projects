package paf.day4workshop.repo;

public interface Queries {
    public static final String SQL_ADD_ORDER = """
            INSERT INTO orders(order_date,customer_name,ship_address,notes,tax)
            VALUES (?,?,?,?,?)
            """;
}
