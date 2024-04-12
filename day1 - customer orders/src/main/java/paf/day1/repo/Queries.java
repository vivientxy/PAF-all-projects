package paf.day1.repo;

public interface Queries {
    public static final String GET_ALL_CUSTOMERS = "SELECT id, first_name, last_name FROM customers";
    public static final String GET_ALL_CUSTOMERS_WITH_PAGINATION = "SELECT id, first_name, last_name FROM customers limit ? offset ?";
    public static final String GET_CUSTOMER_BY_ID = "SELECT id, first_name, last_name FROM customers WHERE id = ?";
    public static final String GET_CUSTOMER_BY_ORDER = "SELECT c.id, c.first_name, c.last_name, o.tax_rate, o.order_date FROM customers c, orders o WHERE c.id = o.customer_id AND c.id = ?";
    public static final String IS_CUSTOMER_EXIST = "SELECT COUNT(*) AS count FROM customers WHERE id = ?";
}
