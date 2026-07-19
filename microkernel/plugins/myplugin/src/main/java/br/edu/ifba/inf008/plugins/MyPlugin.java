package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.ui.OrderView;

import javafx.scene.control.MenuItem;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class MyPlugin implements IPlugin {

    @Override
    public boolean init() {
        // Obtém o controlador de interface gráfica exposto pelo Core do microkernel
        IUIController uiController = ICore.getInstance().getUIController();

        // Cria o item de menu na barra superior sob a categoria "E-commerce"
        MenuItem menuItem = uiController.createMenuItem("E-commerce", "Order Processing");
        
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    // Instancia a classe responsável por desenhar a tela em JavaFX
                    OrderView orderView = new OrderView();
                    
                    // Cria uma nova aba contendo o painel principal da nossa tela
                    uiController.createTab("Order Processing", orderView.getRootPane());
                    
                } catch (Exception ex) {
                    System.err.println("Error while loading the E-commerce plugin tab: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        return true;
    }
}