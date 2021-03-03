import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class PRVP {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ManipuladorArquivo manarq = new ManipuladorArquivo();
		manarq.carregarArquivo("C:\\Users\\marti\\OneDrive\\Desktop\\C-pvrp\\pr01");
		//m: número de veiculos. n: número de clientes. t: número de dias
		//D: maximum duration of a route. Q: maximum load of a vehicle
	
		List<Veiculo> veiculos = construtorAleatorio(manarq.getClientes(), manarq.getVeiculos(), manarq.getM(), manarq.getN(), manarq.getT());
		int x = checarSolucao(veiculos, manarq.clientes, manarq.t);
		for(int i=0;i<manarq.getT();i++) {
			for(int j=0; j<veiculos.size();j++) {
				veiculos.get(j).getRotaDias().get(i).add(0, 0);
				veiculos.get(j).getRotaDias().get(i).add(0);
				System.out.print((i+1)+" ");
				System.out.print((j+1)+" ");
				System.out.print(getLocacaoAtingida(veiculos, manarq.clientes, i, j));
				System.out.print(veiculos.get(j).getRotaDias().get(i));
				System.out.println();
			}
			System.out.println();
		}
		if (x == 0){
			System.out.println("� solu��o!!!");
		}else {
			System.out.println("N�o � solu��o :(");
		}
	}
	
	public static List<Veiculo> construtorAleatorio(List<Cliente> clientes, List<Veiculo> veiculos, int m, int n, int t) {
		Random rand = new Random();
		int randomNum, randomNum2;
		double totalDuration = 0;
		List<Integer> maxLoud = new ArrayList<Integer>();
		List<Integer> maxDuration = new ArrayList<Integer>();
		Set<Integer> c = new TreeSet<Integer>();
		for(int i=0;i<m*t;i++) {
			maxLoud.add(0);
			maxDuration.add(0);
		}
		while(c.size()!= n) {
			randomNum = rand.nextInt(n);
			if(c.contains(randomNum)) {
				randomNum = 1+ rand.nextInt(n-1);
			}else {
				//System.out.println(randomNum);
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
					List<Integer> aux = new ArrayList<Integer>();
					//randomNum2 = 0;
					if(combinacao.charAt(l)== '1') {	
						randomNum2 = rand.nextInt((m - 0));
						while(true) {
							int auxiliar = maxLoud.get(randomNum2+l) + clientes.get(randomNum).demand;
							if(auxiliar < veiculos.get(randomNum2).maxLoud.get(l)) {// && maxDuration.get(randomNum2+l) <= veiculos.get(randomNum2).maxDuration.get(l)) {			
								maxLoud.set(randomNum2+l, maxLoud.get(randomNum2+l)+clientes.get(randomNum).demand);
								maxDuration.set(randomNum2+l, maxDuration.get(randomNum2+l)+clientes.get(randomNum).serviceDuration);
								totalDuration += maxDuration.get(randomNum2+l);
								veiculos.get(randomNum2).rotaDias.get(l).add(clientes.get(randomNum).numeroCliente);
								veiculos.get(randomNum2).lotacaoAtingida.set(l, (double) (maxLoud.get(randomNum2+l)+clientes.get(randomNum).demand));
								break;
							}else {
								randomNum2 = rand.nextInt(m); 
								maxLoud.set(randomNum2+l, 0);
							}
						
						}					
					}
				}
			}
		}
		return veiculos;
	}
		public static int checarSolucao(List<Veiculo> veiculos, List<Cliente> clientes, int t) {
		for(int i=0;i<clientes.size();i++) {
			String combinacao = Integer.toBinaryString(clientes.get(i).getFrequencyOfVisit());
			int frequencia = 0;
			if(combinacao.length() < t) {
				int x = combinacao.length();
				for(int j=0;j<t-x;j++) {
					combinacao = "0" + combinacao;
				}
				for(int l=t-1;l>=0;l--) {
					for(int j=0;j<veiculos.size();j++) {
						if(veiculos.get(j).rotaDias.get(l).contains(clientes.get(i).numeroCliente)){
							frequencia++;
						}
					}
				}
			}
			if(frequencia != clientes.get(i).frequencyOfVisit) {
				return -1;
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