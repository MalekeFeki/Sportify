package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    public String url="jdbc:mysql://localhost:3306/sportify";
    public String login="root";
    public String password="";
    public int id;
    Connection cnx;
    public static MyConnection instance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getCnx() {
        return cnx;
    }

    public void setCnx(Connection cnx) {
        this.cnx = cnx;
    }

    private MyConnection() {
        try {
            cnx= DriverManager.getConnection(url,login,password);
            System.out.println("Connexion établie!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public static MyConnection getInstance(){
        if(instance==null){
            instance=new MyConnection();
        }
        return instance;
    }
}
