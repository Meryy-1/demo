package com.example;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:restaurant.db";
    private static Connection connection;

    // Initialize database and create tables
    public static void initialize() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Create tables if they don't exist
    private static void createTables() {
        String createClientTable = "CREATE TABLE IF NOT EXISTS client (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "number TEXT NOT NULL, " +
                "UNIQUE(name, number)" +
                ")";

        String createAdminTable = "CREATE TABLE IF NOT EXISTS admin (" +
                "admin_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "admin_code TEXT NOT NULL UNIQUE, " +
                "admin_password TEXT NOT NULL" +
                ")";

        String createTablesTable = "CREATE TABLE IF NOT EXISTS tables (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "max_capacity INTEGER NOT NULL, " +
                "availability INTEGER DEFAULT 1" +
                ")";

        String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "client_name TEXT NOT NULL, " +
                "client_number TEXT NOT NULL, " +
                "item_name TEXT NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "price REAL NOT NULL, " +
                "order_date TEXT NOT NULL" +
                ")";

        String createReservationsTable = "CREATE TABLE IF NOT EXISTS reservations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "client_name TEXT NOT NULL, " +
                "client_number TEXT NOT NULL, " +
                "table_id INTEGER NOT NULL, " +
                "party_size INTEGER NOT NULL, " +
                "reservation_date TEXT NOT NULL, " +
                "UNIQUE(client_name, client_number), " +
                "FOREIGN KEY(table_id) REFERENCES tables(id)" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createClientTable);
            stmt.execute(createAdminTable);
            stmt.execute(createTablesTable);
            stmt.execute(createOrdersTable);
            stmt.execute(createReservationsTable);

            // Add default admin if table is empty
            addDefaultAdmin();

            // Add default tables if none exist
            addDefaultTables();
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add default admin account if none exists
    private static void addDefaultAdmin() {
        String checkQuery = "SELECT COUNT(*) FROM admin";
        String insertQuery = "INSERT INTO admin (admin_code, admin_password) VALUES (?, ?)";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(checkQuery)) {

            if (rs.next() && rs.getInt(1) == 0) {
                // No admins exist, add default one
                try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
                    pstmt.setString(1, "admin");
                    pstmt.setString(2, "admin123");
                    pstmt.executeUpdate();
                    System.out.println("Default admin account created (code: admin, password: admin123)");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding default admin: " + e.getMessage());
        }
    }

    // Add default tables if none exist
    private static void addDefaultTables() {
        String checkQuery = "SELECT COUNT(*) FROM tables";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(checkQuery)) {

            if (rs.next() && rs.getInt(1) == 0) {
                // No tables exist, add default ones
                String insertQuery = "INSERT INTO tables (max_capacity, availability) VALUES (?, ?)";

                try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
                    // Add 10 tables with different capacities
                    int[][] tables = { { 2, 1 }, { 2, 1 }, { 4, 1 }, { 4, 1 }, { 4, 1 }, { 6, 1 }, { 6, 1 }, { 8, 1 },
                            { 8, 1 }, { 10, 1 } };

                    for (int[] table : tables) {
                        pstmt.setInt(1, table[0]);
                        pstmt.setInt(2, table[1]);
                        pstmt.executeUpdate();
                    }

                    System.out.println("Default tables created (10 tables with various capacities)");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding default tables: " + e.getMessage());
        }
    }

    // Table class to represent table data
    public static class Table {
        private int id;
        private int maxCapacity;
        private boolean availability;

        public Table(int id, int maxCapacity, boolean availability) {
            this.id = id;
            this.maxCapacity = maxCapacity;
            this.availability = availability;
        }

        public int getId() {
            return id;
        }

        public int getMaxCapacity() {
            return maxCapacity;
        }

        public boolean isAvailable() {
            return availability;
        }
    }

    // Reservation class to represent reservation data
    public static class Reservation {
        private int id;
        private String clientName;
        private String clientNumber;
        private int tableId;
        private int partySize;
        private String reservationDate;

        public Reservation(int id, String clientName, String clientNumber,
                int tableId, int partySize, String reservationDate) {
            this.id = id;
            this.clientName = clientName;
            this.clientNumber = clientNumber;
            this.tableId = tableId;
            this.partySize = partySize;
            this.reservationDate = reservationDate;
        }

        public int getId() {
            return id;
        }

        public String getClientName() {
            return clientName;
        }

        public String getClientNumber() {
            return clientNumber;
        }

        public int getTableId() {
            return tableId;
        }

        public int getPartySize() {
            return partySize;
        }

        public String getReservationDate() {
            return reservationDate;
        }
    }

    // Get all available tables
    public static java.util.List<Table> getAvailableTables() {
        java.util.List<Table> tables = new java.util.ArrayList<>();
        String query = "SELECT id, max_capacity, availability FROM tables WHERE availability = 1";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tables.add(new Table(
                        rs.getInt("id"),
                        rs.getInt("max_capacity"),
                        rs.getInt("availability") == 1));
            }
        } catch (SQLException e) {
            System.err.println("Error getting available tables: " + e.getMessage());
        }

        return tables;
    }

    // Get table by ID
    public static Table getTableById(int tableId) {
        String query = "SELECT id, max_capacity, availability FROM tables WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tableId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Table(
                            rs.getInt("id"),
                            rs.getInt("max_capacity"),
                            rs.getInt("availability") == 1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting table: " + e.getMessage());
        }

        return null;
    }

    // Reserve a table (set availability to 0)
    public static boolean reserveTable(int tableId) {
        String query = "UPDATE tables SET availability = 0 WHERE id = ? AND availability = 1";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tableId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error reserving table: " + e.getMessage());
            return false;
        }
    }

    // Release a table (set availability to 1)
    public static boolean releaseTable(int tableId) {
        String query = "UPDATE tables SET availability = 1 WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, tableId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error releasing table: " + e.getMessage());
            return false;
        }
    }

    // Check if client exists by name and number
    public static boolean clientExists(String name, String number) {
        String query = "SELECT COUNT(*) FROM client WHERE name = ? AND number = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, number);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking client existence: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Add new client to database
    public static boolean addClient(String name, String number) {
        String query = "INSERT INTO client (name, number) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, number);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding client: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Verify admin credentials
    public static boolean verifyAdmin(String adminCode, String password) {
        String query = "SELECT COUNT(*) FROM admin WHERE admin_code = ? AND admin_password = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, adminCode);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error verifying admin: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Add new admin (for future use)
    public static boolean addAdmin(String adminCode, String password) {
        String query = "INSERT INTO admin (admin_code, admin_password) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, adminCode);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding admin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Add new order to database
    public static boolean addOrder(String clientName, String clientNumber, String itemName,
            int quantity, double price, String orderDate) {
        String query = "INSERT INTO orders (client_name, client_number, item_name, quantity, price, order_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, clientName);
            pstmt.setString(2, clientNumber);
            pstmt.setString(3, itemName);
            pstmt.setInt(4, quantity);
            pstmt.setDouble(5, price);
            pstmt.setString(6, orderDate);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding order: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get all orders for a specific client
    public static java.util.List<Order> getClientOrders(String clientName, String clientNumber) {
        java.util.List<Order> orders = new java.util.ArrayList<>();
        String query = "SELECT * FROM orders WHERE client_name = ? AND client_number = ? ORDER BY order_date DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, clientName);
            pstmt.setString(2, clientNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(new Order(
                            rs.getInt("id"),
                            rs.getString("client_name"),
                            rs.getString("client_number"),
                            rs.getString("item_name"),
                            rs.getInt("quantity"),
                            rs.getDouble("price"),
                            rs.getString("order_date")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting client orders: " + e.getMessage());
            e.printStackTrace();
        }

        return orders;
    }

    // Get all orders (for admin view if needed)
    public static java.util.List<Order> getAllOrders() {
        java.util.List<Order> orders = new java.util.ArrayList<>();
        String query = "SELECT * FROM orders ORDER BY order_date DESC";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        rs.getString("client_name"),
                        rs.getString("client_number"),
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("order_date")));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all orders: " + e.getMessage());
            e.printStackTrace();
        }

        return orders;
    }

    // Add a new reservation
    public static boolean addReservation(String clientName, String clientNumber,
            int tableId, int partySize) {
        // First check if client already has a reservation
        if (getClientReservation(clientName, clientNumber) != null) {
            System.err.println("Client already has a reservation");
            return false;
        }

        // Check if table is available
        Table table = getTableById(tableId);
        if (table == null || !table.isAvailable()) {
            System.err.println("Table is not available");
            return false;
        }

        // Reserve the table
        if (!reserveTable(tableId)) {
            return false;
        }

        // Add reservation record
        String query = "INSERT INTO reservations (client_name, client_number, table_id, party_size, reservation_date) "
                +
                "VALUES (?, ?, ?, ?, datetime('now'))";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, clientName);
            pstmt.setString(2, clientNumber);
            pstmt.setInt(3, tableId);
            pstmt.setInt(4, partySize);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding reservation: " + e.getMessage());
            // Rollback table reservation
            releaseTable(tableId);
            return false;
        }
    }

    // Get reservation for a specific client
    public static Reservation getClientReservation(String clientName, String clientNumber) {
        String query = "SELECT * FROM reservations WHERE client_name = ? AND client_number = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, clientName);
            pstmt.setString(2, clientNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Reservation(
                            rs.getInt("id"),
                            rs.getString("client_name"),
                            rs.getString("client_number"),
                            rs.getInt("table_id"),
                            rs.getInt("party_size"),
                            rs.getString("reservation_date"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting client reservation: " + e.getMessage());
        }

        return null;
    }

    // Cancel a client's reservation
    public static boolean cancelClientReservation(String clientName, String clientNumber) {
        Reservation reservation = getClientReservation(clientName, clientNumber);

        if (reservation == null) {
            return false;
        }

        // Delete reservation record
        String query = "DELETE FROM reservations WHERE client_name = ? AND client_number = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, clientName);
            pstmt.setString(2, clientNumber);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Release the table
                releaseTable(reservation.getTableId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error cancelling reservation: " + e.getMessage());
        }

        return false;
    }

    // Get all reservations (for admin view if needed)
    public static java.util.List<Reservation> getAllReservations() {
        java.util.List<Reservation> reservations = new java.util.ArrayList<>();
        String query = "SELECT * FROM reservations ORDER BY reservation_date DESC";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("id"),
                        rs.getString("client_name"),
                        rs.getString("client_number"),
                        rs.getInt("table_id"),
                        rs.getInt("party_size"),
                        rs.getString("reservation_date")));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all reservations: " + e.getMessage());
        }

        return reservations;
    }

    // Close database connection
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }
}
