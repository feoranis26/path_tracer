import processing.core.PVector;
import java.util.ArrayList;

public class Squares {
    static ArrayList<Line> squares(float[][] field, float h, float voxel_size) {
        ArrayList<Line> poly = new ArrayList<Line>();

        for (int i = 0; i < field.length - 1; i++) {
            for (int j = 0; j < field[i].length - 1; j++) {
                float f0 = field[i][j] - h;
                float f1 = field[i + 1][j] - h;
                float f2 = field[i + 1][j + 1] - h;
                float f3 = field[i][j + 1] - h;


                //tessellation = f0 * 3

                //if (tessellation < 0.25) tessellation = 0.25;
                //if (tessellation > 1) tessellation = 1;

                //tessellation = 1 / tessellation

                float x = i * voxel_size;
                float y = j * voxel_size;
                PVector a = new PVector(x + voxel_size * (f0 / (f0 - f1)), y);
                PVector b = new PVector(x + voxel_size, y + voxel_size * (f1 / (f1 - f2)));
                PVector c = new PVector(x + voxel_size * (1 - f2 / (f2 - f3)), y + voxel_size);
                PVector d = new PVector(x, y + voxel_size * (1 - f3 / (f3 - f0)));

                int state = getState(f0, f1, f2, f3);
                switch (state) {
                    case 1:
                        addLine(c, d, poly);
                        break;
                    case 2:
                        addLine(b, c, poly);
                        break;
                    case 3:
                        addLine(b, d, poly);
                        break;
                    case 4:
                        addLine(a, b, poly);
                        break;
                    case 5:
                        addLine(a, d, poly);
                        addLine(b, c, poly);
                        break;
                    case 6:
                        addLine(a, c, poly);
                        break;
                    case 7:
                        addLine(a, d, poly);
                        break;
                    case 8:
                        addLine(a, d, poly);
                        break;
                    case 9:
                        addLine(a, c, poly);
                        break;
                    case 10:
                        addLine(a, b, poly);
                        addLine(c, d, poly);
                        break;
                    case 11:
                        addLine(a, b, poly);
                        break;
                    case 12:
                        addLine(b, d, poly);
                        break;
                    case 13:
                        addLine(b, c, poly);
                        break;
                    case 14:
                        addLine(c, d, poly);
                        break;
                }
            }
        }

        return poly;
    }

    static void addLine(PVector v1, PVector v2, ArrayList<Line> list) {
        Line line = new Line();
        line.start = v1;
        line.end = v2;
        list.add(line);
    }

    static int getState(double a, double b, double c, double d) {
        return (a > 0 ? 8 : 0) + (b > 0 ? 4 : 0) + (c > 0 ? 2 : 0) + (d > 0 ? 1 : 0);
    }
}