package br.com.mercadosilva.modulos;

import br.com.mercadosilva.modulos.persistencia.Persistencia;
import br.com.mercadosilva.modulos.util.QuickSort;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;

public class Products extends Persistencia implements Comparable<Products> {

	private static final long serialVersionUID = 1L;

	private static LinkedList<Products> productsList = new LinkedList<>();

	private String title;
	private double price;
	private int amount;
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	/*
	*
	* Procedimento para salvar um produto
	* @arguments Product novoProduto
	* @return void
	*
	* */
	public void saveProduct (Products novoProduto) throws IOException {

		try {

			// Se o arquivo dos dados dos Produtos não existir.
			if (!this.isExists("db.products")) {
				// Cria uma lista de Produtos e adiciona o novo Produto
				// Em seguida, persiste no arquivo
				productsList.add(novoProduto);
				this.save("db.products", productsList);
				System.out.println("\nProduto inserido com sucesso!\n");
			} else {
				/*
				 *
				 * Caso o arquivo exista, indexo todos os dados do arquivo temporariamente na memória;
				 * Em seguida, adiciona na mesma lista o novo produto, e persiste TODA a lista novamente no arquivo,
				 * com os produtos antigos e o novo produto
				*/
				Products products = new Products();
				productsList = products.getProducts();
				productsList.add(novoProduto);

				/* Persiste a lista no arquivo
				 * Como a classe deste contexto herda da Persistencia, posso utilizar apenas o contexto "this"
				 * para utilizar os métodos da persistência para brincar com I/O
				*/
				this.save("db.products", productsList);
				System.out.println("\nProduto inserido com sucesso!\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 *
	 * Obtem uma lista de produtos
	 * @return LinkedList<Products>
	 *
	 * */
	public LinkedList<Products> getProducts () throws IOException, ClassNotFoundException {

		Object o;
		LinkedList<Products> produtos = null;
		try {
			// Se o arquivo de produtos não é vazio, ele obtem os objetos do mesmo, caso inverso, retorna o valor padrão nulo
			if (!this.isEmpty("db.products")) {
				o = this.get("db.products");
				produtos = (LinkedList<Products>) o;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return produtos;
	}

	/*
	*
	* Obtem todos os produtos em ordem alfabética
	*
	* @return void
	*
	* */
	public void screenProducts () throws IOException, ClassNotFoundException {
		// Para formatar números reais em duas casa decimais
		DecimalFormat decimal = new DecimalFormat("0.00");

		// Se o arquivo for vazio...
		if (this.isEmpty("db.products")) {
			System.out.println("\nAinda não há produtos no estoque!\n");
		} else {
			// Cria a instância para Produtos
			Products produtos = new Products();

			// Obtem todos os produtos
			LinkedList<Products> listaProdutos = produtos.getProducts();
			int size = listaProdutos.size();

			// Aloca um vetor auxiliar para ordená-los
			Products[] aux = new Products[size];

			// Adiciona cada item da lista no array auxiliar dos produtos
			for (int i = 0; i < size; i++)
				aux[i] = listaProdutos.get(i);

			// Ordena o vetor auxiliar
			QuickSort.sort(aux);

			// Itera e retorna os produtos ordenados em ordem alfabética
			int i = 0;
			for (Products p: aux) {

				// Checa se a quantidade do produto de cada iteração do índice "i" é igual a zero, se for, o produto não existe mais no estoque
				if (checkIfThereAreProducts(i))
					System.out.println("NOTIFICAÇÃO: Este produto não existe mais no estoque.");

				System.out.println("Produto: "+aux[i].getTitle());
				System.out.println("Preço: R$ "+decimal.format(aux[i].getPrice()));
				System.out.println("Quantidade em estoque: "+aux[i].getAmount());
				System.out.println("Código: "+aux[i].getCode());
				System.out.println("------------------------------------------");
				i++;
			}

			// "Zera" as duas listas da memória no fim da execução desse ciclo de vida
			// A principio, zera o array auxiliar, ulterior, zera a LinkedList
			Arrays.fill(aux, null);
			listaProdutos.clear();
		}

	}

	/*
	 *
	 * Verifica se a quantidade do produto é igual a zero
	 *
	 * @arguments index
	 * @return Boolean
	 *
	 * */
	public static boolean checkIfThereAreProducts (int index) throws IOException, ClassNotFoundException {

		Products products = new Products();
		LinkedList<Products> listaProdutos = products.getProducts();

		if (listaProdutos.get(index).getAmount() <= 0) return true;

		return false;
	}

	/*
	 * @overload
	 *
	 * Verifica se a quantidade do produto é igual a zero, e se quantidade a ser vendida no momento do lançamento da venda,
	 * é maior que a disponível no estoque.
	 *
	 * @arguments index
	 * @return Boolean
	 *
	 * */
	public static boolean checkIfThereAreProducts (int index, int amount) throws IOException, ClassNotFoundException {

		Products products = new Products();
		LinkedList<Products> listaProdutos = products.getProducts();

		if (listaProdutos.get(index).getAmount() == 0 || amount > listaProdutos.get(index).getAmount()) return true;

		return false;
	}

	/*
	*
	* Esboça o retorno dos produtos pelo seu titulo em ordem alfabética
    */
	public int compareTo (Products produto) {

		return this.getTitle().toLowerCase().compareToIgnoreCase(produto.getTitle().toLowerCase());
	}
}
