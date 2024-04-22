package paf.practice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import paf.practice.model.Trade;
import paf.practice.repo.PlayerRepo;
import paf.practice.service.TradeService;

@SpringBootApplication
public class PracticeApplication implements CommandLineRunner {

	@Autowired
	PlayerRepo repo;

	@Autowired
	TradeService svc;

	public static void main(String[] args) {
		SpringApplication.run(PracticeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String username = "vivientxy";

		System.out.println(">>> AAPL position: " + repo.getOpenPosition(username, "AAPL"));
		System.out.println(">>> AMD position: " + repo.getOpenPosition(username, "AMD"));

		System.out.println(">>> Player: " + repo.getPlayer(username));

		System.out.println(">>> Trades: " + repo.getTrades(username, "AMD"));
		System.out.println(">>> Trades: " + repo.getTradesUsingRowSet(username, "AMD"));

		// Trade trade = new Trade();
		// trade.setTradeId(11);
		// trade.setUsername("vivientxy");
		// trade.setTicker("IWDA");
		// trade.setQuantity(11);
		// trade.setPrice(97.85f);
		// trade.setDirection("B");
		// trade.setTradeDate(new Date());
		// System.out.println("======================================");
		// System.out.println(">>> trade: " + trade);
		// System.out.println(">>> trade added?: " + repo.addTrade(trade));

		// Trade trade2 = new Trade();
		// trade2.setTradeId(12);
		// trade2.setUsername("vivientxy");
		// trade2.setTicker("IWDA");
		// trade2.setQuantity(15);
		// trade2.setPrice(95.22f);
		// trade2.setDirection("B");
		// trade2.setTradeDate(new Date());
		// System.out.println(">>> trade: " + trade2);
		// repo.updateTrade(trade2);
		// System.out.println(">>> trade updated?: " + repo.updateTrade(trade2));

		Trade trade3 = new Trade();
		trade3.setTradeId(52);
		trade3.setUsername("vivientxy");
		trade3.setTicker("IWDA");
		trade3.setQuantity(13);
		trade3.setPrice(100.22f);
		trade3.setDirection("S");
		trade3.setTradeDate(new Date());
		System.out.println(">>> wallet before: " + repo.getPlayer(username).getWallet());
		System.out.println(">>> trade: " + trade3);
		try {
			svc.addTrade(username, trade3);
		} catch (Exception e) {
			System.out.println("exception caught in commandlinerunner. carry on...");
		}
		System.out.println(">>> wallet after: " + repo.getPlayer(username).getWallet());
	}

}
