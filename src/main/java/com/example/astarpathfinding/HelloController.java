package com.example.astarpathfinding;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {

    ArrayList<GraphNode> graphNodes = new ArrayList<>();
    ArrayList<GraphNode> path = new ArrayList<>();
    double aStarPlaneWidth;
    double aStarPlaneHeight;
    int tilesAcross; // number of tiles horizonally
    int tilesDown; // number of tiles vertically
    int tileAmount; // total amount of tiles
    int gridSize = 50;
    double playbackSpeed = 0.1;
    GraphNode start;
    GraphNode goal;




    Image hero_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\boy_down_1.png");
    Image Goal_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\chest.png");
    Image Wall_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\wall.png");
    Image Grass1_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\grass00.png");
    Image Grass2_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\grass01.png");
    Image Upper_left_grass_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\road01.png");
    Image Upper_right_grass_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\road03.png");
    Image Lower_left_grass_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\road06.png");
    Image Lower_right_grass_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\road08.png");
    Image Up_grass_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\road02.png");
    Image Down_grass_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\road07.png");
    Image Left_grass_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\road04.png");
    Image Right_grass_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\road05.png");
    Image Earth_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\earth.png");

    Image Water_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\water01.png");
    Image sand_image = new Image("C:\\Users\\amin\\Desktop\\Pathfinding-main\\Pathfinding-main\\src\\main\\resources\\com\\example\\astarpathfinding\\road00.png");

    ImagePattern hero_pattern = new ImagePattern(hero_image);
    ImagePattern Goal_pattern = new ImagePattern(Goal_image);
    ImagePattern Grass1_pattern = new ImagePattern(Grass1_image);
    ImagePattern Grass2_pattern = new ImagePattern(Grass2_image);
    ImagePattern Wall_pattern = new ImagePattern(Wall_image);
    ImagePattern Blank_pattern = new ImagePattern(Earth_image);
    ImagePattern Upper_left_grass_pattern = new ImagePattern(Upper_left_grass_image);
    ImagePattern Upper_right_grass_pattern = new ImagePattern(Upper_right_grass_image);
    ImagePattern Lower_left_grass_pattern = new ImagePattern(Lower_left_grass_image);
    ImagePattern Lower_right_grass_pattern = new ImagePattern(Lower_right_grass_image);
    ImagePattern Up_pattern = new ImagePattern(Up_grass_image);
    ImagePattern Down_pattern = new ImagePattern(Down_grass_image);
    ImagePattern Left_pattern = new ImagePattern(Left_grass_image);
    ImagePattern Right_pattern = new ImagePattern(Right_grass_image);
    ImagePattern Water_pattern = new ImagePattern(Water_image);
    ImagePattern Sand_pattern = new ImagePattern(sand_image);


    @FXML
    private AnchorPane gamePlane;

    @FXML
    private TextField gridSizeInput;

    @FXML
    private ComboBox<String> RolePicker;

    @FXML
    ProgressBar energy_bar ;

    @FXML
    Label energy_bar_percentage ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aStarPlaneWidth = gamePlane.getPrefWidth();
        aStarPlaneHeight = gamePlane.getPrefHeight();
        updateGrid();


        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Wall",
                        "Goal",
                        "Start",
                        "Blank",
                        "Water",
                        "Sand"
                );
        RolePicker.setItems(options);



    }

    

    public ImagePattern getImage(String option){
        return switch (option) {
            case "Wall" -> Wall_pattern;
            case "Goal" -> Goal_pattern;
            case "Start" -> hero_pattern;
            case "Blank" -> Blank_pattern;
            case "Water" -> Water_pattern;
            case "Sand" -> Sand_pattern;
            default -> null;
        };
    }

    private void updateGrid() {
        tilesAcross = (int) (aStarPlaneWidth / gridSize);
        tileAmount = (int) ((aStarPlaneWidth/gridSize) * (aStarPlaneHeight/gridSize));
        tilesDown = tileAmount/tilesAcross;

        for(int i = 0; i < tileAmount; i++){
            int x = (i % tilesAcross);
            int y = (i / tilesAcross);

            Rectangle rectangle = new Rectangle(x * gridSize,y * gridSize,gridSize,gridSize);
            if((x + y) % 2 == 0){
                rectangle.setFill(Grass1_pattern);
            } else {
                rectangle.setFill(Grass2_pattern);
            }

            if(x==0 && y==0){
                rectangle.setFill(Upper_left_grass_pattern);

            }else {
                if(x==0 && y!=0)
                    rectangle.setFill(Left_pattern);

            }

            if(x==tilesAcross-1 && y==0){
                rectangle.setFill(Upper_right_grass_pattern);

            }else {
                if(x>0 && y==0 && x <tilesAcross-1)
                    rectangle.setFill(Up_pattern);

            }

            if(x==0 && y==tilesDown-1){
                rectangle.setFill(Lower_left_grass_pattern);

            }else {
                if(y==tilesDown-1)
                    rectangle.setFill(Down_pattern);

            }
            if(x==tilesAcross-1 && y==tilesDown-1){
                rectangle.setFill(Lower_right_grass_pattern);
            }else {
                if(x==tilesAcross-1 && y>0)
                    rectangle.setFill(Right_pattern);

            }

            gamePlane.getChildren().add(rectangle);
        }
    }

    @FXML
    void planeClicked(MouseEvent event) {
        drawRectangle(event);
    }

    @FXML
    void planeDragged(MouseEvent event) {
        drawRectangle(event);
    }

    private void drawRectangle(MouseEvent event) {
        ImagePattern Image = getImage(RolePicker.getValue());
        double mouseX = event.getX();
        double mouseY = event.getY();
        /*System.out.println(mouseX);
        System.out.println(mouseY);
        System.out.println((mouseX/gridSize));
        System.out.println((mouseX/gridSize) % tilesAcross);
        System.out.println((int) ((mouseX/gridSize) % tilesAcross)* gridSize);
        System.out.println((int) ((mouseY/gridSize) % tilesDown) * gridSize);*/

        int x = (int) ((mouseX/gridSize) % tilesAcross)* gridSize; // getting position of the clicked rectangle (matrix form )
        int y = (int) ((mouseY/gridSize) % tilesDown) * gridSize;

        ObservableList<Node> rectangles = gamePlane.getChildren();

        for (Node node: rectangles) {
            Rectangle rectangle = (Rectangle) node;
            if(rectangle.getX() == x && rectangle.getY() == y){
                rectangle.setFill(Image);
                return;
            }
        }
    }

    @FXML
    void clearPlane(ActionEvent event) {
        gridSize = Integer.parseInt(gridSizeInput.getText());
        gamePlane.getChildren().clear();
        graphNodes.clear();
        updateGrid();
    }

    public void recharge_energy_bar (){
        energy_bar_percentage.setText("100");
        energy_bar.setProgress(1);
    }

    @FXML
    void run_A_star_algo_showcase(ActionEvent event) throws InterruptedException {
        findGoalNode();
        findStartNode();
        setupNodes();//calacultae nodes heurisitics
        path.clear();
        A_star_algo(start);
        Iterator<GraphNode> nodeIterator = path.iterator();
        nodeIterator.next();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(playbackSpeed), ev -> {

                    //     this.start();

                    colorRectangle(nodeIterator.next().getRectangle(), Color.RED);
                    energy_bar.setProgress(energy_bar.getProgress() - 0.1);
                    int temp = Integer.parseInt(energy_bar_percentage.getText());
                    energy_bar_percentage.setText((temp - 10) + "");
                if(Integer.parseInt(energy_bar_percentage.getText())==0){
                        energy_bar.setProgress(1);
                        energy_bar_percentage.setText( 100+"");


                }
             /*   energy_bar.setProgress(energy_bar.getProgress() - 0.1);
                int temp = Integer.parseInt(energy_bar_percentage.getText());
                energy_bar_percentage.setText((temp - 10) + "");*/

        }));
       timeline.setCycleCount(path.size() - 1);
       //timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.playFromStart();



       // timeline.setOnFinished(e -> findPathTraversal());
    }
