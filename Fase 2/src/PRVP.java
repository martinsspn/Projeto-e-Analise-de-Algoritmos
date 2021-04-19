
import java.util.*;

public class PRVP {
	
	public static void main(String[] args) {
		ManipuladorArquivo manarq = new ManipuladorArquivo();
		manarq.carregarArquivo("C:\\Users\\marti\\OneDrive\\Desktop\\C-pvrp\\pr10");
		//m: número de veiculos. n: número de clientes. t: número de dias
		//D: maximum duration of a route. Q: maximum load of a vehicle
		List<Veiculo> veiculos = monteCarlo(manarq.getClientes(), manarq.getVeiculos(), manarq.getM(), manarq.getN(), manarq.getT());
		int x = checarSolucao(veiculos, manarq.clientes, manarq.t);
		double aux, aux2;
		for(int i=0;i<manarq.getT();i++) {
			for(int j=0; j<veiculos.size();j++) {
				aux = getDuracaoServico(veiculos, manarq.clientes, i, j);
				aux2 = getLocacaoAtingida(veiculos, manarq.clientes, i, j);
				veiculos.get(j).getRotaDias().get(i).add(0, 0);
				veiculos.get(j).getRotaDias().get(i).add(0);
				System.out.print((i+1)+" ");
				System.out.print((j+1)+" ");
				System.out.print(aux + " ");
				System.out.print(aux2 + " ");
				System.out.print(veiculos.get(j).getRotaDias().get(i));
				System.out.println();
			}
			System.out.println();
		}
		if (x == 0){
			System.out.println("É solução!!!");
		}else if(x == -1){
			System.out.println("Não é solução :(");
			System.out.println("Nem todos os clientes foram visitados!");
		}else if(x == -2){
			System.out.println("Não é solução :(");
			System.out.println("Carga máxima ultrapassada!");
		}else{
			System.out.println("Não é solução :(");
			System.out.println("Duração máxima ultrapassada!");
		}
	}

	public static List<Veiculo> monteCarlo(List<Cliente> clientes, List<Veiculo> veiculos, int m, int n, int t){
		Random rand = new Random();
		int randomNum, randomNum2;
		Set<Integer> c = new TreeSet<>();
		List<List<Cliente>> clienteDia = new ArrayList<>();
		for(int i=0;i<t;i++) {
			clienteDia.add(new ArrayList<>());
			clienteDia.set(i, new ArrayList<>());
		}
		while(c.size() != n) {
			randomNum = rand.nextInt(n);
			if (!c.contains(randomNum)) {
				c.add(clientes.get(randomNum).numeroCliente);
				if (clientes.get(randomNum).numberCombinations == 0) {
					continue;
				}
				randomNum2 = rand.nextInt(clientes.get(randomNum).numberCombinations);
				String combinacao = Integer.toBinaryString(clientes.get(randomNum).possibleVisitCombinations.get(randomNum2));
				if (combinacao.length() < t) {
					int x = combinacao.length();
					for (int j = 0; j < t - x; j++) {
						combinacao = "0" + combinacao;
					}
				}
				for (int l = t - 1; l >= 0; l--) {
					if (combinacao.charAt(l) == '1') {
						clienteDia.get(l).add(clientes.get(randomNum));
					}
				}
			}
		}
		List<Integer> aux = new ArrayList<>();
		List<Cliente> anterior = new ArrayList<>();
		for(int i=0;i<t;i++) {
			for (int j = 0; j < m; j++) {
				randomNum = rand.nextInt(clienteDia.get(i).size());
				while (true) {
					if (aux.contains(randomNum)) {
						randomNum = rand.nextInt(clienteDia.get(i).size());
					} else {
						break;
					}
				}
				aux.add(randomNum);
				veiculos.get(j).rotaDias.get(i).add(clienteDia.get(i).get(randomNum).numeroCliente);
				anterior.add(clienteDia.get(i).get(randomNum));
				veiculos.get(j).cargaTotal.set(i, clienteDia.get(i).get(randomNum).demand);
				veiculos.get(j).duracaoTotal.set(i, distance(clienteDia.get(i).get(randomNum), clientes.get(0)));
				clienteDia.get(i).remove(randomNum);
			}
		}
		for(int i=0;i<t;i++) {
			while (clienteDia.get(i).size() > 0) {
				int k = 0;
				Cliente newCliente = gerarCliente(clienteDia.get(i), anterior.get(k));
				while(true){
					int auxiliar = veiculos.get(k).getCargaTotal().get(i) + newCliente.demand;
					double auxiliar2 = veiculos.get(k).duracaoTotal.get(i) + distance(newCliente, anterior.get(i));
					double auxiliar3 = auxiliar2 + distance(newCliente, clientes.get(0));
					if(auxiliar <= veiculos.get(k).cargaMaxima.get(i) && auxiliar2 <= veiculos.get(k).duracaoMaxima.get(i) && auxiliar3 <= veiculos.get(k).duracaoMaxima.get(i)){
						veiculos.get(k).cargaTotal.set(i, veiculos.get(k).cargaTotal.get(i) + newCliente.demand);
						veiculos.get(k).duracaoTotal.set(i, auxiliar2);
						veiculos.get(k).rotaDias.get(i).add(newCliente.numeroCliente);
						clienteDia.get(i).remove(newCliente);
						anterior.set(i, newCliente);
						break;
					}else{
						if(k == m-1){
							veiculos.get(m-1).cargaTotal.set(i, veiculos.get(k).cargaTotal.get(i) + newCliente.demand);
							veiculos.get(m-1).duracaoTotal.set(i, auxiliar2);
							veiculos.get(m-1).rotaDias.get(i).add(newCliente.numeroCliente);
							clienteDia.get(i).remove(newCliente);
							anterior.set(i, newCliente);
							break;
						}
						k++;
					}
				}
			}
		}
		return veiculos;
	}
	
