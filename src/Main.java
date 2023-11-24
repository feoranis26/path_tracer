import processing.core.*;

import java.io.*;
import java.util.ArrayList;

public class Main extends PApplet {
    int VOXEL_SIZE = 1;
    ArrayList<PImage> Images = new ArrayList<>();
    Processor processor = new Processor(this, VOXEL_SIZE);

    File save_directory;
    int output_num;

    public void settings() {
        size(480, 360);
        smooth(8);
    }

    public void setup() {
        frameRate(3f);
        selectFolder("Open images", "selected");
    }

    public void selected(File selection) {
        this.save_directory = selection;
        this.output_num = 0;

        System.out.println("Loading images...");
        File[] files = selection.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg"));

        for (File file : files) {
            PImage image = loadImage(file.getAbsolutePath());
            Images.add(image);
        }
        System.out.println("Finished loading images.");
    }

    public void draw() {
        background(0);
        stroke(255, 0, 0);
        strokeWeight(1);
        fill(255, 0, 0);
        textSize(25);

        imageMode(CENTER);

        if (Images.isEmpty()) {
            text("No image!", width / 2, height / 2);
            return;
        }

        PImage image = Images.remove(0);
        image(image, width / 2, height / 2);
        push();
        noStroke();
        fill(0, 192);
        rect(0, 0, width, height);
        pop();

        //push();
        //stroke(255);
        //for (int a = 0; a < width - 25; a += 25) {
            //line(a, 0, 0, a);
            //line(width, a, a, width);
        //}
        //pop();

        ArrayList<Path> paths = processor.Process(image);

        StringBuilder output_string = new StringBuilder();
        int line_count = 0;

        for (Path path : paths) {
            stroke(random(255), random(255), random(255));
            output_string.append("break\n");

            PVector previous = to_relative(path.points.remove(0));
            for (PVector point : path.points) {
                PVector current = to_relative(point);

                line((float) previous.x * height / 2.0f + width / 2f,
                        (float) previous.y * height / 2.0f + height / 2f,
                        (float) current.x * height / 2.0f + width / 2f,
                        (float) current.y * height / 2.0f + height / 2f);

                output_string.append(String.format("%f %f\n", previous.x, previous.y, current.x, current.y));

                line_count++;
                previous = current;
            }
        }

        text("Strokes: " + line_count, 0, height - 25);
        text("Segments: " + paths.size(), 0, height - 50);

        FileWriter output = null;
        try {
            File output_file = new File(save_directory.getAbsolutePath() + File.separator + "frames" + File.separator + output_num++ + ".path");
            output_file.createNewFile();
            output = new FileWriter(output_file);
            output.write(output_string.toString());
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public PVector to_relative(PVector in) {
        float x_relative = 2 * in.x / (float) height - (float) width / height;
        float y_relative = 2 * in.y / (float) height - 1;

        return new PVector(x_relative, y_relative);
    }
}