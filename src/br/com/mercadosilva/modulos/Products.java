package br.com.mercadosilva.modulos;

import br.com.mercadosilva.modulos.persistencia.Persistencia;
import br.com.mercadosilva.modulos.util.QuickSort;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class Products extends Persistencia implements Comparable<Products> {

	private static final long serialVersionUID = 1L;

	private static LinkedList<Object> productsList = new LinkedList<>();

	private String title;
	private double price;
	private int amount;

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

	public void saveProduct (Products produtos) throws IOException {

		try {

			productsList.add(produtos);
			this.save("db.products", productsList);

			System.out.println("\nProduto inserido com sucesso!");
		} catch (IOException e) {
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
			o = this.get("db.products");
			produtos = (LinkedList<Products>) o;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return produtos;
	}

	/*
	*
	* Obtem todos os produtos em ordem alfabética
	* @return void
	*
	* */
	public void screenProducts () throws IOException, ClassNotFoundException {

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
			System.out.println("Produto: "+aux[i].getTitle());
			System.out.println("Preço: R$ "+aux[i].getPrice());
			System.out.println("Quantidade em estoque: "+aux[i].getAmount()+"\n");
			i++;
		}

		// "Zera" as duas listas da memória no fim da execução desse ciclo de vida
		// A principio, zera o array auxiliar, ulterior, zera a LinkedList
		Arrays.fill(aux, null);
		listaProdutos.clear();
	}

	/*
	public int compareTo(Products produto) {

		if (this.getPrice() > produto.getPrice()) {
			return -1;
		} else if (this.getPrice() < produto.getPrice()) {
			return 1;
		}

		return this.getTitle().compareToIgnoreCase(produto.getTitle());
	}

	public int compareTo(Products p1, Products p2) {
		return p1.getTitle().toUpperCase().compareToIgnoreCase(p2.getTitle().toUpperCase());
	}*/

	@Override
	/*
	*
	* Esboça o retorno dos produtos pelo seu titulo em ordem alfabética
	* */
	public int compareTo(Products produto) {

		return this.getTitle().toLowerCase().compareToIgnoreCase(produto.getTitle().toLowerCase());
	}
}