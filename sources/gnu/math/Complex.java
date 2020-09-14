package gnu.math;

public abstract class Complex extends Quantity {
    private static CComplex imMinusOne;
    private static CComplex imOne;

    public Complex number() {
        return this;
    }

    public boolean isExact() {
        return mo10959re().isExact() && mo10958im().isExact();
    }

    /* Debug info: failed to restart local var, previous not found, register: 4 */
    public Complex toExact() {
        RealNum re = mo10959re();
        RealNum im = mo10958im();
        RatNum xre = re.toExact();
        RatNum xim = im.toExact();
        return (xre == re && xim == im) ? this : new CComplex(xre, xim);
    }

    /* Debug info: failed to restart local var, previous not found, register: 6 */
    public Complex toInexact() {
        return isExact() ? this : new DComplex(mo10959re().doubleValue(), mo10958im().doubleValue());
    }

    public static CComplex imOne() {
        if (imOne == null) {
            imOne = new CComplex(IntNum.zero(), IntNum.one());
        }
        return imOne;
    }

    public static CComplex imMinusOne() {
        if (imMinusOne == null) {
            imMinusOne = new CComplex(IntNum.zero(), IntNum.minusOne());
        }
        return imMinusOne;
    }

    public double doubleValue() {
        return mo10959re().doubleValue();
    }

    public double doubleImagValue() {
        return mo10958im().doubleValue();
    }

    public final double doubleRealValue() {
        return doubleValue();
    }

    public long longValue() {
        return mo10959re().longValue();
    }

    public static Complex make(RealNum re, RealNum im) {
        if (im.isZero()) {
            return re;
        }
        if (!re.isExact() || !im.isExact()) {
            return new DComplex(re.doubleValue(), im.doubleValue());
        }
        return new CComplex(re, im);
    }

    public static Complex make(double re, double im) {
        if (im == 0.0d) {
            return new DFloNum(re);
        }
        return new DComplex(re, im);
    }

    public static DComplex polar(double r, double t) {
        return new DComplex(Math.cos(t) * r, Math.sin(t) * r);
    }

    public static DComplex polar(RealNum r, RealNum t) {
        return polar(r.doubleValue(), t.doubleValue());
    }

    public static Complex power(Complex x, Complex y) {
        if (y instanceof IntNum) {
            return (Complex) x.power((IntNum) y);
        }
        double x_re = x.doubleRealValue();
        double x_im = x.doubleImagValue();
        double y_re = y.doubleRealValue();
        double y_im = y.doubleImagValue();
        if (x_im == 0.0d && y_im == 0.0d && (x_re >= 0.0d || Double.isInfinite(x_re) || Double.isNaN(x_re))) {
            return new DFloNum(Math.pow(x_re, y_re));
        }
        return DComplex.power(x_re, x_im, y_re, y_im);
    }

    public Numeric abs() {
        return new DFloNum(Math.hypot(doubleRealValue(), doubleImagValue()));
    }

    public RealNum angle() {
        return new DFloNum(Math.atan2(doubleImagValue(), doubleRealValue()));
    }

    public static boolean equals(Complex x, Complex y) {
        return x.mo10959re().equals(y.mo10959re()) && x.mo10958im().equals(x.mo10958im());
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Complex)) {
            return false;
        }
        return equals(this, (Complex) obj);
    }

    public static int compare(Complex x, Complex y) {
        int code = x.mo10958im().compare(y.mo10958im());
        return code != 0 ? code : x.mo10959re().compare(y.mo10959re());
    }

    public int compare(Object obj) {
        if (!(obj instanceof Complex)) {
            return ((Numeric) obj).compareReversed(this);
        }
        return compare(this, (Complex) obj);
    }

    public boolean isZero() {
        return mo10959re().isZero() && mo10958im().isZero();
    }

    public String toString(int radix) {
        if (mo10958im().isZero()) {
            return mo10959re().toString(radix);
        }
        String imString = mo10958im().toString(radix) + "i";
        if (imString.charAt(0) != '-') {
            imString = "+" + imString;
        }
        return !mo10959re().isZero() ? mo10959re().toString(radix) + imString : imString;
    }

    public static Complex neg(Complex x) {
        return make(x.mo10959re().rneg(), x.mo10958im().rneg());
    }

    public Numeric neg() {
        return neg(this);
    }

    public static Complex add(Complex x, Complex y, int k) {
        return make(RealNum.add(x.mo10959re(), y.mo10959re(), k), RealNum.add(x.mo10958im(), y.mo10958im(), k));
    }

    public Numeric add(Object y, int k) {
        if (y instanceof Complex) {
            return add(this, (Complex) y, k);
        }
        return ((Numeric) y).addReversed(this, k);
    }

    public Numeric addReversed(Numeric x, int k) {
        if (x instanceof Complex) {
            return add((Complex) x, this, k);
        }
        throw new IllegalArgumentException();
    }

    public static Complex times(Complex x, Complex y) {
        RealNum x_re = x.mo10959re();
        RealNum x_im = x.mo10958im();
        RealNum y_re = y.mo10959re();
        RealNum y_im = y.mo10958im();
        return make(RealNum.add(RealNum.times(x_re, y_re), RealNum.times(x_im, y_im), -1), RealNum.add(RealNum.times(x_re, y_im), RealNum.times(x_im, y_re), 1));
    }

    public Numeric mul(Object y) {
        if (y instanceof Complex) {
            return times(this, (Complex) y);
        }
        return ((Numeric) y).mulReversed(this);
    }

    public Numeric mulReversed(Numeric x) {
        if (x instanceof Complex) {
            return times((Complex) x, this);
        }
        throw new IllegalArgumentException();
    }

    public static Complex divide(Complex x, Complex y) {
        if (!x.isExact() || !y.isExact()) {
            return DComplex.div(x.doubleRealValue(), x.doubleImagValue(), y.doubleRealValue(), y.doubleImagValue());
        }
        RealNum x_re = x.mo10959re();
        RealNum x_im = x.mo10958im();
        RealNum y_re = y.mo10959re();
        RealNum y_im = y.mo10958im();
        RealNum q = RealNum.add(RealNum.times(y_re, y_re), RealNum.times(y_im, y_im), 1);
        return make(RealNum.divide(RealNum.add(RealNum.times(x_re, y_re), RealNum.times(x_im, y_im), 1), q), RealNum.divide(RealNum.add(RealNum.times(x_im, y_re), RealNum.times(x_re, y_im), -1), q));
    }

    public Numeric div(Object y) {
        if (y instanceof Complex) {
            return divide(this, (Complex) y);
        }
        return ((Numeric) y).divReversed(this);
    }

    public Numeric divReversed(Numeric x) {
        if (x instanceof Complex) {
            return divide((Complex) x, this);
        }
        throw new IllegalArgumentException();
    }

    public Complex exp() {
        return polar(Math.exp(doubleRealValue()), doubleImagValue());
    }

    public Complex log() {
        return DComplex.log(doubleRealValue(), doubleImagValue());
    }

    public Complex sqrt() {
        return DComplex.sqrt(doubleRealValue(), doubleImagValue());
    }
}