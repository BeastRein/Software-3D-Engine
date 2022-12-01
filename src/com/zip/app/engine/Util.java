package com.zip.app.engine;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import org.json.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Util {
    public static point rotate(point pos, point rot) {
        //3d point rotational math
        double Axx = cos(rot.z)*cos(rot.x);
        double Axy = cos(rot.z)*sin(rot.x)*sin(rot.y) - sin(rot.z)*cos(rot.y);
        double Axz = cos(rot.z)*sin(rot.x)*cos(rot.y) + sin(rot.z)*sin(rot.y);

        double Ayx = sin(rot.z)*cos(rot.x);
        double Ayy = sin(rot.z)*sin(rot.x)*sin(rot.y) + cos(rot.z)*cos(rot.y);
        double Ayz = sin(rot.z)*sin(rot.x)*cos(rot.y) - cos(rot.z)*sin(rot.y);

        double Azx = -sin(rot.x);
        double Azy = cos(rot.x)*sin(rot.y);
        double Azz = cos(rot.x)*cos(rot.y);

        //return a point containing the rotated coordinates
        return new point(
                Axx*pos.x + Axy*pos.y + Axz*pos.z,
                Ayx*pos.x + Ayy*pos.y + Ayz*pos.z,
                Azx*pos.x + Azy*pos.y + Azz*pos.z
        );
    }
    public static object Loader(String filename, point location) {
        //load model.json file into a string
        String jsonContent;
        try {
            jsonContent = Files.readString(Path.of("Assets/" + filename));
        } catch (Exception e) {
            return new object(new point(0,0,0), new point(0,0,0), new point[] {}, new int[][] {}, new int[][] {});
        }
        //parse json content of file
        JSONObject obj = new JSONObject(jsonContent);

        //process array of points
        JSONArray pointArray = obj.getJSONArray("points");
        point[] points = new point[pointArray.length()];
        for (int i = 0; i < points.length; i++) {
            JSONArray point = pointArray.getJSONArray(i);
            points[i] = new point(point.getDouble(0), point.getDouble(1), point.getDouble(2));
        }

        //process array of lines
        JSONArray lineArray = obj.getJSONArray("lines");
        int[][] lines = new int[lineArray.length()][2];
        for (int i = 0; i < lines.length; i++) {
            lines[i][0] = lineArray.getJSONArray(i).getInt(0);
            lines[i][1] = lineArray.getJSONArray(i).getInt(1);
        }

        //process array of faces
        JSONArray faceArray = obj.getJSONArray("faces");
        int[][] faces = new int[faceArray.length()][5];
        for (int i = 0; i < faces.length; i++) {
            for (int f = 0; f < faceArray.getJSONArray(i).length(); f++) {
                faces[i][f] = faceArray.getJSONArray(i).getInt(f);
            }
        }
        //create the object from the processed json and return it
        return new object(location,new point(0,0,0), points, lines, faces);
    }
}