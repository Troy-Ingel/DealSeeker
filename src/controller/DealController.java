package controller;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import model.Item;
import model.WebSearch;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;

public class DealController
{
    private Thread thread;
    @FXML
    private TableView<Item> dealTable;
    @FXML
    private TableColumn<Item, String> itemDescriptionColumn;
    @FXML
    private TextField timeInput;
    @FXML
    private TextField itemInput;
    @FXML
    private Button searchButton;
    @FXML
    private ImageView questionMarkImage;
    @FXML
    private Label questionMarkLabel;


    public void initialize()
    {
        initItemDescriptionColumn();
        initItemTextInput();
        initTimeTextInput();
        initSearchButton();
        initQuestionMarkImage();
    }

    private List<Item> getWebData(String keyword)
    {
        WebSearch readFromWebPage = new WebSearch(keyword);

        return readFromWebPage.getItems();
    }

    @FXML
    private void searchButtonLogic()
    {
        if ("Start Search".equals(searchButton.getText()))
        {
            populateTable();
            itemInput.setDisable(true);
            timeInput.setDisable(true);
            searchButton.setText("End Search");
        }
        else
        {
            thread.stop();
            itemInput.setDisable(false);
            timeInput.setDisable(false);
            searchButton.setText("Start Search");
        }

    }

    private void populateTable()
    {
        thread = new Thread(() -> {
            try
            {
                // imitating work
                Thread.sleep(Integer.parseInt(timeInput.getText()) * 1000);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            System.out.println("job started" + new Timestamp(System.currentTimeMillis()));
            List<Item> items = getWebData(itemInput.getText());

            Platform.runLater(() -> {
                dealTable.setItems(FXCollections.observableArrayList(items));
                dealTable.refresh();
                dealTable.scrollToColumnIndex(1);
                dealTable.scrollToColumnIndex(0);
                System.out.println("finished job" + new Timestamp(System.currentTimeMillis()));
                showAlert();
            });

            populateTable();
        });
        thread.start();
    }

    private void initSearchButton()
    {
        searchButton.disableProperty().bind(new BooleanBinding()
        {
            {
                super.bind(itemInput.textProperty(),
                    timeInput.textProperty());
            }

            @Override
            protected boolean computeValue()
            {
                return (itemInput.getText().isEmpty()
                    || timeInput.getText().isEmpty()
                    || timeInput.getText().length() > 4);
            }
        });
    }

    private void initItemTextInput()
    {
        itemInput.setOnMousePressed(e -> itemInput.clear());

        itemInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.endsWith(" "))
            {
                itemInput.setText(itemInput.getText().replaceAll("\\s", "+"));
            }
        });
    }

    private void initTimeTextInput()
    {
        timeInput.setOnMousePressed(e -> timeInput.clear());

        timeInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
            {
                timeInput.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void initItemDescriptionColumn()
    {
        itemDescriptionColumn.setSortable(false);

        itemDescriptionColumn.setCellFactory(
            new Callback<TableColumn<Item, String>, TableCell<Item, String>>()
            {
                @Override
                public TableCell<Item, String> call(TableColumn<Item, String> tableColumn)
                {
                    TableCell<Item, String> cell = new WrappingTextFieldTableCell<Item>()
                    {
                        @Override
                        public void updateItem(String string, boolean isEmpty)
                        {
                            super.updateItem(string, isEmpty);
                            if (!isEmpty)
                            {
                                Item model =
                                    getTableView().getItems().get(getTableRow().getIndex());
                                Tooltip tip = new Tooltip();
                                Image image = new Image(model.getImg());
                                ImageView imageView = new ImageView(image);
                                tip.setGraphic(imageView);
                                hackTooltipStartTiming(tip);
                                setTooltip(tip);
                            }
                        }
                    };

                    Text text = new Text();
                    text.setText(text.toString());
                    text.wrappingWidthProperty().bind(cell.widthProperty());

                    return cell;
                }
            });
    }

    @FXML
    private void initQuestionMarkImage()
    {
        questionMarkImage.setImage(new Image("https://image.flaticon.com/icons/png/512/36/36601.png"));
        Tooltip toolTip = new Tooltip("For ethical purposes, please do not perform\n" +
            "searches shorter than 30 seconds as this will flood\n" +
            "the deal website with an unreasonable amount of requests.");
        hackTooltipStartTiming(toolTip);
        questionMarkLabel.setTooltip(toolTip);
    }

    private static void hackTooltipStartTiming(Tooltip tooltip)
    {
        try
        {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(100)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showAlert()
    {
        Stage popup = new Stage();
        popup.setHeight(100);
        popup.setWidth(100);
        Label label = new Label("Search Completed");
        label.setTextFill(Color.RED);
        label.setFont(Font.font ("Verdana", 13));
        AnchorPane anchorPane = new AnchorPane(label);
        popup.setScene(new Scene(anchorPane));
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> popup.hide());

        popup.show();
        delay.play();
    }

}
