# 📋 Checklist do Trabalho Prático (INF008)

### 🔹 1. Camada de Domínio e Regras de Negócio (Domain)
- [x] **Classe `Product`**: Criada com atributos em inglês, validações contra valores negativos e método `reduceStock` controlando o estoque.
- [x] **Classe `Order`**: Criada com agregação/composição de itens, cálculo de subtotal, cálculo de total polimórfico (desconto/frete) e fluxo de processamento de pagamento.
- [ ] **Classe `OrderItem`**: Garantir que está criada com atributos (`product`, `quantity`, `unitPrice`) e método `getSubtotal()` em inglês.
- [ ] **Classe `Cart`**: Garantir que possui a coleção de itens e os métodos para adicionar/remover produtos validando a quantidade.

### 🔹 2. Estruturas Polimórficas (Interfaces e Implementações)
- [x] **Formas de Pagamento (`Payable`)**:
  - [x] Interface/Classe abstrata criada.
  - [x] `BoletoPayment` implementado e revisado (com tratamento de erros de sintaxe).
  - [x] `CreditCardPayment` implementado.
  - [x] `PixPayment` implementado.
- [x] **Políticas de Desconto (`DiscountPolicy`)**:
  - [x] Interface criada com o método `calculateDiscount(double subtotal)`.
  - [x] `CouponDiscountPolicy` implementada.
  - [x] `StudentDiscountPolicy` implementada.
- [x] **Políticas de Entrega (`ShippingPolicy`)**:
  - [x] Interface criada com o método `calculateShippingCost(Order order)`.
  - [x] `StandardShippingPolicy` implementada.
  - [x] `ExpressShippingPolicy` implementada.

### 🔹 3. Tratamento de Exceções Customizadas (Exceptions)
- [x] **`InvalidPaymentException`**: Criada e sendo devidamente lançada no método `processOrderPayment()` da `Order`.
- [ ] **`InsufficientStockException` (ou similar)**: Criar a classe e garantir que ela seja lançada no fluxo (atualmente `Product.reduceStock` lança `IllegalArgumentException`, o ideal para o critério do professor é usar uma exceção customizada própria).

### 🔹 4. Camada de Persistência (Integration & Database)
- [x] **Configuração do Projeto (`pom.xml`)**: Dependência do Driver JDBC do MariaDB isolada corretamente dentro do `pom.xml` do seu plugin.
- [ ] **Classe `DatabaseConnection`**: Criar o arquivo gerenciador da conexão dentro da pasta de repositórios do plugin.
- [ ] **Repositórios (`ProductRepository`, etc.)**: Criar as classes que vão rodar os comandos SQL (`SELECT`) para buscar os dados iniciais disponibilizados no contêiner MariaDB.

### 🔹 5. Interface Gráfica e Acoplamento no Microkernel (UI & Plugin)
- [x] **Estrutura do `MyPlugin`**: Classe ativadora compreendida e preparada para registrar o menu superior do e-commerce no `IUIController`.
- [ ] **Desenho da Interface JavaFX (`OrderView`)**: Construir a tela que permite executar o fluxo mínimo exigido (listar produtos, montar carrinho, selecionar frete/desconto/pagamento e exibir o total final).
- [ ] **Tratamento de Erros Visual**: Capturar as exceções de estoque e pagamento na camada da UI para exibir alertas amigáveis ao usuário sem derrubar a aplicação.

---

### 🔹 6. Entregáveis Finais
- [ ] **Código-fonte Limpo**: Código sem arquivos `.class` compilados e 100% escrito em inglês.
- [ ] **Gravação do Vídeo**: Vídeo explicativo da dupla com no máximo 10 minutos abordando a modelagem, polimorfismo, exceções e demonstrando o fluxo integrado.