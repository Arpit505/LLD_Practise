// ========================= MODELS =========================

import java.util.*;
import java.util.stream.Collectors;

// User base class
abstract class User {
    private String userId;
    private String name;
    private String email;
    private String phone;

    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
}

// Admin class
class Admin extends User {
    private String adminLevel;

    public Admin(String userId, String name, String email, String phone, String adminLevel) {
        super(userId, name, email, phone);
        this.adminLevel = adminLevel;
    }

    public String getAdminLevel() { return adminLevel; }
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }
}

// Customer class
class Customer extends User {
    private static final int MAX_RENTAL_LIMIT = 5;
    private List<DVD> rentedDVDs;
    private double totalDue;

    public Customer(String userId, String name, String email, String phone) {
        super(userId, name, email, phone);
        this.rentedDVDs = new ArrayList<>();
        this.totalDue = 0.0;
    }

    public List<DVD> getRentedDVDs() { return rentedDVDs; }
    public int getRentedCount() { return rentedDVDs.size(); }
    public boolean canRentMore() { return rentedDVDs.size() < MAX_RENTAL_LIMIT; }
    public double getTotalDue() { return totalDue; }

    public void addRentedDVD(DVD dvd) {
        if (canRentMore()) {
            rentedDVDs.add(dvd);
        }
    }

    public void returnDVD(DVD dvd) {
        rentedDVDs.remove(dvd);
    }

    public void addToBill(double amount) {
        totalDue += amount;
    }

    public void payBill(double amount) {
        totalDue -= amount;
    }
}

// Movie class
class Movie {
    private String movieId;
    private String movieName;
    private String genre;
    private String director;
    private int releaseYear;
    private int totalCopies;
    private int availableCopies;
    private double rentalPrice;

    public Movie(String movieId, String movieName, String genre, String director,
                 int releaseYear, int totalCopies, double rentalPrice) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.genre = genre;
        this.director = director;
        this.releaseYear = releaseYear;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.rentalPrice = rentalPrice;
    }

    // Getters and setters
    public String getMovieId() { return movieId; }
    public String getMovieName() { return movieName; }
    public String getGenre() { return genre; }
    public String getDirector() { return director; }
    public int getReleaseYear() { return releaseYear; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public int getRentedCopies() { return totalCopies - availableCopies; }
    public double getRentalPrice() { return rentalPrice; }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
        this.availableCopies = Math.min(availableCopies, totalCopies);
    }

    public void setRentalPrice(double rentalPrice) { this.rentalPrice = rentalPrice; }

    public boolean isAvailable() { return availableCopies > 0; }

    public void rentCopy() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }
}

// DVD class
class DVD {
    private String dvdId;
    private String movieId;
    private boolean isRented;
    private String rentedBy;
    private Date rentedDate;
    private Date dueDate;

    public DVD(String dvdId, String movieId) {
        this.dvdId = dvdId;
        this.movieId = movieId;
        this.isRented = false;
    }

    // Getters and setters
    public String getDvdId() { return dvdId; }
    public String getMovieId() { return movieId; }
    public boolean isRented() { return isRented; }
    public String getRentedBy() { return rentedBy; }
    public Date getRentedDate() { return rentedDate; }
    public Date getDueDate() { return dueDate; }

    public void rentTo(String customerId, Date rentedDate, Date dueDate) {
        this.isRented = true;
        this.rentedBy = customerId;
        this.rentedDate = rentedDate;
        this.dueDate = dueDate;
    }

    public void returnDVD() {
        this.isRented = false;
        this.rentedBy = null;
        this.rentedDate = null;
        this.dueDate = null;
    }
}

// Rental Transaction class
class RentalTransaction {
    private String transactionId;
    private String customerId;
    private String dvdId;
    private Date rentedDate;
    private Date dueDate;
    private Date returnedDate;
    private double rentalFee;
    private double lateFee;
    private TransactionStatus status;

