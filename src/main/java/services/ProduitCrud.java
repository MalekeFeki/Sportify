package services;

import entities.Produit;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitCrud implements IProduitCrud<Produit> {
    private Connection connection;

    public ProduitCrud() {
        connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouterProduit(Produit produit) {
        String req1 = "INSERT INTO produit(name, price) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req1)) {
            pst.setString(1, produit.getName());
            pst.setDouble(2, produit.getPrice());
            pst.executeUpdate();
            System.out.println("Produit ajouté");
        } catch (SQLException e) {
            System.out.println("Error while adding product: " + e.getMessage());
        }
    }

    @Override
    public List<Produit> afficherProduits() {
        List<Produit> produits = new ArrayList<>();
        String req3 = "SELECT * FROM produit";
        try (Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(req3)) {
            while (rs.next()) {
                Produit produit = new Produit(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );
                produits.add(produit);
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching products: " + e.getMessage());
        }
        return produits;
    }

    @Override
    public void modifierProduit(Produit produit) {
        String req2 = "UPDATE produit SET name=?, price=? WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(req2)) {
            pst.setString(1, produit.getName());
            pst.setDouble(2, produit.getPrice());
            pst.setInt(3, produit.getId());
            pst.executeUpdate();
            System.out.println("Produit modifié");
        } catch (SQLException e) {
            System.out.println("Error while modifying product: " + e.getMessage());
        }
    }

    @Override
    public void supprimerProduit(int id) {
        String req3 = "DELETE FROM produit WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(req3)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Produit supprimé");
        } catch (SQLException e) {
            System.out.println("Error while deleting product: " + e.getMessage());
        }
    }
}
