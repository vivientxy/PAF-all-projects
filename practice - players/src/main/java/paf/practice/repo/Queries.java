package paf.practice.repo;

public interface Queries {
    
    public static final String SQL_GET_PLAYER_BY_USERNAME = """
        SELECT *
        FROM game_hub.players
        WHERE username = ?
        """;
    public static final String SQL_GET_OPEN_QTY_BY_USERNAME_TICKER = """
        SELECT SUM(CASE WHEN direction = 'S'
            THEN quantity * -1
            ELSE quantity END) AS open_quantity
        FROM game_hub.trades
        WHERE username = ?
        AND ticker = ?
        GROUP BY username,ticker;
        """;
    public static final String SQL_GET_TRADES_BY_USERNAME_TICKER = """
        SELECT *
        FROM game_hub.trades
        WHERE username = ?
        AND ticker = ?
        """;
    public static final String SQL_ADD_TRADE = """
        INSERT INTO trades(trade_id, ticker, quantity, price, direction, username, trade_date) VALUES (?,?,?,?,?,?,?);
        """;
    public static final String SQL_UPDATE_TRADE = """
        UPDATE trades SET ticker=?, quantity=?, price=?, direction=?, username=?, trade_date=? WHERE trade_id=?;
        """;
    public static final String SQL_UPDATE_WALLET = """
        UPDATE players SET wallet=? WHERE username=?
        """;

}
