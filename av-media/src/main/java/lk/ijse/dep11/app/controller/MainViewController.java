package lk.ijse.dep11.app.controller;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.time.Clock;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

public class MainViewController {
    public BorderPane root;

    public Button btnBrowse;
    public Slider slrScroller;
    public Button btnPause;
    public Button btnSlow;
    public Button btnFast;
    public Button btnPlay;
    public Label lblTitle;
    public Slider slrVolume;
    public Button btnStop;
    public MediaView mvVideo;
    public Button btnClose;
    public Button btnBackward;
    public Button btnForward;
    public HBox bxMusic;
    public ImageView imgBackground;
    public Label lblStartTime;
    public Label lblEndTime;
    MediaPlayer videoPlayer;
    MediaPlayer audioPlayer;

    public void initialize(){

        mvVideo.setViewOrder(1);
        bxMusic.setVisible(false);

        lblTitle.setVisible(false);
        Button[] buttons = new Button[]{btnPlay,btnPause,btnStop,btnSlow,btnFast,btnBackward,btnForward};
//        for (Button button : buttons) {
//            button.setDisable(true);
//        }

//        slrScroller.setBackground(Background.fill(Color.BLUE));
        String[] icons = new String[]{"asset.img/play.png","asset.img/pause.png","asset.img/stop.png",
                "asset.img/slow1.png","asset.img/fast1.png","asset.img/back10.png","asset.img/forward.png"};
        for (int i = 0; i < icons.length; i++) {
            Image image = new Image(icons[i]);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setPreserveRatio(true);
            buttons[i].setGraphic(imageView);
        }



    }
    public void btnBrowseOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().
                addAll(new FileChooser.ExtensionFilter("Video File","*.mp4","*.mkv"),
                            new FileChooser.ExtensionFilter("Audio File","*.mp3"));
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        String[] resultFile = file.getAbsolutePath().split("/");


        if(file.getAbsolutePath().matches("^.*\\.(mp4)$")) {
            imgBackground.setVisible(false);
            bxMusic.setVisible(false);
        }else {
            imgBackground.setVisible(false);
            bxMusic.setVisible(true);
        }
        if(file != null){
            lblTitle.setVisible(true);

            String[] path = file.getAbsolutePath().split("/");
            lblTitle.setText(path[path.length-1]);
            lblTitle.setTooltip(new Tooltip(path[path.length-1]));

            Button[] buttons = new Button[]{btnPlay,btnPause,btnStop,btnSlow,btnFast};
            for (Button button : buttons) {
                button.setDisable(false);
            }

            Media media = new Media(file.toURI().toString());
            videoPlayer = new MediaPlayer(media);

            DoubleProperty widthProperty = mvVideo.fitWidthProperty();
            DoubleProperty heightProperty = mvVideo.fitHeightProperty();

            widthProperty.bind(Bindings.selectDouble(mvVideo.sceneProperty(),"width"));
            heightProperty.bind(Bindings.selectDouble(mvVideo.sceneProperty(),"height"));


            videoPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                    slrScroller.setValue(t1.toSeconds());
                    lblStartTime.setText(getTimeString(videoPlayer.getCurrentTime().toMillis()));
                    lblEndTime.setText(getTimeString( videoPlayer.getTotalDuration().toMillis() - videoPlayer.getCurrentTime().toMillis()));
                }
            });

            videoPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    Duration totalDuration = media.getDuration();
                    slrScroller.setMax(totalDuration.toSeconds());
                }
            });

            slrVolume.setValue(videoPlayer.getVolume()*100);
            slrVolume.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    videoPlayer.setVolume(slrVolume.getValue()/100);
                }
            });

            slrVolume.setValue(videoPlayer.getVolume()*100);
            slrVolume.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    videoPlayer.setVolume(slrVolume.getValue()/100);
                }
            });

            slrScroller.valueProperty().addListener(ov -> {
                if (slrScroller.isValueChanging()) {
                    videoPlayer.seek(new Duration(slrScroller.getValue()));
                }
            });



        }else {
            btnBrowse.requestFocus();
        }
        btnPlay.fire();

    }
    public static String getTimeString(double millis) {
        millis /= 1000;
        String s = formatTime(millis % 60);
        millis /= 60;
        String m = formatTime(millis % 60);
//        millis /= 60;
//        String h = formatTime(millis % 24);
        return m + ":" + s;
    }

    public static String formatTime(double time) {
        int t = (int) time;
        if (t > 9) {
            return String.valueOf(t); }
        return "0" + t;
    }



    public void btnPlayOnAction(ActionEvent actionEvent) {
        if(videoPlayer != null) {
            mvVideo.setMediaPlayer(videoPlayer);

            videoPlayer.play();
            videoPlayer.setRate(1);
        }

    }

    public void btnPauseOnAction(ActionEvent actionEvent) {
        if(videoPlayer != null) videoPlayer.pause();
    }

    public void btnFastOnAction(ActionEvent actionEvent) {
        if(videoPlayer != null) videoPlayer.setRate(2);
    }
    public void btnSlowOnAction(ActionEvent actionEvent){
        if(videoPlayer != null) videoPlayer.setRate(0.5);
    }

    public void btnStopOnAction(ActionEvent actionEvent) {
        if(videoPlayer != null) videoPlayer.stop();
    }

    public void btnCloseOnAction(ActionEvent actionEvent) {
        System.exit(1);
    }

    public void btnCloseOnMouseMoved(MouseEvent mouseEvent) {
        btnClose.setBackground(Background.fill(Color.SALMON));
    }

    public void mvVideoOnMouseClicked(MouseEvent mouseEvent) {
//        if(mouseEvent.getClickCount() <= 1 ){
//            videoPlayer.pause();
//        }
    }

    public void playMouseEnterAnimation(MouseEvent event){
            if(event.getSource() instanceof Button){
                Button button = (Button) event.getSource();
                DropShadow glow = new DropShadow();
                glow.setColor(Color.DEEPSKYBLUE);
                glow.setWidth(20);
                glow.setHeight(20);
                glow.setRadius(20);
                button.setEffect(glow);
                button.setScaleX(1.2);
                button.setScaleY(1.2);
                button.setCursor(Cursor.HAND);
            }
    }

    public void playMouseExitAnimation(MouseEvent event){
        if(event.getSource() instanceof Button){
            Button button = (Button) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), button);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();
            button.setEffect(null);
            button.setScaleX(1);
            button.setScaleY(1);


        }
    }

    public void btnBackwardOnAction(ActionEvent actionEvent) {
        btnBackward.setScaleY(1.2);
        btnBackward.setScaleX(1.2);

    }

    public void btnForwardOnAction(ActionEvent actionEvent) {
        btnBackward.setScaleY(1);
        btnBackward.setScaleX(1);
    }



}


