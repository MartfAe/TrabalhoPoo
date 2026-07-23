package br.edu.ifba.inf008.plugins.ui;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import br.edu.ifba.inf008.plugins.domain.Cart;
import br.edu.ifba.inf008.plugins.domain.Order;
import br.edu.ifba.inf008.plugins.domain.OrderItem;
import br.edu.ifba.inf008.plugins.domain.Product;
import br.edu.ifba.inf008.plugins.exceptions.InsufficientStockException;
import br.edu.ifba.inf008.plugins.exceptions.InvalidPaymentException;
import br.edu.ifba.inf008.plugins.model.discount.CouponDiscountPolicy;
import br.edu.ifba.inf008.plugins.model.discount.DiscountPolicy;
import br.edu.ifba.inf008.plugins.model.discount.StudentDiscountPolicy;
import br.edu.ifba.inf008.plugins.model.payment.BoletoPayment;
import br.edu.ifba.inf008.plugins.model.payment.CreditCardPayment;
import br.edu.ifba.inf008.plugins.model.payment.Payable;
import br.edu.ifba.inf008.plugins.model.payment.PixPayment;
import br.edu.ifba.inf008.plugins.model.shipping.ExpressShippingPolicy;
import br.edu.ifba.inf008.plugins.model.shipping.ShippingPolicy;
import br.edu.ifba.inf008.plugins.model.shipping.StandardShippingPolicy;
import br.edu.ifba.inf008.plugins.repository.JdbcProductRepository;
import br.edu.ifba.inf008.plugins.repository.ProductRepository;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OrderView {

    private final BorderPane root;
    private final Cart cart;
    private final TableView<Product> productTable;
    private TableView<OrderItem> cartTable;
    private final ProductRepository productRepository;

    public OrderView() {
        this.cart = new Cart();
        this.productRepository = new JdbcProductRepository();
        this.productTable = buildProductTable();
        this.root = new BorderPane();
        root.getStyleClass().add("order-view");

        var stylesheet = getClass().getResource("/order-view.css");
        if (stylesheet != null) {
            root.getStylesheets().add(stylesheet.toExternalForm());
        }

        root.setPadding(new Insets(10));
        root.setTop(buildProductSection());
        root.setCenter(buildCartSection());
        root.setBottom(buildSummarySection());
    }

    public Node getRootPane() {
        return root;
    }

    // ---------- Região de cima: produtos disponíveis (igual à Aula 3) ----------

    private VBox buildProductSection() {
        Label title = new Label("Available products");
        title.getStyleClass().add("section-title");

        Spinner<Integer> quantitySpinner = new Spinner<>(1, 999, 1);
        quantitySpinner.setEditable(true);
        quantitySpinner.setPrefWidth(80);

        Label feedbackLabel = new Label();
        feedbackLabel.getStyleClass().add("feedback-error");

        Button addToCartButton = new Button("Add to cart");
        addToCartButton.setOnAction(e -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                feedbackLabel.setText("Select a product first.");
                return;
            }
            try {
                cart.addItem(selected, quantitySpinner.getValue());
                feedbackLabel.setText("");
                refreshCartTable();
            } catch (InsufficientStockException ex) {
                feedbackLabel.setText(ex.getMessage());
            }
        });

        HBox controls = new HBox(8, new Label("Quantity:"), quantitySpinner, addToCartButton, feedbackLabel);

        VBox box = new VBox(8, title, productTable, controls);
        box.setPadding(new Insets(0, 0, 10, 0));
        return box;
    }

    private TableView<Product> buildProductTable() {
        TableView<Product> table = new TableView<>();
        table.setPrefHeight(180);

        TableColumn<Product, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Product, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getUnitPrice()));

        TableColumn<Product, Number> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStockQuantity()));

        table.getColumns().addAll(codeCol, nameCol, priceCol, stockCol);
        table.setItems(loadProducts());
        return table;
    }

    private ObservableList<Product> loadProducts() {
        try {
            List<Product> fromDatabase = productRepository.findAllActive();
            if (!fromDatabase.isEmpty()) {
                return FXCollections.observableArrayList(fromDatabase);
            }
        } catch (SQLException e) {
            System.err.println("Could not load products from the database: " + e.getMessage());
        }
        return FXCollections.observableArrayList(sampleProducts());
    }

    private List<Product> sampleProducts() {
        return Arrays.asList(
            new Product("Wireless Mouse", "P001", "Ergonomic wireless mouse", 89.90, 25),
            new Product("Mechanical Keyboard", "P002", "RGB mechanical keyboard", 249.90, 12),
            new Product("USB-C Hub", "P003", "7-in-1 USB-C hub", 129.90, 30)
        );
    }

    // ---------- Região central: carrinho (igual à Aula 3) ----------

    private VBox buildCartSection() {
        Label title = new Label("Shopping cart");
        title.getStyleClass().add("section-title");

        cartTable = buildCartTable();

        Button removeButton = new Button("Remove selected");
        removeButton.setOnAction(e -> {
            OrderItem selected = cartTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                cart.removeItem(selected);
                refreshCartTable();
            }
        });

        VBox box = new VBox(8, title, cartTable, removeButton);
        box.setPadding(new Insets(10, 0, 10, 0));
        title.getStyleClass().add("section-title");
        return box;
    }

    private TableView<OrderItem> buildCartTable() {
        TableView<OrderItem> table = new TableView<>();
        table.setPrefHeight(160);

        TableColumn<OrderItem, String> nameCol = new TableColumn<>("Product");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProduct().getName()));

        TableColumn<OrderItem, Number> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()));

        TableColumn<OrderItem, Number> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getSubtotal()));

        table.getColumns().addAll(nameCol, qtyCol, subtotalCol);
        return table;
    }

    private void refreshCartTable() {
        cartTable.setItems(FXCollections.observableArrayList(cart.getItems()));
    }

    // ---------- Região de baixo: desconto, frete, pagamento e total (NOVO) ----------

    private VBox buildSummarySection() {
        Label title = new Label("Order summary");
        title.getStyleClass().add("section-title");

        ComboBox<String> discountCombo = new ComboBox<>(
            FXCollections.observableArrayList("None", "Coupon ($20 off)", "Student (15%)"));
        discountCombo.setPromptText("No discount");

        ComboBox<String> shippingCombo = new ComboBox<>(
            FXCollections.observableArrayList("Standard", "Express", "Pickup (free)"));
        shippingCombo.setPromptText("Select shipping");

        ComboBox<String> paymentCombo = new ComboBox<>(
            FXCollections.observableArrayList("Credit Card", "Pix", "Boleto"));
        paymentCombo.setPromptText("Select payment");

        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Card number");
        TextField cardHolderField = new TextField();
        cardHolderField.setPromptText("Card holder name");
        TextField cvvField = new TextField();
        cvvField.setPromptText("CVV");
        cvvField.setPrefWidth(60);

        HBox creditCardFields = new HBox(8, cardNumberField, cardHolderField, cvvField);
        creditCardFields.setVisible(false);
        creditCardFields.setManaged(false);

        paymentCombo.valueProperty().addListener((obs, oldValue, newValue) -> {
            boolean isCreditCard = "Credit Card".equals(newValue);
            creditCardFields.setVisible(isCreditCard);
            creditCardFields.setManaged(isCreditCard);
        });

        Label resultLabel = new Label();

        Button confirmButton = new Button("Confirm order");
        confirmButton.getStyleClass().add("button-primary");
        confirmButton.setOnAction(e -> {
            resultLabel.getStyleClass().setAll("feedback-error");

            if (cart.getItems().isEmpty()) {
                resultLabel.setText("Your cart is empty.");
                return;
            }
            if (shippingCombo.getValue() == null) {
                resultLabel.setText("Select a shipping method.");
                return;
            }
            String paymentChoice = paymentCombo.getValue();
            if (paymentChoice == null) {
                resultLabel.setText("Select a payment method.");
                return;
            }

            Payable payment;
            if ("Credit Card".equals(paymentChoice)) {
                if (cardNumberField.getText().isBlank() || cardHolderField.getText().isBlank()
                        || cvvField.getText().isBlank()) {
                    resultLabel.setText("Fill in the credit card details.");
                    return;
                }
                payment = new CreditCardPayment(cardNumberField.getText(), cardHolderField.getText(), cvvField.getText());
            } else if ("Pix".equals(paymentChoice)) {
                payment = new PixPayment();
            } else {
                payment = new BoletoPayment();
            }

            try {
                Order order = new Order(cart);
                order.setDiscountPolicy(mapDiscount(discountCombo.getValue()));
                order.setShippingPolicy(mapShipping(shippingCombo.getValue()));
                order.setPaymentMethod(payment);

                double total = order.calculateTotal();
                order.processOrderPayment();

                resultLabel.getStyleClass().setAll("feedback-success");                resultLabel.setText(String.format("Order paid! Total: %.2f - Status: %s", total, order.getStatus()));

                cart.clear();
                refreshCartTable();
                productTable.refresh();

            } catch (InvalidPaymentException ex) {
                resultLabel.setText("Payment failed: " + ex.getMessage());
            } catch (InsufficientStockException ex) {
                resultLabel.setText(ex.getMessage());
            }
        });

        HBox combos = new HBox(10,
            new VBox(4, new Label("Discount:"), discountCombo),
            new VBox(4, new Label("Shipping:"), shippingCombo),
            new VBox(4, new Label("Payment:"), paymentCombo));

        VBox box = new VBox(8, title, combos, creditCardFields, confirmButton, resultLabel);
        box.setPadding(new Insets(10, 0, 0, 0));
        title.getStyleClass().add("section-title");
        title.getStyleClass().add("section-title");
        return box;
    }

    private DiscountPolicy mapDiscount(String choice) {
        if ("Coupon ($20 off)".equals(choice)) {
            return new CouponDiscountPolicy(20.0);
        }
        if ("Student (15%)".equals(choice)) {
            return new StudentDiscountPolicy();
        }
        return null;
    }

    private ShippingPolicy mapShipping(String choice) {
        if ("Express".equals(choice)) {
            return new ExpressShippingPolicy();
        }
        if ("Standard".equals(choice)) {
            return new StandardShippingPolicy();
        }
        return null;
    }
}