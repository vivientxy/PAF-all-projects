package paf.practice.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import paf.practice.model.Player;
import paf.practice.model.Trade;

@Repository
public class PlayerRepo implements Queries {

    @Autowired
    JdbcTemplate template;

    // queryForObject
    public int getOpenPosition(String username, String ticker) {
        return template.queryForObject(SQL_GET_OPEN_QTY_BY_USERNAME_TICKER, Integer.class, username, ticker);
    }

    // BeanPropertyRowMapper
    public Player getPlayer(String username) {
        return template.queryForObject(SQL_GET_PLAYER_BY_USERNAME, BeanPropertyRowMapper.newInstance(Player.class), username);
    }

    // query + manually configure RowMapper
    public List<Trade> getTrades(String username, String ticker) {
        return template.query(SQL_GET_TRADES_BY_USERNAME_TICKER, new TradeRowMapper(), username, ticker);
    }

    private class TradeRowMapper implements RowMapper<Trade> {
        @Override
        public Trade mapRow(ResultSet rs, int rowNum) throws SQLException {
            Trade trade = new Trade();
            trade.setTradeId(rs.getInt("trade_id"));
            trade.setUsername(rs.getString("username"));
            trade.setTicker(rs.getString("ticker"));
            trade.setQuantity(rs.getInt("quantity"));
            trade.setPrice(rs.getFloat("price"));
            trade.setDirection(rs.getString("direction"));
            trade.setTradeDate(rs.getDate("trade_date"));
            return trade;
        }
    }

    // queryForRowSet
    public List<Trade> getTradesUsingRowSet(String username, String ticker) throws InvalidResultSetAccessException, ParseException {
        SqlRowSet rs = template.queryForRowSet(SQL_GET_TRADES_BY_USERNAME_TICKER, username, ticker);
        List<Trade> trades = new LinkedList<>();
        while (rs.next()) {
            Trade trade = new Trade();
            trade.setTradeId(rs.getInt("trade_id"));
            trade.setUsername(rs.getString("username"));
            trade.setTicker(rs.getString("ticker"));
            trade.setQuantity(rs.getInt("quantity"));
            trade.setPrice(rs.getFloat("price"));
            trade.setDirection(rs.getString("direction"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date tradeDate = sdf.parse(rs.getString("trade_date"));
            trade.setTradeDate(tradeDate);
            trades.add(trade);
        }
        return Collections.unmodifiableList(trades);
    }

    // add new line of data
    public boolean addTrade(Trade trade) {
        // INSERT INTO trades(ticker, quantity, price, direction, username, trade_date) VALUES (?,?,?,?,?,?);
        try {
            template.update(SQL_ADD_TRADE, trade.getTradeId(), trade.getTicker(), trade.getQuantity(),
                    trade.getPrice(), trade.getDirection(), trade.getUsername(), trade.getTradeDate());
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    // update data
    public boolean updateTrade(Trade trade) {
        // UPDATE trades SET ticker=?, quantity=?, price=?, direction=?, username=?, trade_date=? WHERE trade_id=?;
        return template.update(SQL_UPDATE_TRADE, trade.getTicker(), trade.getQuantity(), trade.getPrice(),
                trade.getDirection(), trade.getUsername(), trade.getTradeDate(), trade.getTradeId()) > 0 ? true : false;
    }

    public boolean updatePlayerWallet(String username, Float wallet) {
        // UPDATE players SET wallet=? WHERE username=?
        return template.update(SQL_UPDATE_WALLET, wallet, username) > 0 ? true : false;
    }
}
