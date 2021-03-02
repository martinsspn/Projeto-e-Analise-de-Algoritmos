import java.util.List;

public class Cliente {
	
	public Integer numeroCliente;
	public Double xCoordinate;
	public Double yCoordinate;
	public Integer serviceDuration;
	public Integer demand;
	public Integer frequencyOfVisit;
	public Integer numberCombinations;
	public List<Integer> possibleVisitCombinations;
	
	public Cliente(Integer numeroCliente, Double xCoordinate, Double yCoordinate, Integer serviceDuration,
			Integer demand, Integer frequencyOfVisit, Integer numberCombinations,
			List<Integer> possibleVisitCombinations) {
		super();
		this.numeroCliente = numeroCliente;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.serviceDuration = serviceDuration;
		this.demand = demand;
		this.frequencyOfVisit = frequencyOfVisit;
		this.numberCombinations = numberCombinations;
		this.possibleVisitCombinations = possibleVisitCombinations;
	}

	public Integer getNumeroCliente() {
		return numeroCliente;
	}

	public Double getxCoordinate() {
		return xCoordinate;
	}

	public Double getyCoordinate() {
		return yCoordinate;
	}

	public Integer getServiceDuration() {
		return serviceDuration;
	}

	public Integer getDemand() {
		return demand;
	}

	public Integer getFrequencyOfVisit() {
		return frequencyOfVisit;
	}

	public Integer getNumberCombinations() {
		return numberCombinations;
	}

	public List<Integer> getPossibleVisitCombinations() {
		return possibleVisitCombinations;
	}
}
 /*i: customer number
x: x coordinate
y: y coordinate
d: service duration
q: demand
f: frequency of visit
a: number of possible visit combinations
list: list of all possible visit combinations
*/