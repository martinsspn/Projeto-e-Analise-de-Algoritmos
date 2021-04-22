
import java.lang.reflect.Array;
import java.util.*;

public class PRVP {
	
	public static void main(String[] args) {
		ManipuladorArquivo manarq = new ManipuladorArquivo();
		manarq.carregarArquivo("C:\\Users\\marti\\OneDrive\\Desktop\\C-pvrp\\pr10");
		//m: número de veiculos. n: número de clientes. t: número de dias
		//D: maximum duration of a route. Q: maximum load of a vehicle
		List<Veiculo> veiculos = monteCarlo(manarq.getClientes(), manarq.getVeiculos(), manarq.getM(), manarq.getN(), manarq.getT());
		List<Veiculo> copy = copiarVeiculos(veiculos);
		int x = checarSolucao(veiculos, manarq.clientes, manarq.t);
		double aux, aux2;
		System.out.println("Construção com método de Monte Carlo:");
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

		System.out.println("\nsolução melhorada com o GRASP:");
		veiculos = buscaLocal(copy, manarq.clientes);
		x = checarSolucao(veiculos, manarq.clientes, manarq.t);
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
	
	public static List<Veiculo> buscaLocal(List<Veiculo> veiculos, List<Cliente> clientes){
		int it = 0;
		while(true){
			List<Veiculo> veiculosCopia = copiarVeiculos(veiculos);
			veiculosCopia = pertubar(veiculosCopia, clientes, 1);
			if(verificar(veiculos, veiculosCopia)){
				veiculos = veiculosCopia;
				it++;
			}
			else{
				System.out.println("Numero de iterações:" + (it+1));
				return veiculos;
			}
		}
	}

	public static List<Veiculo> copiarVeiculos(List<Veiculo> veiculos){
		List<Veiculo> copia = new ArrayList<>();
		for (Veiculo veiculo : veiculos) {
			Veiculo v = new Veiculo(veiculos.get(0).rotaDias.size());
			for(int i=0;i<v.rotaDias.size();i++){
				List<Integer> auxiliar = new ArrayList<>(veiculo.rotaDias.get(i));
				v.rotaDias.set(i, auxiliar);
			}
			List<Integer> auxiliar = new ArrayList<>(veiculo.cargaMaxima);
			v.cargaMaxima = auxiliar;
			auxiliar = new ArrayList<>(veiculo.cargaTotal);
			v.cargaTotal = auxiliar;
			List<Double> auxiliar2 = new ArrayList<>(veiculo.duracaoMaxima);
			v.duracaoMaxima = auxiliar2;
			auxiliar2 = new ArrayList<>(veiculo.duracaoTotal);
			v.duracaoTotal = auxiliar2;
			copia.add(v);
		}
		return copia;
	}

	public static Boolean verificar(List<Veiculo> veiculos, List<Veiculo> veiculosCopia){
		int aux = veiculos.size() - 1;
		int v = 0;
		int vc = 0;
		for(int j=0;j<veiculos.get(aux).getCargaTotal().size();j++){
			if(veiculos.get(aux).getCargaTotal().get(j) > veiculos.get(aux).getCargaMaxima().get(j)){
				if(veiculosCopia.get(aux).getCargaTotal().get(j) > veiculosCopia.get(aux).getCargaMaxima().get(j)){
					if(veiculos.get(aux).getCargaTotal().get(j) > veiculosCopia.get(aux).getCargaTotal().get(j)){
						v = 1 + v;
					}
					else{
						vc = 1 + vc;
					}
				}
				else{
					v = 1 + v;
				}
			}else if(veiculosCopia.get(aux).getCargaTotal().get(j) > veiculosCopia.get(aux).getCargaMaxima().get(j)){
				vc = 1 + vc;
			}
		}
		if(vc < v){
			return true;
		}
		else if(v < vc){
			return false;
		}
		else if(v != 0){
			return true;
		}
		
		double duracacaoV = 0;
		double duracacaoVC = 0;
		double sum = 0;
		for (Veiculo veiculo : veiculos){
			for(int j=0;j<veiculo.getDuracaoTotal().size(); j++){
				sum = sum + veiculo.getDuracaoTotal().get(j);
			}
			duracacaoV = duracacaoV + sum;
		}
		for (Veiculo veiculo : veiculosCopia){
			for(int j=0;j<veiculo.getDuracaoTotal().size(); j++){
				sum = sum + veiculo.getDuracaoTotal().get(j);
			}
			duracacaoVC = duracacaoVC + sum;
		}
		return duracacaoVC <= duracacaoV;
	}


	public static List<Veiculo> pertubar(List<Veiculo> veiculos, List<Cliente> clientes, int opcao){
		switch(opcao){
			case 1 : return trocarCarro(veiculos, clientes);
			//case 2 : return trocarDia(veiculos);
			default: return null;
		}
	}

	public static List<Veiculo> trocarCarro(List<Veiculo> veiculos, List<Cliente> clientes){
		Random random = new Random();
		int r1, r2, r3, r4, aux, min, max,vc;
		for(int i=0; i<veiculos.get(0).rotaDias.size(); i++){
			max = 0;
			vc = 0;
			for(int j=0;j<veiculos.size();j++){
				if(max < veiculos.get(j).cargaTotal.get(i)){
					max = veiculos.get(j).cargaTotal.get(i);
					vc = j;
				}
			}
			if(veiculos.get(vc).cargaTotal.get(i) > veiculos.get(vc).cargaMaxima.get(i)){
				r2 = random.nextInt(veiculos.get(vc).rotaDias.get(i).size());
				aux = veiculos.get(vc).rotaDias.get(i).get(r2);
				veiculos.get(vc).rotaDias.get(i).remove(r2);
				Cliente cliente = null;
				for(Cliente c: clientes){
					if (c.numeroCliente == aux){
						cliente = c;
						break;
					}
				}
				veiculos.get(vc).cargaTotal.set(i, veiculos.get(vc).cargaTotal.get(i) - cliente.demand);
				veiculos.get(vc).duracaoTotal.set(i, getDuracaoServico(veiculos, clientes, i, vc));

				min = veiculos.get(0).cargaTotal.get(i);
				vc = 0;
				for(int j=0;j<veiculos.size();j++){
					if(min > veiculos.get(j).cargaTotal.get(i)){
						min = veiculos.get(j).cargaTotal.get(i);
						vc = j;
					}
				}
				r4 = random.nextInt(veiculos.get(vc).rotaDias.get(i).size()-1);
				veiculos.get(vc).rotaDias.get(i).add(r4, aux);
				veiculos.get(vc).cargaTotal.set(i, veiculos.get(vc).cargaTotal.get(i) + cliente.demand);
				veiculos.get(vc).duracaoTotal.set(i, getDuracaoServico(veiculos, clientes, i, vc));
			}else{
				r1 = random.nextInt(veiculos.size());
				r2 = random.nextInt(veiculos.get(r1).rotaDias.get(i).size());
				aux = veiculos.get(r1).rotaDias.get(i).get(r2);
				veiculos.get(r1).rotaDias.get(i).remove(r2);
				Cliente cliente = null;
				for(Cliente c: clientes){
					if (c.numeroCliente == aux){
						cliente = c;
						break;
					}
				}
				veiculos.get(r1).cargaTotal.set(i, veiculos.get(r1).cargaTotal.get(i) - cliente.demand);
				veiculos.get(r1).duracaoTotal.set(i, getDuracaoServico(veiculos, clientes, i, r1));
				do {
					r3 = random.nextInt(veiculos.size());
				} while (r1 == r3);
				r4 = random.nextInt(veiculos.get(r3).rotaDias.get(i).size()-1);
				veiculos.get(r3).rotaDias.get(i).add(r4, aux);
				veiculos.get(r3).cargaTotal.set(i, veiculos.get(r3).cargaTotal.get(i) + cliente.demand);
				veiculos.get(r3).duracaoTotal.set(i, getDuracaoServico(veiculos, clientes, i, r3));
			}
		}
		return veiculos;
	}

	public static List<Veiculo> trocarDia(List<Veiculo> veiculos){
		return null;
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

	public static double getDuracaoServico(List<Veiculo> veiculos, List<Cliente> clientes, int i, int j) {
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
