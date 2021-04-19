import java.util.ArrayList;
import java.util.List;

public class Veiculo {
	public List<List<Integer>> rotaDias;
	public List<Double> maxLoud;
	public List<Double> maxDuration;
	public List<Double> lotacaoAtingida;
	public Veiculo(int t) {
		rotaDias = new ArrayList<List<Integer>>();
		maxLoud = new ArrayList<Double>();
		maxDuration = new ArrayList<Double>();
		lotacaoAtingida = new ArrayList<Double>();
		for(int i=0;i<t;i++) {
			List<Integer> aux = new ArrayList<Integer>();
			rotaDias.add(aux);
			maxLoud.add(0.0);
			maxDuration.add(0.0);
			lotacaoAtingida.add(0.0);
		}
	}
	public List<List<Integer>> getRotaDias() {
		return rotaDias;
	}
	public List<Double> getMaxLoud() {
		return maxLoud;
	}
	public List<Double> getMaxDuration() {
		return maxDuration;
	}
	public void setRotaDias(List<List<Integer>> rotaDias) {
		this.rotaDias = rotaDias;
	}
	public void setMaxLoud(List<Double> maxLoud) {
		this.maxLoud = maxLoud;
	}
	public void setMaxDuration(List<Double> maxDuration) {
		this.maxDuration = maxDuration;
	}
}
