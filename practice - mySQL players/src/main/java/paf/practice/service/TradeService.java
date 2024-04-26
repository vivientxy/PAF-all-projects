package paf.practice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import paf.practice.model.Trade;
import paf.practice.repo.PlayerRepo;

@Service
public class TradeService {
    
    @Autowired
    PlayerRepo repo;

    @Transactional(rollbackFor = TradeException.class)
    public void addTrade(String username, Trade trade) throws TradeException {
        // start transaction
        try {
            Float proceeds = trade.getQuantity() * trade.getPrice();
            if ("B".equalsIgnoreCase(trade.getDirection()))
                proceeds = proceeds * -1;
            System.out.println("~~~ adding trade ~~~ " + proceeds);
            repo.addTrade(trade);
            System.out.println("~~~ trade added ~~~ ");
            Float wallet = repo.getPlayer(username).getWallet();
            wallet += proceeds;
            System.out.println("~~~ updating player wallet ~~~ ");
            repo.updatePlayerWallet(username, wallet);
            System.out.println("~~~ updated player wallet ~~~ ");
        } catch (Exception ex) {
            // rollback
            System.out.println("~~~ throwing error msg ~~~ ");
            throw new TradeException(ex.getMessage());
        }
        // commit
    }

}
