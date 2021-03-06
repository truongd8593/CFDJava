package com.misacfd.meshconverter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class App {
    public static void main(String[] args) throws IOException {
        Instant start = Instant.now();

        GmshReader gmshReader = GmshReader.getInstance();
        gmshReader.readMesh();
        gmshReader.constructIdNodes();

        FvMsh finiteVolumeMesh = new FvMsh(gmshReader);
        finiteVolumeMesh.assignVertex();
        finiteVolumeMesh.assignFaces();
        finiteVolumeMesh.assignBoundaryCondition();
        finiteVolumeMesh.detectNearestNeighbor();

        IMeshExporter vtkMeshExporter = new VtkMeshExporter();
        vtkMeshExporter.writeUnstructuredQuadMesh(gmshReader, finiteVolumeMesh.getCells());

        IMeshExporter tecplotMeshExporter = new TecplotMeshExporter();
        tecplotMeshExporter.writeUnstructuredQuadMesh(gmshReader, finiteVolumeMesh.getCells());

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println(String.format("Elapsed time is %s miliSeconds", timeElapsed));
    }
}
