import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class StockTradingPlatform1 {
    private String symbol;
    private String name;
    private double price;

    public StockTradingPlatform1(String symbol, String name, double price) {
        this.symbol = symbol.toUpperCase();
        this.name = name;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class Transaction {
    private String type;
    private String symbol;
    private int quantity;
    private double pricePerShare;

    public Transaction(String type, String symbol, int quantity, double pricePerShare) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
    }

    @Override
    public String toString() {
        return String.format(
            "%-6s | %-6s | Qty: %-4d | Price: $%.2f | Total: $%.2f",
            type,
            symbol,
            quantity,
            pricePerShare,
            quantity * pricePerShare
        );
    }
}

class Portfolio {
    private double cashBalance;
    private Map<String, Integer> holdings;
    private ArrayList<Transaction> history;

    public Portfolio(double initialBalance) {
        cashBalance = initialBalance;
        holdings = new HashMap<>();
        history = new ArrayList<>();
    }

    public void buyStock(StockTradingPlatform1 stock, int quantity) {
        double totalCost = stock.getPrice() * quantity;

        if (totalCost > cashBalance) {
            System.out.println("Insufficient funds!");
            return;
        }

        cashBalance -= totalCost;

        holdings.put(
            stock.getSymbol(),
            holdings.getOrDefault(stock.getSymbol(), 0) + quantity
        );

        history.add(
            new Transaction(
                "BUY",
                stock.getSymbol(),
                quantity,
                stock.getPrice()
            )
        );

        System.out.println(
            "Successfully bought " +
            quantity + " shares of " +
            stock.getSymbol()
        );
    }

    public void sellStock(StockTradingPlatform1 stock, int quantity) {
        int currentHoldings =
            holdings.getOrDefault(stock.getSymbol(), 0);

        if (quantity > currentHoldings) {
            System.out.println(
                "You do not own enough shares to sell!"
            );
            return;
        }

        double totalRevenue =
            stock.getPrice() * quantity;

        cashBalance += totalRevenue;

        holdings.put(
            stock.getSymbol(),
            currentHoldings - quantity
        );

        if (holdings.get(stock.getSymbol()) == 0) {
            holdings.remove(stock.getSymbol());
        }

        history.add(
            new Transaction(
                "SELL",
                stock.getSymbol(),
                quantity,
                stock.getPrice()
            )
        );

        System.out.println(
            "Successfully sold " +
            quantity + " shares of " +
            stock.getSymbol()
        );
    }

    public void displayPortfolio(
        Map<String, StockTradingPlatform1> market
    ) {
        System.out.println("\n=========================================");
        System.out.println("             YOUR PORTFOLIO");
        System.out.println("=========================================");

        System.out.printf(
            "Cash Balance: $%.2f%n",
            cashBalance
        );

        double totalHoldingsValue = 0;

        for (Map.Entry<String, Integer> entry :
                holdings.entrySet()) {

            String symbol = entry.getKey();
            int quantity = entry.getValue();

            StockTradingPlatform1 stock =
                market.get(symbol);

            double value =
                stock.getPrice() * quantity;

            totalHoldingsValue += value;

            System.out.printf(
                "%s | Shares: %d | Price: $%.2f | Value: $%.2f%n",
                symbol,
                quantity,
                stock.getPrice(),
                value
            );
        }

        System.out.println("-----------------------------------------");

        System.out.printf(
            "Total Portfolio Value: $%.2f%n",
            cashBalance + totalHoldingsValue
        );

        System.out.println("=========================================");
    }

    public void displayHistory() {
        System.out.println("\n=========================================");
        System.out.println("          TRANSACTION HISTORY");
        System.out.println("=========================================");

        if (history.isEmpty()) {
            System.out.println("No transactions performed yet.");
        } else {
            for (Transaction transaction : history) {
                System.out.println(transaction);
            }
        }

        System.out.println("=========================================");
    }
}

public class StockTradingPlatform {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Map<String, StockTradingPlatform1> market =
            new HashMap<>();

        market.put(
            "AAPL",
            new StockTradingPlatform1(
                "AAPL",
                "Apple Inc.",
                180.50
            )
        );

        market.put(
            "GOOGL",
            new StockTradingPlatform1(
                "GOOGL",
                "Alphabet Inc.",
                140.25
            )
        );

        market.put(
            "TSLA",
            new StockTradingPlatform1(
                "TSLA",
                "Tesla Inc.",
                210.75
            )
        );

        market.put(
            "AMZN",
            new StockTradingPlatform1(
                "AMZN",
                "Amazon.com Inc.",
                175.00
            )
        );

        Portfolio portfolio =
            new Portfolio(10000.00);

        while (true) {

            System.out.println("\n--- STOCK TRADING PLATFORM ---");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");

            System.out.print("Select an option (1-6): ");

            String choice =
                scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    displayMarketData(market);
                    break;

                case "2":

                    displayMarketData(market);

                    System.out.print(
                        "Enter stock symbol to BUY: "
                    );

                    String buySymbol =
                        scanner.nextLine()
                        .trim()
                        .toUpperCase();

                    if (market.containsKey(buySymbol)) {

                        int quantity =
                            getValidQuantity(scanner);

                        if (quantity > 0) {
                            portfolio.buyStock(
                                market.get(buySymbol),
                                quantity
                            );
                        }

                    } else {
                        System.out.println(
                            "Invalid stock symbol."
                        );
                    }

                    break;

                case "3":

                    portfolio.displayPortfolio(market);

                    System.out.print(
                        "Enter stock symbol to SELL: "
                    );

                    String sellSymbol =
                        scanner.nextLine()
                        .trim()
                        .toUpperCase();

                    if (market.containsKey(sellSymbol)) {

                        int quantity =
                            getValidQuantity(scanner);

                        if (quantity > 0) {
                            portfolio.sellStock(
                                market.get(sellSymbol),
                                quantity
                            );
                        }

                    } else {
                        System.out.println(
                            "Invalid stock symbol."
                        );
                    }

                    break;

                case "4":
                    portfolio.displayPortfolio(market);
                    break;

                case "5":
                    portfolio.displayHistory();
                    break;

                case "6":
                    System.out.println(
                        "Thank you for using the Stock Trading Platform!"
                    );
                    scanner.close();
                    return;

                default:
                    System.out.println(
                        "Invalid choice. Please enter 1-6."
                    );
            }
        }
    }

    private static void displayMarketData(
        Map<String, StockTradingPlatform1> market
    ) {

        System.out.println("\n=========================================");
        System.out.println("              MARKET DATA");
        System.out.println("=========================================");

        System.out.printf(
            "%-8s | %-20s | %-10s%n",
            "Symbol",
            "Company",
            "Price"
        );

        System.out.println("-----------------------------------------");

        for (StockTradingPlatform1 stock :
                market.values()) {

            System.out.printf(
                "%-8s | %-20s | $%.2f%n",
                stock.getSymbol(),
                stock.getName(),
                stock.getPrice()
            );
        }

        System.out.println("=========================================");
    }

    private static int getValidQuantity(
        Scanner scanner
    ) {

        System.out.print("Enter quantity: ");

        try {

            int quantity =
                Integer.parseInt(
                    scanner.nextLine().trim()
                );

            if (quantity > 0) {
                return quantity;
            }

            System.out.println(
                "Quantity must be greater than 0."
            );

        } catch (NumberFormatException e) {

            System.out.println(
                "Please enter a valid number."
            );
        }

        return -1;
    }
}