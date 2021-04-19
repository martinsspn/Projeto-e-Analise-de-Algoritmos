import java.util.ArrayList;
import java.util.List;

public class Veiculo {
	public List<List<Integer>> rotaDias;
	public List<Integer> cargaMaxima;
	public List<Double> duracaoMaxima;
	public List<Integer> cargaTotal;
	public List<Double> duracaoTotal;

	public Veiculo(int t) {
		rotaDias = new ArrayList<>();
		cargaMaxima = new ArrayList<>();
		duracaoMaxima = new ArrayList<>();
		cargaTotal = new ArrayList<>();
		duracaoTotal = new ArrayList<>();
		for(int i=0;i<t;i++) {
			List<Integer> aux = new ArrayList<Integer>();
			rotaDias.add(aux);
			cargaMaxima.add(0);
			duracaoMaxima.add(0.0);
			cargaTotal.add(0);
			duracaoTotal.add(0.0);
		}
	}

	public List<Double> getDuracaoTotal() {
		return duracaoTotal;
	}

	public void setDuracaoTotal(List<Double> duracaoTotal) {
		this.duracaoTotal = duracaoTotal;
	}

	public List<Integer> getCargaMaxima() {
		return cargaMaxima;
	}

	public void setCargaMaxima(List<Integer> cargaMaxima) {
		this.cargaMaxima = cargaMaxima;
	}

	public List<Double> getDuracaoMaxima() {
		return duracaoMaxima;
	}

	public void setDuracaoMaxima(List<Double> duracaoMaxima) {
		this.duracaoMaxima = duracaoMaxima;
	}

	public List<Integer> getCargaTotal() {
		return cargaTotal;
	}

	public void setCargaTotal(List<Integer> cargaTotal) {
		this.cargaTotal = cargaTotal;
	}
	public List<List<Integer>> getRotaDias() {
		return rotaDias;
	}
	public void setRotaDias(List<List<Integer>> rotaDias) {
		this.rotaDias = rotaDias;
	}
}
