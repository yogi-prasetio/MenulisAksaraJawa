package com.android.menulisaksarajawa.ui.model.pip;

import java.util.List;

public class PointInPolygon {
    private Point point;
    private boolean pointOnVertex = true;
    private String[] coordinates;
    private List<String> polygon;
    public Float x;
    public Float y;

    public PointInPolygon(float x, float y, List<String> path) {
        this.x = x;
        this.y = y;
        this.point = new Point(x, y);
        this.polygon = path;
    }

    private Point pointStringToCoordinates(String point) {
        coordinates = point.split(",");
//        if (point.length() != 1) {
//            return new Point(-1.0f, -1.0f);
//        }
        return new Point(Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1]));
    }
//
//    private boolean pointOnVertex(Point point, Point[] vertices) {
//        int i = 0;
//        while (i < vertices.length) {
//            if (point.x == vertices[i].x && point.y == vertices[i].y) {
//                return true;
//            }
//            i++;
//        }
//        return false;
//    }

    public String pointInPolygon() {
        return pointInPolygon(polygon);
    }

//    public String pointInPolygon(List<String> polygon) {
//        return pointInPolygon(polygon, true);
//    }

    public String pointInPolygon(List<String> polygon) {
        int intersection = 0;
//        this.pointOnVertex = pointOnVertex;
        Point[] vertices = new Point[polygon.size()];
        for (int i = 0; i < polygon.size(); i++) {
            vertices[i] = pointStringToCoordinates((String) polygon.get(i));
            if (point.x == vertices[i].x && point.y == vertices[i].y) {
                return "Vertex";
            }
        }

        int vertices_count = vertices.length;
        for (int i2 = 1; i2 < vertices_count; i2++) {
            Point vertex1 = vertices[i2 - 1];
            Point vertex2 = vertices[i2];
            if (vertex1.y == vertex2.y && vertex1.y == this.point.y && this.point.x > Math.min(vertex1.x, vertex2.x) && this.point.x < Math.max(vertex1.x, vertex2.x)) {
//                intersection++;
                return "Boundary";
            } else if (this.point.y > Math.min(vertex1.y, vertex2.y) && this.point.y <= Math.max(vertex1.y, vertex2.y) && this.point.x <= Math.max(vertex1.x, vertex2.x) && vertex1.y != vertex2.y) {
                float xinters = (((this.point.y - vertex1.y) * (vertex2.x - vertex1.x)) / (vertex2.y - vertex1.y)) + vertex1.x;
                if (xinters == this.point.x) {
//                    intersection++;
                    return "Boundary";
                } else if (vertex1.x == vertex2.x || this.point.x <= xinters) {
                    intersection++;
                }
            }
        }
        if (intersection % 2 != 0) {
            return "Inside";
        } else {
            return "Outside";
        }
    }
}