	public static List<Veiculo> buscaLocal(List<Veiculo> veiculos){
		while(true){
			List<Veiculo> veiculosCopia = veiculos;
			veiculosCopia = pertubar(veiculosCopia);
			if(){
				veiculos = veiculosCopia;
			}
			else{
				return veiculos;
				
			}
		}
	}

	public static Cliente gerarCliente(List<Cliente> clientes, Cliente anterior){
		quickSortCliente(clientes, anterior, 0, clientes.size());
		Random rand = new Random();
		int numeroAleatorio;
		if(clientes.size()>=5){
			numeroAleatorio = rand.nextInt(5);
		}else{
			numeroAleatorio = rand.nextInt(clientes.size());
		}
		return clientes.get(numeroAleatorio);
	}

	public static List<Cliente> quickSortCliente(List<Cliente> a, Cliente anterior, int ini, int fim){
		if(ini<fim){
			int pp = particao(a, anterior,ini, fim);
			quickSortCliente(a, anterior,ini, pp);
			quickSortCliente(a, anterior,pp+1, fim);
		}
		return a;
	}

	public static int particao(List<Cliente> a, Cliente anterior, int ini, int fim){
		Cliente pivo = a.get(fim-1);
		int inicio = ini;
		int finish = fim;
		for(int i=inicio;i<finish;i++){
			if(distance(a.get(i), anterior) > distance(pivo, anterior)){
				fim++;
			}else{
				fim++;
				ini++;
				Cliente aux = a.get(i);
				a.set(i, a.get(ini-1));
				a.set(ini-1, aux);
			}
		}
		return ini-1;
	}

	public static int checarSolucao(List<Veiculo> veiculos, List<Cliente> clientes, int t) {
		for (Cliente cliente : clientes) {
			String combinacao = Integer.toBinaryString(cliente.getFrequencyOfVisit());
			int frequencia = 0;
			if (combinacao.length() < t) {
				int x = combinacao.length();
				for (int j = 0; j < t - x; j++) {
					combinacao = "0" + combinacao;
				}
				for (int l = t - 1; l >= 0; l--) {
					for (Veiculo veiculo : veiculos) {
						if (veiculo.rotaDias.get(l).contains(cliente.numeroCliente)) {
							frequencia++;
						}
					}
				}
			}
			if (frequencia != cliente.frequencyOfVisit && cliente.frequencyOfVisit !=0) {
				return -1;
			}
		}
		for(int i=0;i<t;i++){
			for(int j=0;j<veiculos.size();j++){
				if(veiculos.get(j).cargaMaxima.get(i) > 0 && veiculos.get(j).cargaMaxima.get(i) < getLocacaoAtingida(veiculos, clientes, i, j)){
					return -2;
				}
				if(veiculos.get(j).duracaoMaxima.get(i) < getDuracaoServico(veiculos, clientes, i, j)){
					return -3;
				}
			}
		}
		return 0;
	}

	public static double getLocacaoAtingida(List<Veiculo> veiculos, List<Cliente> clientes,int i, int j) {
		double x = 0;
		for(int k = 0; k<veiculos.get(j).getRotaDias().get(i).size(); k++) {
			x += clientes.get(veiculos.get(j).rotaDias.get(i).get(k)).getDemand();
		}
		return x;
	}

	public static double getDuracaoServico(List<Veiculo> veiculos, List<Cliente> clientes,int i, int j) {
		double x = 0;
		for(int k = 0; k<veiculos.get(j).getRotaDias().get(i).size(); k++) {
			x += clientes.get(veiculos.get(j).getRotaDias().get(i).get(k)).getServiceDuration();
		}
		return x;
	}

	public static Double distance(Cliente a, Cliente b){
		return Math.sqrt(Math.pow(a.getxCoordinate() - b.getxCoordinate(), 2) + Math.pow(a.getyCoordinate() - b.getyCoordinate() , 2));
	}
}