    public enum TransactionStatus {
        ACTIVE, RETURNED, OVERDUE
    }

    public RentalTransaction(String transactionId, String customerId, String dvdId,
                             Date rentedDate, Date dueDate, double rentalFee) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.dvdId = dvdId;
        this.rentedDate = rentedDate;
        this.dueDate = dueDate;
        this.rentalFee = rentalFee;
        this.status = TransactionStatus.ACTIVE;
        this.lateFee = 0.0;
    }

    // Getters and setters
    public String getTransactionId() { return transactionId; }
    public String getCustomerId() { return customerId; }
    public String getDvdId() { return dvdId; }
    public Date getRentedDate() { return rentedDate; }
    public Date getDueDate() { return dueDate; }
    public Date getReturnedDate() { return returnedDate; }
    public double getRentalFee() { return rentalFee; }
    public double getLateFee() { return lateFee; }
    public TransactionStatus getStatus() { return status; }

    public void setReturnedDate(Date returnedDate) { this.returnedDate = returnedDate; }
    public void setLateFee(double lateFee) { this.lateFee = lateFee; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public double getTotalAmount() { return rentalFee + lateFee; }
}

// ========================= SERVICES =========================

// User Service
class UserService {
    private Map<String, Admin> admins;
    private Map<String, Customer> customers;

    public UserService() {
        this.admins = new HashMap<>();
        this.customers = new HashMap<>();
    }

    public void registerAdmin(Admin admin) {
        admins.put(admin.getUserId(), admin);
    }

    public void registerCustomer(Customer customer) {
        customers.put(customer.getUserId(), customer);
    }

    public Admin getAdmin(String adminId) {
        return admins.get(adminId);
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    public boolean isValidAdmin(String adminId) {
        return admins.containsKey(adminId);
    }

    public boolean isValidCustomer(String customerId) {
        return customers.containsKey(customerId);
    }
}

// Movie Catalog Service
class MovieCatalogService {
    private Map<String, Movie> movies;
    private Map<String, List<DVD>> movieDVDs;

    public MovieCatalogService() {
        this.movies = new HashMap<>();
        this.movieDVDs = new HashMap<>();
    }

    public void addMovie(Movie movie, int dvdCount) {
        movies.put(movie.getMovieId(), movie);
        List<DVD> dvds = new ArrayList<>();

        for (int i = 0; i < dvdCount; i++) {
            String dvdId = movie.getMovieId() + "_DVD_" + (i + 1);
            DVD dvd = new DVD(dvdId, movie.getMovieId());
            dvds.add(dvd);
        }

        movieDVDs.put(movie.getMovieId(), dvds);
    }

    public Movie getMovie(String movieId) {
        return movies.get(movieId);
    }

    public Movie getMovieByTitle(String movieTitle) {
        return movies.values().stream()
                .filter(movie -> movie.getMovieName().equalsIgnoreCase(movieTitle))
                .findFirst()
                .orElse(null);
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies.values());
    }

    public List<DVD> getAvailableDVDs(String movieId) {
        return movieDVDs.get(movieId).stream()
                .filter(dvd -> !dvd.isRented())
                .collect(Collectors.toList());
    }

    public DVD getDVD(String dvdId) {
        return movieDVDs.values().stream()
                .flatMap(List::stream)
                .filter(dvd -> dvd.getDvdId().equals(dvdId))
                .findFirst()
                .orElse(null);
    }

    public void updateMovieStock(String movieId, int newStock) {
        Movie movie = movies.get(movieId);
        if (movie != null) {
            movie.setTotalCopies(newStock);
        }
    }
}

// Bill Calculator Service
class BillCalculatorService {
    private static final double LATE_FEE_PER_DAY = 2.0;

    public double calculateRentalFee(Movie movie, int days) {
        return movie.getRentalPrice() * days;
    }

