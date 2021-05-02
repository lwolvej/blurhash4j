package io.lwolvej.blurhash;

class Util {

    private static double signPow(double value, double exp) {
        return Math.copySign(Math.pow(Math.abs(value), exp), value);
    }

    static double[] decodeDC(String str) {
        int dcValue = Base83.decode(str);
        return new double[]{
                SRGB.toLinear(dcValue >> 16),
                SRGB.toLinear((dcValue >> 8) & 255),
                SRGB.toLinear(dcValue & 255)
        };
    }

    static double[] decodeAC(String str, double realMaxValue) {
        int acValue = Base83.decode(str);
        int quantR = acValue / (19 * 19);
        int quantG = (acValue / 19) % 19;
        int quantB = acValue % 19;
        return new double[]{
                signPow((quantR - 9.0) / 9.0, 2.0) * realMaxValue,
                signPow((quantG - 9.0) / 9.0, 2.0) * realMaxValue,
                signPow((quantB - 9.0) / 9.0, 2.0) * realMaxValue
        };
    }

    static long encodeDC(double[] value) {
        long r = SRGB.fromLinear(value[0]);
        long g = SRGB.fromLinear(value[1]);
        long b = SRGB.fromLinear(value[2]);
        return (r << 16) + (g << 8) + b;
    }

    static long encodeAC(double[] value, double maximumValue) {
        double quantR = Math.floor(Math.max(0, Math.min(18, Math.floor(signPow(value[0] / maximumValue, 0.5) * 9 + 9.5))));
        double quantG = Math.floor(Math.max(0, Math.min(18, Math.floor(signPow(value[1] / maximumValue, 0.5) * 9 + 9.5))));
        double quantB = Math.floor(Math.max(0, Math.min(18, Math.floor(signPow(value[2] / maximumValue, 0.5) * 9 + 9.5))));
        return Math.round(quantR * 19 * 19 + quantG * 19 + quantB);
    }

    static void applyBasisFunction(int[] pixels, int width, int height,
                                   double normalisation, int i, int j,
                                   double[][] factors, int index) {
        double r = 0, g = 0, b = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double basis = normalisation
                        * Math.cos((Math.PI * i * x) / width)
                        * Math.cos((Math.PI * j * y) / height);
                int pixel = pixels[y * width + x];
                r += basis * SRGB.toLinear((pixel >> 16) & 0xff);
                g += basis * SRGB.toLinear((pixel >> 8) & 0xff);
                b += basis * SRGB.toLinear(pixel & 0xff);
            }
        }
        double scale = 1.0 / (width * height);
        factors[index][0] = r * scale;
        factors[index][1] = g * scale;
        factors[index][2] = b * scale;
    }

    static double max(double[][] values, int endExclusive) {
        double result = Double.NEGATIVE_INFINITY;
        for (int i = 1; i < endExclusive; i++) {
            for (int j = 0; j < values[i].length; j++) {
                double value = values[i][j];
                if (value > result) {
                    result = value;
                }
            }
        }
        return result;
    }
}
