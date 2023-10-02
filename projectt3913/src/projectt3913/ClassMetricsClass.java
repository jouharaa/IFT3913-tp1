package projectt3913;

public class ClassMetricsClass{

        public int tloc;
        public int tassert;
        public double tcmp;

        public ClassMetricsClass(int tloc, int tassert, double tcmp) {
            this.tloc = tloc;
            this.tassert = tassert;
            this.tcmp = tcmp;
        }

        public int getTloc() {
            return tloc;
        }

        public int getTassert() {
            return tassert;
        }

        public double getTcmp() {
            return tcmp;
        }

        public boolean isTest() {
            return getClassName().endsWith("Test");
        }

        private String getClassName() {
            return getClass().getSimpleName();
        }
    }