    public double calculateLateFee(Date dueDate, Date returnDate) {
        if (returnDate.before(dueDate) || returnDate.equals(dueDate)) {
            return 0.0;
        }

        long diffInMillis = returnDate.getTime() - dueDate.getTime();
        long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);

        return diffInDays * LATE_FEE_PER_DAY;
    }

    public double calculateTotalBill(Customer customer, List<RentalTransaction> transactions) {
        double total = 0.0;
        for (RentalTransaction transaction : transactions) {
            total += transaction.getTotalAmount();
        }
        return total;
    }
}

// ========================= MAIN CONTROLLER =========================

class VideoRentalController {
    private UserService userService;
    private MovieCatalogService movieCatalogService;
    private BillCalculatorService billCalculatorService;
    private Map<String, RentalTransaction> transactions;
    private int transactionCounter;

    public VideoRentalController() {
        this.userService = new UserService();
        this.movieCatalogService = new MovieCatalogService();
        this.billCalculatorService = new BillCalculatorService();
        this.transactions = new HashMap<>();
        this.transactionCounter = 1;
    }

    // Admin Operations
    public void registerAdmin(String adminId, String name, String email, String phone, String adminLevel) {
        Admin admin = new Admin(adminId, name, email, phone, adminLevel);
        userService.registerAdmin(admin);
    }

    public void addMovieToeCatalog(String adminId, String movieId, String movieName, String genre,
                                   String director, int releaseYear, int totalCopies, double rentalPrice) {
        if (!userService.isValidAdmin(adminId)) {
            throw new IllegalArgumentException("Invalid admin ID");
        }

        Movie movie = new Movie(movieId, movieName, genre, director, releaseYear, totalCopies, rentalPrice);
        movieCatalogService.addMovie(movie, totalCopies);
    }

    // Customer Operations
    public void registerCustomer(String customerId, String name, String email, String phone) {
        Customer customer = new Customer(customerId, name, email, phone);
        userService.registerCustomer(customer);
    }

    public String rentMovie(String customerId, String movieTitle, int rentalDays) {
        Customer customer = userService.getCustomer(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Invalid customer ID");
        }

        if (!customer.canRentMore()) {
            throw new IllegalStateException("Customer has reached rental limit of 5 DVDs");
        }

        Movie movie = movieCatalogService.getMovieByTitle(movieTitle);
        if (movie == null) {
            throw new IllegalArgumentException("Movie not found in catalog");
        }

        if (!movie.isAvailable()) {
            throw new IllegalStateException("Movie is not available for rent");
        }

        List<DVD> availableDVDs = movieCatalogService.getAvailableDVDs(movie.getMovieId());
        if (availableDVDs.isEmpty()) {
            throw new IllegalStateException("No DVDs available for this movie");
        }

        DVD dvd = availableDVDs.get(0);
        Date rentedDate = new Date();
        Date dueDate = new Date(rentedDate.getTime() + (rentalDays * 24 * 60 * 60 * 1000L));

        dvd.rentTo(customerId, rentedDate, dueDate);
        customer.addRentedDVD(dvd);
        movie.rentCopy();

        double rentalFee = billCalculatorService.calculateRentalFee(movie, rentalDays);
        customer.addToBill(rentalFee);

        String transactionId = "TXN_" + transactionCounter++;
        RentalTransaction transaction = new RentalTransaction(transactionId, customerId,
                dvd.getDvdId(), rentedDate, dueDate, rentalFee);
        transactions.put(transactionId, transaction);

        return transactionId;
    }