/*
    public void findPathTraversal(){
        ArrayList<GraphNode> traversalPath = new ArrayList<>();

        path.forEach(node -> node.setVisited(false));

        GraphNode currentNode = path.get(path.size() - 1);
        traversalPath.add(currentNode);

        while (currentNode != start){
            ArrayList<GraphNode> neighbours = getNeighbours(currentNode, path);

            currentNode = neighbours.get(0);

            for (int i = 1; i < neighbours.size(); i++) {
                if(neighbours.get(i).getH() > currentNode.getH()){
                    currentNode = neighbours.get(i);
                }
            }
            traversalPath.add(currentNode);
        }
        Iterator<GraphNode> iterator = traversalPath.iterator();
        Timeline traversalTimeline = new Timeline(new KeyFrame(Duration.seconds(playbackSpeed), ev -> {
            colorRectangle(iterator.next().getRectangle(), Color.BLUE);

        }));
        traversalTimeline.setCycleCount(traversalPath.size() - 1);
        traversalTimeline.playFromStart();
    }
*/
    public void setupNodes(){
        ObservableList<Node> rectangles = gamePlane.getChildren();

        int rectangleIndex = 0;


        for (int i = 0; i < tilesDown; i++) {
            for (int j = 0; j < tilesAcross; j++) {
                Rectangle rectangle = (Rectangle) rectangles.get(rectangleIndex);

                GraphNode graphNode;

                ImagePattern image = ( ImagePattern) rectangle.getFill();
                if(image.equals(Wall_pattern)){
                    graphNode = new GraphNode(rectangle, j, i, -1);
                } else if (image.equals(Sand_pattern)) {
                    graphNode = new GraphNode(rectangle, j, i, calculateHeuristic(rectangle, goal.getRectangle())+5);

                } else if (image.equals(Water_pattern)) {
                    graphNode = new GraphNode(rectangle, j, i, calculateHeuristic(rectangle, goal.getRectangle())+50);

                } else {
                    graphNode = new GraphNode(rectangle, j, i, calculateHeuristic(rectangle, goal.getRectangle()));
                }
                graphNodes.add(graphNode);
                rectangleIndex++;
            }
        }
    }

    public void findGoalNode(){
        ObservableList<Node> rectangles = gamePlane.getChildren();

        int rectangleIndex = 0;

        for (int i = 0; i < tilesDown; i++) {
            for (int j = 0; j < tilesAcross; j++) {
                Rectangle rectangle = (Rectangle) rectangles.get(rectangleIndex);

                ImagePattern image = (ImagePattern) rectangle.getFill();
                if (image.equals(Goal_pattern)){
                    goal = new GraphNode(rectangle, j, i, 0);
                    return;
                }
                rectangleIndex++;
            }
        }
    }
    public void findStartNode(){
        ObservableList<Node> rectangles = gamePlane.getChildren();

        int rectangleIndex = 0;

        for (int i = 0; i < tilesDown; i++) {
            for (int j = 0; j < tilesAcross; j++) {
                Rectangle rectangle = (Rectangle) rectangles.get(rectangleIndex);

                ImagePattern image = ( ImagePattern) rectangle.getFill();
                if (image.equals(hero_pattern)){
                    start = new GraphNode(rectangle, j, i, calculateHeuristic(rectangle, goal.getRectangle()));
                    return;
                }
                rectangleIndex++;
            }
        }
    }

    public double calculateHeuristic(Rectangle rectangle, Rectangle goal){

        return Math.abs(goal.getX() - rectangle.getX()) + Math.abs(goal.getY() - rectangle.getY());
    }

    public void A_star_algo(GraphNode start){
        GraphNode currentNode = start;
        PriorityQueue<GraphNode> queue = new PriorityQueue<>();
        queue.add(currentNode);

        while (!queue.isEmpty() && Integer.parseInt(energy_bar_percentage.getText())!=0){
            currentNode = queue.peek();
            currentNode.setVisited(true);
            path.add(currentNode);
            ArrayList<GraphNode> neighbours = getNeighbours(currentNode, graphNodes);
            for (GraphNode neighbour: neighbours) {
                if(neighbour.getH() == 0){
                    return;
                }
            }
            queue.remove(currentNode);
            queue.addAll(neighbours);
        }
        Alert alert = new Alert(Alert.AlertType.ERROR,"no path found ");
    }

    public void colorRectangle(Rectangle rectangle, Color color){
        rectangle.setFill(color);
    }
    public void fillImageRectangle(Rectangle rectangle, ImagePattern image){
        rectangle.setFill(image);
    }

    public ArrayList<GraphNode> getNeighbours(GraphNode currentNode, ArrayList<GraphNode> possibleGraphNodes){

        ArrayList<GraphNode> neighbours = new ArrayList<>();

        for (GraphNode graphNode: possibleGraphNodes) {
            if(graphNode.isVisited()){
                continue;
            }



            for (int i = -1; i < 2; i++) {


                if(i==0){

                    for (int j = -1; j < 2; j++) {

                        if(graphNode.getI() == currentNode.getI()+i && graphNode.getJ() == currentNode.getJ()+j &&
                                graphNode.getH() != -1){
                            graphNode.setVisited(true);
                            neighbours.add(graphNode);
                        }

                    }


                }else{
                    if(graphNode.getI() == currentNode.getI()+i && graphNode.getJ() == currentNode.getJ() &&
                            graphNode.getH() != -1){
                        graphNode.setVisited(true);
                        neighbours.add(graphNode);
                    }
                }

            }




        }
        return neighbours;
    }

    @FXML
    void showHeuristic(ActionEvent event) {
        findGoalNode();
        findStartNode();
        setupNodes();

        for (GraphNode graphNode: graphNodes) {
            Text text = new Text();
            text.setText("h:"  + (int)graphNode.getH());
            text.setX(graphNode.getX());
            text.setY(graphNode.getY() + 15);

            gamePlane.getChildren().add(text);
        }

    }

}
