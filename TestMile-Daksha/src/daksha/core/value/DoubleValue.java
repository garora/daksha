package daksha.core.value;

import daksha.tpi.batteries.container.Value;
import daksha.tpi.enums.ValueType;

public class DoubleValue extends NumberValue {

	public DoubleValue(Double number) {
		super(ValueType.DOUBLE, number);
	}

	@Override
	public Value clone() {
		return new DoubleValue(this.getRawObject());
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private double getRawObject() {
		return (Double) this.object();
	}

	@Override
	public double asDouble() {
		return (Double) this.object();
	}
}
