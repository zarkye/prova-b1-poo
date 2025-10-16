package br.cesul.musicbattle.view;

import br.cesul.musicbattle.model.Track;
import br.cesul.musicbattle.viewmodel.MusicBattleViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MusicBattleView {
    @FXML private TextField filterField;
    @FXML private ComboBox<String> rankingCombo;
    @FXML private Button voteBtn;
    @FXML private TextField titleField;
    @FXML private TextField artistField;
    @FXML private Button addBtn;
    @FXML private TableView<Track> table;
    @FXML private TableColumn<Track, String> colTitle;
    @FXML private TableColumn<Track, String> colArtist;
    @FXML private TableColumn<Track, Integer> colVotes;

    private final MusicBattleViewModel vm = new MusicBattleViewModel();

    @FXML
    private void initialize(){
        rankingCombo.setItems(FXCollections.observableArrayList("Votos", "Titulo", "Artista"));
        Bindings.bindBidirectional(rankingCombo.valueProperty(), vm.rankProperty());

        filterField.textProperty().addListener((obs, oldV, newV) -> {
            vm.filtrarTracks(newV);
        });

        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colVotes.setCellValueFactory(new PropertyValueFactory<>("votes"));

        table.setItems(vm.getSortedTracks());

        // estava usando um listener para pegar o objeto, mas não tinha necessidade de um listener, ajustei pra só pegar o selectionmodel, muito mais otimizado assim
        voteBtn.setOnAction(e -> {
            Track selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                vm.votar(selected);
                table.getSelectionModel().clearSelection();
            }
        });

        //o disable do botão de votar tava zuado, não usei bind e ele não desabilitava nunca
        voteBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

        titleField.textProperty().bindBidirectional(vm.titleProperty());
        artistField.textProperty().bindBidirectional(vm.artistProperty());

        addBtn.setOnAction(e -> {
            vm.addTrack();
            titleField.clear();
            artistField.clear();
        });
    }
}
