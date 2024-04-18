package ibf2023.paf.day24.repo;

public interface Queries {
    
    public static final String SQL_LI_INSERT_LINEITEM = """
        INSERT INTO line_items(item, quantity, order_id) VALUES (?,?,?)
        """;
    
    public static final String SQL_PO_INSERT_PURCHASEORDER = """
        INSERT INTO orders(order_id, email, delivery_date, rush, comments) VALUES (?,?,?,?,?)
        """;
}
