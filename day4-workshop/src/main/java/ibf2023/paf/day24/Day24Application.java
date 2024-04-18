package ibf2023.paf.day24;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ibf2023.paf.day24.model.LineItem;
import ibf2023.paf.day24.model.Order;
import ibf2023.paf.day24.service.OrderService;

@SpringBootApplication
public class Day24Application implements CommandLineRunner {

	@Autowired
	OrderService svc;

	public static void main(String[] args) {
		SpringApplication.run(Day24Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<LineItem> lineItems = new LinkedList<>();
		LineItem li = new LineItem();
		li.setItem("apple");
		li.setQuantity(10);
		lineItems.add(li);

		li = new LineItem();
		li.setItem("orange");
		li.setQuantity(5);
		lineItems.add(li);

		Order po = new Order();
		po.setEmail("acme@gmail.com");
		po.setDeliveryDate(new Date());
		po.setRush(true);
		po.setComments("Test order");
		po.setLineItems(lineItems);

		svc.insertPurchaseOrder(po);

		System.exit(0);
	}

}
