import java.sql.Connection;
import java.sql.DriverManager;

public class JDBC {
	public static Connection getConnection(){
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/moveeat_db";
		String user = "root";
        String pass = "";
        
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	conn = DriverManager.getConnection(url, user, pass);
        }catch (Exception exc){
        	System.out.println("Connection Failed!"+exc.getMessage());
        }
        return conn;
	}
    public static void main(String[] args) {
        Connection connection = getConnection();
        if (connection != null) {
            System.out.println("Connection successful!");
        }
    }
}