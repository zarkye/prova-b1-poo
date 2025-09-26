package br.cesul.musicbattle.viewmodel;

import br.cesul.musicbattle.model.Track;
import br.cesul.musicbattle.repository.TrackRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class MusicBattleViewModel {

    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();


    public StringProperty titleProperty(){return title;}
    public StringProperty artistProperty(){return artist;}
    private final StringProperty rank = new SimpleStringProperty();
    public StringProperty rankProperty(){return rank;}

    private final TrackRepository repo = new TrackRepository();

    private final ObservableList<Track> tracks = FXCollections.observableArrayList();
    public FilteredList<Track> trackFilteredList;

    public ObservableList<Track> getTracks(){return tracks;}
    public FilteredList<Track> getTrackFilteredList(){return trackFilteredList;}

    public MusicBattleViewModel(){
        tracks.addAll(repo.findAll());
        tracks.addListener((ListChangeListener<? super Track>) c -> {
            while(c.next()){
                if(c.wasAdded()){
                    c.getAddedSubList().forEach(repo::insert);
                }
            }
        });
        rank.addListener((obs, oldV, newV) -> {
            if(newV == null) return;
            switch (newV) {
                case "Votos" -> rankVotes();
                case "Titulo" -> rankTitulo();
                case "Artista" -> rankArtista();
            }
        });
        trackFilteredList = new FilteredList<>(tracks, p -> true);
    }

    public void filtrarTracks(String filtro){
        if(filtro == null || filtro.isEmpty()){
            trackFilteredList.setPredicate(t -> true);
        } else {
            String lowerFiltro = filtro.toLowerCase();
            trackFilteredList.setPredicate(t ->
                    (t.getArtist() != null && t.getArtist().toLowerCase().contains(lowerFiltro)) ||
                            (t.getTitle() != null && t.getTitle().toLowerCase().contains(lowerFiltro))
                    );
        }

    }

//    public void rankVotes(){
//        trackFilteredList.sort((o1, o2) -> Integer.compare(o2.getVotes(), o1.getVotes()));
//    }
//
//    public void rankTitulo(){
//        trackFilteredList.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o2.getTitle(), o1.getTitle()));
//    }
//
//    public void rankArtista(){
//        trackFilteredList.sort(((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o2.getArtist(), o1.getArtist())));
//    }

    public void addTrack(){
        tracks.add(0, createTrack());
        resetFields();
    }

    private void resetFields(){
        title.set("");
        artist.set("");
    }

    public void votar(Track newV){
        repo.incrementVote(newV.getId());
        tracks.setAll(repo.findAll());
    }

    private Track createTrack(){
        return new Track.Builder()
                .titleBuilder(title.getValue())
                .artistBuilder(artist.getValue())
                .votesBuilder(0)
                .build();
    }

}
