import java.util.ArrayList;
import java.util.List;

public class Veiculo {
	public List<List<Integer>> rotaDias;
	public List<Double> cargaMaxima;
	public List<Double> duracaoMaxima;
	public List<Double> cargaTotal;
	public List<Double> duracaoTotal;

	public Veiculo(int t) {
		rotaDias = new ArrayList<List<Integer>>();
		cargaMaxima = new ArrayList<Double>();
		duracaoMaxima = new ArrayList<Double>();
		cargaTotal = new ArrayList<Double>();
		duracaoTotal = new ArrayList<>();
		for(int i=0;i<t;i++) {
			List<Integer> aux = new ArrayList<Integer>();
			rotaDias.add(aux);
			cargaMaxima.add(0.0);
			duracaoMaxima.add(0.0);
			cargaTotal.add(0.0);
			duracaoTotal.add(0.0);
		}
	}

	public List<Double> getDuracaoTotal() {
		return duracaoTotal;
	}

	public void setDuracaoTotal(List<Double> duracaoTotal) {
		this.duracaoTotal = duracaoTotal;
	}

	public List<Double> getCargaMaxima() {
		return cargaMaxima;
	}

	public void setCargaMaxima(List<Double> cargaMaxima) {
		this.cargaMaxima = cargaMaxima;
	}

	public List<Double> getDuracaoMaxima() {
		return duracaoMaxima;
	}

	public void setDuracaoMaxima(List<Double> duracaoMaxima) {
		this.duracaoMaxima = duracaoMaxima;
	}

	public List<Double> getCargaTotal() {
		return cargaTotal;
	}

	public void setCargaTotal(List<Double> cargaTotal) {
		this.cargaTotal = cargaTotal;
	}
	public List<List<Integer>> getRotaDias() {
		return rotaDias;
	}
	public void setRotaDias(List<List<Integer>> rotaDias) {
		this.rotaDias = rotaDias;
	}
}
