import java.util.*;
import java.text.DecimalFormat;

public class PRVP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<List<Double>> matriz = new ArrayList<List<Double>>();
		ManipuladorArquivo manarq = new ManipuladorArquivo();
		manarq.carregarArquivo("C:\\Users\\marti\\OneDrive\\Desktop\\C-pvrp\\pr01");
		DecimalFormat df = new DecimalFormat("#.##");
		//m: número de veiculos. n: número de clientes. t: número de dias
		//D: maximum duration of a route. Q: maximum load of a vehicle
		int x=5;
		int opt = 1;
		List<Veiculo> veiculos = null;
		gerarMatriz(manarq.clientes,  matriz);
		veiculos = construtorAleatorio(manarq.getClientes(), manarq.getVeiculos(), manarq.getM(), manarq.getN(), manarq.getT(), opt);
		x = checarSolucao(veiculos, manarq.clientes, manarq.t);
		for(int i=0;i<manarq.getT();i++) {
			for(int j=0; j<veiculos.size();j++) {
				veiculos.get(j).getRotaDias().get(i).add(0, 0);
				veiculos.get(j).getRotaDias().get(i).add(0);
				System.out.print((i+1)+" ");
				System.out.print((j+1)+" ");
				System.out.print(df.format(getLocacaoAtingida(veiculos, manarq.clientes, i, j)) + " ");
				System.out.print(df.format(getDuracaoServico(veiculos, manarq.clientes, i, j)) + " ");
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

	public static List<Veiculo> construtorAleatorio(List<Cliente> clientes, List<Veiculo> veiculos, int m, int n, int t, int opt) {
		Random rand = new Random();
		int randomNum, randomNum2;
		List<List<Integer>> maxLoud = new ArrayList<List<Integer>>();
		List<List<Double>> maxDuration = new ArrayList<List<Double>>();
		Set<Integer> c = new TreeSet<Integer>();
		for(int i=0;i<m*t;i++) {
			List<Integer> aux = new ArrayList<Integer>();
			List<Double> aux2 = new ArrayList<>();
			for(int j=0;j<t;j++) {
				aux.add(0);
				aux2.add(0.0);
			}
			maxLoud.add(aux);
			maxDuration.add(aux2);
		}
		while(c.size()!= n) {
			randomNum = rand.nextInt(n);
			if(c.contains(randomNum)) {
				randomNum = 1+ rand.nextInt(n-1);
			}else {
				c.add(clientes.get(randomNum).numeroCliente);
				if(clientes.get(randomNum).numberCombinations == 0) {
					continue;
				}
				randomNum2 = rand.nextInt(clientes.get(randomNum).numberCombinations);
				String combinacao = Integer.toBinaryString(clientes.get(randomNum).possibleVisitCombinations.get(randomNum2));
				if(combinacao.length() < t) {
					int x = combinacao.length();
					for(int j=0;j<t-x;j++) {
						combinacao = "0" + combinacao;
					}
				}
				for(int l=t-1; l>=0; l--) {
					if(combinacao.charAt(l)== '1') {
						int k = 0;
						Cliente anterior;
						while(true) {
							int auxiliar = maxLoud.get(k).get(l) + clientes.get(randomNum).demand;
							if (veiculos.get(k).rotaDias.get(l).size() > 0) {
								anterior = clientes.get(veiculos.get(k).rotaDias.get(l).get(veiculos.get(k).rotaDias.get(l).size() - 1));
							} else {
								anterior = clientes.get(0);
							}
							double auxiliar2 = maxDuration.get(k).get(l) + distance(anterior, clientes.get(randomNum));
							if(k == m-1){
								maxLoud.get(k).set(l, maxLoud.get(k).get(l) + clientes.get(randomNum).demand);
								maxDuration.get(k).set(l, maxDuration.get(k).get(l) + auxiliar2);
								veiculos.get(k).rotaDias.get(l).add(clientes.get(randomNum).numeroCliente);
								break;
							}
							if (opt == 1 && auxiliar <= veiculos.get(k).maxLoud.get(l) && auxiliar2 <= veiculos.get(k).maxDuration.get(l)) {
								if (auxiliar2 + distance(clientes.get(randomNum), clientes.get(0)) <= veiculos.get(k).maxDuration.get(l)) {
									maxLoud.get(k).set(l, maxLoud.get(k).get(l) + clientes.get(randomNum).demand);
									maxDuration.get(k).set(l, maxDuration.get(k).get(l) + auxiliar2);
									veiculos.get(k).rotaDias.get(l).add(clientes.get(randomNum).numeroCliente);
									break;
								}else{
									k++;
								}
							}else if(opt == 2 && auxiliar2 <= veiculos.get(k).maxDuration.get(l)){
								if (auxiliar2 + distance(clientes.get(randomNum), clientes.get(0)) <= veiculos.get(k).maxDuration.get(l)) {
									maxDuration.get(k).set(l, maxDuration.get(k).get(l) + auxiliar2);
									veiculos.get(k).rotaDias.get(l).add(clientes.get(randomNum).numeroCliente);
									break;
								}else {
									k++;
								}
							} else{
								k++;
							}
						}
					}
				}
			}
		}
		return veiculos;
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
			if (frequencia != cliente.frequencyOfVisit) {
				return -1;
			}
		}
		for(int i=0;i<t;i++){
			for(int j=0;j<veiculos.size();j++){
				if(veiculos.get(i).maxLoud.get(i) > 0 && veiculos.get(j).maxLoud.get(i) < getLocacaoAtingida(veiculos, clientes, i, j)){
					return -2;
				}
				if(veiculos.get(j).maxDuration.get(i) < getDuracaoServico(veiculos, clientes, i, j)){
					return -3;
				}
			}
		}
		return 0;
	}


	public static double getLocacaoAtingida(List<Veiculo> veiculos, List<Cliente> clientes,int i, int j) {
		int x = 0;
		for(int k = 0; k<veiculos.get(j).getRotaDias().get(i).size(); k++) {
			x += clientes.get(veiculos.get(j).getRotaDias().get(i).get(k)).getDemand();
		}
		return x;
	}

	public static double getDuracaoServico(List<Veiculo> veiculos, List<Cliente> clientes,int i, int j) {
		double x = 0;
		for(int k = 0; k<veiculos.get(j).getRotaDias().get(i).size(); k++) {
			x += clientes.get(veiculos.get(j).getRotaDias().get(i).get(k)).getServiceDuration();
		}
		x += distance(clientes.get(clientes.size()-1), clientes.get(0));
		return x;
	}
	public static void gerarMatriz(List<Cliente> clientes, List<List<Double>> matriz){
		for(int k = 0; k<clientes.size(); k++) {
			matriz.add(new ArrayList<>());
			for(int j = 0; j<clientes.size(); j++) {
				//matriz.get(k).get(j).add(distance(clientes.get(k), clientes.get(j)));
				matriz.get(k).add(j, distance(clientes.get(k), clientes.get(j)));
			}
		}
	}

	public static Double distance(Cliente a, Cliente b){
		return Math.sqrt(Math.pow(a.getxCoordinate() - b.getxCoordinate(), 2) + Math.pow(a.getyCoordinate() - b.getyCoordinate() , 2));
	}
}



//To do:
/*Implementar as verificações, olhar pq n ta criando 2 dias para cada veiculo
ConstrutivoAleatorio(clientes)
1:Inicializa a soluçãos com todas as rotas dos veículos vazias.
2:for(todo cliente i em Uc)do
3:	Selecione uma agenda de visita r aleatória pertencente a Ri
4:	for(todo dia l em L)do
5:		if(cliente i precisa ser visitado no dia l na agenda r)then
6:			Selecione um veículo aleatório v pertencente ao conjunto de veículos de s
7:			Insira i na rota de v do dia l na última posição.
8:		end if
9:	end for
10:end for
11:Retorne s;
*/
