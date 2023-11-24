import processing.core.*;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Processor {
    PApplet applet;

    int voxel_size = 1;
    Processor(PApplet applet, int voxel_size){
        this.applet = applet;
        this.voxel_size = voxel_size;
    }

    public ArrayList<Path> Process(PImage image){
        image.resize(0, image.height / voxel_size);

        float[][] field = new float[image.width][image.height];
        for(int x = 0; x < image.width; x++){
            for(int y = 0; y < image.height; y++)
            {
                field[x][y] = (float)applet.brightness(image.get(x, y)) / 256.0f;
            }
        }

        ArrayList<Line> lines = Squares.squares(field, 0.25f, voxel_size);

        ArrayList<Path> paths = new ArrayList<>();

        System.out.println("Starting path ordering!");
        while(!lines.isEmpty()){
            System.out.println("Starting new segment!");

            ArrayList<PVector> points = new ArrayList<>();
            Path path = new Path();
            path.points = points;
            paths.add(path);

            Line current = lines.remove(0);

            path.start = current.start;
            points.add(current.start);

            while(true) {
                points.add(current.end);

                Line next = null;
                double min_dist = 1000;
                for(Line line : lines){
                    double dist = current.end.dist(line.end);
                    if(dist < 5 * voxel_size && dist < min_dist)
                        next = line;

                    min_dist = Math.min(min_dist, dist);
                }

                if (next == null) {
                    System.out.println("Unable to find next line segment, count:" + lines.size() + ", minimum dist: " + min_dist);
                    break;
                }

                current = next;
                lines.remove(current);
            }

            path.end = current.end;
        }
        System.out.println("Finished line ordering!");

        return paths;
    }
}
