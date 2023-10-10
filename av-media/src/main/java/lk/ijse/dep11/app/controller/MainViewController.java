package lk.ijse.dep11.app.controller;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
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
import java.util.Arrays;

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
    public ImageView imgBackground;
    public Button btnClose;
    public Button btnBackward;
    public Button btnForward;
    public HBox bxMusic;
    MediaPlayer videoPlayer;
    MediaPlayer audioPlayer;

    public void initialize(){
        imgBackground.setVisible(false);

        mvVideo.setViewOrder(1);
        imgBackground.setViewOrder(2);

        lblTitle.setVisible(false);
        Button[] buttons = new Button[]{btnPlay,btnPause,btnStop,btnSlow,btnFast,btnBackward,btnForward};
//        for (Button button : buttons) {
//            button.setDisable(true);
//        }

//        slrScroller.setBackground(Background.fill(Color.BLUE));
        String[] icons = new String[]{"asset.img/play1.png","asset.img/pause.png","asset.img/stop.png",
                "asset.img/SLOW.png","asset.img/FAST.png","asset.img/back10.png","asset.img/forward.png"};
        for (int i = 0; i < icons.length; i++) {
            Image image = new Image(icons[i]);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setPreserveRatio(true);
            buttons[i].setGraphic(imageView);
        }

        DoubleProperty widthProperty = imgBackground.fitWidthProperty();
        DoubleProperty heightProperty = imgBackground.fitHeightProperty();

        widthProperty.bind(Bindings.selectDouble(imgBackground.sceneProperty(),"width"));
        heightProperty.bind(Bindings.selectDouble(imgBackground.sceneProperty(),"height"));



    }
    public void btnBrowseOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().
                addAll(new FileChooser.ExtensionFilter("Video File","*.mp4","*.mkv"),
                            new FileChooser.ExtensionFilter("Audio File","*.mp3"));
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        String[] resultFile = file.getAbsolutePath().split("/");


        if(file.getAbsolutePath().matches("^.*\\.(mp4)$")) bxMusic.setVisible(false);
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

            imgBackground.setVisible(false);

            videoPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                    slrScroller.setValue(t1.toSeconds());
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

        }else {
            btnBrowse.requestFocus();
        }
        btnPlay.fire();

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

    public void btnBackwardOnAction(ActionEvent actionEvent) {
        btnBackward.setScaleY(1.2);
        btnBackward.setScaleX(1.2);

    }

    public void btnForwardOnAction(ActionEvent actionEvent) {
        btnBackward.setScaleY(1);
        btnBackward.setScaleX(1);
    }


    public void btnPlayOnMouseEntered(MouseEvent mouseEvent) {
        btnPlay.setScaleX(1.2);
        btnPlay.setScaleY(1.2);
    }

    public void btnPlayOnMouseExited(MouseEvent mouseEvent) {
        btnPlay.setScaleY(1);
        btnPlay.setScaleX(1);
    }

    public void btnPauseOnMouseEntered(MouseEvent mouseEvent) {
        btnPause.setScaleX(1.2);
        btnPause.setScaleY(1.2);
    }
    public void btnPauseOnMouseExited(MouseEvent mouseEvent) {
        btnPause.setScaleX(1);
        btnPause.setScaleY(1);
    }

    public void btnStopOnMouseEntered(MouseEvent mouseEvent) {
        btnStop.setScaleX(1.2);
        btnStop.setScaleY(1.2);
    }

    public void btnStopOnMouseExited(MouseEvent mouseEvent) {
        btnStop.setScaleX(1);
        btnStop.setScaleY(1);
    }

    public void btnSlowOnMouseEntered(MouseEvent mouseEvent) {
        btnSlow.setScaleX(1.2);
        btnSlow.setScaleY(1.2);
    }

    public void btnSlowOnMouseExited(MouseEvent mouseEvent) {
        btnSlow.setScaleX(1);
        btnSlow.setScaleY(1);
    }

    public void btnFastOnMouseExited(MouseEvent mouseEvent) {
        btnFast.setScaleX(1);
        btnFast.setScaleY(1);
    }

    public void btnForwardOnMouseEntered(MouseEvent mouseEvent) {
        btnForward.setScaleX(1.2);
        btnForward.setScaleX(1.2);
    }
    public void btnForwardOnMouseExited(MouseEvent mouseEvent) {
        btnForward.setScaleY(1);
        btnForward.setScaleX(1);
    }
    public void btnFastOnMouseEntered(MouseEvent mouseEvent) {
        btnFast.setScaleX(1.2);
        btnFast.setScaleY(1.2);
    }
    public void btnBackwardOnMouseEntered(MouseEvent mouseEvent) {
        btnBackward.setScaleX(1.2);
        btnBackward.setScaleY(1.2);
    }
    public void btnBackwardOnMouseExited(MouseEvent mouseEvent) {
        btnBackward.setScaleY(1);
        btnBackward.setScaleX(1);
    }


    public void slrVolumeOnMouseEntered(MouseEvent mouseEvent) {
        slrVolume.setScaleX(1.2);
        slrVolume.setScaleY(1.2);
    }

    public void slrVolumeOnMouseExited(MouseEvent mouseEvent) {
        slrVolume.setScaleX(1);
        slrVolume.setScaleY(1);
    }

    public void btnBrowseOnMouseEntered(MouseEvent mouseEvent) {
        btnBrowse.setScaleX(1.2);
        btnBrowse.setScaleY(1.2);
    }

    public void btnBrowseOnMouseExited(MouseEvent mouseEvent) {
        btnBrowse.setScaleY(1);
        btnBrowse.setScaleX(1);
    }
}


