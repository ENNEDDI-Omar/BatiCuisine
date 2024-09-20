package java.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    //création des propriétées
    private static final String URL = "jdbc:postgresql://localhost:5432/baticuisine";
    private static final String User = "omario";
    private static  final String Password = "omario";
    private static Connection conx = null;
    private static DatabaseConnection instance = null;

    /**
     *Constructeur privé pour instanciation extérieur
     */
    //mise en place du constructeur privé
    private DatabaseConnection()
    {
        connect();
    }

    /**
     *Instanciation de la classe par la methode getInstance
     *
     * @return instance en singleton dela dbConnection
     */
    public static synchronized DatabaseConnection getInstance()
    {
        if (instance == null)
        {
            instance = new DatabaseConnection();
        }else {
            try {

                if (conx == null || conx.isClosed()) {
                    instance.connect();
                }
            } catch (SQLException e) {
                System.out.println("Failed to validate the database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Méthode pour établir de la connexion
     */
    private void connect()
    {
        try {
            //loader le jdbc driver
            Class.forName("org.postgresql.Driver");

            //établissement de la connection
            conx = DriverManager.getConnection(URL, User, Password);
            System.out.println("Database connection established successfully!");

        }catch (SQLException e)
        {
            System.out.println("Database connection failed!" + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver class not found!" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Methode pour getConx pour Rétablir la connxion
     * @return la connexion actuel
     */
    public Connection getConx()
    {
        try {
            if (conx == null || conx.isClosed())
            {
                connect();
            }
        } catch (SQLException e) {
            System.out.println("Failled to re-establish connection!");
            e.printStackTrace();
        }
        return conx;
    }

    /**
     * Méthode pour férmer la connexion avec la base de données
     */
    public static void closeConnexion()
    {
        if (conx != null)
        {
            try {
                conx.close();
                conx = null;
                System.out.println("Database connection is closed successfully.");
            } catch (SQLException e) {
                System.out.println("Failed to close database connection!" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