    public double returnMovie(String customerId, String dvdId) {
        Customer customer = userService.getCustomer(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Invalid customer ID");
        }

        DVD dvd = movieCatalogService.getDVD(dvdId);
        if (dvd == null) {
            throw new IllegalArgumentException("Invalid DVD ID");
        }

        if (!dvd.isRented() || !dvd.getRentedBy().equals(customerId)) {
            throw new IllegalStateException("DVD is not rented by this customer");
        }

        RentalTransaction transaction = transactions.values().stream()
                .filter(t -> t.getCustomerId().equals(customerId) && t.getDvdId().equals(dvdId)
                        && t.getStatus() == RentalTransaction.TransactionStatus.ACTIVE)
                .findFirst()
                .orElse(null);

        if (transaction == null) {
            throw new IllegalStateException("No active transaction found for this DVD");
        }

        Date returnDate = new Date();
        double lateFee = billCalculatorService.calculateLateFee(transaction.getDueDate(), returnDate);

        transaction.setReturnedDate(returnDate);
        transaction.setLateFee(lateFee);
        transaction.setStatus(RentalTransaction.TransactionStatus.RETURNED);

        if (lateFee > 0) {
            customer.addToBill(lateFee);
        }

        dvd.returnDVD();
        customer.returnDVD(dvd);

        Movie movie = movieCatalogService.getMovie(dvd.getMovieId());
        movie.returnCopy();

        return transaction.getTotalAmount();
    }

    // Utility Methods
    public List<Movie> getAllMoviesWithStock() {
        return movieCatalogService.getAllMovies();
    }

    public Customer getCustomerDetails(String customerId) {
        return userService.getCustomer(customerId);
    }

    public List<RentalTransaction> getCustomerTransactions(String customerId) {
        return transactions.values().stream()
                .filter(t -> t.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    public double getCustomerTotalDue(String customerId) {
        Customer customer = userService.getCustomer(customerId);
        return customer != null ? customer.getTotalDue() : 0.0;
    }
}

// ========================= DEMO USAGE =========================

import java.util.*;
        import java.util.stream.Collectors;

public class VideoRentalSystemDemo {
    public static void main(String[] args) {
        VideoRentalController controller = new VideoRentalController();

        // Register Admin
        controller.registerAdmin("ADMIN_001", "John Admin", "john@rental.com", "1234567890", "SUPER_ADMIN");

        // Add movies to catalog
        controller.addMovieToeCatalog("ADMIN_001", "MOV_001", "The Matrix", "Sci-Fi", "The Wachowskis", 1999, 5, 3.99);
        controller.addMovieToeCatalog("ADMIN_001", "MOV_002", "Inception", "Sci-Fi", "Christopher Nolan", 2010, 3, 4.99);
        controller.addMovieToeCatalog("ADMIN_001", "MOV_003", "Titanic", "Romance", "James Cameron", 1997, 2, 2.99);

        // Register customers
        controller.registerCustomer("CUST_001", "Alice Johnson", "alice@email.com", "9876543210");
        controller.registerCustomer("CUST_002", "Bob Smith", "bob@email.com", "8765432109");

        // Rent movies
        try {
            String txn1 = controller.rentMovie("CUST_001", "The Matrix", 3);
            System.out.println("Rented The Matrix. Transaction ID: " + txn1);

            String txn2 = controller.rentMovie("CUST_001", "Inception", 5);
            System.out.println("Rented Inception. Transaction ID: " + txn2);

            String txn3 = controller.rentMovie("CUST_002", "Titanic", 2);
            System.out.println("Rented Titanic. Transaction ID: " + txn3);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Display catalog
        System.out.println("\n=== Movie Catalog ===");
        List<Movie> movies = controller.getAllMoviesWithStock();
        for (Movie movie : movies) {
            System.out.println(movie.getMovieName() + " - Available: " + movie.getAvailableCopies() +
                    "/" + movie.getTotalCopies() + " - Price: $" + movie.getRentalPrice());
        }

        // Display customer details
        System.out.println("\n=== Customer Details ===");
        Customer customer = controller.getCustomerDetails("CUST_001");
        System.out.println("Customer: " + customer.getName());
        System.out.println("Rented DVDs: " + customer.getRentedCount());
        System.out.println("Total Due: $" + customer.getTotalDue());
    }
}