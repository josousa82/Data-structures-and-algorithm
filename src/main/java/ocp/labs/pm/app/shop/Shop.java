/*
 * Copyright (c) 2021 sousaJ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ocp.labs.pm.app.shop;

import ocp.labs.pm.data.Product;
import ocp.labs.pm.data.ProductFilters;
import ocp.labs.pm.data.ProductManager;
import ocp.labs.pm.data.Rating;
import ocp.labs.pm.sorters.Sorters;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

/**
 * {@code} class represents an application that manages products
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {

	public static void main (String[] args)  {

		ProductManager pm = ProductManager.getInstance();
		AtomicInteger clientCount = new AtomicInteger(0);
		Callable<String> client = () -> {
			String clientId = "Client " + clientCount.incrementAndGet();
			String threadName = Thread.currentThread().getName();
			int productId = ThreadLocalRandom.current().nextInt(101, 108) ;
			String languageTag = ProductManager.getSupportedLocales()
											   .stream()
											   .skip(ThreadLocalRandom.current().nextInt(6))
											   .findFirst()
											   .get();

			StringBuilder log = new StringBuilder();

			log.append(clientId + " " + threadName + "\n-\tstart of log\t-\n");

			pm.getDiscounts(languageTag)
			  .entrySet()
			  .stream()
			  .map(entry -> entry.getKey() + "\t" + entry.getValue())
			  .collect(Collectors.joining("\n"));

			Product product = pm.reviewProduct(productId, Rating.FOUR_STAR, "Yet another review");
			log.append((product != null)?"\nProduct " + productId + " reviewed\n":"\nProduct " + productId + " not reviewed\n");

			pm.printProductReport(productId, languageTag, clientId);
			log.append(clientId + " generated report for " + productId + " product.");

			log.append("\n-\tend of log\t-\n");
			return log.toString();
		};

		List<Callable<String>> clients = Stream.generate(() -> client)
											   .limit(7)
											   .collect(Collectors.toList());
		ExecutorService executorService = Executors.newFixedThreadPool(3);

		try {
			List<Future<String>> results = executorService.invokeAll(clients);
			executorService.shutdown();
			results.stream().forEach(stringFuture -> {
				try {
					out.println(stringFuture.get());
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
					Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, "Error writing to the console ", e);
				}
			});
		} catch (InterruptedException e) {
			Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, "Error invoking clients", e);
			e.printStackTrace();
		}

		//		printProductsWithFiltersExample(pm);
	}

	private static void printProductsWithFiltersExample (ProductManager pm) {

		var en = "en-GB";
		var pr1 = pm.printProducts(ProductFilters.nonNull, ProductFilters.sortById, en);
		out.println("pr1 = " + pr1);

		var pr2 = pm.printProducts(ProductFilters.filterAllDrink.and(ProductFilters.priceBelowTwo),
								   Sorters.sortByRating.thenComparing(Sorters.sortByPrice), en);
		out.println("pr2 = " + pr2);

		var pr3 = pm.printProducts(ProductFilters.filterAllFood, Sorters.sortByRating, en);
		out.println("pr3 = " + pr3);

		var pr4 = pm.printProducts(ProductFilters.priceBelowTwo, Sorters.sortByPrice.reversed(), en);
		out.println("pr4 = " + pr4);

		pm.getDiscounts("en-GB").forEach((rating, discount) -> out.println(rating + "\t" + discount));
	}

	private static void printProduct (Product p1) {
		String formatPattern = "id: {0} - Product: {1} with price {2} and a discount of {3} has the final price of " +
				"{4}" + ".\nProduct Rating: {5}";
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK);
		String messageFormatted = MessageFormat.format(formatPattern, p1.getId(), p1.getName(),
													   currencyFormat.format(p1.getPrice()), currencyFormat
				.format(p1.getDiscount()), currencyFormat.format((p1.getPrice()
																	.subtract(p1.getDiscount()))), p1.getRating()
																									 .getStars());
		out.println(messageFormatted);
	}
}
