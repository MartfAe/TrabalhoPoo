package br.edu.ifba.inf008.plugins.repository;

import java.sql.SQLException;
import java.util.List;

import br.edu.ifba.inf008.plugins.domain.Product;

public interface ProductRepository {
    List<Product> findAllActive() throws SQLException;
}