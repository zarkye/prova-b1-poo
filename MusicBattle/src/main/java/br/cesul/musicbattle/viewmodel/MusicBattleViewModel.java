package br.cesul.musicbattle.viewmodel;

import br.cesul.musicbattle.model.Track;
import br.cesul.musicbattle.repository.TrackRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.Comparator;

public class MusicBattleViewModel {

    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final StringProperty rank = new SimpleStringProperty();

    private final TrackRepository repo = new TrackRepository();
    private final ObservableList<Track> tracks = FXCollections.observableArrayList();
    private final FilteredList<Track> trackFilteredList;
    private final SortedList<Track> sortedTracks; // agora com uma sortedlist da pra rankear, antes não usava e por isso não conseguia dar o sort

    public MusicBattleViewModel() {
        tracks.addAll(repo.findAll());

        trackFilteredList = new FilteredList<>(tracks, t -> true);
        sortedTracks = new SortedList<>(trackFilteredList);
        //com as duas listas, uma pra sorted e uma pra filtrada, a lógica se une e a tela fica bem mais reativa, atualizou um, a outra também vai atualizar

        rank.setValue("Votos");
        // não tendo um rank settado quando inicia e o usuário clicar em votar fazia dar nullpointerexception quando o applyranking era chamado
        // agora com ele settado para o votos por exemplo já elimina essa possibilidade de erro

        rank.addListener((obs, oldV, newV) -> applyRanking(newV));
    }

    public StringProperty titleProperty() { return title; }
    public StringProperty artistProperty() { return artist; }
    public StringProperty rankProperty() { return rank; }

    public SortedList<Track> getSortedTracks() { return sortedTracks; }


    public void filtrarTracks(String filtro) {
        if (filtro == null || filtro.isBlank()) {
            trackFilteredList.setPredicate(t -> true);
        } else {
            String lower = filtro.toLowerCase();
            trackFilteredList.setPredicate(t ->
                    (t.getArtist() != null && t.getArtist().toLowerCase().contains(lower)) ||
                            (t.getTitle() != null && t.getTitle().toLowerCase().contains(lower))
            );
        }
    }
    // troquei a lógica de rank pra uma função só, usando comparator para a sortedlist
    private void applyRanking(String newV) {
        Comparator<Track> comparator = switch (newV) {
            case "Votos" -> Comparator.comparingInt(Track::getVotes).reversed();
            case "Titulo" -> Comparator.comparing(t -> t.getTitle().toLowerCase());
            case "Artista" -> Comparator.comparing(t -> t.getArtist().toLowerCase());
            default -> null;
        };
        sortedTracks.setComparator(comparator);
    }

    public void addTrack() {
        Track newTrack = createTrack();
        repo.insert(newTrack);
        tracks.add(0, newTrack);
        resetFields();
    }

    public void votar(Track votedTrack) {
        if (votedTrack == null) return;

        boolean success = repo.incrementVote(votedTrack.getId());
        // verificação adicional para somente mudar na tela se algo foi alterado no banco
        if(success){
            votedTrack.setVotes(votedTrack.getVotes() + 1);
        }

        int idx = tracks.indexOf(votedTrack);
        if (idx >= 0) {
            tracks.set(idx, votedTrack);
        }

        applyRanking(rank.get());
    }

    private void resetFields() {
        title.set("");
        artist.set("");
    }

    private Track createTrack() {
        String titulo = title.get();
        String artista = artist.get();
        if (titulo == null || titulo.isBlank() || artista == null || artista.isBlank()) {
            throw new IllegalArgumentException("Título e artista não podem ser vazios.");
        }
        return new Track.Builder()
                .titleBuilder(titulo.trim())
                .artistBuilder(artista.trim())
                .votesBuilder(0)
                .build();
    }
}
