package br.edu.ifba.inf008.plugins.model.shipping;
import br.edu.ifba.inf008.plugins.domain.Order;

public interface ShippingPolicy {

    double calculateShippingCost(Order order);

}
