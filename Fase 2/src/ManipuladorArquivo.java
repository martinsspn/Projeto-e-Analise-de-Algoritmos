import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManipuladorArquivo {

	public int m, n, t;
	public List<Double> D;
	public List<Integer> Q;
	public List<Cliente> clientes;
	public List<Veiculo> veiculos;
        
    public ManipuladorArquivo() {
    	Q = new ArrayList<>();
    	D = new ArrayList<>();
    	clientes = new ArrayList<>();
    	veiculos = new ArrayList<>();
    }

	public int getM() {
		return m;
	}

	public int getN() {
		return n;
	}

	public int getT() {
		return t;
	}

	public List<Integer> getQ() {
		return Q;
	}

	public List<Double> getD() {
		return D;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}
	
	public List<Veiculo> getVeiculos(){
		return veiculos;
	}

	public static void escritor(String path) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        String linha = "";
        Scanner in = new Scanner(System.in);
        System.out.println("Escreva algo: ");
        linha = in.nextLine();
        buffWrite.append(linha + "\n");
        buffWrite.close();
    }
    
    public void carregarArquivo(String path) {       
		try {
			BufferedReader buffRead = new BufferedReader(new FileReader(path));
			String linha = buffRead.readLine();
			String[] aux = linha.split(" ");
			m = Integer.parseInt(aux[1]);
			n = Integer.parseInt(aux[2]) + 1;
			t = Integer.parseInt(aux[3]);
			//D: maximum duration of a route 
			//Q: maximum load of a vehicle
			D = new ArrayList<>();
			Q = new ArrayList<>();
			for(int i=0;i<t;i++) {
				linha = buffRead.readLine();
				aux = linha.split(" ");
				D.add(Double.parseDouble(aux[0]));
				Q.add(Integer.parseInt(aux[1]));
			}
			for(int i=0;i<m;i++) {
				Veiculo veiculo = new Veiculo(t);
				veiculo.duracaoMaxima = D;
				veiculo.cargaMaxima = Q;
				veiculos.add(veiculo);
			}
			clientes = new ArrayList<Cliente>();
			for(int i=0;i<n;i++) {
				linha = buffRead.readLine();
				//System.out.println(linha);
				aux = linha.split(" ");
				//for(int j=0;j<aux.length;j++)	System.out.println(aux[j]);
		
				List<Integer> aux2 = new ArrayList<Integer>();
				List<Double> aux3 = new ArrayList<Double>();
				List<Integer> lista = new ArrayList<Integer>();
				int x=0;
				for(int j=0;j<aux.length;j++) {
					try { 
						if(aux3.size() > 0) {
							if(x<5) {
								aux2.add(Integer.parseInt(aux[j]));
								x++;
							}else {
								lista.add(Integer.parseInt(aux[j]));
							}	
						}else {
							if(x<7) {
								aux2.add(Integer.parseInt(aux[j]));
								x++;
							}else {
								lista.add(Integer.parseInt(aux[j]));
							}
						}
					}catch(NumberFormatException e) {
						try{
							aux3.add(Double.parseDouble(aux[j]));
						}catch(NumberFormatException r) {

						}
					}
				}
				Cliente cliente;
				if(aux3.size() == 2) {
					cliente = new Cliente(aux2.get(0), aux3.get(0), aux3.get(1), aux2.get(1), aux2.get(2), aux2.get(3), aux2.get(4), lista);
						
				}else {
					cliente = new Cliente(aux2.get(0), (double)aux2.get(1), (double)aux2.get(2), aux2.get(3), aux2.get(4), aux2.get(5), aux2.get(6), lista);

				}
				clientes.add(cliente);
			}
			buffRead.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
